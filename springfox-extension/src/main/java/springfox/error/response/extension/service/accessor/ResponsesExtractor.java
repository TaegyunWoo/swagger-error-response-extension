/**
 * packageName    : io.github.taegyunwoo.springfox.error.appender.service.accessor
 * fileName       : ResponsesExtractor
 * author         : Taegyun Woo
 * description    : A class that accesses SpringFox and adds an error response.
 */

package springfox.error.response.extension.service.accessor;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.service.Response;
import springfox.documentation.spi.service.contexts.OperationContext;

import java.lang.reflect.Field;

@Component
public class ResponsesExtractor implements SpringFoxAccessor<LinkedMultiValueMap<String, Response>, OperationContext> {
  @Override
  public LinkedMultiValueMap<String, Response> extract(OperationContext context) {
    Field responsesField = null;
    LinkedMultiValueMap<String, Response> responses = null;

    //get `responses` Field from OperationBuilder
    try {
      responsesField = OperationBuilder.class.getDeclaredField("responses");
    } catch (NoSuchFieldException e) {
      //ignored
    }

    //extract value of `responses` field from OperationBuilder object
    responsesField.setAccessible(true);
    try {
      responses = (LinkedMultiValueMap<String, Response>) responsesField.get(context.operationBuilder());
    } catch (IllegalAccessException e) {
      //ignored
    }

    return responses;
  }
}
