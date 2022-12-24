/**
 * packageName    : io.github.taegyunwoo.springfox.error.appender.annotation
 * fileName       : ErrorResponse
 * author         : Taegyun Woo
 * description    : Annotation to manage Swagger error responses with Enum.
 *                  If this annotation is applied to a controller method,
 *                  a Swagger error response format is created with the related enum.
 */

package springfox.error.response.extension.annotation;

import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ErrorResponse {
  Class[] errorEnums();
  Header[] headers() default {};
  Content[] content() default {};
}
