/**
 * packageName    : springfox.error.response.extension.configuration
 * fileName       : ErrorResponseExtensionConfig
 * author         : Taegyun Woo
 * description    : SpringFox Error Response Extension Configuration
 */

package springfox.error.response.extension.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "springfox.error.response.extension.service"
})
public class ErrorResponseExtensionConfig {
  
}
