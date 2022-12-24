/**
 * packageName    : io.github.taegyunwoo.springfox.error.appender.service.accessor
 * fileName       : SpringFoxAccessor
 * author         : Taegyun Woo
 * description    : Interface to access SpringFox library.
 */

package springfox.error.response.extension.service.accessor;

public interface SpringFoxAccessor<R, P> {
  /**
   * extract the object that created by SpringFox Library
   * @param context kind of SpringFox context object, for extract
   * @return extracted object from SpringFox
   */
  R extract(P context);
}
