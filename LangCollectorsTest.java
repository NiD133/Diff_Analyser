/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LangCollectors}.
 * The tests are organized by:
 * - which joining overload is used (no delimiter, delimiter only, delimiter+prefix+suffix, and same with a custom toString),
 * - input type (Strings vs non-Strings),
 * - source (LangCollectors.collect(varargs), Stream.of, Arrays.stream),
 * and include null-element and null-array behavior.
 */
@DisplayName("LangCollectors joining()")
class LangCollectorsTest {

    private static final class Fixture {
        final int value;

        private Fixture(final int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    // Common inputs
    private static final Long L1 = 1L;
    private static final Long L2 = 2L;
    private static final Long L3 = 3L;

    private static final Function<Object, String> OBJ_TO_STRING = Objects::toString;

    // Collectors under test, with descriptive names
    private static final Collector<Object, ?, String> JOIN_NONE = LangCollectors.joining();
    private static final Collector<Object, ?, String> JOIN_DASH = LangCollectors.joining("-");
    private static final Collector<Object, ?, String> JOIN_DASH_ANGLE = LangCollectors.joining("-", "<", ">");
    private static final Collector<Object, ?, String> JOIN_DASH_ANGLE_TOSTR = LangCollectors.joining("-", "<", ">", OBJ_TO_STRING);
    private static final Collector<Object, ?, String> JOIN_DASH_ANGLE_TOSTR_NUL =
            LangCollectors.joining("-", "<", ">", o -> Objects.toString(o, "NUL"));

    // Small helpers to reduce noise and keep the focus on expectations
    private static String collectVarargs(final Collector<Object, ?, String> c, final Object... elements) {
        return LangCollectors.collect(c, elements);
    }

    private static String collectVarargsNullArray(final Collector<Object, ?, String> c) {
        return LangCollectors.collect(c, (Object[]) null);
    }

    private static String collectStreamOf(final Collector<Object, ?, String> c, final Object... elements) {
        return Stream.of(elements).collect(c);
    }

    private static String collectArraysStream(final Collector<Object, ?, String> c, final Object[] array) {
        return Arrays.stream(array).collect(c);
    }

    private static Fixture fx(final int v) {
        return new Fixture(v);
    }

    // ===== Varargs via LangCollectors.collect(...) =====

    @Test
    @DisplayName("collect(varargs) - Strings with delimiter only")
    void collect_varargs_strings_delimiterOnly() {
        assertEquals("", collectVarargs(JOIN_DASH));
        assertEquals("1", collectVarargs(JOIN_DASH, "1"));
        assertEquals("1-2", collectVarargs(JOIN_DASH, "1", "2"));
        assertEquals("1-2-3", collectVarargs(JOIN_DASH, "1", "2", "3"));
        assertEquals("1-null-3", collectVarargs(JOIN_DASH, "1", null, "3"));
    }

    @Test
    @DisplayName("collect(varargs) - Non-Strings with no delimiter")
    void collect_varargs_nonStrings_noDelimiter() {
        assertEquals("", collectVarargs(JOIN_NONE));
        assertEquals("1", collectVarargs(JOIN_NONE, L1));
        assertEquals("12", collectVarargs(JOIN_NONE, L1, L2));
        assertEquals("123", collectVarargs(JOIN_NONE, L1, L2, L3));
        assertEquals("1null3", collectVarargs(JOIN_NONE, L1, null, L3));
        assertEquals("12", collectVarargs(JOIN_NONE, new AtomicLong(1), new AtomicLong(2)));
        assertEquals("12", collectVarargs(JOIN_NONE, fx(1), fx(2)));
    }

    @Test
    @DisplayName("collect(varargs) - Non-Strings with delimiter only")
    void collect_varargs_nonStrings_delimiterOnly() {
        assertEquals("", collectVarargs(JOIN_DASH));
        assertEquals("1", collectVarargs(JOIN_DASH, L1));
        assertEquals("1-2", collectVarargs(JOIN_DASH, L1, L2));
        assertEquals("1-2-3", collectVarargs(JOIN_DASH, L1, L2, L3));
        assertEquals("1-null-3", collectVarargs(JOIN_DASH, L1, null, L3));
        assertEquals("1-2", collectVarargs(JOIN_DASH, new AtomicLong(1), new AtomicLong(2)));
        assertEquals("1-2", collectVarargs(JOIN_DASH, fx(1), fx(2)));
    }

    @Test
    @DisplayName("collect(varargs) - Non-Strings with delimiter + prefix/suffix")
    void collect_varargs_nonStrings_delimiterPrefixSuffix() {
        assertEquals("<>", collectVarargs(JOIN_DASH_ANGLE));
        assertEquals("<1>", collectVarargs(JOIN_DASH_ANGLE, L1));
        assertEquals("<1-2>", collectVarargs(JOIN_DASH_ANGLE, L1, L2));
        assertEquals("<1-2-3>", collectVarargs(JOIN_DASH_ANGLE, L1, L2, L3));
        assertEquals("<1-null-3>", collectVarargs(JOIN_DASH_ANGLE, L1, null, L3));
        assertEquals("<1-2>", collectVarargs(JOIN_DASH_ANGLE, new AtomicLong(1), new AtomicLong(2)));
        assertEquals("<1-2>", collectVarargs(JOIN_DASH_ANGLE, fx(1), fx(2)));
    }

    @Test
    @DisplayName("collect(varargs) - Non-Strings with delimiter + prefix/suffix + custom toString")
    void collect_varargs_nonStrings_delimiterPrefixSuffix_customToString() {
        assertEquals("<>", collectVarargs(JOIN_DASH_ANGLE_TOSTR));
        assertEquals("<1>", collectVarargs(JOIN_DASH_ANGLE_TOSTR, L1));
        assertEquals("<1-2>", collectVarargs(JOIN_DASH_ANGLE_TOSTR, L1, L2));
        assertEquals("<1-2-3>", collectVarargs(JOIN_DASH_ANGLE_TOSTR, L1, L2, L3));
        assertEquals("<1-null-3>", collectVarargs(JOIN_DASH_ANGLE_TOSTR, L1, null, L3));
        assertEquals("<1-NUL-3>", collectVarargs(JOIN_DASH_ANGLE_TOSTR_NUL, L1, null, L3));
        assertEquals("<1-2>", collectVarargs(JOIN_DASH_ANGLE_TOSTR, new AtomicLong(1), new AtomicLong(2)));
        assertEquals("<1-2>", collectVarargs(JOIN_DASH_ANGLE_TOSTR, fx(1), fx(2)));
    }

    @Test
    @DisplayName("collect(varargs) - Null array is treated as empty")
    void collect_varargs_nullArray() {
        assertEquals("", collectVarargsNullArray(JOIN_NONE));
        assertEquals("", collectVarargsNullArray(JOIN_DASH));
        assertEquals("<>", collectVarargsNullArray(JOIN_DASH_ANGLE));
        assertEquals("<>", collectVarargsNullArray(JOIN_DASH_ANGLE_TOSTR_NUL));
    }

    @Test
    @DisplayName("collect(varargs) - Strings with no delimiter")
    void collect_varargs_strings_noDelimiter() {
        assertEquals("", collectVarargs(JOIN_NONE));
        assertEquals("1", collectVarargs(JOIN_NONE, "1"));
        assertEquals("12", collectVarargs(JOIN_NONE, "1", "2"));
        assertEquals("123", collectVarargs(JOIN_NONE, "1", "2", "3"));
        assertEquals("1null3", collectVarargs(JOIN_NONE, "1", null, "3"));
    }

    @Test
    @DisplayName("collect(varargs) - Strings with delimiter + prefix/suffix and custom toString")
    void collect_varargs_strings_delimiterPrefixSuffix() {
        assertEquals("<>", collectVarargs(JOIN_DASH_ANGLE));
        assertEquals("<1>", collectVarargs(JOIN_DASH_ANGLE, "1"));
        assertEquals("<1-2>", collectVarargs(JOIN_DASH_ANGLE, "1", "2"));
        assertEquals("<1-2-3>", collectVarargs(JOIN_DASH_ANGLE, "1", "2", "3"));
        assertEquals("<1-null-3>", collectVarargs(JOIN_DASH_ANGLE, "1", null, "3"));
        assertEquals("<1-NUL-3>", collectVarargs(JOIN_DASH_ANGLE_TOSTR_NUL, "1", null, "3"));
    }

    // ===== Stream.of(...) =====

    @Test
    @DisplayName("Stream.of - Strings with no delimiter")
    void streamOf_strings_noDelimiter() {
        assertEquals("", collectStreamOf(JOIN_NONE));
        assertEquals("1", collectStreamOf(JOIN_NONE, "1"));
        assertEquals("12", collectStreamOf(JOIN_NONE, "1", "2"));
        assertEquals("123", collectStreamOf(JOIN_NONE, "1", "2", "3"));
        assertEquals("1null3", collectStreamOf(JOIN_NONE, "1", null, "3"));
    }

    @Test
    @DisplayName("Stream.of - Strings with delimiter only")
    void streamOf_strings_delimiterOnly() {
        assertEquals("", collectStreamOf(JOIN_DASH));
        assertEquals("1", collectStreamOf(JOIN_DASH, "1"));
        assertEquals("1-2", collectStreamOf(JOIN_DASH, "1", "2"));
        assertEquals("1-2-3", collectStreamOf(JOIN_DASH, "1", "2", "3"));
        assertEquals("1-null-3", collectStreamOf(JOIN_DASH, "1", null, "3"));
    }

    @Test
    @DisplayName("Stream.of - Strings with delimiter + prefix/suffix (+ custom toString)")
    void streamOf_strings_delimiterPrefixSuffix() {
        assertEquals("<>", collectStreamOf(JOIN_DASH_ANGLE));
        assertEquals("<1>", collectStreamOf(JOIN_DASH_ANGLE, "1"));
        assertEquals("<1-2>", collectStreamOf(JOIN_DASH_ANGLE, "1", "2"));
        assertEquals("<1-2-3>", collectStreamOf(JOIN_DASH_ANGLE, "1", "2", "3"));
        assertEquals("<1-null-3>", collectStreamOf(JOIN_DASH_ANGLE, "1", null, "3"));
        assertEquals("<1-NUL-3>", collectStreamOf(JOIN_DASH_ANGLE_TOSTR_NUL, "1", null, "3"));
    }

    @Test
    @DisplayName("Stream.of - Non-Strings with no delimiter")
    void streamOf_nonStrings_noDelimiter() {
        assertEquals("", collectStreamOf(JOIN_NONE));
        assertEquals("1", collectStreamOf(JOIN_NONE, L1));
        assertEquals("12", collectStreamOf(JOIN_NONE, L1, L2));
        assertEquals("123", collectStreamOf(JOIN_NONE, L1, L2, L3));
        assertEquals("1null3", collectStreamOf(JOIN_NONE, L1, null, L3));
        assertEquals("12", collectStreamOf(JOIN_NONE, new AtomicLong(1), new AtomicLong(2)));
        assertEquals("12", collectStreamOf(JOIN_NONE, fx(1), fx(2)));
    }

    @Test
    @DisplayName("Stream.of - Non-Strings with delimiter only")
    void streamOf_nonStrings_delimiterOnly() {
        assertEquals("", collectStreamOf(JOIN_DASH));
        assertEquals("1", collectStreamOf(JOIN_DASH, L1));
        assertEquals("1-2", collectStreamOf(JOIN_DASH, L1, L2));
        assertEquals("1-2-3", collectStreamOf(JOIN_DASH, L1, L2, L3));
        assertEquals("1-null-3", collectStreamOf(JOIN_DASH, L1, null, L3));
        assertEquals("1-2", collectStreamOf(JOIN_DASH, new AtomicLong(1), new AtomicLong(2)));
        assertEquals("1-2", collectStreamOf(JOIN_DASH, fx(1), fx(2)));
    }

    @Test
    @DisplayName("Stream.of - Non-Strings with delimiter + prefix/suffix (+ custom toString)")
    void streamOf_nonStrings_delimiterPrefixSuffix() {
        assertEquals("<>", collectStreamOf(JOIN_DASH_ANGLE));
        assertEquals("<1>", collectStreamOf(JOIN_DASH_ANGLE, L1));
        assertEquals("<1-2>", collectStreamOf(JOIN_DASH_ANGLE, L1, L2));
        assertEquals("<1-2-3>", collectStreamOf(JOIN_DASH_ANGLE, L1, L2, L3));
        assertEquals("<1-null-3>", collectStreamOf(JOIN_DASH_ANGLE, L1, null, L3));
        assertEquals("<1-NUL-3>", collectStreamOf(JOIN_DASH_ANGLE_TOSTR_NUL, L1, null, L3));
        assertEquals("<1-2>", collectStreamOf(JOIN_DASH_ANGLE, new AtomicLong(1), new AtomicLong(2)));
        assertEquals("<1-2>", collectStreamOf(JOIN_DASH_ANGLE, fx(1), fx(2)));
    }

    // ===== Arrays.stream(...) =====
    // Keep a few typed-array cases to ensure behavior with arrays of specific element types.

    @Test
    @DisplayName("Arrays.stream - Non-Strings with no delimiter")
    void arraysStream_nonStrings_noDelimiter() {
        assertEquals("", collectArraysStream(JOIN_NONE, new Object[]{}));
        assertEquals("1", collectArraysStream(JOIN_NONE, new Long[]{L1}));
        assertEquals("12", collectArraysStream(JOIN_NONE, new Long[]{L1, L2}));
        assertEquals("123", collectArraysStream(JOIN_NONE, new Long[]{L1, L2, L3}));
        assertEquals("1null3", collectArraysStream(JOIN_NONE, new Long[]{L1, null, L3}));
        assertEquals("12", collectArraysStream(JOIN_NONE, new AtomicLong[]{new AtomicLong(1), new AtomicLong(2)}));
        assertEquals("12", collectArraysStream(JOIN_NONE, new Fixture[]{fx(1), fx(2)}));
    }

    @Test
    @DisplayName("Arrays.stream - Non-Strings with delimiter only")
    void arraysStream_nonStrings_delimiterOnly() {
        assertEquals("", collectArraysStream(JOIN_DASH, new Object[]{}));
        assertEquals("1", collectArraysStream(JOIN_DASH, new Long[]{L1}));
        assertEquals("1-2", collectArraysStream(JOIN_DASH, new Long[]{L1, L2}));
        assertEquals("1-2-3", collectArraysStream(JOIN_DASH, new Long[]{L1, L2, L3}));
        assertEquals("1-null-3", collectArraysStream(JOIN_DASH, new Long[]{L1, null, L3}));
        assertEquals("1-2", collectArraysStream(JOIN_DASH, new AtomicLong[]{new AtomicLong(1), new AtomicLong(2)}));
        assertEquals("1-2", collectArraysStream(JOIN_DASH, new Fixture[]{fx(1), fx(2)}));
    }
}