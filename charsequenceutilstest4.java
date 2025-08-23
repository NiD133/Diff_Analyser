package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIndexOutOfBoundsException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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
public class CharSequenceUtilsTest extends AbstractLangTest {

    /**
     * A simple CharSequence wrapper that prevents the methods under test
     * from short-circuiting to String-specific implementations, ensuring the
     * generic CharSequence logic is exercised.
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
    }

    @Nested
    @DisplayName("Tests for subSequence(CharSequence, int)")
    class SubSequenceTest {

        @Test
        @DisplayName("should return null for null input")
        void withNullInput() {
            assertNull(CharSequenceUtils.subSequence(null, -1));
            assertNull(CharSequenceUtils.subSequence(null, 0));
            assertNull(CharSequenceUtils.subSequence(null, 1));
        }

        @Test
        @DisplayName("should return correct subsequence for valid input")
        void withValidInput() {
            assertEquals(StringUtils.EMPTY, CharSequenceUtils.subSequence(StringUtils.EMPTY, 0));
            assertEquals("012", CharSequenceUtils.subSequence("012", 0));
            assertEquals("12", CharSequenceUtils.subSequence("012", 1));
            assertEquals("2", CharSequenceUtils.subSequence("012", 2));
            assertEquals(StringUtils.EMPTY, CharSequenceUtils.subSequence("012", 3));
        }

        @Test
        @DisplayName("should throw IndexOutOfBoundsException for invalid start index")
        void withInvalidStartIndex() {
            assertIndexOutOfBoundsException(() -> CharSequenceUtils.subSequence("abc", -1), "start");
            assertIndexOutOfBoundsException(() -> CharSequenceUtils.subSequence("abc", 4), "start");
        }
    }

    @Nested
    @DisplayName("Tests for lastIndexOf(CharSequence, CharSequence, int)")
    class LastIndexOfTest {

        static Stream<Arguments> lastIndexOfSource() {
            return Stream.of(
                // Test with different CharSequence implementations
                arguments("abc", "b", 2, 1),
                arguments(new StringBuilder("abc"), "b", 2, 1),
                arguments(new StringBuffer("abc"), "b", 2, 1),
                arguments("abc", new StringBuilder("b"), 2, 1),
                arguments(new StringBuilder("abc"), new StringBuilder("b"), 2, 1),
                arguments(new StringBuffer("abc"), new StringBuffer("b"), 2, 1),
                arguments(new StringBuilder("abc"), new StringBuffer("b"), 2, 1)
            );
        }

        @DisplayName("should work with various CharSequence implementations")
        @ParameterizedTest(name = "[{index}] cs={0}, search={1}, start={2} -> {3}")
        @MethodSource("lastIndexOfSource")
        void withDifferentCharSequenceImplementations(final CharSequence cs, final CharSequence search, final int start, final int expected) {
            assertEquals(expected, CharSequenceUtils.lastIndexOf(cs, search, start));
        }

        static Stream<Arguments> lastIndexOfStringPairSource() {
            return Stream.of(
                arguments("abcde", "cd"),
                arguments("abab", "b"),
                arguments("abc", "d"),
                arguments("abc", ""),
                arguments("", "a"),
                arguments("", "")
            );
        }

        @DisplayName("should behave like String.lastIndexOf for various start indices")
        @ParameterizedTest(name = "[{index}] text={0}, search={1}")
        @MethodSource("lastIndexOfStringPairSource")
        void againstStringImplementation(final String text, final String search) {
            // Use WrapperString to ensure CharSequence-specific logic is tested
            final CharSequence wrappedText = new WrapperString(text);
            final CharSequence wrappedSearch = new WrapperString(search);

            final int maxLength = Math.max(text.length(), search.length());
            // Test a range of start indices around the string lengths
            for (int i = -maxLength - 5; i <= maxLength + 5; i++) {
                final int expected = text.lastIndexOf(search, i);
                final int actual = CharSequenceUtils.lastIndexOf(wrappedText, wrappedSearch, i);
                final int finalI = i;
                assertEquals(expected, actual,
                    () -> String.format("lastIndexOf(\"%s\", \"%s\", %d) failed", text, search, finalI));
            }
            // Test edge cases for the start index
            assertEquals(text.lastIndexOf(search, Integer.MIN_VALUE),
                CharSequenceUtils.lastIndexOf(wrappedText, wrappedSearch, Integer.MIN_VALUE));
            assertEquals(text.lastIndexOf(search, Integer.MAX_VALUE),
                CharSequenceUtils.lastIndexOf(wrappedText, wrappedSearch, Integer.MAX_VALUE));
        }
    }

    @Nested
    @DisplayName("Tests for regionMatches(CharSequence, boolean, int, CharSequence, int, int)")
    class RegionMatchesTest {

        static Stream<Arguments> regionMatchesSource() {
            return Stream.of(
                // source, ignoreCase, srcOffset, other, otherOffset, len, expectedResult
                arguments("Abc",    false, 0, "abc",    0, 3, false),
                arguments("Abc",    true,  0, "abc",    0, 3, true),
                arguments("Abc",    false, 1, "abc",    1, 2, true),
                arguments("Abc",    true,  1, "abc",    1, 2, true),
                arguments("Abcd",   false, 1, "abcD",   1, 2, true),
                arguments("Abcd",   true,  1, "abcD",   1, 2, true),
                arguments("a",      true,  0, "abc",    0, 1, true),
                arguments("a",      true,  0, "abc",    0, 0, true),
                arguments("",       true,  0, "",       0, 1, false),
                arguments("",       true, -1, "",      -1,-1, false),
                // Expected Exception cases
                arguments("a",      true,  0, null,     0, 0, NullPointerException.class),
                arguments(null,     true,  0, "a",      0, 0, NullPointerException.class),
                arguments(null,     true,  0, null,     0, 0, NullPointerException.class)
            );
        }

        @ParameterizedTest(name = "[{index}] {0}, {1}, {2}, {3}, {4}, {5} -> {6}")
        @MethodSource("regionMatchesSource")
        void withVariousInputs(final CharSequence source, final boolean ignoreCase, final int sourceOffset,
                               final CharSequence other, final int otherOffset, final int length, final Object expected) {
            if (expected instanceof Class) {
                @SuppressWarnings("unchecked")
                final Class<? extends Throwable> throwableClass = (Class<? extends Throwable>) expected;
                assertThrows(throwableClass, () ->
                    CharSequenceUtils.regionMatches(source, ignoreCase, sourceOffset, other, otherOffset, length));
            } else {
                final boolean result = CharSequenceUtils.regionMatches(source, ignoreCase, sourceOffset, other, otherOffset, length);
                assertEquals(expected, result);
            }
        }
    }
}