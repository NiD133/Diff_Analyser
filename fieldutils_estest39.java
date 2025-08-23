package org.joda.time.field;

import org.junit.Test;

/**
 * Unit tests for {@link FieldUtils}.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeMultiply(long, long) throws an ArithmeticException when the
     * multiplication result would overflow the range of a long.
     *
     * This test case uses Long.MIN_VALUE * Long.MIN_VALUE, which is a classic
     * overflow scenario.
     */
    @Test(expected = ArithmeticException.class)
    public void safeMultiply_shouldThrowExceptionOnLongOverflow() {
        // The safeMultiply method is expected to throw an ArithmeticException
        // because the result of multiplying Long.MIN_VALUE by itself is too large
        // to be stored in a long.
        FieldUtils.safeMultiply(Long.MIN_VALUE, Long.MIN_VALUE);
    }
}