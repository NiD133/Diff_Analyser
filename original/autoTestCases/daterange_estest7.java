package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DateRangeTest {

    @Test(expected = NullPointerException.class)
    public void testDateRangeCreationWithNullDatesThrowsException() {
        // Arrange: Both start and end dates are null.
        Date startDate = null;
        Date endDate = null;

        // Act: Attempt to create a DateRange object with null dates.
        // Expect: A NullPointerException to be thrown because the DateRange constructor does not handle null dates.
        new DateRange(startDate, endDate);
    }
}