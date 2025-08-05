package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import org.junit.Test;

/**
 * Unit tests for the ToNumberPolicy strategies.
 */
public class ToNumberPolicyTest {

  @Test
  public void testDoubleStrategy() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.DOUBLE;

    // Test reading valid numbers
    assertThat(strategy.readNumber(createJsonReader("10.1"))).isEqualTo(10.1);
    assertThat(strategy.readNumber(createJsonReader("3.141592653589793238462643383279")))
        .isEqualTo(3.141592653589793D);

    // Test reading a number that results in infinity
    MalformedJsonException exception = assertThrows(
        MalformedJsonException.class,
        () -> strategy.readNumber(createJsonReader("1e400"))
    );
    assertThat(exception)
        .hasMessageThat()
        .isEqualTo(
            "JSON forbids NaN and infinities: Infinity at line 1 column 6 path $\n"
                + "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json"
        );

    // Test reading an invalid number format
    assertThrows(
        NumberFormatException.class,
        () -> strategy.readNumber(createJsonReader("\"not-a-number\""))
    );
  }

  @Test
  public void testLazilyParsedNumberStrategy() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;

    // Test reading valid numbers
    assertThat(strategy.readNumber(createJsonReader("10.1"))).isEqualTo(new LazilyParsedNumber("10.1"));
    assertThat(strategy.readNumber(createJsonReader("3.141592653589793238462643383279")))
        .isEqualTo(new LazilyParsedNumber("3.141592653589793238462643383279"));
    assertThat(strategy.readNumber(createJsonReader("1e400"))).isEqualTo(new LazilyParsedNumber("1e400"));
  }

  @Test
  public void testLongOrDoubleStrategy() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;

    // Test reading valid numbers
    assertThat(strategy.readNumber(createJsonReader("10"))).isEqualTo(10L);
    assertThat(strategy.readNumber(createJsonReader("10.1"))).isEqualTo(10.1);
    assertThat(strategy.readNumber(createJsonReader("3.141592653589793238462643383279")))
        .isEqualTo(3.141592653589793D);

    // Test reading a number that results in infinity
    assertMalformedJsonException(strategy, "1e400", "Infinity");

    // Test reading an invalid number format
    assertJsonParseException(strategy, "\"not-a-number\"", "Cannot parse not-a-number");

    // Test reading special floating-point values in lenient mode
    assertThat(strategy.readNumber(createLenientJsonReader("NaN"))).isEqualTo(Double.NaN);
    assertThat(strategy.readNumber(createLenientJsonReader("Infinity"))).isEqualTo(Double.POSITIVE_INFINITY);
    assertThat(strategy.readNumber(createLenientJsonReader("-Infinity"))).isEqualTo(Double.NEGATIVE_INFINITY);

    // Test reading special floating-point values in strict mode
    assertMalformedJsonException(strategy, "NaN", "malformed JSON");
    assertMalformedJsonException(strategy, "Infinity", "malformed JSON");
    assertMalformedJsonException(strategy, "-Infinity", "malformed JSON");
  }

  @Test
  public void testBigDecimalStrategy() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.BIG_DECIMAL;

    // Test reading valid numbers
    assertThat(strategy.readNumber(createJsonReader("10.1"))).isEqualTo(new BigDecimal("10.1"));
    assertThat(strategy.readNumber(createJsonReader("3.141592653589793238462643383279")))
        .isEqualTo(new BigDecimal("3.141592653589793238462643383279"));
    assertThat(strategy.readNumber(createJsonReader("1e400"))).isEqualTo(new BigDecimal("1e400"));

    // Test reading an invalid number format
    assertJsonParseException(strategy, "\"not-a-number\"", "Cannot parse not-a-number");
  }

  @Test
  public void testNullValues() throws IOException {
    // Test that null values are never expected for any strategy
    assertIllegalStateException(ToNumberPolicy.DOUBLE, "Expected a double but was NULL");
    assertIllegalStateException(ToNumberPolicy.LAZILY_PARSED_NUMBER, "Expected a string but was NULL");
    assertIllegalStateException(ToNumberPolicy.LONG_OR_DOUBLE, "Expected a string but was NULL");
    assertIllegalStateException(ToNumberPolicy.BIG_DECIMAL, "Expected a string but was NULL");
  }

  // Helper method to create a JsonReader from a JSON string
  private static JsonReader createJsonReader(String json) {
    return new JsonReader(new StringReader(json));
  }

  // Helper method to create a lenient JsonReader from a JSON string
  private static JsonReader createLenientJsonReader(String json) {
    JsonReader jsonReader = createJsonReader(json);
    jsonReader.setStrictness(Strictness.LENIENT);
    return jsonReader;
  }

  // Helper method to assert MalformedJsonException
  private void assertMalformedJsonException(ToNumberStrategy strategy, String json, String expectedMessage) {
    MalformedJsonException exception = assertThrows(
        MalformedJsonException.class,
        () -> strategy.readNumber(createJsonReader(json))
    );
    assertThat(exception)
        .hasMessageThat()
        .contains(expectedMessage);
  }

  // Helper method to assert JsonParseException
  private void assertJsonParseException(ToNumberStrategy strategy, String json, String expectedMessage) {
    JsonParseException exception = assertThrows(
        JsonParseException.class,
        () -> strategy.readNumber(createJsonReader(json))
    );
    assertThat(exception)
        .hasMessageThat()
        .contains(expectedMessage);
  }

  // Helper method to assert IllegalStateException
  private void assertIllegalStateException(ToNumberStrategy strategy, String expectedMessage) {
    IllegalStateException exception = assertThrows(
        IllegalStateException.class,
        () -> strategy.readNumber(createJsonReader("null"))
    );
    assertThat(exception)
        .hasMessageThat()
        .contains(expectedMessage);
  }
}