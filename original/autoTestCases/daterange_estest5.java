package org.example;

import org.junit.jupiter.api.Test; // Use JUnit 5 annotations
import static org.junit.jupiter.api.Assertions.*; // Use JUnit 5 assertions
import org.jfree.data.Range;

import java.util.Date;

public class DateRangeTest { // Renamed class for clarity and context

    @Test
    void testExpandToIncludeWithInvalidDateRange() {
        // Arrange: Create an instance of DateRange, which likely represents an empty or invalid date range.
        DateRange dateRange = new DateRange();

        // Act: Attempt to expand the DateRange to include the numerical value 1.203...  This is likely what causes the problem.
        // The Range.expandToInclude method is probably designed for numerical ranges, not ranges involving date objects directly.
        // The DateRange object is implicitly cast to a double which is what is used in the expandToInclude function, but its initial state is incorrect.

        // Assert:  Verify that an IllegalArgumentException is thrown.
        // The assertion checks the expected behavior when attempting to expand a potentially invalid DateRange with a number.
        // DateRange, in this test, is a custom class not supplied in the question, so we're inferring its behavior based on the error.
        assertThrows(IllegalArgumentException.class, () -> {
            Range range = Range.expandToInclude(dateRange, 1.2030679447063568);
            range.toString(); // Trigger the exception within the lambda for accurate error reporting.

        }, "Expanding the date range with an arbitrary number should throw an IllegalArgumentException.");
        // Added a helpful message to the assertion, making the intent clearer.

    }
}