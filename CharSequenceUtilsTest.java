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
     * Abstract class to define a test run.
     */
    private abstract static class RunTest {

        abstract boolean invoke();

        void run(final TestData data, final String id) {
            if (data.expectedException != null) {
                assertThrows(data.expectedException, this::invoke, id + " Expected " + data.expectedException);
            } else {
                final boolean result = invoke();
                assertEquals(data.expectedResult, result, id + " Failed test " + data);
            }
        }
    }

    /**
     * Data class to hold test parameters and expected results.
     */
    static class TestData {
        final String source;
        final boolean ignoreCase;
        final int sourceOffset;
        final String target;
        final int targetOffset;
        final int length;
        final boolean expectedResult;
        final Class<? extends Throwable> expectedException;

        TestData(final String source, final boolean ignoreCase, final int sourceOffset, final String target, final int targetOffset, final int length,
                 final boolean expectedResult) {
            this.source = source;
            this.ignoreCase = ignoreCase;
            this.sourceOffset = sourceOffset;
            this.target = target;
            this.targetOffset = targetOffset;
            this.length = length;
            this.expectedResult = expectedResult;
            this.expectedException = null;
        }

        TestData(final String source, final boolean ignoreCase, final int sourceOffset, final String target, final int targetOffset, final int length,
                 final Class<? extends Throwable> expectedException) {
            this.source = source;
            this.ignoreCase = ignoreCase;
            this.sourceOffset = sourceOffset;
            this.target = target;
            this.targetOffset = targetOffset;
            this.length = length;
            this.expectedResult = false;
            this.expectedException = expectedException;
        }

        @Override
        public String toString() {
            return String.format("%s[%d] %s %s[%d] %d => %s",
                    source, sourceOffset,
                    ignoreCase ? "case-insensitive" : "case-sensitive",
                    target, targetOffset,
                    length,
                    expectedException != null ? expectedException : expectedResult);
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
            new TestData("Abcd", false, 1, "abcD", 1, 2, true),
    };

    static Stream<Arguments> provideCharSequencesForLastIndex() {
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

    @Test
    void testConstructor() {
        assertNotNull(new CharSequenceUtils());
        final Constructor<?>[] constructors = CharSequenceUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertTrue(Modifier.isPublic(constructors[0].getModifiers()));
        assertTrue(Modifier.isPublic(CharSequenceUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(CharSequenceUtils.class.getModifiers()));
    }

    @ParameterizedTest
    @MethodSource("provideCharSequencesForLastIndex")
    void testLastIndexOfWithDifferentCharSequences(final CharSequence cs, final CharSequence search, final int start, final int expected) {
        assertEquals(expected, CharSequenceUtils.lastIndexOf(cs, search, start));
    }

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

    private void testNewLastIndexOfSingle(final CharSequence a, final CharSequence b) {
        final int maxLength = Math.max(a.length(), b.length());
        for (int i = -maxLength - 10; i <= maxLength + 10; i++) {
            testNewLastIndexOfSingle(a, b, i);
        }
        testNewLastIndexOfSingle(a, b, Integer.MIN_VALUE);
        testNewLastIndexOfSingle(a, b, Integer.MAX_VALUE);
    }

    private void testNewLastIndexOfSingle(final CharSequence a, final CharSequence b, final int start) {
        testNewLastIndexOfSinglePair(a, b, start);
        testNewLastIndexOfSinglePair(b, a, start);
    }

    private void testNewLastIndexOfSinglePair(final CharSequence a, final CharSequence b, final int start) {
        assertEquals(a.toString().lastIndexOf(b.toString(), start),
                CharSequenceUtils.lastIndexOf(new WrapperString(a.toString()), new WrapperString(b.toString()), start),
                "testNewLastIndexOf fails! original : " + a + " seg : " + b + " start : " + start);
    }

    @Test
    void testRegionMatches() {
        for (final TestData data : TEST_DATA) {
            new RunTest() {
                @Override
                boolean invoke() {
                    return data.source.regionMatches(data.ignoreCase, data.sourceOffset, data.target, data.targetOffset, data.length);
                }
            }.run(data, "String");
            new RunTest() {
                @Override
                boolean invoke() {
                    return CharSequenceUtils.regionMatches(data.source, data.ignoreCase, data.sourceOffset, data.target, data.targetOffset, data.length);
                }
            }.run(data, "CSString");
            new RunTest() {
                @Override
                boolean invoke() {
                    return CharSequenceUtils.regionMatches(new StringBuilder(data.source), data.ignoreCase, data.sourceOffset, data.target, data.targetOffset, data.length);
                }
            }.run(data, "CSNonString");
        }
    }

    @Test
    void testSubSequence() {
        assertNull(CharSequenceUtils.subSequence(null, -1));
        assertNull(CharSequenceUtils.subSequence(null, 0));
        assertNull(CharSequenceUtils.subSequence(null, 1));

        assertEquals(StringUtils.EMPTY, CharSequenceUtils.subSequence(StringUtils.EMPTY, 0));
        assertEquals("012", CharSequenceUtils.subSequence("012", 0));
        assertEquals("12", CharSequenceUtils.subSequence("012", 1));
        assertEquals("2", CharSequenceUtils.subSequence("012", 2));
        assertEquals(StringUtils.EMPTY, CharSequenceUtils.subSequence("012", 3));
    }

    @Test
    void testSubSequenceNegativeStart() {
        assertIndexOutOfBoundsException(() -> CharSequenceUtils.subSequence(StringUtils.EMPTY, -1));
    }

    @Test
    void testSubSequenceTooLong() {
        assertIndexOutOfBoundsException(() -> CharSequenceUtils.subSequence(StringUtils.EMPTY, 1));
    }

    @Test
    void testToCharArray() {
        final StringBuilder builder = new StringBuilder("abcdefg");
        final char[] expected = builder.toString().toCharArray();
        assertArrayEquals(expected, CharSequenceUtils.toCharArray(builder));
        assertArrayEquals(expected, CharSequenceUtils.toCharArray(builder.toString()));
        assertArrayEquals(ArrayUtils.EMPTY_CHAR_ARRAY, CharSequenceUtils.toCharArray(null));
    }
}