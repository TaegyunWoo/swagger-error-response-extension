/**
 * packageName    : io.github.taegyunwoo.springfox.error.appender.exception
 * fileName       : CannotParseEnumException
 * author         : Taegyun Woo
 * description    : Exception that throws when an enum class containing error response information cannot be parsed.
 */

package springfox.error.response.extension.exception;

public class CannotParseEnumException extends Exception {
  public CannotParseEnumException() {
  }

  public CannotParseEnumException(String message) {
    super(message);
  }
}
