/**
 * packageName    : io.github.taegyunwoo.springfox.error.appender.service
 * fileName       : EnumParser
 * author         : Taegyun Woo
 * description    : Interface that classes that parse error information from Enum classes must implement.
 */

package springfox.error.response.extension.service;

import springfox.error.response.extension.exception.CannotParseEnumException;

import java.util.Map;

public interface ErrorEnumParser {
  /**
   * parsing method
   * @param enumClass enum class you want to parse
   * @return parsed result
   */
  Map<ErrorEnumInfo, Map<String, String>> parse(Class<? extends ErrorEnumInfo> enumClass) throws CannotParseEnumException;

  /**
   * check if it is an enum class that can be parsed
   * @param enumClass enum class to check
   * @return true: can parse
   * @throws CannotParseEnumException if enum class is unable to be parsed
   */
  boolean supports(Class<? extends ErrorEnumInfo> enumClass) throws CannotParseEnumException;
}
