package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void getMinutes_returnsCorrectValue_forMinValueConstant() {
        // Arrange: Get the constant representing the minimum number of minutes.
        Minutes minValue = Minutes.MIN_VALUE;

        // Act: Call the getMinutes() method to retrieve the integer value.
        int actualMinutes = minValue.getMinutes();

        // Assert: The returned value should be equal to Integer.MIN_VALUE.
        assertEquals(Integer.MIN_VALUE, actualMinutes);
    }
}