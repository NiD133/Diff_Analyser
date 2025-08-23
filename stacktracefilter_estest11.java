package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link StackTraceFilter} class.
 */
public class StackTraceFilterTest {

    /**
     * Verifies that filterFirst() returns null when the throwable's stack trace is empty,
     * as there is no "first" element to filter.
     */
    @Test
    public void filterFirst_shouldReturnNull_whenStackTraceIsEmpty() {
        // Arrange: Create a StackTraceFilter and a throwable with an empty stack trace.
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        Throwable throwableWithEmptyStackTrace = new Throwable();
        throwableWithEmptyStackTrace.setStackTrace(new StackTraceElement[0]);
        boolean isInline = false;

        // Act: Call the method under test.
        StackTraceElement firstFilteredElement = stackTraceFilter.filterFirst(throwableWithEmptyStackTrace, isInline);

        // Assert: Verify that the result is null.
        assertNull("Expected null when filtering a throwable with an empty stack trace", firstFilteredElement);
    }
}