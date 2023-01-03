package springfox.error.response.extension.service;

import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import springfox.error.response.extension.annotation.ErrorResponse;
import springfox.error.response.extension.exception.CannotParseEnumException;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willReturn;

class SimpErrorResponseDescBuilderTest {
  @Mock
  private ErrorEnumParser errorEnumParser;
  private SimpErrorResponseDescBuilder simpErrorResponseDescBuilder;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    simpErrorResponseDescBuilder = new SimpErrorResponseDescBuilder(errorEnumParser);
  }

  @Test
  @DisplayName("Description Build Succeed")
  void buildDescriptionSucceed() throws CannotParseEnumException {
    //GIVEN
    // Create ErrorResponse Annotation Object
    ErrorResponse errorResponseAnnotation = getErrorResponseAnnotation();
    Class errorEnum = errorResponseAnnotation.errorEnums()[0];

    // Mock - errorEnumParser.parse(...)
    Map<ErrorEnumInfo, Map<String, String>> constantInfoMap1 = getErrorEnum1ParseResult();
    Map<ErrorEnumInfo, Map<String, String>> constantInfoMap2 = getErrorEnum2ParseResult();
    willReturn(constantInfoMap1).given(errorEnumParser).parse(ErrorEnum1.class);
    willReturn(constantInfoMap2).given(errorEnumParser).parse(ErrorEnum2.class);

    //WHEN
    Map<String, String> result = simpErrorResponseDescBuilder.buildDescription(errorResponseAnnotation);

    //THEN
    String httpStatus1 = String.valueOf(ErrorEnum1.MY_ERROR_CODE_1.getCode().value());
    String httpStatus2 = String.valueOf(ErrorEnum1.MY_ERROR_CODE_2.getCode().value());
    String descriptionOfHttpStatus1 = result.get(httpStatus1);
    String descriptionOfHttpStatus2 = result.get(httpStatus2);

    assertAll(
        //Pre-test condition
        () -> assertAll(
            // ErrorEnum1.MY_ERROR_CODE_1.getCode() == ErrorEnum2.MY_ERROR_CODE_1.getCode()
            () -> assertSame(ErrorEnum1.MY_ERROR_CODE_1.getCode(), ErrorEnum2.MY_ERROR_CODE_1.getCode()),
            // ErrorEnum1.MY_ERROR_CODE_2.getCode() == ErrorEnum2.MY_ERROR_CODE_2.getCode()
            () -> assertSame(ErrorEnum1.MY_ERROR_CODE_2.getCode(), ErrorEnum2.MY_ERROR_CODE_2.getCode())
        ),
        //check description
        () -> assertAll(
            () -> assertEquals(ErrorEnum1.MY_ERROR_CODE_1.getCode().name()
                    + "\n\n- `" + ErrorEnum1.MY_ERROR_CODE_1 + "`: " + ErrorEnum1.MY_ERROR_CODE_1.getDescription()
                    + "\n\n- `" + ErrorEnum2.MY_ERROR_CODE_1 + "`: " + ErrorEnum2.MY_ERROR_CODE_1.getDescription(),
                descriptionOfHttpStatus1
            ),
            () -> assertEquals(ErrorEnum1.MY_ERROR_CODE_2.getCode().name()
                    + "\n\n- `" + ErrorEnum1.MY_ERROR_CODE_2 + "`: " + ErrorEnum1.MY_ERROR_CODE_2.getDescription()
                    + "\n\n- `" + ErrorEnum2.MY_ERROR_CODE_2 + "`: " + ErrorEnum2.MY_ERROR_CODE_2.getDescription(),
                descriptionOfHttpStatus2
            )
        )
    );
  }

  private ErrorResponse getErrorResponseAnnotation() {
    ErrorResponse errorResponseAnnotation = new ErrorResponse() {

      @Override
      public Class<? extends Annotation> annotationType() {
        return ErrorResponse.class;
      }

      @Override
      public Class[] errorEnums() {
        return new Class[] {ErrorEnum1.class, ErrorEnum2.class};
      }

      @Override
      public Header[] headers() {
        return null;
      }

      @Override
      public Content[] content() {
        return null;
      }
    };
    return errorResponseAnnotation;
  }

  private Map<ErrorEnumInfo, Map<String, String>> getErrorEnum1ParseResult() {
    Map<ErrorEnumInfo, Map<String, String>> constantInfoMap = new HashMap<>();
    Map<String, String> fieldInfoMap1 = new HashMap<>();
    Map<String, String> fieldInfoMap2 = new HashMap<>();
    fieldInfoMap1.put(ErrorEnumInfo.CODE_FIELD_NAME, String.valueOf(ErrorEnum1.MY_ERROR_CODE_1.getCode().value()));
    fieldInfoMap1.put(ErrorEnumInfo.DESCRIPTION_FIELD_NAME, ErrorEnum1.MY_ERROR_CODE_1.getDescription());
    fieldInfoMap2.put(ErrorEnumInfo.CODE_FIELD_NAME, String.valueOf(ErrorEnum1.MY_ERROR_CODE_2.getCode().value()));
    fieldInfoMap2.put(ErrorEnumInfo.DESCRIPTION_FIELD_NAME, ErrorEnum1.MY_ERROR_CODE_2.getDescription());
    constantInfoMap.put(ErrorEnum1.MY_ERROR_CODE_1, fieldInfoMap1);
    constantInfoMap.put(ErrorEnum1.MY_ERROR_CODE_2, fieldInfoMap2);
    return constantInfoMap;
  }

  private Map<ErrorEnumInfo, Map<String, String>> getErrorEnum2ParseResult() {
    Map<ErrorEnumInfo, Map<String, String>> constantInfoMap = new HashMap<>();
    Map<String, String> fieldInfoMap1 = new HashMap<>();
    Map<String, String> fieldInfoMap2 = new HashMap<>();
    fieldInfoMap1.put(ErrorEnumInfo.CODE_FIELD_NAME, String.valueOf(ErrorEnum2.MY_ERROR_CODE_1.getCode().value()));
    fieldInfoMap1.put(ErrorEnumInfo.DESCRIPTION_FIELD_NAME, ErrorEnum2.MY_ERROR_CODE_1.getDescription());
    fieldInfoMap2.put(ErrorEnumInfo.CODE_FIELD_NAME, String.valueOf(ErrorEnum2.MY_ERROR_CODE_2.getCode().value()));
    fieldInfoMap2.put(ErrorEnumInfo.DESCRIPTION_FIELD_NAME, ErrorEnum2.MY_ERROR_CODE_2.getDescription());
    constantInfoMap.put(ErrorEnum2.MY_ERROR_CODE_1, fieldInfoMap1);
    constantInfoMap.put(ErrorEnum2.MY_ERROR_CODE_2, fieldInfoMap2);
    return constantInfoMap;
  }

  enum ErrorEnum1 implements ErrorEnumInfo {
    MY_ERROR_CODE_1(HttpStatus.BAD_REQUEST, "my error code 1 of ErrorEnum1"),
    MY_ERROR_CODE_2(HttpStatus.FORBIDDEN, "my error code 2 of ErrorEnum1");

    private HttpStatus code;
    private String description;

    ErrorEnum1(HttpStatus code, String description) {
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

  enum ErrorEnum2 implements ErrorEnumInfo {
    MY_ERROR_CODE_1(HttpStatus.BAD_REQUEST, "my error code 1 of ErrorEnum2"),
    MY_ERROR_CODE_2(HttpStatus.FORBIDDEN, "my error code 2 of ErrorEnum2");

    private HttpStatus code;
    private String description;

    ErrorEnum2(HttpStatus code, String description) {
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
}