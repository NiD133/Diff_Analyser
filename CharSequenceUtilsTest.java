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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link CharSequenceUtils}.
 */
class CharSequenceUtilsTest extends AbstractLangTest {

    /**
     * A CharSequence wrapper that is not a String.
     * This is used to ensure that the CharSequence-based methods are tested,
     * rather than any String-specific optimizations.
     */
    private static class WrapperString implements CharSequence {
        private final CharSequence inner;

        WrapperString(final CharSequence inner) {
            this.inner = inner;
        }

        @Override
        public char charAt(final int index) {
            return inner.charAt(index);
        }

        @Override
        public int length() {
            return inner.length();
        }

        @Override
        public CharSequence subSequence(final int start, final int end) {
            return inner.subSequence(start, end);
        }

        @Override
        public String toString() {
            return inner.toString();
        }

        // In Java 8+, these have default implementations, but overriding them
        // ensures our wrapper is used for all CharSequence operations.
        @Override
        public IntStream chars() {
            return inner.chars();
        }

        @Override
        public IntStream codePoints() {
            return inner.codePoints();
        }
    }

    @Test
    @DisplayName("The constructor should be public and the class should be non-final")
    void testConstructor() {
        assertNotNull(new CharSequenceUtils());
        final Constructor<?>[] cons = CharSequenceUtils.class.getDeclaredConstructors();
        assertEquals(1, cons.length);
        assertTrue(Modifier.isPublic(cons[0].getModifiers()));
        assertTrue(Modifier.isPublic(CharSequenceUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(CharSequenceUtils.class.getModifiers()));
    }

    // --- regionMatches Tests ---

    static Stream<Arguments> regionMatchesData() {
        // Arguments: source, ignoreCase, sourceOffset, other, otherOffset, length, expectedResult, expectedThrowable
        return Stream.of(
            arguments("abc", true, 0, "abc", 0, 3, true, null),
            arguments("Abc", true, 0, "abc", 0, 3, true, null),
            arguments("Abc", false, 0, "abc", 0, 3, false, null),
            arguments("Abc", true, 1, "bc", 0, 2, true, null),
            arguments("Abc", false, 1, "bc", 0, 2, true, null),
            arguments("Abcd", true, 1, "bce", 0, 2, false, null),
            arguments("a", true, 0, "abc", 0, 0, true, null),
            arguments("", true, 0, "", 0, 1, false, null),
            // Tests for exceptions
            arguments("a", true, 0, null, 0, 0, false, NullPointerException.class),
            arguments(null, true, 0, "a", 0, 0, false, NullPointerException.class),
            arguments(null, true, 0, null, 0, 0, false, NullPointerException.class)
        );
    }

    @DisplayName("regionMatches should behave like String.regionMatches for various inputs")
    @ParameterizedTest(name = "[{index}] regionMatches(\"{0}\", {1}, {2}, \"{3}\", {4}, {5})")
    @MethodSource("regionMatchesData")
    void testRegionMatches(final String source, final boolean ignoreCase, final int sourceOffset,
                           final String other, final int otherOffset, final int length,
                           final boolean expectedResult, final Class<? extends Throwable> expectedThrowable) {

        if (expectedThrowable != null) {
            assertThrows(expectedThrowable, () -> CharSequenceUtils.regionMatches(source, ignoreCase, sourceOffset, other, otherOffset, length));
            // Also test with a non-String CharSequence
            if (source != null) {
                assertThrows(expectedThrowable, () -> CharSequenceUtils.regionMatches(new StringBuilder(source), ignoreCase, sourceOffset, other, otherOffset, length));
            }
        } else {
            // 1. Verify against the baseline: String.regionMatches
            final boolean stringResult = source.regionMatches(ignoreCase, sourceOffset, other, otherOffset, length);
            assertEquals(expectedResult, stringResult, "Baseline String.regionMatches behavior mismatch");

            // 2. Test CharSequenceUtils with a String
            final boolean csuStringResult = CharSequenceUtils.regionMatches(source, ignoreCase, sourceOffset, other, otherOffset, length);
            assertEquals(expectedResult, csuStringResult, "CharSequenceUtils.regionMatches(String) result mismatch");

            // 3. Test CharSequenceUtils with a non-String CharSequence (StringBuilder)
            final boolean csuBuilderResult = CharSequenceUtils.regionMatches(new StringBuilder(source), ignoreCase, sourceOffset, other, otherOffset, length);
            assertEquals(expectedResult, csuBuilderResult, "CharSequenceUtils.regionMatches(StringBuilder) result mismatch");
        }
    }

    // --- lastIndexOf Tests ---

    static Stream<Arguments> lastIndexOfWithStandardCharSequence() {
        return Stream.of(
            arguments("abc", "b", 2, 1),
            arguments(new StringBuilder("abc"), "b", 2, 1),
            arguments(new StringBuffer("abc"), "b", 2, 1),
            arguments("abc", new StringBuilder("b"), 2, 1),
            arguments(new StringBuilder("abc"), new StringBuilder("b"), 2, 1),
            arguments(new StringBuffer("abc"), new StringBuffer("b"), 2, 1),
            arguments(new StringBuilder("abc"), new StringBuffer("b"), 2, 1)
        );
    }

    @DisplayName("lastIndexOf should work with different CharSequence implementations")
    @ParameterizedTest(name = "[{index}] lastIndexOf({0}, {1}, {2}) should be {3}")
    @MethodSource("lastIndexOfWithStandardCharSequence")
    void testLastIndexOf_withDifferentCharSequences(final CharSequence cs, final CharSequence search, final int start, final int expected) {
        assertEquals(expected, CharSequenceUtils.lastIndexOf(cs, search, start));
    }

    static Stream<Arguments> lastIndexOfFixedCases() {
        return Stream.of(
            arguments("808087847-1321060740-635567660180086727-925755305", "-1321060740-635567660"),
            arguments("", ""),
            arguments("1", ""),
            arguments("", "1"),
            arguments("1", "1"),
            arguments("11", "1"),
            arguments("1", "11"),
            arguments("apache", "a"),
            arguments("apache", "p"),
            arguments("apache", "e"),
            arguments("apache", "x"),
            arguments("oraoraoraora", "r"),
            arguments("mudamudamudamuda", "d"),
            // This case tests a specific code path involving a partial match
            arguments("junk-ststarting", "starting")
        );
    }

    @DisplayName("lastIndexOf should return the same result as String.lastIndexOf for fixed cases")
    @ParameterizedTest(name = "[{index}] lastIndexOf(\"{0}\", \"{1}\")")
    @MethodSource("lastIndexOfFixedCases")
    void testLastIndexOf_fixedCases(final String text, final String search) {
        // Test with a wide range of start indices to cover edge cases
        final int maxLen = Math.max(text.length(), search.length());
        for (int i = -maxLen - 10; i <= maxLen + 10; i++) {
            assertLastIndexOfAgreesWithString(text, search, i);
        }
        assertLastIndexOfAgreesWithString(text, search, Integer.MIN_VALUE);
        assertLastIndexOfAgreesWithString(text, search, Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("lastIndexOf should handle long and randomly generated strings")
    void testLastIndexOf_randomized() {
        final Random random = new Random(123); // Seeded for deterministic results
        final StringBuilder segment = new StringBuilder();
        // Build a segment just over the implementation's TO_STRING_LIMIT
        while (segment.length() <= CharSequenceUtils.TO_STRING_LIMIT) {
            segment.append(random.nextInt());
        }

        StringBuilder text = new StringBuilder(segment);
        assertLastIndexOfAgreesWithString(text, segment, text.length());

        // Randomly grow the text by prepending or appending and re-test
        for (int i = 0; i < 100; i++) {
            if (random.nextBoolean()) {
                text.append(random.nextInt(10));
            } else {
                text.insert(0, random.nextInt(100));
            }
            // The random test focuses on string content, not the start index,
            // so we test with a single, common start position.
            assertLastIndexOfAgreesWithString(text, segment, text.length());
        }
    }

    /**
     * Asserts that CharSequenceUtils.lastIndexOf produces the same result as String.lastIndexOf.
     * It tests both (text, search) and (search, text) to be thorough.
     */
    private void assertLastIndexOfAgreesWithString(final CharSequence text, final CharSequence search, final int start) {
        assertLastIndexOf(text, search, start);
        assertLastIndexOf(search, text, start);
    }

    /**
     * The core assertion logic that compares CharSequenceUtils.lastIndexOf against String.lastIndexOf.
     */
    private void assertLastIndexOf(final CharSequence text, final CharSequence search, final int start) {
        final String textStr = text.toString();
        final String searchStr = search.toString();
        final int expected = textStr.lastIndexOf(searchStr, start);

        // Use WrapperString to ensure we test the CharSequence-based implementation
        final CharSequence textWrapper = new WrapperString(textStr);
        final CharSequence searchWrapper = new WrapperString(searchStr);
        final int actual = CharSequenceUtils.lastIndexOf(textWrapper, searchWrapper, start);

        assertEquals(expected, actual, () -> String.format(
                "lastIndexOf(Wrapper(\"%s\"), Wrapper(\"%s\"), %d) failed",
                text, search, start));
    }

    // --- subSequence Tests ---

    @Nested
    @DisplayName("subSequence(CharSequence, int)")
    class SubSequenceTest {

        @Test
        @DisplayName("should return null for null input")
        void shouldReturnNullForNullInput() {
            assertNull(CharSequenceUtils.subSequence(null, -1));
            assertNull(CharSequenceUtils.subSequence(null, 0));
            assertNull(CharSequenceUtils.subSequence(null, 1));
        }

        @Test
        @DisplayName("should return correct subsequence for valid inputs")
        void shouldReturnCorrectSubSequenceForValidInput() {
            assertEquals(StringUtils.EMPTY, CharSequenceUtils.subSequence(StringUtils.EMPTY, 0));
            assertEquals("012", CharSequenceUtils.subSequence("012", 0));
            assertEquals("12", CharSequenceUtils.subSequence("012", 1));
            assertEquals("2", CharSequenceUtils.subSequence("012", 2));
            assertEquals(StringUtils.EMPTY, CharSequenceUtils.subSequence("012", 3));
        }

        static Stream<Arguments> invalidSubSequenceArgs() {
            return Stream.of(
                arguments(StringUtils.EMPTY, -1), // Negative start
                arguments(StringUtils.EMPTY, 1),  // Start > length
                arguments("abc", -1),             // Negative start
                arguments("abc", 4)               // Start > length
            );
        }

        @ParameterizedTest(name = "should throw for start = {1} on \"{0}\"")
        @MethodSource("invalidSubSequenceArgs")
        @DisplayName("should throw IndexOutOfBoundsException for invalid start index")
        void shouldThrowExceptionForInvalidStartIndex(final CharSequence cs, final int start) {
            assertThrows(IndexOutOfBoundsException.class, () -> CharSequenceUtils.subSequence(cs, start));
        }
    }

    // --- toCharArray Tests ---

    @Test
    @DisplayName("toCharArray should convert any CharSequence to a char array")
    void testToCharArray() {
        final StringBuilder builder = new StringBuilder("abcdefg");
        final char[] expected = builder.toString().toCharArray();

        assertArrayEquals(expected, CharSequenceUtils.toCharArray(builder));
        assertArrayEquals(expected, CharSequenceUtils.toCharArray(builder.toString()));
        assertArrayEquals(ArrayUtils.EMPTY_CHAR_ARRAY, CharSequenceUtils.toCharArray(null));
    }
}