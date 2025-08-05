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

public class ToNumberPolicyTest {
    // ---------- DOUBLE POLICY TESTS ---------- //
    @Test
    public void testDouble_validNumbers() throws IOException {
        ToNumberStrategy strategy = ToNumberPolicy.DOUBLE;
        assertThat(strategy.readNumber(fromString("10.1"))).isEqualTo(10.1);
        assertThat(strategy.readNumber(fromString("3.141592653589793238462643383279")))
            .isEqualTo(3.141592653589793D);
    }

    @Test
    public void testDouble_overflowThrowsException() {
        ToNumberStrategy strategy = ToNumberPolicy.DOUBLE;
        MalformedJsonException e = assertThrows(
            MalformedJsonException.class,
            () -> strategy.readNumber(fromString("1e400"))
        );
        assertThat(e).hasMessageThat().isEqualTo(
            "JSON forbids NaN and infinities: Infinity at line 1 column 6 path $\n" +
            "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json"
        );
    }

    @Test
    public void testDouble_nonNumberThrowsException() {
        ToNumberStrategy strategy = ToNumberPolicy.DOUBLE;
        assertThrows(
            NumberFormatException.class,
            () -> strategy.readNumber(fromString("\"not-a-number\""))
        );
    }

    // ---------- LAZILY_PARSED_NUMBER POLICY TESTS ---------- //
    @Test
    public void testLazilyParsedNumber_validNumbers() throws IOException {
        ToNumberStrategy strategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;
        assertThat(strategy.readNumber(fromString("10.1")))
            .isEqualTo(new LazilyParsedNumber("10.1"));
        assertThat(strategy.readNumber(fromString("3.141592653589793238462643383279")))
            .isEqualTo(new LazilyParsedNumber("3.141592653589793238462643383279"));
        assertThat(strategy.readNumber(fromString("1e400")))
            .isEqualTo(new LazilyParsedNumber("1e400"));
    }

    // ---------- LONG_OR_DOUBLE POLICY TESTS ---------- //
    @Test
    public void testLongOrDouble_integerValue() throws IOException {
        ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;
        assertThat(strategy.readNumber(fromString("10"))).isEqualTo(10L);
    }

    @Test
    public void testLongOrDouble_fractionalValue() throws IOException {
        ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;
        assertThat(strategy.readNumber(fromString("10.1"))).isEqualTo(10.1);
        assertThat(strategy.readNumber(fromString("3.141592653589793238462643383279")))
            .isEqualTo(3.141592653589793D);
    }

    @Test
    public void testLongOrDouble_overflowThrowsException() {
        ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;
        MalformedJsonException e = assertThrows(
            MalformedJsonException.class,
            () -> strategy.readNumber(fromString("1e400"))
        );
        assertThat(e).hasMessageThat().isEqualTo(
            "JSON forbids NaN and infinities: Infinity; at path $"
        );
    }

    @Test
    public void testLongOrDouble_nonNumberThrowsException() {
        ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;
        JsonParseException e = assertThrows(
            JsonParseException.class,
            () -> strategy.readNumber(fromString("\"not-a-number\""))
        );
        assertThat(e).hasMessageThat().isEqualTo(
            "Cannot parse not-a-number; at path $"
        );
    }

    @Test
    public void testLongOrDouble_lenientSpecialValues() throws IOException {
        ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;
        assertThat(strategy.readNumber(fromStringLenient("NaN")))
            .isEqualTo(Double.NaN);
        assertThat(strategy.readNumber(fromStringLenient("Infinity")))
            .isEqualTo(Double.POSITIVE_INFINITY);
        assertThat(strategy.readNumber(fromStringLenient("-Infinity")))
            .isEqualTo(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testLongOrDouble_strictNaNThrowsException() {
        ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;
        MalformedJsonException e = assertThrows(
            MalformedJsonException.class,
            () -> strategy.readNumber(fromString("NaN"))
        );
        assertThat(e).hasMessageThat().isEqualTo(
            "Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 1 column 1 path $\n" +
            "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json"
        );
    }

    @Test
    public void testLongOrDouble_strictInfinityThrowsException() {
        ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;
        MalformedJsonException e = assertThrows(
            MalformedJsonException.class,
            () -> strategy.readNumber(fromString("Infinity"))
        );
        assertThat(e).hasMessageThat().isEqualTo(
            "Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 1 column 1 path $\n" +
            "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json"
        );
    }

    @Test
    public void testLongOrDouble_strictNegativeInfinityThrowsException() {
        ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;
        MalformedJsonException e = assertThrows(
            MalformedJsonException.class,
            () -> strategy.readNumber(fromString("-Infinity"))
        );
        assertThat(e).hasMessageThat().isEqualTo(
            "Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 1 column 1 path $\n" +
            "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json"
        );
    }

    // ---------- BIG_DECIMAL POLICY TESTS ---------- //
    @Test
    public void testBigDecimal_validNumbers() throws IOException {
        ToNumberStrategy strategy = ToNumberPolicy.BIG_DECIMAL;
        assertThat(strategy.readNumber(fromString("10.1")))
            .isEqualTo(new BigDecimal("10.1"));
        assertThat(strategy.readNumber(fromString("3.141592653589793238462643383279")))
            .isEqualTo(new BigDecimal("3.141592653589793238462643383279"));
        assertThat(strategy.readNumber(fromString("1e400")))
            .isEqualTo(new BigDecimal("1e400"));
    }

    @Test
    public void testBigDecimal_nonNumberThrowsException() {
        ToNumberStrategy strategy = ToNumberPolicy.BIG_DECIMAL;
        JsonParseException e = assertThrows(
            JsonParseException.class,
            () -> strategy.readNumber(fromString("\"not-a-number\""))
        );
        assertThat(e).hasMessageThat().isEqualTo(
            "Cannot parse not-a-number; at path $"
        );
    }

    // ---------- NULL HANDLING TESTS ---------- //
    @Test
    public void testDouble_readNullThrowsException() {
        assertNullHandlingThrowsIllegalStateException(ToNumberPolicy.DOUBLE, 
            "Expected a double but was NULL at line 1 column 5 path $\n" +
            "See https://github.com/google/gson/blob/main/Troubleshooting.md#adapter-not-null-safe"
        );
    }

    @Test
    public void testLazilyParsedNumber_readNullThrowsException() {
        assertNullHandlingThrowsIllegalStateException(ToNumberPolicy.LAZILY_PARSED_NUMBER, 
            "Expected a string but was NULL at line 1 column 5 path $\n" +
            "See https://github.com/google/gson/blob/main/Troubleshooting.md#adapter-not-null-safe"
        );
    }

    @Test
    public void testLongOrDouble_readNullThrowsException() {
        assertNullHandlingThrowsIllegalStateException(ToNumberPolicy.LONG_OR_DOUBLE, 
            "Expected a string but was NULL at line 1 column 5 path $\n" +
            "See https://github.com/google/gson/blob/main/Troubleshooting.md#adapter-not-null-safe"
        );
    }

    @Test
    public void testBigDecimal_readNullThrowsException() {
        assertNullHandlingThrowsIllegalStateException(ToNumberPolicy.BIG_DECIMAL, 
            "Expected a string but was NULL at line 1 column 5 path $\n" +
            "See https://github.com/google/gson/blob/main/Troubleshooting.md#adapter-not-null-safe"
        );
    }

    private void assertNullHandlingThrowsIllegalStateException(
        ToNumberStrategy strategy, String expectedMessage) {
        IllegalStateException e = assertThrows(
            IllegalStateException.class,
            () -> strategy.readNumber(fromString("null"))
        );
        assertThat(e).hasMessageThat().isEqualTo(expectedMessage);
    }

    // ---------- HELPER METHODS ---------- //
    private static JsonReader fromString(String json) {
        return new JsonReader(new StringReader(json));
    }

    private static JsonReader fromStringLenient(String json) {
        JsonReader jsonReader = fromString(json);
        jsonReader.setStrictness(Strictness.LENIENT);
        return jsonReader;
    }
}