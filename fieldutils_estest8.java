package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeToInt correctly converts a long value that is exactly
     * at the upper boundary of the integer range (Integer.MAX_VALUE).
     */
    @Test
    public void safeToInt_whenLongIsIntMaxValue_returnsIntMaxValue() {
        // Arrange: Define the input value at the upper boundary of the int range.
        // Using the constant Integer.MAX_VALUE makes the test's intent self-documenting.
        long longAtIntMaxValue = Integer.MAX_VALUE;

        // Act: Call the method under test.
        int result = FieldUtils.safeToInt(longAtIntMaxValue);

        // Assert: Verify that the conversion is correct and no exception is thrown.
        assertEquals(Integer.MAX_VALUE, result);
    }
}