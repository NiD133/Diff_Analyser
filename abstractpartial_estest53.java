package org.joda.time.base;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Test suite for the AbstractPartial class.
 *
 * <p>This test focuses on the getValues() method, using LocalDateTime as a concrete
 * implementation of AbstractPartial to verify its behavior.
 */
public class AbstractPartialTest {

    /**
     * Verifies that getValues() on a LocalDateTime instance returns an integer array
     * containing the correct values for each of its date and time fields.
     */
    @Test
    public void getValues_onLocalDateTime_shouldReturnArrayOfFieldValues() {
        // Arrange: Create a specific LocalDateTime to make the test deterministic and explicit.
        // The original test relied on a mocked `LocalDateTime.now()`.
        LocalDateTime dateTime = new LocalDateTime(2014, 2, 14, 20, 21, 21, 320);

        // For LocalDateTime, the values are [year, monthOfYear, dayOfMonth, millisOfDay].
        // We define the expected values with clear variable names to document the array structure.
        int expectedYear = 2014;
        int expectedMonth = 2;
        int expectedDay = 14;
        // Calculate millisOfDay explicitly to show how the value is derived, avoiding a "magic number".
        int expectedMillisOfDay = new LocalTime(20, 21, 21, 320).getMillisOfDay(); // 73281320

        int[] expectedValues = new int[]{expectedYear, expectedMonth, expectedDay, expectedMillisOfDay};

        // Act: Call the method under test.
        int[] actualValues = dateTime.getValues();

        // Assert: Verify that the actual values match the expected values.
        assertArrayEquals(expectedValues, actualValues);
    }
}