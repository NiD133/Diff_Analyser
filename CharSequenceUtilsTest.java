package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIndexOutOfBoundsException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
 * Tests for CharSequenceUtils with a focus on readability and maintainability.
 */
@DisplayName("CharSequenceUtils tests")
class CharSequenceUtilsTest extends AbstractLangTest {

    /**
     * A CharSequence wrapper used to ensure our tests exercise CharSequence API paths,
     * not just String-specific code paths.
     */
    static class WrapperString implements CharSequence {
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
        public IntStream chars() {
            return inner.chars();
        }

        @Override
        public IntStream codePoints() {
            return inner.codePoints();
        }

        @Override
        public String toString() {
            return inner.toString();
        }
    }

    // ---------------------------------------------------------------------
    // lastIndexOf(CharSequence, CharSequence, int)
    // ---------------------------------------------------------------------

    static Stream<Arguments> lastIndexWithStandardCharSequence() {
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

    @DisplayName("lastIndexOf matches for different CharSequence implementations")
    @ParameterizedTest
    @MethodSource("lastIndexWithStandardCharSequence")
    void testLastIndexOfWithDifferentCharSequences(final CharSequence cs, final CharSequence search, final int start, final int expected) {
        assertEquals(expected, CharSequenceUtils.lastIndexOf(cs, search, start));
    }

    @DisplayName("lastIndexOf produces the same result as String#lastIndexOf (fuzz + edge cases)")
    @Test
    void testLastIndexOf_matchesStringImplementation() {
        // A few focused cases
        assertLastIndexOfMatchesString("808087847-1321060740-635567660180086727-925755305", "-1321060740-635567660");
        assertLastIndexOfMatchesString("", "");
        assertLastIndexOfMatchesString("1", "");
        assertLastIndexOfMatchesString("", "1");
        assertLastIndexOfMatchesString("1", "1");
        assertLastIndexOfMatchesString("11", "1");
        assertLastIndexOfMatchesString("1", "11");

        assertLastIndexOfMatchesString("apache", "a");
        assertLastIndexOfMatchesString("apache", "p");
        assertLastIndexOfMatchesString("apache", "e");
        assertLastIndexOfMatchesString("apache", "x");
        assertLastIndexOfMatchesString("oraoraoraora", "r");
        assertLastIndexOfMatchesString("mudamudamudamuda", "d");

        // Ensures the code path that handles multi-char partial matches is exercised
        assertLastIndexOfMatchesString("junk-ststarting", "starting");

        // Fuzz against String#lastIndexOf while also covering TO_STRING_LIMIT boundaries
        final Random random = new Random();
        final StringBuilder segment = new StringBuilder();
        // Build a segment longer than TO_STRING_LIMIT to exercise toString() boundary handling
        while (segment.length() <= CharSequenceUtils.TO_STRING_LIMIT) {
            segment.append(random.nextInt());
        }
        StringBuilder original = new StringBuilder(segment);
        assertLastIndexOfMatchesString(original, segment);

        for (int i = 0; i < 100; i++) {
            if (random.nextDouble() < 0.5) {
                original.append(random.nextInt() % 10);
            } else {
                original = new StringBuilder().append(random.nextInt() % 100).append(original);
            }
            assertLastIndexOfMatchesString(original, segment);
        }
    }

    private void assertLastIndexOfMatchesString(final CharSequence haystack, final CharSequence needle) {
        final int max = Math.max(haystack.length(), needle.length());
        for (int start = -max - 10; start <= max + 10; start++) {
            assertLastIndexOfMatchesString(haystack, needle, start);
            // Also check the reverse order to broaden coverage of asymmetric inputs
            assertLastIndexOfMatchesString(needle, haystack, start);
        }
        assertLastIndexOfMatchesString(haystack, needle, Integer.MIN_VALUE);
        assertLastIndexOfMatchesString(haystack, needle, Integer.MAX_VALUE);
    }

    private void assertLastIndexOfMatchesString(final CharSequence a, final CharSequence b, final int start) {
        final int expected = a.toString().lastIndexOf(b.toString(), start);
        final int actual = CharSequenceUtils.lastIndexOf(new WrapperString(a.toString()), new WrapperString(b.toString()), start);
        assertEquals(expected, actual, () -> "lastIndexOf mismatch: haystack='" + a + "', needle='" + b + "', start=" + start);
    }

    // ---------------------------------------------------------------------
    // regionMatches
    // ---------------------------------------------------------------------

    /**
     * Test case holder for regionMatches.
     */
    static class RegionCase {
        final String source;
        final boolean ignoreCase;
        final int srcOffset;
        final String other;
        final int otherOffset;
        final int length;
        final Class<? extends Throwable> expectedThrowable; // null for non-exception cases

        RegionCase(final String source, final boolean ignoreCase, final int srcOffset,
                   final String other, final int otherOffset, final int length) {
            this(source, ignoreCase, srcOffset, other, otherOffset, length, null);
        }

        RegionCase(final String source, final boolean ignoreCase, final int srcOffset,
                   final String other, final int otherOffset, final int length,
                   final Class<? extends Throwable> expectedThrowable) {
            this.source = source;
            this.ignoreCase = ignoreCase;
            this.srcOffset = srcOffset;
            this.other = other;
            this.otherOffset = otherOffset;
            this.length = length;
            this.expectedThrowable = expectedThrowable;
        }

        @Override
        public String toString() {
            final String mode = ignoreCase ? "ignoreCase" : "matchCase";
            return "RegionCase{" +
                "source='" + source + '\'' +
                ", " + mode +
                ", srcOffset=" + srcOffset +
                ", other='" + other + '\'' +
                ", otherOffset=" + otherOffset +
                ", length=" + length +
                (expectedThrowable != null ? ", throws=" + expectedThrowable.getSimpleName() : "") +
                '}';
        }
    }

    static Stream<RegionCase> regionCases() {
        return Stream.of(
            //            source  ignoreCase srcOff other  otherOff len   (expected via String, or throwable)
            new RegionCase("",     true,     -1,    "",    -1,      -1), // false
            new RegionCase("",     true,      0,    "",     0,       1), // false
            new RegionCase("a",    true,      0,    "abc",  0,       0), // true
            new RegionCase("a",    true,      0,    "abc",  0,       1), // true
            new RegionCase("a",    true,      0,    null,   0,       0, NullPointerException.class),
            new RegionCase(null,   true,      0,    null,   0,       0, NullPointerException.class),
            new RegionCase(null,   true,      0,    "",     0,       0, NullPointerException.class),
            new RegionCase("Abc",  true,      0,    "abc",  0,       3), // true
            new RegionCase("Abc",  false,     0,    "abc",  0,       3), // false
            new RegionCase("Abc",  true,      1,    "abc",  1,       2), // true
            new RegionCase("Abc",  false,     1,    "abc",  1,       2), // true
            new RegionCase("Abcd", true,      1,    "abcD", 1,       2), // true
            new RegionCase("Abcd", false,     1,    "abcD", 1,       2)  // true
        );
    }

    @DisplayName("regionMatches behaves like String#regionMatches for String and generic CharSequence")
    @ParameterizedTest(name = "{0}")
    @MethodSource("regionCases")
    void testRegionMatches(final RegionCase c) {
        if (c.expectedThrowable != null) {
            // String#regionMatches (may throw due to null source or other)
            assertThrows(c.expectedThrowable,
                () -> c.source.regionMatches(c.ignoreCase, c.srcOffset, c.other, c.otherOffset, c.length),
                "String#regionMatches: expected exception");

            // CharSequenceUtils with String source
            assertThrows(c.expectedThrowable,
                () -> CharSequenceUtils.regionMatches(c.source, c.ignoreCase, c.srcOffset, c.other, c.otherOffset, c.length),
                "CharSequenceUtils#regionMatches (String): expected exception");

            // CharSequenceUtils with non-String source (StringBuilder)
            assertThrows(c.expectedThrowable,
                () -> CharSequenceUtils.regionMatches(new StringBuilder(c.source), c.ignoreCase, c.srcOffset, c.other, c.otherOffset, c.length),
                "CharSequenceUtils#regionMatches (StringBuilder): expected exception");
        } else {
            final boolean expected = c.source.regionMatches(c.ignoreCase, c.srcOffset, c.other, c.otherOffset, c.length);

            final boolean actualString = CharSequenceUtils.regionMatches(c.source, c.ignoreCase, c.srcOffset, c.other, c.otherOffset, c.length);
            assertEquals(expected, actualString, "CharSequenceUtils (String) disagrees with String#regionMatches");

            final boolean actualNonString = CharSequenceUtils.regionMatches(new StringBuilder(c.source), c.ignoreCase, c.srcOffset, c.other, c.otherOffset, c.length);
            assertEquals(expected, actualNonString, "CharSequenceUtils (StringBuilder) disagrees with String#regionMatches");
        }
    }

    // ---------------------------------------------------------------------
    // subSequence
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("subSequence")
    class SubSequenceTests {

        @Test
        @DisplayName("returns null when input CharSequence is null")
        void nullInputReturnsNull() {
            // Per API contract: null input yields null result regardless of start
            // (bounds checks are not performed on null)
            org.junit.jupiter.api.Assertions.assertNull(CharSequenceUtils.subSequence(null, -1));
            org.junit.jupiter.api.Assertions.assertNull(CharSequenceUtils.subSequence(null, 0));
            org.junit.jupiter.api.Assertions.assertNull(CharSequenceUtils.subSequence(null, 1));
        }

        @Test
        @DisplayName("returns correct subsequences for valid inputs")
        void returnsValidSubSequences() {
            assertEquals(StringUtils.EMPTY, CharSequenceUtils.subSequence(StringUtils.EMPTY, 0));
            assertEquals("012", CharSequenceUtils.subSequence("012", 0));
            assertEquals("12", CharSequenceUtils.subSequence("012", 1));
            assertEquals("2", CharSequenceUtils.subSequence("012", 2));
            assertEquals(StringUtils.EMPTY, CharSequenceUtils.subSequence("012", 3));
        }

        @Test
        @DisplayName("throws on negative start")
        void throwsOnNegativeStart() {
            assertIndexOutOfBoundsException(() -> CharSequenceUtils.subSequence(StringUtils.EMPTY, -1));
        }

        @Test
        @DisplayName("throws when start > length")
        void throwsWhenStartExceedsLength() {
            assertIndexOutOfBoundsException(() -> CharSequenceUtils.subSequence(StringUtils.EMPTY, 1));
        }
    }

    // ---------------------------------------------------------------------
    // toCharArray
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("toCharArray converts any CharSequence to a char[] and handles null")
    void testToCharArray() {
        final StringBuilder builder = new StringBuilder("abcdefg");
        final char[] expected = builder.toString().toCharArray();

        assertArrayEquals(expected, CharSequenceUtils.toCharArray(builder));
        assertArrayEquals(expected, CharSequenceUtils.toCharArray(builder.toString()));
        assertArrayEquals(ArrayUtils.EMPTY_CHAR_ARRAY, CharSequenceUtils.toCharArray(null));
    }

    // ---------------------------------------------------------------------
    // Constructor visibility
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("Constructor is public and class is non-final (for bean tools)")
    void testConstructor() {
        assertNotNull(new CharSequenceUtils()); // Deprecated but intentionally public
        final Constructor<?>[] cons = CharSequenceUtils.class.getDeclaredConstructors();
        assertEquals(1, cons.length);
        assertTrue(Modifier.isPublic(cons[0].getModifiers()));
        assertTrue(Modifier.isPublic(CharSequenceUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(CharSequenceUtils.class.getModifiers()));
    }
}