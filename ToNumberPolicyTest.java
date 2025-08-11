/*
 * Copyright (C) 2021 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import com.google.gson.stream.Strictness;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import org.junit.Test;

/** Tests for {@link ToNumberPolicy}. */
public class ToNumberPolicyTest {

  // region DOUBLE policy tests
  @Test
  public void doublePolicy_readsNumbersAsDouble() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.DOUBLE;

    // Test a simple decimal
    assertThat(strategy.readNumber(fromString("10.1"))).isEqualTo(10.1);

    // Test a high-precision decimal, which will be parsed as a standard double
    assertThat(strategy.readNumber(fromString("3.141592653589793238462643383279")))
        .isEqualTo(3.141592653589793D);
  }

  @Test
  public void doublePolicy_throwsMalformedJsonException_forInfinity() {
    ToNumberStrategy strategy = ToNumberPolicy.DOUBLE;

    MalformedJsonException e =
        assertThrows(MalformedJsonException.class, () -> strategy.readNumber(fromString("1e400")));
    assertThat(e).hasMessageThat().contains("JSON forbids NaN and infinities: Infinity");
  }

  @Test
  public void doublePolicy_throwsNumberFormatException_forNonNumericString() {
    ToNumberStrategy strategy = ToNumberPolicy.DOUBLE;

    // The underlying parser tries to parse the string content as a number
    assertThrows(
        NumberFormatException.class, () -> strategy.readNumber(fromString("\"not-a-number\"")));
  }
  // endregion

  // region LAZILY_PARSED_NUMBER policy tests
  @Test
  public void lazilyParsedNumberPolicy_readsNumbersAsLazilyParsedNumber() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;

    assertThat(strategy.readNumber(fromString("10.1"))).isEqualTo(new LazilyParsedNumber("10.1"));

    String highPrecisionNumber = "3.141592653589793238462643383279";
    assertThat(strategy.readNumber(fromString(highPrecisionNumber)))
        .isEqualTo(new LazilyParsedNumber(highPrecisionNumber));

    // This policy should handle numbers that would overflow a double
    String largeNumber = "1e400";
    assertThat(strategy.readNumber(fromString(largeNumber)))
        .isEqualTo(new LazilyParsedNumber(largeNumber));
  }
  // endregion

  // region LONG_OR_DOUBLE policy tests
  @Test
  public void longOrDoublePolicy_readsIntegralAsLong() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;
    assertThat(strategy.readNumber(fromString("10"))).isEqualTo(10L);
  }

  @Test
  public void longOrDoublePolicy_readsDecimalAsDouble() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;
    assertThat(strategy.readNumber(fromString("10.1"))).isEqualTo(10.1);
    assertThat(strategy.readNumber(fromString("3.141592653589793238462643383279")))
        .isEqualTo(3.141592653589793D);
  }

  @Test
  public void longOrDoublePolicy_throwsMalformedJsonException_forLargeNumber() {
    ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;
    MalformedJsonException e =
        assertThrows(MalformedJsonException.class, () -> strategy.readNumber(fromString("1e400")));
    assertThat(e).hasMessageThat().contains("JSON forbids NaN and infinities: Infinity");
  }

  @Test
  public void longOrDoublePolicy_throwsJsonParseException_forNonNumericString() {
    ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;
    JsonParseException e =
        assertThrows(
            JsonParseException.class, () -> strategy.readNumber(fromString("\"not-a-number\"")));
    assertThat(e).hasMessageThat().contains("Cannot parse not-a-number");
  }

  @Test
  public void longOrDoublePolicy_readsSpecialDoubles_whenLenient() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;
    assertThat(strategy.readNumber(fromStringLenient("NaN"))).isEqualTo(Double.NaN);
    assertThat(strategy.readNumber(fromStringLenient("Infinity")))
        .isEqualTo(Double.POSITIVE_INFINITY);
    assertThat(strategy.readNumber(fromStringLenient("-Infinity")))
        .isEqualTo(Double.NEGATIVE_INFINITY);
  }

  @Test
  public void longOrDoublePolicy_throwsMalformedJsonException_forSpecialDoubles_whenStrict() {
    ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;
    String expectedMessage =
        "Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON";

    MalformedJsonException e1 =
        assertThrows(MalformedJsonException.class, () -> strategy.readNumber(fromString("NaN")));
    assertThat(e1).hasMessageThat().contains(expectedMessage);

    MalformedJsonException e2 =
        assertThrows(MalformedJsonException.class, () -> strategy.readNumber(fromString("Infinity")));
    assertThat(e2).hasMessageThat().contains(expectedMessage);

    MalformedJsonException e3 =
        assertThrows(
            MalformedJsonException.class, () -> strategy.readNumber(fromString("-Infinity")));
    assertThat(e3).hasMessageThat().contains(expectedMessage);
  }
  // endregion

  // region BIG_DECIMAL policy tests
  @Test
  public void bigDecimalPolicy_readsNumbersAsBigDecimal() throws IOException {
    ToNumberStrategy strategy = ToNumberPolicy.BIG_DECIMAL;

    assertThat(strategy.readNumber(fromString("10.1"))).isEqualTo(new BigDecimal("10.1"));

    String highPrecisionNumber = "3.141592653589793238462643383279";
    assertThat(strategy.readNumber(fromString(highPrecisionNumber)))
        .isEqualTo(new BigDecimal(highPrecisionNumber));

    // This policy should handle numbers that would overflow a double
    String largeNumber = "1e400";
    assertThat(strategy.readNumber(fromString(largeNumber)))
        .isEqualTo(new BigDecimal(largeNumber));
  }

  @Test
  public void bigDecimalPolicy_throwsJsonParseException_forNonNumericString() {
    ToNumberStrategy strategy = ToNumberPolicy.BIG_DECIMAL;
    JsonParseException e =
        assertThrows(
            JsonParseException.class, () -> strategy.readNumber(fromString("\"not-a-number\"")));
    assertThat(e).hasMessageThat().contains("Cannot parse not-a-number");
  }
  // endregion

  // region General policy behavior tests
  @Test
  public void allPolicies_throwIllegalStateException_forNull() {
    // The readNumber methods are not designed to be null-safe and should throw
    // IllegalStateException when the next token is a JSON null.

    // Test DOUBLE policy
    ToNumberStrategy doubleStrategy = ToNumberPolicy.DOUBLE;
    IllegalStateException e1 =
        assertThrows(
            IllegalStateException.class, () -> doubleStrategy.readNumber(fromString("null")));
    assertThat(e1).hasMessageThat().contains("Expected a double but was NULL");

    // Test LAZILY_PARSED_NUMBER policy
    ToNumberStrategy lazilyParsedNumberStrategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;
    IllegalStateException e2 =
        assertThrows(
            IllegalStateException.class,
            () -> lazilyParsedNumberStrategy.readNumber(fromString("null")));
    assertThat(e2).hasMessageThat().contains("Expected a string but was NULL");

    // Test LONG_OR_DOUBLE policy
    ToNumberStrategy longOrDoubleStrategy = ToNumberPolicy.LONG_OR_DOUBLE;
    IllegalStateException e3 =
        assertThrows(
            IllegalStateException.class,
            () -> longOrDoubleStrategy.readNumber(fromString("null")));
    assertThat(e3).hasMessageThat().contains("Expected a string but was NULL");

    // Test BIG_DECIMAL policy
    ToNumberStrategy bigDecimalStrategy = ToNumberPolicy.BIG_DECIMAL;
    IllegalStateException e4 =
        assertThrows(
            IllegalStateException.class, () -> bigDecimalStrategy.readNumber(fromString("null")));
    assertThat(e4).hasMessageThat().contains("Expected a string but was NULL");
  }
  // endregion

  private static JsonReader fromString(String json) {
    return new JsonReader(new StringReader(json));
  }

  private static JsonReader fromStringLenient(String json) {
    JsonReader jsonReader = fromString(json);
    jsonReader.setStrictness(Strictness.LENIENT);
    return jsonReader;
  }
}