package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIndexOutOfBoundsException;
import static org.junit.jupiter.api.Assertions.*;
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
 * Unit tests for CharSequenceUtils.
 */
class CharSequenceUtilsTest extends AbstractLangTest {

    /**
     * Abstract class to define a test run with a specific invocation.
     */
    private abstract static class RunTest {

        /**
         * Method to be implemented by subclasses to invoke the test logic.
         * 
         * @return boolean result of the test invocation.
         */
        abstract boolean invoke();

        /**
         * Executes the test with the provided data and identifier.
         * 
         * @param data TestData object containing test parameters.
         * @param id   Identifier for the test case.
         */
        void run(final TestData data, final String id) {
            if (data.throwable != null) {
                assertThrows(data.throwable, this::invoke, id + " Expected " + data.throwable);
            } else {
                final boolean result = invoke();
                assertEquals(data.expected, result, id + " Failed test " + data);
            }
        }
    }

    /**
     * Class to hold test data parameters.
     */
    static class TestData {
        final String source;
        final boolean ignoreCase;
        final int toffset;
        final String other;
        final int ooffset;
        final int len;
        final boolean expected;
        final Class<? extends Throwable> throwable;

        TestData(final String source, final boolean ignoreCase, final int toffset, final String other, final int ooffset, final int len,
                final boolean expected) {
            this.source = source;
            this.ignoreCase = ignoreCase;
            this.toffset = toffset;
            this.other = other;
            this.ooffset = ooffset;
            this.len = len;
            this.expected = expected;
            this.throwable = null;
        }

        TestData(final String source, final boolean ignoreCase, final int toffset, final String other, final int ooffset, final int len,
                final Class<? extends Throwable> throwable) {
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
            return String.format("%s[%d] %s %s[%d] %d => %s", 
                source, toffset, ignoreCase ? "caseblind" : "samecase", other, ooffset, len, 
                throwable != null ? throwable : expected);
        }
    }

    /**
     * Wrapper class for CharSequence to test custom implementations.
     */
    static class WrapperString implements CharSequence {
        private final CharSequence inner;

        WrapperString(final CharSequence inner) {
            this.inner = inner;
        }

        @Override
        public char charAt(final int index) {
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
        public CharSequence subSequence(final int start, final int end) {
            return inner.subSequence(start, end);
        }

        @Override
        public String toString() {
            return inner.toString();
        }
    }

    // Test data for regionMatches tests
    private static final TestData[] TEST_DATA = {
            new TestData("", true, -1, "", -1, -1, false),
            new TestData("", true, 0, "", 0, 1, false),
            new TestData("a", true, 0, "abc", 0, 0, true),
            new TestData("a", true, 0, "abc", 0, 1, true),
            new TestData("a", true, 0, null, 0, 0, NullPointerException.class),
            new TestData(null, true, 0, null, 0, 0, NullPointerException.class),
            new TestData(null, true, 0, "", 0, 0, NullPointerException.class),
            new TestData("Abc", true, 0, "abc", 0, 3, true),
            new TestData("Abc", false, 0, "abc", 0, 3, false),
            new TestData("Abc", true, 1, "abc", 1, 2, true),
            new TestData("Abc", false, 1, "abc", 1, 2, true),
            new TestData("Abcd", true, 1, "abcD", 1, 2, true),
            new TestData("Abcd", false, 1, "abcD", 1, 2, true)
    };

    /**
     * Provides test arguments for lastIndexOf tests with standard CharSequence.
     * 
     * @return Stream of arguments for parameterized tests.
     */
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

    /**
     * Tests the constructor of CharSequenceUtils.
     */
    @Test
    void testConstructor() {
        assertNotNull(new CharSequenceUtils());
        final Constructor<?>[] constructors = CharSequenceUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertTrue(Modifier.isPublic(constructors[0].getModifiers()));
        assertTrue(Modifier.isPublic(CharSequenceUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(CharSequenceUtils.class.getModifiers()));
    }

    /**
     * Tests lastIndexOf with different CharSequence implementations.
     */
    @ParameterizedTest
    @MethodSource("lastIndexWithStandardCharSequence")
    void testLastIndexOfWithDifferentCharSequences(final CharSequence cs, final CharSequence search, final int start, final int expected) {
        assertEquals(expected, CharSequenceUtils.lastIndexOf(cs, search, start));
    }

    /**
     * Tests new lastIndexOf implementation with various inputs.
     */
    @Test
    void testNewLastIndexOf() {
        testNewLastIndexOfSingle("808087847-1321060740-635567660180086727-925755305", "-1321060740-635567660", 21);
        testNewLastIndexOfSingle("", "");
        testNewLastIndexOfSingle("1", "");
        testNewLastIndexOfSingle("", "1");
        testNewLastIndexOfSingle("1", "1");
        testNewLastIndexOfSingle("11", "1");
        testNewLastIndexOfSingle("1", "11");

        testNewLastIndexOfSingle("apache", "a");
        testNewLastIndexOfSingle("apache", "p");
        testNewLastIndexOfSingle("apache", "e");
        testNewLastIndexOfSingle("apache", "x");
        testNewLastIndexOfSingle("oraoraoraora", "r");
        testNewLastIndexOfSingle("mudamudamudamuda", "d");
        testNewLastIndexOfSingle("junk-ststarting", "starting");

        final Random random = new Random();
        final StringBuilder segment = new StringBuilder();
        while (segment.length() <= CharSequenceUtils.TO_STRING_LIMIT) {
            segment.append(random.nextInt());
        }
        StringBuilder original = new StringBuilder(segment);
        testNewLastIndexOfSingle(original, segment);
        for (int i = 0; i < 100; i++) {
            if (random.nextDouble() < 0.5) {
                original.append(random.nextInt() % 10);
            } else {
                original = new StringBuilder().append(random.nextInt() % 100).append(original);
            }
            testNewLastIndexOfSingle(original, segment);
        }
    }

    /**
     * Helper method to test new lastIndexOf implementation with single inputs.
     */
    private void testNewLastIndexOfSingle(final CharSequence a, final CharSequence b) {
        final int maxLength = Math.max(a.length(), b.length());
        for (int i = -maxLength - 10; i <= maxLength + 10; i++) {
            testNewLastIndexOfSingle(a, b, i);
        }
        testNewLastIndexOfSingle(a, b, Integer.MIN_VALUE);
        testNewLastIndexOfSingle(a, b, Integer.MAX_VALUE);
    }

    /**
     * Helper method to test new lastIndexOf implementation with single inputs and start index.
     */
    private void testNewLastIndexOfSingle(final CharSequence a, final CharSequence b, final int start) {
        testNewLastIndexOfSingleSingle(a, b, start);
        testNewLastIndexOfSingleSingle(b, a, start);
    }

    /**
     * Helper method to test new lastIndexOf implementation with single inputs and start index.
     */
    private void testNewLastIndexOfSingleSingle(final CharSequence a, final CharSequence b, final int start) {
        assertEquals(a.toString().lastIndexOf(b.toString(), start),
                CharSequenceUtils.lastIndexOf(new WrapperString(a.toString()), new WrapperString(b.toString()), start),
                "testNewLastIndexOf fails! original : " + a + " seg : " + b + " start : " + start);
    }

    /**
     * Tests regionMatches method with various inputs.
     */
    @Test
    void testRegionMatches() {
        for (final TestData data : TEST_DATA) {
            new RunTest() {
                @Override
                boolean invoke() {
                    return data.source.regionMatches(data.ignoreCase, data.toffset, data.other, data.ooffset, data.len);
                }
            }.run(data, "String");
            new RunTest() {
                @Override
                boolean invoke() {
                    return CharSequenceUtils.regionMatches(data.source, data.ignoreCase, data.toffset, data.other, data.ooffset, data.len);
                }
            }.run(data, "CSString");
            new RunTest() {
                @Override
                boolean invoke() {
                    return CharSequenceUtils.regionMatches(new StringBuilder(data.source), data.ignoreCase, data.toffset, data.other, data.ooffset, data.len);
                }
            }.run(data, "CSNonString");
        }
    }

    /**
     * Tests subSequence method with various inputs.
     */
    @Test
    void testSubSequence() {
        // Test with null input
        assertNull(CharSequenceUtils.subSequence(null, -1));
        assertNull(CharSequenceUtils.subSequence(null, 0));
        assertNull(CharSequenceUtils.subSequence(null, 1));

        // Test with non-null input
        assertEquals(StringUtils.EMPTY, CharSequenceUtils.subSequence(StringUtils.EMPTY, 0));
        assertEquals("012", CharSequenceUtils.subSequence("012", 0));
        assertEquals("12", CharSequenceUtils.subSequence("012", 1));
        assertEquals("2", CharSequenceUtils.subSequence("012", 2));
        assertEquals(StringUtils.EMPTY, CharSequenceUtils.subSequence("012", 3));
    }

    /**
     * Tests subSequence method with negative start index.
     */
    @Test
    void testSubSequenceNegativeStart() {
        assertIndexOutOfBoundsException(() -> CharSequenceUtils.subSequence(StringUtils.EMPTY, -1));
    }

    /**
     * Tests subSequence method with start index greater than length.
     */
    @Test
    void testSubSequenceTooLong() {
        assertIndexOutOfBoundsException(() -> CharSequenceUtils.subSequence(StringUtils.EMPTY, 1));
    }

    /**
     * Tests toCharArray method with various inputs.
     */
    @Test
    void testToCharArray() {
        final StringBuilder builder = new StringBuilder("abcdefg");
        final char[] expected = builder.toString().toCharArray();
        assertArrayEquals(expected, CharSequenceUtils.toCharArray(builder));
        assertArrayEquals(expected, CharSequenceUtils.toCharArray(builder.toString()));
        assertArrayEquals(ArrayUtils.EMPTY_CHAR_ARRAY, CharSequenceUtils.toCharArray(null));
    }
}