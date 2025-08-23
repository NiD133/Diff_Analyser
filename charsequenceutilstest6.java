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
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("deprecation") // Tests a deprecated constructor.
class CharSequenceUtilsTest extends AbstractLangTest {

    // A CharSequence wrapper that prevents casting to String, forcing the use of CharSequence-specific methods.
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

        @Override
        public IntStream chars() {
            return inner.chars();
        }

        @Override
        public IntStream codePoints() {
            return inner.codePoints();
        }
    }

    // region: Tests for lastIndexOf
    // ==========================================================================

    static Stream<Arguments> lastIndexOfWithDifferentCharSequencesSource() {
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
    @MethodSource("lastIndexOfWithDifferentCharSequencesSource")
    @DisplayName("lastIndexOf should work correctly with various CharSequence implementations")
    void testLastIndexOf_withDifferentCharSequences(final CharSequence cs, final CharSequence search, final int start, final int expected) {
        assertEquals(expected, CharSequenceUtils.lastIndexOf(cs, search, start));
    }

    @Test
    @DisplayName("lastIndexOf should behave identically to String.lastIndexOf for a wide range of inputs")
    void testLastIndexOf_againstStringImplementation() {
        assertLastIndexOfBehavesLikeString("abc", "b");
        assertLastIndexOfBehavesLikeString("abc", "d");
        assertLastIndexOfBehavesLikeString("a", "abc");
        assertLastIndexOfBehavesLikeString("", "");
        assertLastIndexOfBehavesLikeString("ab", "");
    }

    /**
     * Tests CharSequenceUtils.lastIndexOf against String.lastIndexOf using a wide range of start indices.
     */
    private void assertLastIndexOfBehavesLikeString(final CharSequence cs, final CharSequence search) {
        final int maxLen = Math.max(cs.length(), search.length());
        // Test a range of start indices around the string lengths
        for (int i = -maxLen - 10; i <= maxLen + 10; i++) {
            assertLastIndexOfForPair(cs, search, i);
            assertLastIndexOfForPair(search, cs, i); // Test the reverse combination
        }
        // Test edge case start indices
        assertLastIndexOfForPair(cs, search, Integer.MIN_VALUE);
        assertLastIndexOfForPair(cs, search, Integer.MAX_VALUE);
    }

    /**
     * Asserts that CharSequenceUtils.lastIndexOf(cs, search, start) returns the same as cs.toString().lastIndexOf(search.toString(), start).
     * Uses WrapperString to ensure the CharSequence-based implementation is tested.
     */
    private void assertLastIndexOfForPair(final CharSequence cs, final CharSequence search, final int start) {
        final int expected = cs.toString().lastIndexOf(search.toString(), start);
        final int actual = CharSequenceUtils.lastIndexOf(new WrapperString(cs.toString()), new WrapperString(search.toString()), start);
        assertEquals(expected, actual,
            () -> String.format("lastIndexOf failed for cs: '%s', search: '%s', start: %d", cs, search, start));
    }
    // endregion

    // region: Tests for regionMatches
    // ==========================================================================

    private static class RegionMatchesTestData {
        final CharSequence source;
        final boolean ignoreCase;
        final int sourceOffset;
        final CharSequence other;
        final int otherOffset;
        final int length;
        final Object expected; // Boolean for result, Class for exception

        RegionMatchesTestData(final CharSequence source, final boolean ignoreCase, final int sourceOffset,
                              final CharSequence other, final int otherOffset, final int length, final Object expected) {
            this.source = source;
            this.ignoreCase = ignoreCase;
            this.sourceOffset = sourceOffset;
            this.other = other;
            this.otherOffset = otherOffset;
            this.length = length;
            this.expected = expected;
        }

        @Override
        public String toString() {
            return String.format("regionMatches(%s, %b, %d, %s, %d, %d) -> %s",
                source, ignoreCase, sourceOffset, other, otherOffset, length, expected);
        }
    }

    static Stream<RegionMatchesTestData> regionMatchesDataSource() {
        return Stream.of(
            //           Source, IgnoreCase, srcOffset, Other, otherOffset, length, Expected
            new RegionMatchesTestData("", true, -1, "", -1, -1, false),
            new RegionMatchesTestData("", true, 0, "", 0, 1, false),
            new RegionMatchesTestData("a", true, 0, "abc", 0, 0, true),
            new RegionMatchesTestData("a", true, 0, "abc", 0, 1, true),
            new RegionMatchesTestData("a", true, 0, null, 0, 0, NullPointerException.class),
            new RegionMatchesTestData(null, true, 0, null, 0, 0, NullPointerException.class),
            new RegionMatchesTestData(null, true, 0, "", 0, 0, NullPointerException.class),
            new RegionMatchesTestData("Abc", true, 0, "abc", 0, 3, true),
            new RegionMatchesTestData("Abc", false, 0, "abc", 0, 3, false),
            new RegionMatchesTestData("Abc", true, 1, "abc", 1, 2, true),
            new RegionMatchesTestData("Abc", false, 1, "abc", 1, 2, true),
            new RegionMatchesTestData("Abcd", true, 1, "abcD", 1, 2, true),
            new RegionMatchesTestData("Abcd", false, 1, "abcD", 1, 2, true)
        );
    }

    @ParameterizedTest
    @MethodSource("regionMatchesDataSource")
    @DisplayName("regionMatches should return expected result or throw exception for various inputs")
    void testRegionMatches(final RegionMatchesTestData data) {
        if (data.expected instanceof Class) {
            @SuppressWarnings("unchecked")
            final Class<? extends Throwable> expectedThrowable = (Class<? extends Throwable>) data.expected;
            assertThrows(expectedThrowable, () -> CharSequenceUtils.regionMatches(data.source, data.ignoreCase,
                data.sourceOffset, data.other, data.otherOffset, data.length));
        } else {
            final boolean expectedResult = (Boolean) data.expected;
            final boolean actualResult = CharSequenceUtils.regionMatches(data.source, data.ignoreCase,
                data.sourceOffset, data.other, data.otherOffset, data.length);
            assertEquals(expectedResult, actualResult);
        }
    }
    // endregion

    // region: Tests for subSequence
    // ==========================================================================

    @Test
    @DisplayName("subSequence should throw IndexOutOfBoundsException if start is greater than length")
    void testSubSequence_withStartGreaterThanLength_throwsException() {
        assertIndexOutOfBoundsException(() -> CharSequenceUtils.subSequence(StringUtils.EMPTY, 1),
            "String index out of range: -1");
        assertIndexOutOfBoundsException(() -> CharSequenceUtils.subSequence("abc", 4),
            "String index out of range: -1");
    }
    // endregion

    // region: Tests for constructor
    // ==========================================================================
    @Test
    @DisplayName("Constructor should be public for tooling compatibility")
    void testConstructor() throws ReflectiveOperationException {
        final Constructor<CharSequenceUtils> constructor = CharSequenceUtils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPublic(constructor.getModifiers()), "Constructor should be public");
        assertNotNull(constructor.newInstance(), "Instance created with public constructor");
    }
    // endregion
}