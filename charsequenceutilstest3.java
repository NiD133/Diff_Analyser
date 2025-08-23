package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link CharSequenceUtils} class.
 * This suite focuses on the regionMatches and lastIndexOf methods.
 */
class CharSequenceUtilsTest {

    /**
     * A simple CharSequence wrapper.
     * This is used to ensure that CharSequenceUtils methods are tested against a generic
     * CharSequence implementation, not just String or StringBuilder which might have
     * optimized paths.
     */
    private static class WrapperString implements CharSequence {
        private final CharSequence inner;

        WrapperString(final CharSequence inner) {
            this.inner = inner;
        }

        @Override
        public int length() {
            return inner.length();
        }

        @Override
        public char charAt(final int index) {
            return inner.charAt(index);
        }

        @Override
        public CharSequence subSequence(final int start, final int end) {
            return inner.subSequence(start, end);
        }

        @Override
        public String toString() {
            return inner.toString();
        }

        @Override
        public IntStream chars() {
            return inner.chars();
        }

        @Override
        public IntStream codePoints() {
            return inner.codePoints();
        }
    }

    @Nested
    @DisplayName("regionMatches Tests")
    class RegionMatchesTest {

        static Stream<Arguments> regionMatchesSource() {
            // Arguments: source, ignoreCase, srcOffset, other, otherOffset, len, expectedResult, expectedThrowable
            return Stream.of(
                // Basic cases
                arguments("Abc", true, 0, "abc", 0, 3, true, null),
                arguments("Abc", false, 0, "abc", 0, 3, false, null),
                arguments("Abc", true, 1, "abc", 1, 2, true, null),
                arguments("Abc", false, 1, "abc", 1, 2, true, null),
                arguments("Abcd", true, 1, "abcD", 1, 2, true, null),
                arguments("Abcd", false, 1, "abcD", 1, 2, true, null),

                // Edge cases with offsets and lengths
                arguments("a", true, 0, "abc", 0, 1, true, null),
                arguments("a", true, 0, "abc", 0, 0, true, null), // Zero length region is always true
                arguments("", true, 0, "", 0, 1, false, null), // Out of bounds length

                // Invalid offsets (Note: String.regionMatches returns false, not an exception)
                arguments("", true, -1, "", -1, -1, false, null),

                // Null inputs
                arguments("a", true, 0, null, 0, 0, false, NullPointerException.class),
                arguments(null, true, 0, null, 0, 0, false, NullPointerException.class),
                arguments(null, true, 0, "", 0, 0, false, NullPointerException.class)
            );
        }

        @ParameterizedTest(name = "[{index}] regionMatches({0}, {1}, {2}, {3}, {4}, {5})")
        @MethodSource("regionMatchesSource")
        void regionMatches_withVariousInputs_shouldReturnExpectedResultOrThrow(
            final String source, final boolean ignoreCase, final int sourceOffset,
            final String other, final int otherOffset, final int length,
            final boolean expectedResult, final Class<? extends Throwable> expectedThrowable) {

            if (expectedThrowable != null) {
                // Test that CharSequenceUtils throws the correct exception for both String and CharSequence inputs
                assertThrows(expectedThrowable,
                    () -> CharSequenceUtils.regionMatches(source, ignoreCase, sourceOffset, other, otherOffset, length),
                    "Should throw for String inputs");

                final CharSequence csSource = source != null ? new StringBuilder(source) : null;
                final CharSequence csOther = other != null ? new StringBuilder(other) : null;
                assertThrows(expectedThrowable,
                    () -> CharSequenceUtils.regionMatches(csSource, ignoreCase, sourceOffset, csOther, otherOffset, length),
                    "Should throw for CharSequence inputs");
            } else {
                // For non-exceptional cases, verify the result against the expected boolean value.
                // Test with String inputs
                assertEquals(expectedResult,
                    CharSequenceUtils.regionMatches(source, ignoreCase, sourceOffset, other, otherOffset, length),
                    "Failed with String inputs");

                // Test with non-String CharSequence inputs (StringBuilder)
                final CharSequence csSource = new StringBuilder(source);
                final CharSequence csOther = new StringBuilder(other);
                assertEquals(expectedResult,
                    CharSequenceUtils.regionMatches(csSource, ignoreCase, sourceOffset, csOther, otherOffset, length),
                    "Failed with CharSequence inputs");
            }
        }
    }

    @Nested
    @DisplayName("lastIndexOf Tests")
    class LastIndexOfTest {

        static Stream<Arguments> lastIndexOfImplementationSource() {
            return Stream.of(
                // Test with various CharSequence implementations
                arguments("abc", "b", 2, 1),
                arguments(new StringBuilder("abc"), "b", 2, 1),
                arguments(new StringBuffer("abc"), "b", 2, 1),
                arguments("abc", new StringBuilder("b"), 2, 1),
                arguments(new StringBuilder("abc"), new StringBuilder("b"), 2, 1),
                arguments(new StringBuffer("abc"), new StringBuffer("b"), 2, 1),
                arguments(new StringBuilder("abc"), new StringBuffer("b"), 2, 1)
            );
        }

        @ParameterizedTest
        @MethodSource("lastIndexOfImplementationSource")
        void lastIndexOf_withVariousCharSequenceImplementations_shouldReturnCorrectIndex(
            final CharSequence cs, final CharSequence search, final int start, final int expected) {
            assertEquals(expected, CharSequenceUtils.lastIndexOf(cs, search, start));
        }

        static Stream<Arguments> lastIndexOfComparisonSource() {
            return Stream.of(
                arguments("abc", "b"),
                arguments("abcabc", "abc"),
                arguments("ababa", "aba"),
                arguments("", ""),
                arguments("abc", ""),
                arguments("", "a"),
                arguments("mississippi", "issi"),
                arguments("b", "abc")
            );
        }

        @ParameterizedTest
        @MethodSource("lastIndexOfComparisonSource")
        void lastIndexOf_shouldBehaveLikeStringLastIndexOf(final String text, final String search) {
            // Use WrapperString to ensure the generic CharSequence logic is tested
            final CharSequence csText = new WrapperString(text);
            final CharSequence csSearch = new WrapperString(search);
            final int maxLen = Math.max(text.length(), search.length());

            // Test a range of start indices around the string lengths
            for (int i = -5; i <= maxLen + 5; i++) {
                final int expected = text.lastIndexOf(search, i);
                final int currentIndex = i; // for use in lambda
                assertEquals(expected, CharSequenceUtils.lastIndexOf(csText, csSearch, currentIndex),
                    () -> String.format("lastIndexOf(\"%s\", \"%s\", %d) failed", text, search, currentIndex));
            }

            // Test edge case start indices
            assertEquals(text.lastIndexOf(search, Integer.MIN_VALUE),
                CharSequenceUtils.lastIndexOf(csText, csSearch, Integer.MIN_VALUE),
                "Failed with start index Integer.MIN_VALUE");
            assertEquals(text.lastIndexOf(search, Integer.MAX_VALUE),
                CharSequenceUtils.lastIndexOf(csText, csSearch, Integer.MAX_VALUE),
                "Failed with start index Integer.MAX_VALUE");
        }
    }
}