package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link StackTraceFilter} to ensure it correctly cleans stack traces.
 */
public class StackTraceFilterTest {

    /**
     * Verifies that the filter removes stack trace elements that originate
     * from Mockito's internal classes. This is crucial for providing clean stack
     * traces to the user, free from framework-specific noise.
     */
    @Test
    public void shouldRemoveInternalMockitoFrameFromStackTrace() {
        // Arrange
        StackTraceFilter stackTraceFilter = new StackTraceFilter();

        // Create a stack trace element representing a call from an internal Mockito class.
        // The filter is expected to identify and remove this based on its class name.
        StackTraceElement mockitoInternalFrame = new StackTraceElement(
            "org.mockito.internal.PremainAttach", // This is an internal Mockito class
            "someInternalMethod",
            "PremainAttach.java",
            42
        );
        StackTraceElement[] originalStackTrace = new StackTraceElement[]{mockitoInternalFrame};

        // Act
        // Call the filter with 'keepTop' set to false, so no elements are preserved unconditionally.
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(originalStackTrace, false);

        // Assert
        // The resulting stack trace should be empty because the single internal frame was removed.
        assertEquals("The filtered stack trace should be empty", 0, filteredStackTrace.length);
    }
}