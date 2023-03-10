# Swagger Error Response Extension
|Latest Version| Build                                                                                                                                         | Coverage | License Scan                                                                                                                                                                                                                                             | License                                                                                                     | Contribution                                                                                                                                    |
|--------------|-----------------------------------------------------------------------------------------------------------------------------------------------|---------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
|[![](https://jitpack.io/v/TaegyunWoo/swagger-error-response-extension.svg)](https://jitpack.io/#TaegyunWoo/swagger-error-response-extension)| [![](https://jitci.com/gh/TaegyunWoo/swagger-error-response-extension/svg)](https://jitci.com/gh/TaegyunWoo/swagger-error-response-extension)         |[![codecov](https://codecov.io/github/TaegyunWoo/swagger-error-response-extension/branch/master/graph/badge.svg?token=UO3BUVA6PS)](https://codecov.io/github/TaegyunWoo/swagger-error-response-extension)| [![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FTaegyunWoo%2Fswagger-error-response-extension.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2FTaegyunWoo%2Fswagger-error-response-extension?ref=badge_shield) | [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) |  [![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/TaegyunWoo/swagger-error-response-extension/pulls) |

|Spring Boot| Supports | SpringFox | Supports |SpringDoc|Supports|
|-----------|------------|-----------|------------|----------|---|
|2.7.7| ???  | 3.0.0     | ???  |??????|??????|

| Demo Project                                                                                                                                                                                                                                                                   | English README.md     |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------|
| [![TaegyunWoo/swagger-error-response-extension-demo - GitHub](https://github-readme-stats-sepia-three.vercel.app/api/pin/?username=TaegyunWoo&repo=swagger-error-response-extension-demo&theme=vision-friendly-dark)](https://github.com/TaegyunWoo/swagger-error-response-extension-demo) | [English README.md](https://github.com/TaegyunWoo/swagger-error-response-extension) |

<br/>

## Core Directories

```text
.
????????? README.md
????????? doc                                                 : Library Documents
????????? jitpack.yml                                         : Jitpack Deployment Setting Properties
????????? springfox-extension                                 : Module for springfox
    ????????? build.gradle
    ????????? src
        ????????? main
            ????????? java/springfox/error/response/extension
                ????????? annotation                          : all annotations
                ????????? configuration                       : spring bean configuration
                ????????? exception                           : all exceptions
                ????????? service                             : core logics
                    ????????? accessor                        : springfox accessor
            ????????? resources
        ????????? test                                        : test codes
```

<br/>

## Index
- [Swagger Error Response Extension ??? ????????????????](#swagger-error-response-extension-???-???????????????)
- [??? ????????????????](#???-???????????????)
- [????????? ???????????????? - SpringFox](#?????????-???????????????---springfox)
- [?????????????????? - SpringFox](#??????????????????---springfox)
- [????????? ???????????????? - SpringDoc](#?????????-???????????????---springdoc)
- [?????????????????? - SpringDoc](#??????????????????---springdoc)
- [???????????? ??????](#????????????-??????)

<br/>

## Swagger Error Response Extension ??? ????????????????
Swagger Error Response Extension ??? ???????????? ?????? ????????? ???????????? ????????? ???????????? Swagger Response Description ??? ??????????????? ????????? ???????????? ????????????????????????.

?????? ???????????????!

![error-example.jpg](./img/error-example.jpg)

Spring ???????????? Swagger??? ???????????? ?????? ???????????? `SpringFox`??? `SpringDoc` ??????????????? ????????? ???????????????.

> ????????? `SpringFox` ??? ???????????? ????????????.

<br/>

## ??? ????????????????
### ?????? ???????????? ??? ?????? ????????? ??????????????? ????????????????

????????? ????????? ????????????, ?????? ????????? ????????? ?????? Client ???????????? Server ????????? ?????? ????????? ????????? ?????? ????????? ?????? ????????? ?????????.

?????? ????????????????

????????? ????????? API??? ???????????? ?????? Server ??????????????? ????????????.

????????? `???????????? ?????? id??? ???????????? ????????? ??????`??? `?????? ??????????????? ???????????? ????????? ??????` ?????? ??????????????? ???????????? ????????? ??????????????? ????????????.

???????????? ??? ?????? ???????????????, ?????? ??????????????? ?????? ????????? ????????? API??? ?????????????????????.

- `???????????? ?????? id` -> `WRONG_ID` ?????? ??????
- `?????? ????????????` -> `WRONG_PW` ?????? ??????

?????? ?????? API??? ???????????????, `WRONG_ID`, `WRONG_PW` ?????? ??????????????? ???????????? ??????????????? ??????????????? Client ??????????????? ???????????? ?????????.

> 'Client ??????????????? ????????????????????? ???'??? Swagger Error Response Extension ?????????????????? ????????? ???????????????!  
????????? ?????? ?????? ???????????????.

?????? Client ???????????? ??? ??????????????? ??????, ??? ????????? ?????? ??????????????? ?????? ????????? ????????? ????????? ??? ????????????.

<br/>

### ?????? ????????? ??? ??????????????? Client ??????????????? ?????????????????? ????????? ??? ?????????????
??? ?????????????????? ?????? Document??? ???????????? ?????????????????? ???????????? ?????? ????????? ????????? ??? ????????????.

????????? Swagger ??? ?????? API Documentation??? ?????? ?????? ???, ?????? API ?????? ????????? ??? ?????????????????? ?????????????????? ??? ??? ????????? ?????????????

<br/>

### ????????????. ????????? Swagger ??? ??? ?????????????????? ?????? ??????????????????!
?????? `SpringFox` ??? ?????? Swagger??? ????????? ???, ?????? ??????????????? ?????? ????????? ????????? ?????? ???????????? ?????????.

```java
@ApiResponses(value = {
      @ApiResponse(responseCode = "401", description = "WRONG_ID: account id is not exists. WRONG_PW: wrong password.") //Add Description
})
@GetMapping("/login")
String login(@RequestBody RequestDto requestDto) {
  return "login succeed";
}
```

????????? ?????? ????????? ???????????? ?????? ??????????????????.

????????? ??? API??? ??????????????? 10?????? ???????????????, ????????? ?????? ??????????????? ??????????????? ?????????????

?????? ????????? ????????? ??????????????? ???????????? ?????????! ????????? ?????? ????????? ?????? ????????? ????????? ??????????

<br/>

### ????????? Swagger Error Response Extension ??? ??????????????????.
?????? ???????????? ???????????? ??????, Swagger Error Response Extension??? **Enum??? ?????? ??????????????? ??????**?????????.

Enum ???????????? ??????????????? ????????? ??????????????? ????????? ?????????????????? import ??????, **?????? ???????????? ???????????? Swagger Response API??? description??? ??????**?????????.

?????? **?????? ????????? ???????????? point??? enum ????????? ??? ???**?????? ?????? ??? ????????????. ???, enum ???????????? ???????????? ??????????????? ?????? ????????? ????????? ??? ????????????.

??????????????? ?????? ????????? enum ???????????? ???????????????, **?????? ???????????? enum ???????????? ????????? ??? ?????????, ????????? ????????? Swagger ?????? ????????? ??????**??? ??? ????????????. 

<br/>

## ????????? ???????????????? - SpringFox
### Gradle Dependencies
?????? `build.gradle`??? ???????????? ???????????????.

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
SpringFox??? ???????????? ?????? ?????? ????????? ???????????????.

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
?????? ??????????????? Swagger Error Response Extension ??? ??????????????????.

?????? ???????????? ????????? ?????? Enum ???????????? ???????????????.

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

- ?????? ????????? ?????? Enum ??? ????????? `springfox.error.response.extension.service.ErrorEnumInfo` ?????????????????? implements ???????????????.
- 2?????? Field??? ???????????? ?????????. **(Field ????????? ???????????? ?????? ?????? ???????????????!)**
  - `private HttpStatus code`
  - `private String description`
- `code`, `description` ??????????????? ?????? ???????????? ???????????????.
- ??? Field??? getter ???????????? override ?????????.
- enum ????????? ????????????, ??? Field??? ??????????????? ????????? ????????????.
  - enum constant -> error code
  - `code` Field -> HttpStatus Code
  - `description` Field -> description of error code

<br/>

### Controller
????????? ????????? enum??? ????????????(Swagger API)??? ???????????? ?????????.

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

?????? ????????? ???????????????!

?????? ????????? ?????? ????????? ????????? ??? ????????????.

![error-example.jpg](./img/error-example.jpg)

<br/>

## ?????????????????? - SpringFox
?????? ?????? ????????? swagger response description??? ???????????? ?????????, `ErrorResponseDescriptionBuilder` ?????????????????? ???????????? ?????????.

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

?????????????????? `springfox.error.response.extension.service.SimpErrorResponseDescBuilder` ?????? ??? component ??? bean?????? ???????????? ???????????? ?????????.

??????????????? ?????? ????????? ????????? [SimpErrorResponseDescBuilder.java](../springfox-extension/src/main/java/springfox/error/response/extension/service/SimpErrorResponseDescBuilder.java) ??? ???????????????.

<br/>

## ????????? ???????????????? - SpringDoc
> ?????? ???????????????.

<br/>

## ?????????????????? - SpringDoc
> ?????? ???????????????.

<br/>

## ???????????? ??????
- ?????? SpringFox ???????????? ???????????????. SpringDoc?????? ??????????????? ??????????????? ???????????????. 

<br/>

## License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FTaegyunWoo%2Fswagger-error-response-extension.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2FTaegyunWoo%2Fswagger-error-response-extension?ref=badge_large)
