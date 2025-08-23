package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeMultiply(long, int) throws an ArithmeticException when the
     * multiplication results in a value that overflows the long data type.
     * This is tested using the edge case of Long.MIN_VALUE * -1.
     */
    @Test
    public void safeMultiply_longMinValueByMinusOne_throwsArithmeticExceptionForOverflow() {
        // The multiplication of Long.MIN_VALUE by -1 is a classic overflow case,
        // as the result (2^63) is one greater than Long.MAX_VALUE (2^63 - 1).
        
        // Act & Assert
        ArithmeticException exception = assertThrows(
            ArithmeticException.class,
            () -> FieldUtils.safeMultiply(Long.MIN_VALUE, -1)
        );

        // Verify the exception message is informative
        assertEquals(
            "Multiplication overflows a long: -9223372036854775808 * -1",
            exception.getMessage()
        );
    }
}