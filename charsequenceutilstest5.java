package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIndexOutOfBoundsException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

/**
 * Tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest extends AbstractLangTest {

    /**
     * A wrapper for CharSequence that ensures the test is not dependent on
     * specific implementations like String, StringBuilder, or StringBuffer.
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

        // The methods below are not needed for the tests but are required by the CharSequence interface.
        @Override
        public IntStream chars() {
            return inner.chars();
        }

        @Override
        public IntStream codePoints() {
            return inner.codePoints();
        }
    }

    // region Constructor Test
    @Test
    @DisplayName("Test that the public constructor exists and is public for tools")
    void testConstructor() throws NoSuchMethodException {
        assertNotNull(new CharSequenceUtils());
        final Constructor<CharSequenceUtils> ctor = CharSequenceUtils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPublic(ctor.getModifiers()));
        assertTrue(ctor.isAccessible());
    }
    // endregion

    // region subSequence() Tests
    @Test
    @DisplayName("subSequence should throw IndexOutOfBoundsException for a negative start index")
    void subSequence_withNegativeStart_throwsException() {
        assertIndexOutOfBoundsException(() -> CharSequenceUtils.subSequence(StringUtils.EMPTY, -1),
            "Expected IndexOutOfBoundsException for start = -1");
    }
    // endregion

    // region lastIndexOf() Tests
    static Stream<Arguments> lastIndexOfWithStandardCharSequenceSource() {
        // Test with standard CharSequence implementations
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
    @DisplayName("lastIndexOf should work correctly with different CharSequence implementations")
    void lastIndexOf_withDifferentCharSequences(final CharSequence cs, final CharSequence search, final int start, final int expected) {
        assertEquals(expected, CharSequenceUtils.lastIndexOf(cs, search, start));
    }

    @Test
    @DisplayName("lastIndexOf should behave like String.lastIndexOf() for various inputs")
    void lastIndexOf_agreesWithStringLastIndexOf() {
        assertLastIndexOfAgreesWithJavaString("", "");
        assertLastIndexOfAgreesWithJavaString("a", "");
        assertLastIndexOfAgreesWithJavaString("a", "a");
        assertLastIndexOfAgreesWithJavaString("abc", "b");
        assertLastIndexOfAgreesWithJavaString("abc", "d");
        assertLastIndexOfAgreesWithJavaString("abcabc", "b");
        assertLastIndexOfAgreesWithJavaString("abcabc", "bc");
    }

    /**
     * Verifies that CharSequenceUtils.lastIndexOf produces the same result as
     * String.lastIndexOf for a wide range of start indices.
     */
    private void assertLastIndexOfAgreesWithJavaString(final CharSequence cs, final CharSequence search) {
        final int maxLength = Math.max(cs.length(), search.length());
        // Check a range of start indices around the string lengths
        for (int i = -5; i <= maxLength + 5; i++) {
            assertLastIndexOfAgrees(cs, search, i);
            assertLastIndexOfAgrees(search, cs, i);
        }
        // Check boundary start indices
        assertLastIndexOfAgrees(cs, search, Integer.MIN_VALUE);
        assertLastIndexOfAgrees(search, cs, Integer.MIN_VALUE);
        assertLastIndexOfAgrees(cs, search, Integer.MAX_VALUE);
        assertLastIndexOfAgrees(search, cs, Integer.MAX_VALUE);
    }

    /**
     * Asserts that CharSequenceUtils.lastIndexOf(cs, search, start) returns the same
     * value as cs.toString().lastIndexOf(search.toString(), start).
     */
    private void assertLastIndexOfAgrees(final CharSequence cs, final CharSequence search, final int start) {
        final String csStr = cs.toString();
        final String searchStr = search.toString();
        final int expected = csStr.lastIndexOf(searchStr, start);
        // Use WrapperString to ensure the method is tested against a generic CharSequence
        final int actual = CharSequenceUtils.lastIndexOf(new WrapperString(csStr), new WrapperString(searchStr), start);
        assertEquals(expected, actual,
            () -> String.format("cs: '%s', search: '%s', start: %d", cs, search, start));
    }
    // endregion

    // region regionMatches() Tests

    /**
     * Holds data for a single regionMatches test case.
     */
    private static class RegionMatchesTestData {
        private final CharSequence charSequence;
        private final boolean ignoreCase;
        private final int thisStart;
        private final CharSequence substring;
        private final int substringStart;
        private final int length;
        private final boolean expectedResult;
        private final Class<? extends Throwable> expectedThrowable;

        RegionMatchesTestData(final String charSequence, final boolean ignoreCase, final int thisStart,
                              final String substring, final int substringStart, final int length, final boolean expectedResult) {
            this(charSequence, ignoreCase, thisStart, substring, substringStart, length, expectedResult, null);
        }

        RegionMatchesTestData(final String charSequence, final boolean ignoreCase, final int thisStart,
                              final String substring, final int substringStart, final int length, final Class<? extends Throwable> expectedThrowable) {
            this(charSequence, ignoreCase, thisStart, substring, substringStart, length, false, expectedThrowable);
        }

        private RegionMatchesTestData(final String charSequence, final boolean ignoreCase, final int thisStart,
                                      final String substring, final int substringStart, final int length,
                                      final boolean expectedResult, final Class<? extends Throwable> expectedThrowable) {
            this.charSequence = charSequence;
            this.ignoreCase = ignoreCase;
            this.thisStart = thisStart;
            this.substring = substring;
            this.substringStart = substringStart;
            this.length = length;
            this.expectedResult = expectedResult;
            this.expectedThrowable = expectedThrowable;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("cs: '").append(charSequence).append("', ignoreCase: ").append(ignoreCase)
              .append(", thisStart: ").append(thisStart)
              .append(", sub: '").append(substring).append("', subStart: ").append(substringStart)
              .append(", len: ").append(length)
              .append(" -> ");
            if (expectedThrowable != null) {
                sb.append(expectedThrowable.getSimpleName());
            } else {
                sb.append(expectedResult);
            }
            return sb.toString();
        }
    }

    private static final RegionMatchesTestData[] REGION_MATCHES_TEST_DATA = {
        //           Source  IgnoreCase thisStart Other  subStart Length Result
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
    };

    static Stream<Arguments> regionMatchesSource() {
        return Stream.of(REGION_MATCHES_TEST_DATA).map(data ->
            arguments(data.toString(), data.charSequence, data.ignoreCase, data.thisStart,
                      data.substring, data.substringStart, data.length,
                      data.expectedResult, data.expectedThrowable)
        );
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("regionMatchesSource")
    @DisplayName("regionMatches should behave correctly for various inputs")
    void regionMatches(final String testName, final CharSequence cs, final boolean ignoreCase, final int thisStart,
                       final CharSequence substring, final int start, final int length,
                       final boolean expectedResult, final Class<? extends Throwable> expectedThrowable) {
        if (expectedThrowable != null) {
            assertThrows(expectedThrowable, () ->
                CharSequenceUtils.regionMatches(cs, ignoreCase, thisStart, substring, start, length));
        } else {
            final boolean actual = CharSequenceUtils.regionMatches(cs, ignoreCase, thisStart, substring, start, length);
            assertEquals(expectedResult, actual);
        }
    }
    // endregion
}