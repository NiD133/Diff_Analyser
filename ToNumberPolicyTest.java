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
 * Tests for ToNumberPolicy. Focus is on readability: short, focused tests with clear intent,
 * minimal duplication, and descriptive helpers.
 */
public class ToNumberPolicyTest {

  private static final String MALFORMED_JSON_TROUBLESHOOTING_URL =
      "https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json";
  private static final String ADAPTER_NOT_NULL_SAFE_TROUBLESHOOTING_URL =
      "https://github.com/google/gson/blob/main/Troubleshooting.md#adapter-not-null-safe";

  // --- DOUBLE policy ---

  @Test
  public void doublePolicy_readsFiniteValues() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.DOUBLE;

    assertThat(strategy.readNumber(newStrictReader("10.1"))).isEqualTo(10.1);
    // Long-precision input should be truncated to double precision (Math.PI)
    assertThat(strategy.readNumber(newStrictReader("3.141592653589793238462643383279")))
        .isEqualTo(Math.PI);
  }

  @Test
  public void doublePolicy_forbidsInfinityAndNaN_inStrictMode() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.DOUBLE;

    MalformedJsonException ex =
        assertThrows(MalformedJsonException.class, () -> strategy.readNumber(newStrictReader("1e400")));
    assertThat(ex)
        .hasMessageThat()
        .isEqualTo(
            "JSON forbids NaN and infinities: Infinity at line 1 column 6 path $\n"
                + "See " + MALFORMED_JSON_TROUBLESHOOTING_URL);
  }

  @Test
  public void doublePolicy_rejectsNonNumericString() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.DOUBLE;

    assertThrows(NumberFormatException.class, () -> strategy.readNumber(newStrictReader("\"not-a-number\"")));
  }

  // --- LAZILY_PARSED_NUMBER policy ---

  @Test
  public void lazilyParsedNumber_returnsParsedWrapper() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;

    assertThat(strategy.readNumber(newStrictReader("10.1"))).isEqualTo(new LazilyParsedNumber("10.1"));
    assertThat(strategy.readNumber(newStrictReader("3.141592653589793238462643383279")))
        .isEqualTo(new LazilyParsedNumber("3.141592653589793238462643383279"));
    assertThat(strategy.readNumber(newStrictReader("1e400")))
        .isEqualTo(new LazilyParsedNumber("1e400"));
  }

  // --- LONG_OR_DOUBLE policy ---

  @Test
  public void longOrDouble_returnsLongOrDoubleDependingOnInput() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;

    assertThat(strategy.readNumber(newStrictReader("10"))).isEqualTo(10L);
    assertThat(strategy.readNumber(newStrictReader("10.1"))).isEqualTo(10.1);
    // Long-precision input should be truncated to double precision (Math.PI)
    assertThat(strategy.readNumber(newStrictReader("3.141592653589793238462643383279")))
        .isEqualTo(Math.PI);
  }

  @Test
  public void longOrDouble_forbidsInfinityInStrictMode() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;

    Exception ex =
        assertThrows(MalformedJsonException.class, () -> strategy.readNumber(newStrictReader("1e400")));
    assertThat(ex)
        .hasMessageThat()
        .isEqualTo("JSON forbids NaN and infinities: Infinity; at path $");
  }

  @Test
  public void longOrDouble_rejectsNonNumericString() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;

    JsonParseException ex =
        assertThrows(JsonParseException.class, () -> strategy.readNumber(newStrictReader("\"not-a-number\"")));
    assertThat(ex).hasMessageThat().isEqualTo("Cannot parse not-a-number; at path $");
  }

  @Test
  public void longOrDouble_acceptsSpecialDoublesWhenLenient() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;

    assertThat(strategy.readNumber(newLenientReader("NaN"))).isEqualTo(Double.NaN);
    assertThat(strategy.readNumber(newLenientReader("Infinity"))).isEqualTo(Double.POSITIVE_INFINITY);
    assertThat(strategy.readNumber(newLenientReader("-Infinity"))).isEqualTo(Double.NEGATIVE_INFINITY);
  }

  @Test
  public void longOrDouble_rejectsSpecialDoublesWhenStrict() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;

    String strictnessMsg =
        "Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 1 column 1 path $\n"
            + "See " + MALFORMED_JSON_TROUBLESHOOTING_URL;

    MalformedJsonException ex1 =
        assertThrows(MalformedJsonException.class, () -> strategy.readNumber(newStrictReader("NaN")));
    assertThat(ex1).hasMessageThat().isEqualTo(strictnessMsg);

    MalformedJsonException ex2 =
        assertThrows(MalformedJsonException.class, () -> strategy.readNumber(newStrictReader("Infinity")));
    assertThat(ex2).hasMessageThat().isEqualTo(strictnessMsg);

    MalformedJsonException ex3 =
        assertThrows(MalformedJsonException.class, () -> strategy.readNumber(newStrictReader("-Infinity")));
    assertThat(ex3).hasMessageThat().isEqualTo(strictnessMsg);
  }

  // --- BIG_DECIMAL policy ---

  @Test
  public void bigDecimal_readsArbitraryPrecisionValues() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.BIG_DECIMAL;

    assertThat(strategy.readNumber(newStrictReader("10.1"))).isEqualTo(new BigDecimal("10.1"));
    assertThat(strategy.readNumber(newStrictReader("3.141592653589793238462643383279")))
        .isEqualTo(new BigDecimal("3.141592653589793238462643383279"));
    assertThat(strategy.readNumber(newStrictReader("1e400")))
        .isEqualTo(new BigDecimal("1e400"));
  }

  @Test
  public void bigDecimal_rejectsNonNumericString() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.BIG_DECIMAL;

    JsonParseException ex =
        assertThrows(JsonParseException.class, () -> strategy.readNumber(newStrictReader("\"not-a-number\"")));
    assertThat(ex).hasMessageThat().isEqualTo("Cannot parse not-a-number; at path $");
  }

  // --- Null handling across all policies ---

  @Test
  public void nullsAreNeverExpected() throws IOException {
    assertNullRejected(
        ToNumberPolicy.DOUBLE,
        "Expected a double but was NULL at line 1 column 5 path $\n"
            + "See " + ADAPTER_NOT_NULL_SAFE_TROUBLESHOOTING_URL);

    assertNullRejected(
        ToNumberPolicy.LAZILY_PARSED_NUMBER,
        "Expected a string but was NULL at line 1 column 5 path $\n"
            + "See " + ADAPTER_NOT_NULL_SAFE_TROUBLESHOOTING_URL);

    assertNullRejected(
        ToNumberPolicy.LONG_OR_DOUBLE,
        "Expected a string but was NULL at line 1 column 5 path $\n"
            + "See " + ADAPTER_NOT_NULL_SAFE_TROUBLESHOOTING_URL);

    assertNullRejected(
        ToNumberPolicy.BIG_DECIMAL,
        "Expected a string but was NULL at line 1 column 5 path $\n"
            + "See " + ADAPTER_NOT_NULL_SAFE_TROUBLESHOOTING_URL);
  }

  // --- Helpers ---

  private static JsonReader newStrictReader(String json) {
    return new JsonReader(new StringReader(json));
  }

  private static JsonReader newLenientReader(String json) {
    JsonReader reader = newStrictReader(json);
    reader.setStrictness(Strictness.LENIENT);
    return reader;
  }

  private static void assertNullRejected(ToNumberStrategy strategy, String expectedMessage)
      throws IOException {
    IllegalStateException ex =
        assertThrows(IllegalStateException.class, () -> strategy.readNumber(newStrictReader("null")));
    assertThat(ex).hasMessageThat().isEqualTo(expectedMessage);
  }
}