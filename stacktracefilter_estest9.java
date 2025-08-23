package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link StackTraceFilter}.
 */
public class StackTraceFilterTest {

    /**
     * Verifies that the filter method returns a new, empty array instance
     * when the input is an empty array, rather than returning the original instance.
     * This ensures the method has no unexpected side effects on the input array.
     */
    @Test
    public void filter_givenEmptyStackTrace_returnsNewEmptyArray() {
        // Arrange
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        StackTraceElement[] emptyStackTrace = new StackTraceElement[0];

        // Act
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(emptyStackTrace, false);

        // Assert
        assertNotNull("The filtered array should not be null", filteredStackTrace);
        assertEquals("The filtered array should be empty", 0, filteredStackTrace.length);
        assertNotSame("The filtered array should be a new instance", emptyStackTrace, filteredStackTrace);
    }
}