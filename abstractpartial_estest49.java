package org.joda.time.base;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/*
 * Note: While this test file is named for AbstractPartial, this specific test case
 * validates the behavior of a concrete subclass, LocalDate.
 */
public class AbstractPartial_ESTestTest49 extends AbstractPartial_ESTest_scaffolding {

    /**
     * Tests that converting a LocalDate to a java.util.Date results in an object
     * representing midnight on that date in the system's default time zone.
     */
    @Test
    public void toDate_fromLocalDate_representsMidnightInDefaultTimeZone() {
        // Arrange
        // A LocalDate represents a date without a time or time zone.
        LocalDate epochLocalDate = new LocalDate(1970, 1, 1);

        // The toDate() method should convert this to an instant representing
        // midnight on that date in the default system time zone.
        // We create an expected Date object that represents this instant for a robust comparison.
        DateTime expectedInstant = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeZone.getDefault());
        Date expectedDate = expectedInstant.toDate();

        // Act
        // Convert the LocalDate to a java.util.Date, which is the action under test.
        Date actualDate = epochLocalDate.toDate();

        // Assert
        // Verify that the actual Date object matches the expected one.
        // This correctly compares the underlying millisecond-from-epoch values.
        assertEquals(expectedDate, actualDate);
    }
}