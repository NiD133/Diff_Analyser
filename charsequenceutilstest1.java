package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Random;
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
     * A custom CharSequence implementation that wraps another CharSequence.
     * This is used to ensure that the methods under test work correctly with any
     * CharSequence implementation, not just standard ones like String or StringBuilder.
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
            return new WrapperString(inner.subSequence(start, end));
        }

        @Override
        public String toString() {
            return inner.toString();
        }
    }

    @Nested
    @DisplayName("regionMatches(CharSequence, boolean, int, CharSequence, int, int)")
    class RegionMatchesTest {

        static Stream<Arguments> regionMatchesSource() {
            // Arguments: cs, ignoreCase, thisStart, substring, start, length, expected
            return Stream.of(
                arguments("abc", false, 0, "abc", 0, 3, true),
                arguments("Abc", false, 0, "abc", 0, 3, false),
                arguments("Abc", true, 0, "abc", 0, 3, true),
                arguments("Abc", false, 1, "bc", 0, 2, true),
                arguments("Abc", true, 1, "bC", 0, 2, true),
                arguments("Abcd", false, 1, "abcD", 1, 2, true),
                // Edge cases from original data
                arguments("a", true, 0, "abc", 0, 1, true),
                arguments("a", true, 0, "abc", 0, 0, true), // length 0 is always true
                arguments("", true, 0, "", 0, 1, false), // length > source length
                arguments("", true, -1, "", -1, -1, false) // negative offsets
            );
        }

        @ParameterizedTest
        @MethodSource("regionMatchesSource")
        void testRegionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
            final CharSequence substring, final int start, final int length, final boolean expected) {
            final boolean actual = CharSequenceUtils.regionMatches(cs, ignoreCase, thisStart, substring, start, length);
            assertEquals(expected, actual);
        }

        @Test
        void testRegionMatches_withNulls_throwsNullPointerException() {
            assertThrows(NullPointerException.class, () -> CharSequenceUtils.regionMatches(null, true, 0, "a", 0, 1));
            assertThrows(NullPointerException.class, () -> CharSequenceUtils.regionMatches("a", true, 0, null, 0, 1));
        }
    }

    @Nested
    @DisplayName("lastIndexOf(CharSequence, CharSequence, int)")
    class LastIndexOfTest {

        static Stream<Arguments> lastIndexOfWithStandardCharSequenceSource() {
            // Arguments: charSequence, searchSequence, start, expected
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
        void testLastIndexOf_withStandardCharSequences(final CharSequence cs, final CharSequence search, final int start, final int expected) {
            assertEquals(expected, CharSequenceUtils.lastIndexOf(cs, search, start));
        }

        @Test
        void testLastIndexOf_againstStringImplementation_withRandomData() {
            final Random random = new Random();
            for (int i = 0; i < 100; i++) {
                final String sourceStr = RandomStringUtils.random(random.nextInt(50));
                final String searchStr = RandomStringUtils.random(random.nextInt(10));

                // Test with a range of start indices around the string lengths
                final int startRange = Math.max(sourceStr.length(), searchStr.length()) + 5;
                for (int start = -startRange; start <= startRange; start++) {
                    assertLastIndexOfBehavesLikeString(sourceStr, searchStr, start);
                    // Also test with search string as source and vice-versa
                    assertLastIndexOfBehavesLikeString(searchStr, sourceStr, start);
                }

                // Test edge case start indices
                assertLastIndexOfBehavesLikeString(sourceStr, searchStr, Integer.MIN_VALUE);
                assertLastIndexOfBehavesLikeString(sourceStr, searchStr, Integer.MAX_VALUE);
            }
        }

        private void assertLastIndexOfBehavesLikeString(final String source, final String search, final int start) {
            final CharSequence wrapperSource = new WrapperString(source);
            final CharSequence wrapperSearch = new WrapperString(search);

            final int expected = source.lastIndexOf(search, start);
            final int actual = CharSequenceUtils.lastIndexOf(wrapperSource, wrapperSearch, start);

            assertEquals(expected, actual,
                () -> String.format("lastIndexOf(\"%s\", \"%s\", %d) failed", source, search, start));
        }
    }

    @Test
    void testConstructor() {
        assertNotNull(new CharSequenceUtils());
        final Constructor<?>[] cons = CharSequenceUtils.class.getDeclaredConstructors();
        assertEquals(1, cons.length, "Should have only one constructor");
        assertTrue(Modifier.isPublic(cons[0].getModifiers()), "Constructor should be public");
        assertTrue(Modifier.isPublic(CharSequenceUtils.class.getModifiers()), "Class should be public");
        assertFalse(Modifier.isFinal(CharSequenceUtils.class.getModifiers()), "Class should not be final");
    }
}