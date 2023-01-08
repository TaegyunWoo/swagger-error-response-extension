# Swagger Error Response Extension
|Latest Version| Build                                                                                                                                         | Coverage | License Scan                                                                                                                                                                                                                                             | License                                                                                                     | Contribution                                                                                                                                    |
|--------------|-----------------------------------------------------------------------------------------------------------------------------------------------|---------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
|[![](https://jitpack.io/v/TaegyunWoo/swagger-error-response-extension.svg)](https://jitpack.io/#TaegyunWoo/swagger-error-response-extension)| [![](https://jitci.com/gh/TaegyunWoo/swagger-error-response-extension/svg)](https://jitci.com/gh/TaegyunWoo/swagger-error-response-extension)         |[![codecov](https://codecov.io/github/TaegyunWoo/swagger-error-response-extension/branch/master/graph/badge.svg?token=UO3BUVA6PS)](https://codecov.io/github/TaegyunWoo/swagger-error-response-extension)| [![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FTaegyunWoo%2Fswagger-error-response-extension.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2FTaegyunWoo%2Fswagger-error-response-extension?ref=badge_shield) | [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) |  [![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/TaegyunWoo/swagger-error-response-extension/pulls) |

|Spring Boot| Supports | SpringFox | Supports |SpringDoc|Supports|
|-----------|----------|-----------|------------|----------|---|
|2.7.7| ✅        | 3.0.0     | ✅  |✖️|✖️|

| Demo Project                                                                                                                                                                                                                                                                   | Korean README.md                                                                                                 |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|
| [![TaegyunWoo/swagger-error-response-extension-demo - GitHub](https://github-readme-stats-sepia-three.vercel.app/api/pin/?username=TaegyunWoo&repo=swagger-error-response-extension-demo&theme=vision-friendly-dark)](https://github.com/TaegyunWoo/swagger-error-response-extension-demo) | [Korean README.md](https://github.com/TaegyunWoo/swagger-error-response-extension/tree/master/doc/README-KOR.md) |

<br/>

## Core Directories

```text
.
├── README.md
├── doc                                                 : Library Documents
├── jitpack.yml                                         : Jitpack Deployment Setting Properties
└── springfox-extension                                 : Module for springfox
    ├── build.gradle
    └── src
        ├── main
            ├── java/springfox/error/response/extension
                ├── annotation                          : all annotations
                ├── configuration                       : spring bean configuration
                ├── exception                           : all exceptions
                └── service                             : core logics
                    └── accessor                        : springfox accessor
            └── resources
        └── test                                        : test codes
```

<br/>

## Index
- [What is Swagger Error Response Extension?](#what-is-swagger-error-response-extension)
- [Why do you need it?](#why-do-you-need-it)
- [How do I use it? - SpringFox](#how-do-i-use-it---springfox)
- [Customizing - SpringFox](#customizing---springfox)
- [How do I use it? - SpringDoc](#how-do-i-use-it---springdoc)
- [Customizing - SpringDoc](#customizing---springdoc)
- [Future plans](#future-plans)

<br/>

## What is Swagger Error Response Extension?
Swagger Error Response Extension is a simple open source library that easily adds error code information defined by the developer to Swagger Response Description.

Like this...

![error-example.jpg](./doc/img/error-example.jpg)

It operates on the `SpringFox` or `SpringDoc` libraries.

> Currently, only `SpringFox` is supported.

<br/>

## Why do you need it?
### First of all, why do developers use their own defined error codes?

There may be various reasons, but the most important reason is to help smooth communication and logic development between Client and Server developers.

Here's a example.

Let's say we're a Server developer trying to create a login API.

And there is a requirement that the user should be notified of both 'when trying to sign in with a non-existent id' and 'when trying to sign in with an incorrect password'.

If so, you should implement the API to respond with a different error code for each failure case.

- `account id does not exist` -> Respond `WRONG_ID` Code
- `wrong password` -> Respond `WRONG_PW` Code

Create an API as above, and inform the client developer that you have implemented it to respond by defining error codes such as `WRONG_ID` and `WRONG_PW`.

> 'You need to tell your client developers' that's why you need the Swagger Error Response Extension library!  
We will continue to explain the details.

Then, the client developer can look at each error code and decide which notification to display to the user according to that code.

<br/>

### Then, how can we inform the client developer of this error code simply and clearly?
One way is to organize these error codes in an external document and share them among developers.

However, when doing API Documentation through Swagger, wouldn't it be more convenient if these error codes were summarized in the relevant API documentation description?

<br/>

### Great! Then let's add these error codes directly to Swagger.
If you use Swagger through `SpringFox`, you can directly add a description of the error code as below.

```java
@ApiResponses(value = {
      @ApiResponse(responseCode = "401", description = "WRONG_ID: account id is not exists. WRONG_PW: wrong password.") //Add Description
})
@GetMapping("/login")
String login(@RequestBody RequestDto requestDto) {
  return "login succeed";
}
```

Adding a direct description like this is also effective.

But what if one API has more than 10 error codes, or if the existing error codes are removed?

All you have to do is add or edit the description yourself! But are you confident enough to fix it without making any mistakes?

<br/>

### So the Swagger Error Response Extension was born.
To improve this inconvenience, Swagger Error Response Extension manages error codes through **Enum**.

If you define the error code and description in the Enum class and import them in the controller, **related information is automatically added to the description of the Swagger Response API**.

Also, **points that manage error codes can be gathered into one enum class**. In other words, you can control the description of the error code by managing only the enum class.

Finally, since the error code is managed as an enum class, **you can use the enum class in actual logic, and you can bring it to Swagger as it is and use it**. 

<br/>

## How do I use it? - SpringFox
### Gradle Dependencies
First, add dependencies to `build.gradle`.

```groovy
repositories {
	maven { url 'https://jitpack.io' } //Add JitPack Repository
}
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'io.springfox:springfox-boot-starter:3.0.0'                     //Add SpringFox Library
    implementation 'com.github.TaegyunWoo:swagger-error-response-extension:v1.1.2' //Add Swagger Error Response Extension Library
}
```

<br/>

### Swagger Configuration
Proceed with the basic configuration to use SpringFox.

```java
@Configuration
public class SwaggerConfig {
  
  @Bean
  public Docket customImplementation(TypeResolver typeResolver){
    return new Docket(DocumentationType.OAS_30)
        .additionalModels(
            typeResolver.resolve(ErrorResponseDto.class) //your DTO class for response error (Optional)
        )
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller")) //your base package of controller
        .build();
  }

}
```

<br/>

### Error Enum
Now let's use the Swagger Error Response Extension in earnest.

Define an Enum class to contain error code information.

```java
import org.springframework.http.HttpStatus;
import springfox.error.response.extension.service.ErrorEnumInfo;

public enum LoginErrorCode implements ErrorEnumInfo {
  WRONG_ID(HttpStatus.valueOf(401), "account id is wrong."),
  WRONG_PW(HttpStatus.valueOf(401), "password is wrong.");
  
  private HttpStatus code;
  private String description;

  LoginErrorCode(HttpStatus code, String description) {
    this.code = code;
    this.description = description;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public HttpStatus getCode() {
    return code;
  }
}
```

1. Enum to contain error information must implement `springfox.error.response.extension.service.ErrorEnumInfo` interface.
2. You need to declare 2 fields. **(Note that the field names must be the same!)**
    - `private HttpStatus code`
    - `private String description`
3. Declare a constructor with `code` and `description` parameters.
4. Override the getter method of each field.
5. Declare enum constants, and properly contain error information in each field.
    - enum constant -> error code
    - `code` Field -> HttpStatus Code
    - `description` Field -> description of error code

<br/>

### Controller
Just apply the enum defined above to the controller (Swagger API).

```java
@RequestMapping("/login")
@RestController
public class Controller {
  
  //from swagger-error-response-extension
  @ErrorResponse(errorEnums = {LoginErrorCode.class}, content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
  //from springfox
  @ApiResponses(value = {
          @ApiResponse(responseCode = "401", description = "WRONG_ID: account id is not exists. WRONG_PW: wrong password.") //Add Description
  })
  @GetMapping()
  String login(@RequestBody RequestDto requestDto) {
    return "login succeed";
  }
  
}
```

All settings are done!

Now you can see the result like below.

![error-example.jpg](./doc/img/error-example.jpg)

<br/>

## Customizing - SpringFox
If you want to configure the swagger response description in a different format, you can implement the `ErrorResponseDescriptionBuilder` interface.

```java
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.error.response.extension.annotation.ErrorResponse;
import springfox.error.response.extension.service.ErrorResponseDescriptionBuilder;

import java.util.HashMap;
import java.util.Map;

@Component //add bean
@Primary //apply this component instead of library component
public class CustomErrorResponseDescBuilder implements ErrorResponseDescriptionBuilder {

  @Override
  public Map<String, String> buildDescription(ErrorResponse errorResponseAnno) {
    // map to save description per http status
    Map<String, String> descriptionMap = new HashMap<>(); //<HTTP Status, Description>

    descriptionMap.put("404", "This is my custom description");

    return descriptionMap;
  }
  
}
```

Instead of the library's `springfox.error.response.extension.service.SimpErrorResponseDescBuilder`, the above component is registered as a bean and operates.

For details on the operation principle, refer to [SimpErrorResponseDescBuilder.java](./springfox-extension/src/main/java/springfox/error/response/extension/service/SimpErrorResponseDescBuilder.java).

<br/>

## How do I use it? - SpringDoc
> A Module supporting SpringDoc will be updated.

<br/>

## Customizing - SpringDoc
> A Module supporting SpringDoc will be updated.

<br/>

## Future plans
- Currently works only with SpringFox. We plan to update to support SpringDoc as well. 

<br/>

## License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FTaegyunWoo%2Fswagger-error-response-extension.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2FTaegyunWoo%2Fswagger-error-response-extension?ref=badge_large)
