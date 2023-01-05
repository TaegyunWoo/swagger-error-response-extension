/**
 * packageName    : org.springframework.stereotype.Component
 * fileName       : SimpErrorEnumParser
 * author         : Taegyun Woo
 * description    : An implementation that parses an enum class containing error response information.
 */

package springfox.error.response.extension.service;

import org.springframework.stereotype.Component;
import springfox.error.response.extension.exception.CannotParseEnumException;

import java.util.HashMap;
import java.util.Map;

@Component
public class SimpErrorEnumParser implements ErrorEnumParser {

  /**
   * A method that parses an enum class (an enum that inherits the ErrorCodeApi interface)
   * that has error information.
   * @param enumClass enum class to parse
   * @return parsing result
   * @throws CannotParseEnumException unable to parse
   */
  @Override
  public Map<ErrorEnumInfo, Map<String, String>> parse(Class<? extends ErrorEnumInfo> enumClass) throws CannotParseEnumException {
    Map<ErrorEnumInfo, Map<String, String>> constantInfoMap = new HashMap<>();
    Object[] enumConstants = enumClass.getEnumConstants();

    for (Object constant : enumConstants) {
      ErrorEnumInfo enumConstant = (ErrorEnumInfo) constant;
      Map<String, String> fieldInfo = new HashMap<>();

      // If there are some 2xx http status in error enum class, reject to parse
      if (enumConstant.getCode().is2xxSuccessful())
        throw new CannotParseEnumException("Use SpringFox's '@ApiResponse' annotation when you response for 2xx http status.");

      fieldInfo.put(ErrorEnumInfo.CODE_FIELD_NAME,
          String.valueOf(enumConstant.getCode().value()));
      fieldInfo.put(ErrorEnumInfo.DESCRIPTION_FIELD_NAME,
          String.valueOf(enumConstant.getDescription()));
      constantInfoMap.put(enumConstant, fieldInfo);
    }

    return constantInfoMap;
  }

  /**
   * Method to check if parsing is possible
   * @param enumClass enum class to check
   * @return true: able to parse
   * @throws CannotParseEnumException unable to parse
   */
  @Override
  public boolean supports(Class enumClass) throws CannotParseEnumException {
    // Check that it is an Enum class
    if ( !enumClass.isEnum() )
      throw new CannotParseEnumException("ErrorCode enum class is not 'enum class'.");

    // Check that the `ErrorEnumInfo` interface is inherited
    if ( !ErrorEnumInfo.class.isAssignableFrom(enumClass) )
      throw new CannotParseEnumException("ErrorCode enum class must implement " + ErrorEnumInfo.class.getName());

    // Check that there is one `description` field and one `code` field each
    try {
      enumClass.getDeclaredField(ErrorEnumInfo.DESCRIPTION_FIELD_NAME);
    } catch (NoSuchFieldException e) {
      throw new CannotParseEnumException("ErrorCode enum class must have 'String description' field.");
    }
    try {
      enumClass.getDeclaredField(ErrorEnumInfo.CODE_FIELD_NAME);
    } catch (NoSuchFieldException e) {
      throw new CannotParseEnumException("ErrorCode enum class must have 'HttpStatus code' field.");
    }

    return true;
  }
}
