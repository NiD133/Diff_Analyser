package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;

// The EvoSuite runner and scaffolding are kept to show the refactoring in the original context,
// but in a typical project, a standard JUnit runner would be used.
@RunWith(EvoRunner.class)
public class StackTraceFilter_ESTestTest5 extends StackTraceFilter_ESTest_scaffolding {

    /**
     * Verifies that the filter method throws a NullPointerException when processing
     * a stack trace array that contains a null element. This ensures the method
     * correctly handles malformed input, as the underlying cleaner will attempt to
     * access methods on the null element, leading to the expected exception.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void filterShouldThrowNullPointerExceptionWhenStackTraceContainsNullElement() {
        // Arrange
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        // Create a stack trace array containing a null element, which is an invalid input.
        StackTraceElement[] stackTraceWithNull = new StackTraceElement[1];

        // Act & Assert
        // Call the filter method with the invalid input.
        // The test will pass only if a NullPointerException is thrown,
        // as declared in the @Test annotation's 'expected' attribute.
        stackTraceFilter.filter(stackTraceWithNull, false);
    }
}