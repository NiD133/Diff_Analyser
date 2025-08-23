package org.joda.time.field;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for {@link FieldUtils}.
 */
public class FieldUtilsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that safeAdd(int, int) throws an ArithmeticException when the
     * addition results in an integer underflow.
     */
    @Test
    public void safeAdd_shouldThrowArithmeticException_onIntegerUnderflow() {
        // Arrange: Define two integers that will cause an underflow when added.
        // Integer.MIN_VALUE is -2,147,483,648. Adding any negative number will underflow.
        final int value1 = -1640;
        final int value2 = Integer.MIN_VALUE;

        // Assert: Configure the expected exception type and message.
        thrown.expect(ArithmeticException.class);
        thrown.expectMessage(is("The calculation caused an overflow: -1640 + -2147483648"));

        // Act: Call the method that is expected to throw the exception.
        FieldUtils.safeAdd(value1, value2);
    }
}