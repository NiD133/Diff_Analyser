package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the StackTraceFilter class.
 */
public class StackTraceFilterTest {

    /**
     * Verifies that the filter method retains a "clean" stack trace element,
     * which represents user code rather than framework code.
     */
    @Test
    public void filter_whenGivenSingleCleanStackTraceElement_shouldNotFilterItOut() {
        // Arrange
        StackTraceFilter stackTraceFilter = new StackTraceFilter();

        // Create a sample stack trace element that represents user code.
        // This element should be considered "clean" and not be removed by the filter.
        StackTraceElement userCodeElement = new StackTraceElement(
            "com.mycompany.myapp.MyClass", "myMethod", "MyClass.java", 42
        );
        StackTraceElement[] inputStackTrace = { userCodeElement };

        // Act: Filter the stack trace. The 'keepTop' parameter is false, ensuring
        // the filtering logic is applied to all elements.
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(inputStackTrace, false);

        // Assert
        // 1. The filter should return a new array instance, not modify the input.
        assertNotSame("The filter should return a new array instance.", inputStackTrace, filteredStackTrace);

        // 2. The resulting array should contain exactly one element.
        assertEquals("The filtered stack trace should still contain one element.", 1, filteredStackTrace.length);

        // 3. The element should be the original user code element.
        assertSame("The clean element should be retained.", userCodeElement, filteredStackTrace[0]);
    }
}