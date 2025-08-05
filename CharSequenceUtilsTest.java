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
package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIndexOutOfBoundsException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests CharSequenceUtils
 */
class CharSequenceUtilsTest extends AbstractLangTest {

    private static class RegionMatchesTestData {
        final String source;
        final boolean ignoreCase;
        final int toffset;
        final String other;
        final int ooffset;
        final int len;
        final boolean expected;
        final Class<? extends Throwable> throwable;

        RegionMatchesTestData(String source, boolean ignoreCase, int toffset, String other, int ooffset, int len,
                boolean expected) {
            this.source = source;
            this.ignoreCase = ignoreCase;
            this.toffset = toffset;
            this.other = other;
            this.ooffset = ooffset;
            this.len = len;
            this.expected = expected;
            this.throwable = null;
        }

        RegionMatchesTestData(String source, boolean ignoreCase, int toffset, String other, int ooffset, int len,
                Class<? extends Throwable> throwable) {
            this.source = source;
            this.ignoreCase = ignoreCase;
            this.toffset = toffset;
            this.other = other;
            this.ooffset = ooffset;
            this.len = len;
            this.expected = false;
            this.throwable = throwable;
        }

        @Override
        public String toString() {
            return "Source: '" + source + "' [" + toffset + "], "
                    + (ignoreCase ? "ignore case" : "case sensitive") + ", "
                    + "Other: '" + other + "' [" + ooffset + "], "
                    + "Length: " + len + " => "
                    + (throwable != null ? "Expect " + throwable.getSimpleName() : "Expect " + expected);
        }
    }

    private static class TestCharSequenceWrapper implements CharSequence {
        private final CharSequence inner;

        TestCharSequenceWrapper(CharSequence inner) {
            this.inner = inner;
        }

        @Override
        public char charAt(int index) {
            return inner.charAt(index);
        }

        @Override
        public IntStream chars() {
            return inner.chars();
        }

        @Override
        public IntStream codePoints() {
            return inner.codePoints();
        }

        @Override
        public int length() {
            return inner.length();
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return inner.subSequence(start, end);
        }

        @Override
        public String toString() {
            return inner.toString();
        }
    }

    private static final RegionMatchesTestData[] REGION_MATCHES_TEST_DATA = {
            // @formatter:off
            //           Source  IgnoreCase Offset Other  Offset Length Result
            new RegionMatchesTestData("",     true,      -1,     "",    -1,    -1,    false),
            new RegionMatchesTestData("",     true,       0,     "",     0,     1,    false),
            new RegionMatchesTestData("a",    true,       0,     "abc",  0,     0,    true),
            new RegionMatchesTestData("a",    true,       0,     "abc",  0,     1,    true),
            new RegionMatchesTestData("a",    true,       0,     null,   0,     0,    NullPointerException.class),
            new RegionMatchesTestData(null,   true,       0,     null,   0,     0,    NullPointerException.class),
            new RegionMatchesTestData(null,   true,       0,     "",     0,     0,    NullPointerException.class),
            new RegionMatchesTestData("Abc",  true,       0,     "abc",  0,     3,    true),
            new RegionMatchesTestData("Abc",  false,      0,     "abc",  0,     3,    false),
            new RegionMatchesTestData("Abc",  true,       1,     "abc",  1,     2,    true),
            new RegionMatchesTestData("Abc",  false,      1,     "abc",  1,     2,    true),
            new RegionMatchesTestData("Abcd", true,       1,     "abcD", 1,     2,    true),
            new RegionMatchesTestData("Abcd", false,      1,     "abcD", 1,     2,    true),
            // @formatter:on
    };

    static Stream<Arguments> lastIndexWithStandardCharSequence() {
        // @formatter:off
        return Stream.of(
            arguments("abc", "b", 2, 1),
            arguments(new StringBuilder("abc"), "b", 2, 1),
            arguments(new StringBuffer("abc"), "b", 2, 1),
            arguments("abc", new StringBuilder("b"), 2, 1),
            arguments(new StringBuilder("abc"), new StringBuilder("b"), 2, 1),
            arguments(new StringBuffer("abc"), new StringBuffer("b"), 2, 1),
            arguments(new StringBuilder("abc"), new StringBuffer("b"), 2, 1)
        );
        // @formatter:on
    }

    @Test
    void testConstructor() {
        assertNotNull(new CharSequenceUtils());
        Constructor<?>[] constructors = CharSequenceUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length, "Should have only one constructor");
        assertTrue(Modifier.isPublic(constructors[0].getModifiers()), "Constructor should be public");
        assertTrue(Modifier.isPublic(CharSequenceUtils.class.getModifiers()), "Class should be public");
        assertFalse(Modifier.isFinal(CharSequenceUtils.class.getModifiers()), "Class should not be final");
    }

    @ParameterizedTest
    @MethodSource("lastIndexWithStandardCharSequence")
    void testLastIndexOfWithDifferentCharSequences(CharSequence cs, CharSequence search, int start, int expected) {
        assertEquals(expected, CharSequenceUtils.lastIndexOf(cs, search, start));
    }

    @Test
    void testLastIndexOf_FixedCases() {
        assertLastIndexOfForAllStartPositions("808087847-1321060740-635567660180086727-925755305", "-1321060740-635567660", 21);
        assertLastIndexOfForAllStartPositions("", "");
        assertLastIndexOfForAllStartPositions("1", "");
        assertLastIndexOfForAllStartPositions("", "1");
        assertLastIndexOfForAllStartPositions("1", "1");
        assertLastIndexOfForAllStartPositions("11", "1");
        assertLastIndexOfForAllStartPositions("1", "11");
        assertLastIndexOfForAllStartPositions("apache", "a");
        assertLastIndexOfForAllStartPositions("apache", "p");
        assertLastIndexOfForAllStartPositions("apache", "e");
        assertLastIndexOfForAllStartPositions("apache", "x");
        assertLastIndexOfForAllStartPositions("oraoraoraora", "r");
        assertLastIndexOfForAllStartPositions("mudamudamudamuda", "d");
        // Tests partial match handling in the implementation
        assertLastIndexOfForAllStartPositions("junk-ststarting", "starting");
    }

    @Test
    void testLastIndexOf_RandomCases() {
        Random random = new Random();
        StringBuilder segment = new StringBuilder();
        while (segment.length() <= CharSequenceUtils.TO_STRING_LIMIT) {
            segment.append(random.nextInt());
        }
        StringBuilder original = new StringBuilder(segment);
        assertLastIndexOfForAllStartPositions(original, segment);
        
        for (int i = 0; i < 100; i++) {
            if (random.nextDouble() < 0.5) {
                original.append(random.nextInt() % 10);
            } else {
                original = new StringBuilder().append(random.nextInt() % 100).append(original);
            }
            assertLastIndexOfForAllStartPositions(original, segment);
        }
    }

    private void assertLastIndexOfForAllStartPositions(CharSequence a, CharSequence b) {
        int maxLength = Math.max(a.length(), b.length());
        for (int start = -maxLength - 10; start <= maxLength + 10; start++) {
            assertLastIndexOfForStartPosition(a, b, start);
        }
        assertLastIndexOfForStartPosition(a, b, Integer.MIN_VALUE);
        assertLastIndexOfForStartPosition(a, b, Integer.MAX_VALUE);
    }

    private void assertLastIndexOfForStartPosition(CharSequence a, CharSequence b, int start) {
        assertLastIndexOfForSequencePair(a, b, start);
        assertLastIndexOfForSequencePair(b, a, start);
    }

    private void assertLastIndexOfForSequencePair(CharSequence a, CharSequence b, int start) {
        int expected = a.toString().lastIndexOf(b.toString(), start);
        int actual = CharSequenceUtils.lastIndexOf(new TestCharSequenceWrapper(a.toString()), new TestCharSequenceWrapper(b.toString()), start);
        assertEquals(expected, actual, () -> 
            "lastIndexOf mismatch for: a='" + a + "', b='" + b + "', start=" + start
        );
    }

    @Test
    void testRegionMatches_StringImplementation() {
        for (RegionMatchesTestData data : REGION_MATCHES_TEST_DATA) {
            String id = "String: " + data;
            if (data.throwable != null) {
                assertThrows(data.throwable, () -> 
                    data.source.regionMatches(data.ignoreCase, data.toffset, data.other, data.ooffset, data.len),
                    id
                );
            } else {
                boolean result = data.source.regionMatches(data.ignoreCase, data.toffset, data.other, data.ooffset, data.len);
                assertEquals(data.expected, result, id);
            }
        }
    }

    @Test
    void testRegionMatches_CSStringImplementation() {
        for (RegionMatchesTestData data : REGION_MATCHES_TEST_DATA) {
            String id = "CSString: " + data;
            if (data.throwable != null) {
                assertThrows(data.throwable, () -> 
                    CharSequenceUtils.regionMatches(data.source, data.ignoreCase, data.toffset, data.other, data.ooffset, data.len),
                    id
                );
            } else {
                boolean result = CharSequenceUtils.regionMatches(
                    data.source, data.ignoreCase, data.toffset, data.other, data.ooffset, data.len
                );
                assertEquals(data.expected, result, id);
            }
        }
    }

    @Test
    void testRegionMatches_CSNonStringImplementation() {
        for (RegionMatchesTestData data : REGION_MATCHES_TEST_DATA) {
            String id = "CSNonString: " + data;
            if (data.throwable != null) {
                assertThrows(data.throwable, () -> 
                    CharSequenceUtils.regionMatches(
                        new StringBuilder(data.source), data.ignoreCase, data.toffset, data.other, data.ooffset, data.len
                    ),
                    id
                );
            } else {
                boolean result = CharSequenceUtils.regionMatches(
                    new StringBuilder(data.source), data.ignoreCase, data.toffset, data.other, data.ooffset, data.len
                );
                assertEquals(data.expected, result, id);
            }
        }
    }

    @Test
    void testSubSequence() {
        assertNull(CharSequenceUtils.subSequence(null, -1), "Null input should return null");
        assertNull(CharSequenceUtils.subSequence(null, 0), "Null input should return null");
        assertNull(CharSequenceUtils.subSequence(null, 1), "Null input should return null");
        
        assertEquals(StringUtils.EMPTY, CharSequenceUtils.subSequence("", 0), "Empty string at start 0");
        assertEquals("012", CharSequenceUtils.subSequence("012", 0), "Full sequence");
        assertEquals("12", CharSequenceUtils.subSequence("012", 1), "Offset 1");
        assertEquals("2", CharSequenceUtils.subSequence("012", 2), "Offset 2");
        assertEquals(StringUtils.EMPTY, CharSequenceUtils.subSequence("012", 3), "Offset at end");
    }

    @Test
    void testSubSequenceNegativeStartThrows() {
        assertIndexOutOfBoundsException(() -> 
            CharSequenceUtils.subSequence("", -1),
            "Expected IndexOutOfBoundsException for negative start"
        );
    }

    @Test
    void testSubSequenceTooLongThrows() {
        assertIndexOutOfBoundsException(() -> 
            CharSequenceUtils.subSequence("", 1),
            "Expected IndexOutOfBoundsException for start beyond length"
        );
    }

    @Test
    void testToCharArray() {
        StringBuilder builder = new StringBuilder("abcdefg");
        char[] expected = "abcdefg".toCharArray();
        assertArrayEquals(expected, CharSequenceUtils.toCharArray(builder), "StringBuilder conversion");
        assertArrayEquals(expected, CharSequenceUtils.toCharArray("abcdefg"), "String conversion");
        assertArrayEquals(ArrayUtils.EMPTY_CHAR_ARRAY, CharSequenceUtils.toCharArray(null), "Null input should return empty array");
    }
}