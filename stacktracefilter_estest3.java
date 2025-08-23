package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link StackTraceFilter}.
 */
public class StackTraceFilterTest {

    private final StackTraceFilter stackTraceFilter = new StackTraceFilter();

    @Test
    public void filterFirstShouldReturnFirstElementWhenStackTraceIsClean() {
        // Arrange: Create a stack trace that does not contain any Mockito-internal frames.
        // This represents a "clean" stack trace from the perspective of the filter.
        StackTraceElement userCodeElement = new StackTraceElement(
            "com.example.myapp.MyService", "businessLogic", "MyService.java", 42
        );
        StackTraceElement[] cleanStackTrace = { userCodeElement };

        Throwable throwable = new Throwable();
        throwable.setStackTrace(cleanStackTrace);

        // Act: Call the method under test.
        StackTraceElement firstFilteredElement = stackTraceFilter.filterFirst(throwable, true);

        // Assert: The filter should return the first element because it's not internal Mockito code.
        assertSame("Expected the first element of the clean stack trace to be returned",
                     userCodeElement, firstFilteredElement);
    }
}