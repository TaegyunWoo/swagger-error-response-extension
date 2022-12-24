/**
 * packageName    : io.github.taegyunwoo.springfox.error.appender.service
 * fileName       : ErrorResponseExtension
 * author         : Taegyun Woo
 * description    : A class that customizes SpringFox's error response based on the error enum class.
 */

package springfox.error.response.extension.service;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import springfox.documentation.builders.ExampleBuilder;
import springfox.documentation.schema.Example;
import springfox.documentation.schema.property.ModelSpecificationFactory;
import springfox.documentation.service.Header;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ResponseContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.error.response.extension.annotation.ErrorResponse;
import springfox.error.response.extension.service.accessor.SpringFoxAccessor;

import java.util.*;

import static java.util.Optional.ofNullable;
import static springfox.documentation.swagger.annotations.Annotations.resolvedTypeFromSchema;
import static springfox.documentation.swagger.readers.operation.ResponseHeaders.headers;

@Component
@Order(SwaggerPluginSupport.OAS_PLUGIN_ORDER)
public class ErrorResponseExtension implements OperationBuilderPlugin {
  private final TypeResolver typeResolver;
  private final ModelSpecificationFactory modelSpecifications;
  private final SpringFoxAccessor<LinkedMultiValueMap<String, Response>, OperationContext> springFoxAccessor;
  private final ErrorResponseDescriptionBuilder descriptionBuilder;

  public ErrorResponseExtension(TypeResolver typeResolver,
                                ModelSpecificationFactory modelSpecifications,
                                SpringFoxAccessor<LinkedMultiValueMap<String, Response>, OperationContext> springFoxAccessor,
                                ErrorResponseDescriptionBuilder descriptionBuilder) {
    this.typeResolver = typeResolver;
    this.modelSpecifications = modelSpecifications;
    this.springFoxAccessor = springFoxAccessor;
    this.descriptionBuilder = descriptionBuilder;
  }

  @Override
  public void apply(OperationContext context) {
    //Get ErrorResponseAnnotation from context
    ErrorResponse errorResponseAnno = getErrorResponseAnnotation(context);
    if (errorResponseAnno == null) return;

    // Get SpringFox's original Responses object
    LinkedMultiValueMap<String, Response> originalResponses = springFoxAccessor.extract(context);

    // Construct a string to be included in the description of the error response
    Map<String, String> descriptionMap = descriptionBuilder.buildDescription(errorResponseAnno);

    ResponseContext responseContext = new ResponseContext(
        context.getDocumentationContext(),
        context);

    // Build Header property
    Map<String, Header> headers = buildHeadersProperty(errorResponseAnno);

    // Build Example property
    List<Example> examples = buildExamples(responseContext, context, errorResponseAnno);

    // Apply description, status code, header, and example properties for each http status code
    Response originalResponse = originalResponses.get("200").get(0); // Default response object
    Set<String> descriptionMapKeySet = descriptionMap.keySet();

    for (String httpStatusKey : descriptionMapKeySet) {
      Response customResponse = responseContext.responseBuilder()
          .copyOf(originalResponse)
          .code(httpStatusKey) // Apply http status code
          .description(descriptionMap.get(httpStatusKey)) // Apply description
          .headers(headers.values()) // Append headers to default headers value
          .examples(examples) // Append examples to default examples value
          .build();

      if (originalResponses.containsKey(httpStatusKey)) {
        originalResponses.get(httpStatusKey).add(customResponse);
      } else {
        List<Response> customResponseList = new ArrayList<>();
        customResponseList.add(customResponse);
        originalResponses.put(httpStatusKey, customResponseList);
      }
    }
  }

  @Override
  public boolean supports(DocumentationType delimiter) {
    return true; //Allow all types (SWAGGER_12, SWAGGER_2, OAS_30, ..)
  }

  /**
   * A method that creates the response's header property values.
   * @param errorResponseAnno An Annotation class that is containing header property values
   * @return Map of header values
   */
  private Map<String, Header> buildHeadersProperty(ErrorResponse errorResponseAnno) {
    Map<String, Header> headers = new HashMap<>();
    Optional<ResolvedType> type;

    for (Content each : errorResponseAnno.content()) {
      type = resolvedType(each.schema());
      if (type.isEmpty()) {
        continue;
      }
      headers.putAll(headers(errorResponseAnno.headers()));
    }

    return headers;
  }

  /**
   * A method that creates the response's example property values.
   * @param responseContext Original response context from SpringFox
   * @param operationContext Original operation context from SpringFox
   * @param errorResponseAnno An Annotation class that is containing example property values
   * @return List of example values
   */
  private List<Example> buildExamples(ResponseContext responseContext,
                                      OperationContext operationContext,
                                      ErrorResponse errorResponseAnno) {
    List<Example> examples = new ArrayList<>();
    Optional<ResolvedType> type;

    for (Content each : errorResponseAnno.content()) {
      type = resolvedType(each.schema());
      if (type.isEmpty()) {
        continue;
      }
      ModelContext modelContext = operationContext.operationModelsBuilder()
          .addReturn(
              typeResolver.resolve(each.schema().implementation()),
              Optional.empty());
      for (ExampleObject eachExample : each.examples()) {
        if (!eachExample.value().isEmpty()) {
          examples.add(new ExampleBuilder()
              .mediaType(each.mediaType())
              .description(eachExample.description())
              .summary(eachExample.summary())
              .id(eachExample.name())
              .value(eachExample.value()).build());
        }
      }

      type.ifPresent(t -> responseContext.responseBuilder()
          .representation(each.mediaType().isEmpty() ? MediaType.ALL : MediaType.valueOf(each.mediaType()))
          .apply(r -> r.model(
              m -> m.copyOf(modelSpecifications.create(modelContext, t)))));
    }

    return examples;
  }

  /**
   * Get ErrorResponseAnnotation from OperationContext
   * @param context Current context
   * @return ErrorResponseAnnotation (if there is no ErrorResponse anno., returns null)
   */
  private ErrorResponse getErrorResponseAnnotation(OperationContext context) {
    Optional<ErrorResponse> errorResponseAnnotation = context.findAnnotation(ErrorResponse.class);
    if (errorResponseAnnotation.isEmpty()) return null;
    ErrorResponse errorResponseAnno = errorResponseAnnotation.get();
    return errorResponseAnno;
  }

  private Optional<ResolvedType> resolvedType(Schema responseSchema) {
    return ofNullable(resolvedTypeFromSchema(typeResolver,  null).apply(responseSchema));
  }
}
