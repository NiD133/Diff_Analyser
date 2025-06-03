package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date;
import org.jfree.data.Range;
import org.jfree.data.time.DateRange; // Corrected import
import java.util.Calendar;

public class UnderstandableDateRangeTest {

    @Test
    public void testInvalidDateRangeConstruction() {
        // Arrange: Create two dates where startDate is after endDate.
        // Using Calendar for more readable date creation.
        Calendar cal = Calendar.getInstance();
        cal.set(1901, Calendar.AUGUST, 1); // August 1, 1901
        Date startDate = cal.getTime();

        cal.set(0, Calendar.JANUARY, 1); // January 1, 1900
        Date endDate = cal.getTime();


        // Act & Assert:  Attempting to create a DateRange with startDate > endDate
        // should throw an IllegalArgumentException.
        try {
            new DateRange(startDate, endDate);
            fail("Expected IllegalArgumentException due to invalid date range.");
        } catch (IllegalArgumentException e) {
            // Assert that the exception message is relevant to the problem.
            assertTrue(e.getMessage().contains("Range(double, double): require lower"));
        }
    }
}