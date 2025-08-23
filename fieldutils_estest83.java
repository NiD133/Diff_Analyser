package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    @Test
    public void safeAdd_int_throwsExceptionOnOverflow() {
        // Verifies that safeAdd(int, int) throws an ArithmeticException
        // when the addition results in an integer overflow.
        try {
            // Act: Attempt to add two integers that will cause an overflow.
            FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MAX_VALUE);
            fail("Expected an ArithmeticException to be thrown due to overflow");
        } catch (ArithmeticException e) {
            // Assert: Check that the correct exception was thrown with the expected message.
            assertEquals(
                "The calculation caused an overflow: 2147483647 + 2147483647",
                e.getMessage());
        }
    }
}