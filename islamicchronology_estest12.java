package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on verifying the internal assembly logic of the IslamicChronology.
 */
public class IslamicChronology_ESTestTest12 {

    /**
     * Tests that the assemble() method correctly populates the chronology's fields.
     *
     * The assemble() method is responsible for setting up all the DateTimeField instances
     * (like year, month, day, etc.) that define the chronology. This test verifies
     * that all essential fields are non-null after assemble() has been called, ensuring
     * the chronology is correctly initialized.
     */
    @Test
    public void assemble_shouldPopulateAllChronologyFields() {
        // Arrange: Create an instance of the chronology and a Fields object to be populated.
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        AssembledChronology.Fields fields = new AssembledChronology.Fields();

        // Act: Call the method under test to populate the fields.
        islamicChronology.assemble(fields);

        // Assert: Verify that the essential date and time fields have been initialized.
        assertNotNull("Era field should be populated", fields.era);
        assertNotNull("Year field should be populated", fields.year);
        assertNotNull("Month of year field should be populated", fields.monthOfYear);
        assertNotNull("Day of month field should be populated", fields.dayOfMonth);
        assertNotNull("Day of year field should be populated", fields.dayOfYear);
        assertNotNull("Day of week field should be populated", fields.dayOfWeek);
        assertNotNull("Weekyear field should be populated", fields.weekyear);
        assertNotNull("Week of weekyear field should be populated", fields.weekOfWeekyear);
        assertNotNull("Hour of day field should be populated", fields.hourOfDay);
        assertNotNull("Minute of hour field should be populated", fields.minuteOfHour);
        assertNotNull("Second of minute field should be populated", fields.secondOfMinute);
        assertNotNull("Millis of second field should be populated", fields.millisOfSecond);
        assertNotNull("Millis of day field should be populated", fields.millisOfDay);
    }
}