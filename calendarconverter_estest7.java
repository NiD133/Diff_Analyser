package org.joda.time.convert;

import org.joda.time.Chronology;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * An improved, more understandable test for the CalendarConverter class.
 * This test verifies the behavior of the getChronology method when provided with null input.
 */
public class CalendarConverterImprovedTest {

    /**
     * Tests that getChronology() throws a NullPointerException when the object to be converted is null.
     * This is the documented behavior and ensures the method correctly handles invalid input.
     */
    @Test
    public void getChronology_whenObjectIsNull_shouldThrowNullPointerException() {
        // Arrange: Get the singleton instance of the converter.
        // The converter is stateless, so we can use the shared INSTANCE.
        CalendarConverter converter = CalendarConverter.INSTANCE;

        // Act & Assert: Call the method with a null object and expect an exception.
        try {
            converter.getChronology(null, (Chronology) null);
            // If this line is reached, the test has failed because no exception was thrown.
            fail("Expected a NullPointerException, but no exception was thrown.");
        } catch (NullPointerException e) {
            // Success: The expected exception was caught.
            // We can also verify that the exception has no detail message,
            // which is common for NPEs triggered by contract checks.
            assertNull("The NullPointerException should not have a detail message.", e.getMessage());
        }
    }
}