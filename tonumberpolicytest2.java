package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link ToNumberPolicy#LAZILY_PARSED_NUMBER}.
 *
 * <p>This policy ensures that numbers are read as a {@link LazilyParsedNumber},
 * preserving their original string representation to prevent precision loss or overflow
 * until a specific numeric type is requested.
 */
public class ToNumberPolicyLazilyParsedNumberTest {

  private final ToNumberStrategy lazilyParsedNumberStrategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;

  private static JsonReader fromString(String json) {
    return new JsonReader(new StringReader(json));
  }

  @Test
  public void readNumber_whenDecimal_returnsLazilyParsedNumber() throws IOException {
    String jsonNumber = "10.1";
    Number result = lazilyParsedNumberStrategy.readNumber(fromString(jsonNumber));

    // The policy should wrap the number without parsing it, preserving the original string.
    assertThat(result).isEqualTo(new LazilyParsedNumber(jsonNumber));
    assertThat(result.toString()).isEqualTo(jsonNumber);
  }

  @Test
  public void readNumber_whenHighPrecisionDecimal_returnsLazilyParsedNumber() throws IOException {
    String jsonNumber = "3.141592653589793238462643383279";
    Number result = lazilyParsedNumberStrategy.readNumber(fromString(jsonNumber));

    // Using LazilyParsedNumber is crucial here to prevent the precision loss that would
    // occur if this number were immediately parsed as a `double`.
    assertThat(result).isEqualTo(new LazilyParsedNumber(jsonNumber));
    assertThat(result.toString()).isEqualTo(jsonNumber);
  }

  @Test
  public void readNumber_whenLargeExponent_returnsLazilyParsedNumber() throws IOException {
    String jsonNumber = "1e400";
    Number result = lazilyParsedNumberStrategy.readNumber(fromString(jsonNumber));

    // Using LazilyParsedNumber prevents this value from being parsed as `Double.POSITIVE_INFINITY`.
    // The original string representation is preserved for later conversion, for example to a BigDecimal.
    assertThat(result).isEqualTo(new LazilyParsedNumber(jsonNumber));
    assertThat(result.toString()).isEqualTo(jsonNumber);
  }
}