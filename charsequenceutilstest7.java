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
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class CharSequenceUtilsTestTest7 extends AbstractLangTest {

    private static final TestData[] TEST_DATA = { // @formatter:off
    //           Source  IgnoreCase Offset Other  Offset Length Result
    new TestData("", true, -1, "", -1, -1, false), new TestData("", true, 0, "", 0, 1, false), new TestData("a", true, 0, "abc", 0, 0, true), new TestData("a", true, 0, "abc", 0, 1, true), new TestData("a", true, 0, null, 0, 0, NullPointerException.class), new TestData(null, true, 0, null, 0, 0, NullPointerException.class), new TestData(null, true, 0, "", 0, 0, NullPointerException.class), new TestData("Abc", true, 0, "abc", 0, 3, true), new TestData("Abc", false, 0, "abc", 0, 3, false), new TestData("Abc", true, 1, "abc", 1, 2, true), new TestData("Abc", false, 1, "abc", 1, 2, true), new TestData("Abcd", true, 1, "abcD", 1, 2, true), new TestData("Abcd", false, 1, "abcD", 1, 2, true) // @formatter:on
    };

    static Stream<Arguments> lastIndexWithStandardCharSequence() {
        // @formatter:off
        return Stream.of(arguments("abc", "b", 2, 1), arguments(new StringBuilder("abc"), "b", 2, 1), arguments(new StringBuffer("abc"), "b", 2, 1), arguments("abc", new StringBuilder("b"), 2, 1), arguments(new StringBuilder("abc"), new StringBuilder("b"), 2, 1), arguments(new StringBuffer("abc"), new StringBuffer("b"), 2, 1), arguments(new StringBuilder("abc"), new StringBuffer("b"), 2, 1));
        // @formatter:on
    }

    @ParameterizedTest
    @MethodSource("lastIndexWithStandardCharSequence")
    void testLastIndexOfWithDifferentCharSequences(final CharSequence cs, final CharSequence search, final int start, final int expected) {
        assertEquals(expected, CharSequenceUtils.lastIndexOf(cs, search, start));
    }

    private void testNewLastIndexOfSingle(final CharSequence a, final CharSequence b) {
        final int maxa = Math.max(a.length(), b.length());
        for (int i = -maxa - 10; i <= maxa + 10; i++) {
            testNewLastIndexOfSingle(a, b, i);
        }
        testNewLastIndexOfSingle(a, b, Integer.MIN_VALUE);
        testNewLastIndexOfSingle(a, b, Integer.MAX_VALUE);
    }

    private void testNewLastIndexOfSingle(final CharSequence a, final CharSequence b, final int start) {
        testNewLastIndexOfSingleSingle(a, b, start);
        testNewLastIndexOfSingleSingle(b, a, start);
    }

    private void testNewLastIndexOfSingleSingle(final CharSequence a, final CharSequence b, final int start) {
        assertEquals(a.toString().lastIndexOf(b.toString(), start), CharSequenceUtils.lastIndexOf(new WrapperString(a.toString()), new WrapperString(b.toString()), start), "testNewLastIndexOf fails! original : " + a + " seg : " + b + " start : " + start);
    }

    private abstract static class RunTest {

        abstract boolean invoke();

        void run(final TestData data, final String id) {
            if (data.throwable != null) {
                assertThrows(data.throwable, this::invoke, id + " Expected " + data.throwable);
            } else {
                final boolean stringCheck = invoke();
                assertEquals(data.expected, stringCheck, id + " Failed test " + data);
            }
        }
    }

    static class TestData {

        final String source;

        final boolean ignoreCase;

        final int toffset;

        final String other;

        final int ooffset;

        final int len;

        final boolean expected;

        final Class<? extends Throwable> throwable;

        TestData(final String source, final boolean ignoreCase, final int toffset, final String other, final int ooffset, final int len, final boolean expected) {
            this.source = source;
            this.ignoreCase = ignoreCase;
            this.toffset = toffset;
            this.other = other;
            this.ooffset = ooffset;
            this.len = len;
            this.expected = expected;
            this.throwable = null;
        }

        TestData(final String source, final boolean ignoreCase, final int toffset, final String other, final int ooffset, final int len, final Class<? extends Throwable> throwable) {
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
            final StringBuilder sb = new StringBuilder();
            sb.append(source).append("[").append(toffset).append("]");
            sb.append(ignoreCase ? " caseblind " : " samecase ");
            sb.append(other).append("[").append(ooffset).append("]");
            sb.append(" ").append(len).append(" => ");
            if (throwable != null) {
                sb.append(throwable);
            } else {
                sb.append(expected);
            }
            return sb.toString();
        }
    }

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

    @Test
    void testToCharArray() {
        final StringBuilder builder = new StringBuilder("abcdefg");
        final char[] expected = builder.toString().toCharArray();
        assertArrayEquals(expected, CharSequenceUtils.toCharArray(builder));
        assertArrayEquals(expected, CharSequenceUtils.toCharArray(builder.toString()));
        assertArrayEquals(ArrayUtils.EMPTY_CHAR_ARRAY, CharSequenceUtils.toCharArray(null));
    }
}
