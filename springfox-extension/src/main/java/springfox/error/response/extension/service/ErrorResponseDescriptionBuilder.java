/**
 * packageName    : io.github.taegyunwoo.springfox.error.appender.service
 * fileName       : ErrorResponseDescriptionBuilder
 * author         : Taegyun Woo
 * description    : Interface that must be implemented by a class
 *                  that creates a Swagger Response Description message
 *                  with information passed from the ErrorResponse annotation.
 */

package springfox.error.response.extension.service;

import springfox.error.response.extension.annotation.ErrorResponse;

import java.util.Map;

public interface ErrorResponseDescriptionBuilder {
  Map<String, String> buildDescription(ErrorResponse errorResponseAnno);
}
