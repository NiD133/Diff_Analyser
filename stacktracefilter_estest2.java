package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link StackTraceFilter}.
 */
public class StackTraceFilterTest {

    @Test
    public void filterFirst_shouldReturnFirstElement_whenItIsNotFiltered() {
        // Arrange
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        
        // Create a stack trace element that represents user code, which should not be filtered.
        StackTraceElement userCodeElement = new StackTraceElement(
            "com.example.myapp.MyClass", 
            "myMethod", 
            "MyClass.java", 
            42
        );
        StackTraceElement[] stackTrace = { userCodeElement };

        Throwable throwable = new Throwable();
        throwable.setStackTrace(stackTrace);

        // Act
        // The 'isInline' parameter is false, indicating we should check the very first element.
        StackTraceElement firstUnfilteredElement = stackTraceFilter.filterFirst(throwable, false);

        // Assert
        // Since the first element is user code, the filter should return it directly.
        assertSame(
            "Should return the first stack trace element as it's not a Mockito-internal class",
            userCodeElement,
            firstUnfilteredElement
        );
    }
}