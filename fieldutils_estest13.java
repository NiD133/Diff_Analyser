package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    @Test
    public void safeToInt_shouldReturnSameValue_whenLongIsWithinIntRange() {
        // Arrange: A long value that can be safely cast to an int.
        long valueToConvert = 0L;
        int expectedValue = 0;

        // Act: Convert the long to an int using the method under test.
        int actualValue = FieldUtils.safeToInt(valueToConvert);

        // Assert: The converted value should be equal to the original.
        assertEquals(expectedValue, actualValue);
    }
}