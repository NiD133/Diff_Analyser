package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link CharSequenceUtils#lastIndexOf(CharSequence, CharSequence, int)}.
 *
 * <p>This class focuses on two main areas of testing for {@code lastIndexOf}:
 * <ol>
 *     <li>Correctness with various {@link CharSequence} implementations (String, StringBuilder, etc.).</li>
 *     <li>Equivalence with the behavior of {@link String#lastIndexOf(String, int)} across a wide range of inputs,
 *     including edge cases and randomized long strings.</li>
 * </ol>
 */
public class CharSequenceUtilsLastIndexOfTest {

    /**
     * A simple wrapper for a CharSequence. This is used to ensure that the code under test
     * uses the generic {@link CharSequence} logic rather than any potential optimizations
     * for specific implementations like {@link String}.
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

        // The following methods are not strictly required by CharSequenceUtils but are
        // part of the CharSequence interface since Java 8.
        @Override
        public IntStream chars() {
            return inner.chars();
        }

        @Override
        public IntStream codePoints() {
            return inner.codePoints();
        }
    }

    static Stream<Arguments> lastIndexOfWithStandardCharSequenceSource() {
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

    @ParameterizedTest
    @MethodSource("lastIndexOfWithStandardCharSequenceSource")
    @DisplayName("Test lastIndexOf with different CharSequence implementations")
    void testLastIndexOf_withDifferentCharSequenceImplementations(final CharSequence charSequence, final CharSequence searchSequence, final int start, final int expected) {
        assertEquals(expected, CharSequenceUtils.lastIndexOf(charSequence, searchSequence, start));
    }

    static Stream<Arguments> lastIndexOfEquivalenceTestCases() {
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
            // This case tests a specific code path where a partial match ("st")
            // occurs earlier in the string than the full match.
            arguments("junk-ststarting", "starting")
        );
    }

    @ParameterizedTest
    @MethodSource("lastIndexOfEquivalenceTestCases")
    @DisplayName("Test lastIndexOf behaves like String.lastIndexOf for various cases")
    void testLastIndexOf_behavesLikeStringLastIndexOf(final String text, final String searchText) {
        assertLastIndexOfAgreesWithString(text, searchText);
    }

    @Test
    @DisplayName("Test lastIndexOf behaves like String.lastIndexOf with randomized long strings")
    void testLastIndexOf_withRandomizedLongStrings() {
        final Random random = new Random();
        final StringBuilder searchTextBuilder = new StringBuilder();

        // Create a search text that is longer than the internal toString limit in CharSequenceUtils
        // to exercise the code path for longer sequences.
        while (searchTextBuilder.length() <= CharSequenceUtils.TO_STRING_LIMIT) {
            searchTextBuilder.append(random.nextInt());
        }
        final String searchText = searchTextBuilder.toString();

        // Start with the text being the same as the search text
        final StringBuilder textBuilder = new StringBuilder(searchText);
        assertLastIndexOfAgreesWithString(textBuilder.toString(), searchText);

        // Randomly add characters to the text to search in and re-test
        for (int i = 0; i < 100; i++) {
            if (random.nextBoolean()) {
                // Append a short random string
                textBuilder.append(random.nextInt(10));
            } else {
                // Prepend a short random string
                textBuilder.insert(0, random.nextInt(100));
            }
            assertLastIndexOfAgreesWithString(textBuilder.toString(), searchText);
        }
    }

    /**
     * Asserts that {@link CharSequenceUtils#lastIndexOf(CharSequence, CharSequence, int)} returns the
     * same result as {@link String#lastIndexOf(String, int)} for the given strings.
     * <p>
     * This method exhaustively checks a wide range of start indices, including edge cases like
     * {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE}, to ensure behavioral equivalence.
     * It also checks for symmetry by swapping the text and searchText.
     * </p>
     *
     * @param text       the text to be searched.
     * @param searchText the text to search for.
     */
    private void assertLastIndexOfAgreesWithString(final String text, final String searchText) {
        final int maxLength = Math.max(text.length(), searchText.length());
        // Check a wide range of start indices around the string lengths
        for (int i = -maxLength - 10; i <= maxLength + 10; i++) {
            assertLastIndexOfAgreesWithStringAtIndex(text, searchText, i);
            assertLastIndexOfAgreesWithStringAtIndex(searchText, text, i); // Symmetric check
        }
        // Check boundary start indices
        assertLastIndexOfAgreesWithStringAtIndex(text, searchText, Integer.MIN_VALUE);
        assertLastIndexOfAgreesWithStringAtIndex(searchText, text, Integer.MIN_VALUE);
        assertLastIndexOfAgreesWithStringAtIndex(text, searchText, Integer.MAX_VALUE);
        assertLastIndexOfAgreesWithStringAtIndex(searchText, text, Integer.MAX_VALUE);
    }

    /**
     * Asserts that CharSequenceUtils.lastIndexOf and String.lastIndexOf produce the same result
     * for a specific start index.
     */
    private void assertLastIndexOfAgreesWithStringAtIndex(final String text, final String searchText, final int startIndex) {
        final int expected = text.lastIndexOf(searchText, startIndex);

        // Use WrapperString to ensure we are testing the CharSequence overload
        final int actual = CharSequenceUtils.lastIndexOf(new WrapperString(text), new WrapperString(searchText), startIndex);

        assertEquals(expected, actual, () -> String.format(
            "Mismatch for lastIndexOf(\"%s\", \"%s\", %d). Expected: %d, Actual: %d",
            text, searchText, startIndex, expected, actual));
    }
}