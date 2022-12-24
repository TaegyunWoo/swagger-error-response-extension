/**
 * packageName    : io.github.taegyunwoo.springfox.error.appender.service
 * fileName       : ErrorEnumInfo
 * author         : Taegyun Woo
 * description    : Interface that Enum classes that contain error response information must implement.
 */

package springfox.error.response.extension.service;

import org.springframework.http.HttpStatus;

public interface ErrorEnumInfo {
  String CODE_FIELD_NAME = "code";
  String DESCRIPTION_FIELD_NAME = "description";

  /**
   * just common getter
   */
  HttpStatus getCode();

  /**
   * just common getter
   */
  String getDescription();
}
