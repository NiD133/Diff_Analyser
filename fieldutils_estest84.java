package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeAdd correctly computes the sum of a positive integer and
     * Integer.MIN_VALUE without throwing an exception, as the result does not underflow.
     */
    @Test
    public void testSafeAddWithPositiveAndMinValue() {
        // Arrange
        int positiveValue = 14;
        int minValue = Integer.MIN_VALUE;

        // Act
        int result = FieldUtils.safeAdd(positiveValue, minValue);

        // Assert
        // The expected result is the standard Java addition, as this specific case does not underflow.
        assertEquals(minValue + positiveValue, result);
    }
}