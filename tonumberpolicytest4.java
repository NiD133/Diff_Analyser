package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import org.junit.Test;

/** Tests for the {@link ToNumberPolicy#BIG_DECIMAL} strategy. */
public class ToNumberPolicyTest {

  private static final ToNumberStrategy BIG_DECIMAL_STRATEGY = ToNumberPolicy.BIG_DECIMAL;

  private static JsonReader fromString(String json) {
    return new JsonReader(new StringReader(json));
  }

  @Test
  public void bigDecimalPolicy_readsSimpleDecimal() throws IOException {
    String json = "10.1";
    Number result = BIG_DECIMAL_STRATEGY.readNumber(fromString(json));
    assertThat(result).isEqualTo(new BigDecimal("10.1"));
  }

  @Test
  public void bigDecimalPolicy_readsHighPrecisionDecimal() throws IOException {
    String piJson = "3.141592653589793238462643383279";
    Number result = BIG_DECIMAL_STRATEGY.readNumber(fromString(piJson));
    assertThat(result).isEqualTo(new BigDecimal(piJson));
  }

  @Test
  public void bigDecimalPolicy_readsScientificNotation() throws IOException {
    String json = "1e400";
    Number result = BIG_DECIMAL_STRATEGY.readNumber(fromString(json));
    assertThat(result).isEqualTo(new BigDecimal("1e400"));
  }

  @Test
  public void bigDecimalPolicy_throwsOnNonNumericValue() {
    String json = "\"not-a-number\"";

    JsonParseException exception =
        assertThrows(
            JsonParseException.class, () -> BIG_DECIMAL_STRATEGY.readNumber(fromString(json)));

    assertThat(exception).hasMessageThat().isEqualTo("Cannot parse not-a-number; at path $");
  }
}