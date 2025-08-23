package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void dividedBy_byZero_throwsArithmeticException() {
        // Arrange
        Years oneYear = Years.ONE;

        // Act & Assert
        try {
            oneYear.dividedBy(0);
            fail("Expected an ArithmeticException to be thrown for division by zero.");
        } catch (ArithmeticException e) {
            // Verify that the exception message is correct, which is standard for integer division by zero.
            assertEquals("/ by zero", e.getMessage());
        }
    }
}