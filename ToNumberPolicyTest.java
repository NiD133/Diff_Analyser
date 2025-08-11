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
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import org.junit.Test;

/**
 * Tests for ToNumberPolicy enum which defines different strategies for parsing JSON numbers.
 * Each policy handles number parsing differently based on the desired output type and precision requirements.
 */
public class ToNumberPolicyTest {

  // Test data constants for better readability and maintainability
  private static final String SIMPLE_DECIMAL = "10.1";
  private static final String HIGH_PRECISION_PI = "3.141592653589793238462643383279";
  private static final String LARGE_EXPONENT = "1e400"; // Causes overflow to infinity
  private static final String INVALID_NUMBER_STRING = "\"not-a-number\"";
  private static final String SIMPLE_INTEGER = "10";
  private static final String NULL_VALUE = "null";
  
  // Special values for lenient parsing
  private static final String NAN_VALUE = "NaN";
  private static final String POSITIVE_INFINITY = "Infinity";
  private static final String NEGATIVE_INFINITY = "-Infinity";

  @Test
  public void testDoublePolicy_ParsesValidNumbers() throws IOException {
    // Given: DOUBLE policy converts all numbers to Double type
    ToNumberStrategy doublePolicy = ToNumberPolicy.DOUBLE;
    
    // When & Then: Simple decimal is parsed correctly
    assertThat(doublePolicy.readNumber(createJsonReader(SIMPLE_DECIMAL)))
        .isEqualTo(10.1);
    
    // When & Then: High precision number is parsed with double precision limits
    assertThat(doublePolicy.readNumber(createJsonReader(HIGH_PRECISION_PI)))
        .isEqualTo(3.141592653589793D);
  }

  @Test
  public void testDoublePolicy_RejectsInfinityInStrictMode() throws IOException {
    ToNumberStrategy doublePolicy = ToNumberPolicy.DOUBLE;
    
    // When: Parsing a number that would result in infinity
    // Then: Should throw MalformedJsonException in strict mode
    MalformedJsonException exception = assertThrows(
        MalformedJsonException.class, 
        () -> doublePolicy.readNumber(createJsonReader(LARGE_EXPONENT))
    );
    
    assertThat(exception)
        .hasMessageThat()
        .contains("JSON forbids NaN and infinities: Infinity");
  }

  @Test
  public void testDoublePolicy_RejectsInvalidNumberStrings() throws IOException {
    ToNumberStrategy doublePolicy = ToNumberPolicy.DOUBLE;
    
    // When: Parsing an invalid number string
    // Then: Should throw NumberFormatException
    assertThrows(
        NumberFormatException.class, 
        () -> doublePolicy.readNumber(createJsonReader(INVALID_NUMBER_STRING))
    );
  }

  @Test
  public void testLazilyParsedNumberPolicy_PreservesOriginalStringRepresentation() throws IOException {
    // Given: LAZILY_PARSED_NUMBER policy preserves exact string representation
    ToNumberStrategy lazyPolicy = ToNumberPolicy.LAZILY_PARSED_NUMBER;
    
    // When & Then: All numbers are wrapped in LazilyParsedNumber preserving original format
    assertThat(lazyPolicy.readNumber(createJsonReader(SIMPLE_DECIMAL)))
        .isEqualTo(new LazilyParsedNumber("10.1"));
    
    assertThat(lazyPolicy.readNumber(createJsonReader(HIGH_PRECISION_PI)))
        .isEqualTo(new LazilyParsedNumber("3.141592653589793238462643383279"));
    
    // Even large exponents that would overflow are preserved as strings
    assertThat(lazyPolicy.readNumber(createJsonReader(LARGE_EXPONENT)))
        .isEqualTo(new LazilyParsedNumber("1e400"));
  }

  @Test
  public void testLongOrDoublePolicy_ChoosesAppropriateType() throws IOException {
    // Given: LONG_OR_DOUBLE policy chooses Long for integers, Double for decimals
    ToNumberStrategy longOrDoublePolicy = ToNumberPolicy.LONG_OR_DOUBLE;
    
    // When & Then: Integer values are parsed as Long
    assertThat(longOrDoublePolicy.readNumber(createJsonReader(SIMPLE_INTEGER)))
        .isEqualTo(10L);
    
    // When & Then: Decimal values are parsed as Double
    assertThat(longOrDoublePolicy.readNumber(createJsonReader(SIMPLE_DECIMAL)))
        .isEqualTo(10.1);
    
    // When & Then: High precision decimals are limited by Double precision
    assertThat(longOrDoublePolicy.readNumber(createJsonReader(HIGH_PRECISION_PI)))
        .isEqualTo(3.141592653589793D);
  }

  @Test
  public void testLongOrDoublePolicy_RejectsInfinityInStrictMode() throws IOException {
    ToNumberStrategy longOrDoublePolicy = ToNumberPolicy.LONG_OR_DOUBLE;
    
    // When: Parsing number that results in infinity in strict mode
    // Then: Should throw MalformedJsonException
    Exception exception = assertThrows(
        MalformedJsonException.class, 
        () -> longOrDoublePolicy.readNumber(createJsonReader(LARGE_EXPONENT))
    );
    assertThat(exception)
        .hasMessageThat()
        .contains("JSON forbids NaN and infinities: Infinity");
  }

  @Test
  public void testLongOrDoublePolicy_RejectsInvalidNumberStrings() throws IOException {
    ToNumberStrategy longOrDoublePolicy = ToNumberPolicy.LONG_OR_DOUBLE;
    
    // When: Parsing invalid number string
    // Then: Should throw JsonParseException
    Exception exception = assertThrows(
        JsonParseException.class, 
        () -> longOrDoublePolicy.readNumber(createJsonReader(INVALID_NUMBER_STRING))
    );
    assertThat(exception)
        .hasMessageThat()
        .isEqualTo("Cannot parse not-a-number; at path $");
  }

  @Test
  public void testLongOrDoublePolicy_AcceptsSpecialValuesInLenientMode() throws IOException {
    ToNumberStrategy longOrDoublePolicy = ToNumberPolicy.LONG_OR_DOUBLE;
    
    // When & Then: Lenient mode accepts NaN and infinity values
    assertThat(longOrDoublePolicy.readNumber(createLenientJsonReader(NAN_VALUE)))
        .isEqualTo(Double.NaN);
    
    assertThat(longOrDoublePolicy.readNumber(createLenientJsonReader(POSITIVE_INFINITY)))
        .isEqualTo(Double.POSITIVE_INFINITY);
    
    assertThat(longOrDoublePolicy.readNumber(createLenientJsonReader(NEGATIVE_INFINITY)))
        .isEqualTo(Double.NEGATIVE_INFINITY);
  }

  @Test
  public void testLongOrDoublePolicy_RejectsSpecialValuesInStrictMode() throws IOException {
    ToNumberStrategy longOrDoublePolicy = ToNumberPolicy.LONG_OR_DOUBLE;
    
    // Test that NaN is rejected in strict mode
    Exception exception = assertThrows(
        MalformedJsonException.class, 
        () -> longOrDoublePolicy.readNumber(createJsonReader(NAN_VALUE))
    );
    assertThat(exception)
        .hasMessageThat()
        .contains("Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON");

    // Test that positive infinity is rejected in strict mode
    exception = assertThrows(
        MalformedJsonException.class, 
        () -> longOrDoublePolicy.readNumber(createJsonReader(POSITIVE_INFINITY))
    );
    assertThat(exception)
        .hasMessageThat()
        .contains("Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON");

    // Test that negative infinity is rejected in strict mode
    exception = assertThrows(
        MalformedJsonException.class, 
        () -> longOrDoublePolicy.readNumber(createJsonReader(NEGATIVE_INFINITY))
    );
    assertThat(exception)
        .hasMessageThat()
        .contains("Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON");
  }

  @Test
  public void testBigDecimalPolicy_PreservesArbitraryPrecision() throws IOException {
    // Given: BIG_DECIMAL policy preserves arbitrary precision
    ToNumberStrategy bigDecimalPolicy = ToNumberPolicy.BIG_DECIMAL;
    
    // When & Then: All numbers are parsed as BigDecimal with full precision
    assertThat(bigDecimalPolicy.readNumber(createJsonReader(SIMPLE_DECIMAL)))
        .isEqualTo(new BigDecimal("10.1"));
    
    assertThat(bigDecimalPolicy.readNumber(createJsonReader(HIGH_PRECISION_PI)))
        .isEqualTo(new BigDecimal("3.141592653589793238462643383279"));
    
    // Even very large exponents are handled without overflow
    assertThat(bigDecimalPolicy.readNumber(createJsonReader(LARGE_EXPONENT)))
        .isEqualTo(new BigDecimal("1e400"));
  }

  @Test
  public void testBigDecimalPolicy_RejectsInvalidNumberStrings() throws IOException {
    ToNumberStrategy bigDecimalPolicy = ToNumberPolicy.BIG_DECIMAL;
    
    // When: Parsing invalid number string
    // Then: Should throw JsonParseException
    JsonParseException exception = assertThrows(
        JsonParseException.class, 
        () -> bigDecimalPolicy.readNumber(createJsonReader(INVALID_NUMBER_STRING))
    );
    assertThat(exception)
        .hasMessageThat()
        .isEqualTo("Cannot parse not-a-number; at path $");
  }

  @Test
  public void testAllPolicies_RejectNullValues() throws IOException {
    // All number policies should reject null values and throw IllegalStateException
    
    // DOUBLE policy
    IllegalStateException exception = assertThrows(
        IllegalStateException.class,
        () -> ToNumberPolicy.DOUBLE.readNumber(createJsonReader(NULL_VALUE))
    );
    assertThat(exception)
        .hasMessageThat()
        .contains("Expected a double but was NULL");

    // LAZILY_PARSED_NUMBER policy
    exception = assertThrows(
        IllegalStateException.class,
        () -> ToNumberPolicy.LAZILY_PARSED_NUMBER.readNumber(createJsonReader(NULL_VALUE))
    );
    assertThat(exception)
        .hasMessageThat()
        .contains("Expected a string but was NULL");

    // LONG_OR_DOUBLE policy
    exception = assertThrows(
        IllegalStateException.class,
        () -> ToNumberPolicy.LONG_OR_DOUBLE.readNumber(createJsonReader(NULL_VALUE))
    );
    assertThat(exception)
        .hasMessageThat()
        .contains("Expected a string but was NULL");

    // BIG_DECIMAL policy
    exception = assertThrows(
        IllegalStateException.class,
        () -> ToNumberPolicy.BIG_DECIMAL.readNumber(createJsonReader(NULL_VALUE))
    );
    assertThat(exception)
        .hasMessageThat()
        .contains("Expected a string but was NULL");
  }

  /**
   * Creates a JsonReader in strict mode from the given JSON string.
   */
  private static JsonReader createJsonReader(String json) {
    return new JsonReader(new StringReader(json));
  }

  /**
   * Creates a JsonReader in lenient mode from the given JSON string.
   * Lenient mode allows parsing of special values like NaN and Infinity.
   */
  private static JsonReader createLenientJsonReader(String json) {
    JsonReader jsonReader = createJsonReader(json);
    jsonReader.setStrictness(Strictness.LENIENT);
    return jsonReader;
  }
}