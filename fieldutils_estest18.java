package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    @Test
    public void safeNegate_shouldReturnPositiveValue_whenInputIsNegative() {
        // Arrange: Define the input and the expected outcome.
        final int negativeValue = -70;
        final int expectedResult = 70;

        // Act: Call the method under test.
        final int actualResult = FieldUtils.safeNegate(negativeValue);

        // Assert: Verify that the actual result matches the expected result.
        assertEquals(expectedResult, actualResult);
    }
}