package org.joda.time.base;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Test suite for the AbstractPartial class, focusing on the toDateTime(ReadableInstant) method.
 */
public class AbstractPartialTest {

    /**
     * Verifies that toDateTime(ReadableInstant) returns a new DateTime instance,
     * even when the partial's fields perfectly match the base instant's fields.
     */
    @Test
    public void toDateTimeWithBaseInstant_shouldReturnNewInstance() {
        // Arrange
        // A LocalDate representing the epoch date in UTC.
        LocalDate partialDate = new LocalDate(0L, DateTimeZone.UTC); // 1970-01-01

        // A DateTime representing the epoch instant in UTC, to be used as a base.
        // The date component of this instant is identical to the partialDate.
        DateTime baseInstant = new DateTime(0L, DateTimeZone.UTC); // 1970-01-01T00:00:00.000Z

        // Act
        // Combine the partial date with the base instant.
        DateTime resultDateTime = partialDate.toDateTime(baseInstant);

        // Assert
        // 1. The resulting DateTime should be equal in value to the base instant.
        assertEquals("The resulting DateTime should have the same value as the base instant",
                baseInstant, resultDateTime);

        // 2. Crucially, the method must return a new instance, not the base instance itself.
        assertNotSame("The toDateTime method should always return a new instance",
                baseInstant, resultDateTime);
    }
}