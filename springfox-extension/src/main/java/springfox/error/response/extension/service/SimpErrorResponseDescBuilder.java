/**
 * packageName    : io.github.taegyunwoo.springfox.error.appender.service
 * fileName       : SimpErrorResponseDescBuilder
 * author         : Taegyun Woo
 * description    : An implementation that creates a Swagger Response Description message
 *                  with the information received from the ErrorResponse annotation.
 */

package springfox.error.response.extension.service;

import org.springframework.stereotype.Component;
import springfox.error.response.extension.annotation.ErrorResponse;
import springfox.error.response.extension.exception.CannotParseEnumException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class SimpErrorResponseDescBuilder implements ErrorResponseDescriptionBuilder {
  private final ErrorEnumParser errorEnumParser;

  public SimpErrorResponseDescBuilder(ErrorEnumParser errorEnumParser) {
    this.errorEnumParser = errorEnumParser;
  }

  /**
   * Build description that will be included in the 'Swagger Response'
   * @param errorResponseAnno Annotation Class that has error data
   * @return Map Object that has descriptions per each Http Status Code (< HTTP_Status_Code, Description >)
   */
  @Override
  public Map<String, String> buildDescription(ErrorResponse errorResponseAnno) {
    // Map to store description per http status code
    Map<String, String> descriptionMap = new HashMap<>(); // <HTTP_Status_Code, Description>

    // Repeat for each element (Error Enum) stored in ErrorResponse.value
    for (Class errorEnumClass : errorResponseAnno.errorEnums()) {
      // Get constant information of implementation of ErrorEnumClass
      Map<ErrorEnumInfo, Map<String, String>> constantInfoMap = extractConstantInfo(errorEnumClass);

      // For each HTTP status code, a description is created and stored.
      Set<ErrorEnumInfo> constantInfoKeySet = constantInfoMap.keySet();
      for (ErrorEnumInfo key : constantInfoKeySet) {
        String httpStatusCode = constantInfoMap.get(key).get(ErrorEnumInfo.CODE_FIELD_NAME);
        String description = constantInfoMap.get(key).get(ErrorEnumInfo.DESCRIPTION_FIELD_NAME);

        if (descriptionMap.containsKey(httpStatusCode)) {
          StringBuilder mergedDescription = new StringBuilder(descriptionMap.get(httpStatusCode));
          mergedDescription.append("\n\n- `" + key.toString() + "`: " + description);
          descriptionMap.put(httpStatusCode, mergedDescription.toString());
        } else {
          String newDescription = key.getCode().name() + "\n\n- `" + key + "`: " + description;
          descriptionMap.put(httpStatusCode, newDescription);
        }
      }
    }
    return descriptionMap;
  }

  /**
   * Extract constant information from ErrorEnum (that the developer created)
   * @param errorEnumClass Error Enum
   * @return result
   */
  private Map<ErrorEnumInfo, Map<String, String>> extractConstantInfo(Class errorEnumClass) {
    // check that the developer implements error enum class properly
    try {
      errorEnumParser.supports(errorEnumClass);
    } catch (CannotParseEnumException e) {
      throw new RuntimeException(e);
    }

    // extract enum constants and fields value by getter
    Map<ErrorEnumInfo, Map<String, String>> constantInfoMap = null;
    try {
      constantInfoMap = errorEnumParser.parse(errorEnumClass);
    } catch (CannotParseEnumException e) {
      throw new RuntimeException(e);
    }
    return constantInfoMap;
  }
}
