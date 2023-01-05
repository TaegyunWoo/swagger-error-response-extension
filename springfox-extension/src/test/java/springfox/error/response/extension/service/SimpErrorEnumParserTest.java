package springfox.error.response.extension.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import springfox.error.response.extension.exception.CannotParseEnumException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SimpErrorEnumParserTest {
  private SimpErrorEnumParser simpErrorEnumParser;

  @BeforeEach
  void setUp() {
    this.simpErrorEnumParser = new SimpErrorEnumParser();
  }

  @Test
  @DisplayName("parse succeed")
  void parseSucceed() throws CannotParseEnumException {
    //GIVEN
    Class succeedEnumClass = SucceedErrorEnum.class;

    //WHEN
    Map<ErrorEnumInfo, Map<String, String>> result = simpErrorEnumParser.parse(succeedEnumClass);
    Map<String, String> code1ParseResult = result.get(SucceedErrorEnum.MY_ERROR_CODE_1);
    Map<String, String> code2ParseResult = result.get(SucceedErrorEnum.MY_ERROR_CODE_2);

    //THEN
    assertAll(
        //check MY_ERROR_CODE_1's code info
        () -> assertEquals(String.valueOf(SucceedErrorEnum.MY_ERROR_CODE_1.getCode().value()),
            code1ParseResult.get(ErrorEnumInfo.CODE_FIELD_NAME)),
        //check MY_ERROR_CODE_1's description info
        () -> assertEquals(SucceedErrorEnum.MY_ERROR_CODE_1.getDescription(),
            code1ParseResult.get(ErrorEnumInfo.DESCRIPTION_FIELD_NAME)),
        //check MY_ERROR_CODE_2's code info
        () -> assertEquals(String.valueOf(SucceedErrorEnum.MY_ERROR_CODE_2.getCode().value()),
            code2ParseResult.get(ErrorEnumInfo.CODE_FIELD_NAME)),
        //check MY_ERROR_CODE_2's description info
        () -> assertEquals(SucceedErrorEnum.MY_ERROR_CODE_2.getDescription(),
            code2ParseResult.get(ErrorEnumInfo.DESCRIPTION_FIELD_NAME))
    );
  }

  @Test
  @DisplayName("parse enum contains 2xx status")
  void parse2xxStatus() {
    //GIVEN
    Class errorEnum2xxClass = ErrorEnum2xx.class;

    //WHEN
    //simpErrorEnumParser.parse(errorEnum2xxClass);

    //THEN
    assertThrows(CannotParseEnumException.class, () -> simpErrorEnumParser.parse(errorEnum2xxClass));
  }

  @Test
  @DisplayName("supports true")
  void supportsTrue() {
    //GIVEN
    Class succeedErrorEnumClass = SucceedErrorEnum.class;

    //WHEN
    //simpErrorEnumParser.supports(succeedErrorEnumClass);

    //THEN
    assertAll(
        //no Exception
        () -> assertDoesNotThrow(() -> simpErrorEnumParser.supports(succeedErrorEnumClass)),
        //return true
        () -> assertTrue(simpErrorEnumParser.supports(succeedErrorEnumClass))
    );
  }

  @Test
  @DisplayName("supports not enum class")
  void supportsNotEnum() {
    //GIVEN
    Class notEnumClass = NotEnumClass.class;

    //WHEN
    //simpErrorEnumParser.supports(notEnumClass);

    //THEN
    assertThrows(CannotParseEnumException.class, () -> simpErrorEnumParser.supports(notEnumClass), "ErrorCode enum class is not 'enum class'.");
  }

  @Test
  @DisplayName("supports not inherit ErrorEnumInfo Interface")
  void supportsNotInheritErrorEnumInfo() {
    //GIVEN
    Class notInheritClass = NotInheritErrorEnumInfoErrorEnum.class;

    //WHEN
    //simpErrorEnumParser.supports(notInheritClass);

    //THEN
    assertThrows(CannotParseEnumException.class, () -> simpErrorEnumParser.supports(notInheritClass), "ErrorCode enum class must implement " + ErrorEnumInfo.class.getName());
  }

  @Test
  @DisplayName("supports enum without code field")
  void supportsNoCodeField() {
    //GIVEN
    Class noCodeFieldClass = ChangedCodeFieldNameErrorEnum.class;

    //WHEN
    //simpErrorEnumParser.supports(noCodeFieldClass);

    //THEN
    assertThrows(CannotParseEnumException.class, () -> simpErrorEnumParser.supports(noCodeFieldClass), "ErrorCode enum class must have 'HttpStatus code' field.");
  }

  @Test
  @DisplayName("supports enum without description field")
  void supportsNoDescriptionField() {
    //GIVEN
    Class noDescriptionFieldClass = ChangedCodeFieldNameErrorEnum.class;

    //WHEN
    //simpErrorEnumParser.supports(noDescriptionFieldClass);

    //THEN
    assertThrows(CannotParseEnumException.class, () -> simpErrorEnumParser.supports(noDescriptionFieldClass), "ErrorCode enum class must have 'String description' field.");
  }

  enum SucceedErrorEnum implements ErrorEnumInfo {
    MY_ERROR_CODE_1(HttpStatus.BAD_REQUEST, "my error code 1 of ErrorEnum1"),
    MY_ERROR_CODE_2(HttpStatus.FORBIDDEN, "my error code 2 of ErrorEnum1");

    private HttpStatus code;
    private String description;

    SucceedErrorEnum(HttpStatus code, String description) {
      this.code = code;
      this.description = description;
    }

    @Override
    public HttpStatus getCode() {
      return code;
    }

    @Override
    public String getDescription() {
      return description;
    }
  }

  enum ErrorEnum2xx implements ErrorEnumInfo {
    MY_ERROR_CODE_1(HttpStatus.OK, "my error code 1 of ErrorEnum1"),
    MY_ERROR_CODE_2(HttpStatus.ACCEPTED, "my error code 2 of ErrorEnum1");

    private HttpStatus code;
    private String description;

    ErrorEnum2xx(HttpStatus code, String description) {
      this.code = code;
      this.description = description;
    }

    @Override
    public HttpStatus getCode() {
      return code;
    }

    @Override
    public String getDescription() {
      return description;
    }
  }

  class NotEnumClass {}

  enum NotInheritErrorEnumInfoErrorEnum {
    MY_ERROR_CODE_1(HttpStatus.BAD_REQUEST, "my error code 1 of ErrorEnum1"),
    MY_ERROR_CODE_2(HttpStatus.FORBIDDEN, "my error code 2 of ErrorEnum1");

    private HttpStatus code;
    private String description;

    NotInheritErrorEnumInfoErrorEnum(HttpStatus code, String description) {
      this.code = code;
      this.description = description;
    }

    public HttpStatus getCode() {
      return code;
    }

    public String getDescription() {
      return description;
    }
  }

  enum ChangedCodeFieldNameErrorEnum implements ErrorEnumInfo {
    MY_ERROR_CODE_1(HttpStatus.BAD_REQUEST, "my error code 1 of ErrorEnum1"),
    MY_ERROR_CODE_2(HttpStatus.FORBIDDEN, "my error code 2 of ErrorEnum1");

    private HttpStatus myCode;
    private String description;

    ChangedCodeFieldNameErrorEnum(HttpStatus code, String description) {
      this.myCode = code;
      this.description = description;
    }

    @Override
    public HttpStatus getCode() {
      return myCode;
    }

    @Override
    public String getDescription() {
      return description;
    }
  }

  enum ChangedDescriptionFieldNameErrorEnum implements ErrorEnumInfo {
    MY_ERROR_CODE_1(HttpStatus.BAD_REQUEST, "my error code 1 of ErrorEnum1"),
    MY_ERROR_CODE_2(HttpStatus.FORBIDDEN, "my error code 2 of ErrorEnum1");

    private HttpStatus code;
    private String myDescription;

    ChangedDescriptionFieldNameErrorEnum(HttpStatus code, String description) {
      this.code = code;
      this.myDescription = description;
    }

    @Override
    public HttpStatus getCode() {
      return code;
    }

    @Override
    public String getDescription() {
      return myDescription;
    }
  }
}