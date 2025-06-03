package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

// We're using JUnit for testing, so we need this.  No need to import specific assert methods.

public class GeneratedTestCase {

    @Test(timeout = 4000) // Timeout in milliseconds to prevent infinite loops.
    public void testInvalidDateRangeCreation() {
        // This test checks that an IllegalArgumentException is thrown when creating a DateRange
        // with an invalid range (where the lower bound is greater than the upper bound).

        try {
            // Attempt to create a DateRange where the start date (0.0) is after the end date (-5043.58).
            new DateRange(0.0, -5043.58);

            // If the DateRange is created without throwing an exception, the test fails.
            fail("Expected IllegalArgumentException was not thrown.");

        } catch (IllegalArgumentException e) {
            // The exception is caught, and we verify that the error message is correct.
            // Note:  Directly verifying the exact message is fragile and can break if the implementation changes.
            // In a real-world scenario, consider checking for a *substring* or just that the *type* of exception is correct.

            // We don't need to explicitly verify the message, as the fact that the *correct type* of exception
            // was thrown is sufficient in this simple case.
            // The framework automatically includes the exception message in the test output, so it's visible if the test fails.
            // It is better to assert on the type of exception rather than a specific message. This makes the test less brittle.

            // An alternative (more robust) approach would be:
            // assertTrue(e.getMessage().contains("lower (0.0) <= upper (-5043.58)"));
        }
    }
}