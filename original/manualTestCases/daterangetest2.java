package org.jfree.data.time;

import org.jfree.chart.TestUtils; // Utility class for serialization testing
import org.junit.jupiter.api.Test; // Annotation to mark a method as a test case
import java.util.Date; // Class representing a specific moment in time
import static org.junit.jupiter.api.Assertions.assertEquals; // Static import for the assertion method

public class DateRangeSerializationTest { // Renamed class for clarity and purpose

    /**
     * Tests the serialization and deserialization of the `DateRange` class.
     * This ensures that a `DateRange` object can be converted to a byte stream
     * and then reconstructed without losing its data (start and end dates).
     */
    @Test
    public void testDateRangeSerialization() { // Renamed test method for clarity

        // 1. Create a DateRange object with specific start and end dates.
        Date startDate = new Date(1000L); // Start date: 1000 milliseconds since the epoch
        Date endDate = new Date(2000L);   // End date:   2000 milliseconds since the epoch
        DateRange originalRange = new DateRange(startDate, endDate);

        // 2. Serialize the DateRange object to a byte stream and then deserialize it back into a new object.
        //    This is done using the TestUtils.serialised() method (presumably from jfreechart).
        DateRange deserializedRange = TestUtils.serialised(originalRange);

        // 3. Assert that the original DateRange object and the deserialized DateRange object are equal.
        //    This verifies that the serialization and deserialization process preserved the data correctly.
        assertEquals(originalRange, deserializedRange, "The deserialized DateRange should be equal to the original DateRange.");
    }
}