package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the internal assembly of fields in GregorianChronology.
 *
 * Note: The class name is kept from the original for consistency,
 * but a more conventional name would be GregorianChronologyTest.
 */
public class GregorianChronology_ESTestTest2 {

    /**
     * Verifies that the internal assemble() method correctly populates the chronology's
     * date, time, and duration fields. This is a crucial part of the chronology's initialization.
     */
    @Test
    public void assemble_shouldPopulateChronologyFields() {
        // Arrange: Create a GregorianChronology instance and an empty Fields object.
        // The assemble() method is a protected part of the class's internal setup.
        GregorianChronology chronology = GregorianChronology.getInstance();
        AssembledChronology.Fields fields = new AssembledChronology.Fields();

        // Act: Call the method under test to populate the fields.
        chronology.assemble(fields);

        // Assert: Verify that a representative sample of date, time, and duration fields
        // have been initialized. This provides confidence that the chronology is
        // constructed correctly without making the test overly brittle.
        assertNotNull("Year field should be populated", fields.year);
        assertNotNull("Month of year field should be populated", fields.monthOfYear);
        assertNotNull("Day of month field should be populated", fields.dayOfMonth);
        assertNotNull("Hour of day field should be populated", fields.hourOfDay);
        assertNotNull("Second of minute field should be populated", fields.secondOfMinute);
        assertNotNull("Millis of second field should be populated", fields.millisOfSecond);

        assertNotNull("Years duration field should be populated", fields.years);
        assertNotNull("Days duration field should be populated", fields.days);
        assertNotNull("Millis duration field should be populated", fields.millis);
    }
}