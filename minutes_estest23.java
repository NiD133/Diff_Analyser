package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class contains tests for the {@link Minutes} class.
 * The original test class name and inheritance are kept to maintain context from the test suite.
 */
public class Minutes_ESTestTest23 extends Minutes_ESTest_scaffolding {

    /**
     * Tests that subtracting a zero-value Minutes object from another
     * does not change its value.
     */
    @Test
    public void minus_whenSubtractingZero_shouldReturnOriginalValue() {
        // Arrange
        // The original test used Minutes.ONE, which is a clear starting point.
        final Minutes oneMinute = Minutes.ONE;

        // Act
        // The core operation is subtracting zero. The original test performed this
        // in a confusing way (oneMinute.ONE.minus(Minutes.minutes(0))).
        // This is simplified to a direct and clear subtraction using the ZERO constant.
        final Minutes result = oneMinute.minus(Minutes.ZERO);

        // Assert
        // The result of "1 minute - 0 minutes" should still be 1 minute.
        // Asserting object equality is more expressive than just checking the integer value.
        assertEquals(oneMinute, result);
    }
}