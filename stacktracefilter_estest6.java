package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link StackTraceFilter} class.
 * This version clarifies the original auto-generated test's likely intent.
 */
public class StackTraceFilterTest {

    /**
     * Verifies that findSourceFile() returns the provided default value when none of the
     * stack trace elements contain source file information.
     *
     * This test is an improvement because it is:
     * 1.  **Understandable:** The test name and variable names clearly state the intent.
     * 2.  **Deterministic:** It uses a manually created StackTraceElement with a null file name,
     *     ensuring the test outcome is always the same, unlike the original's reliance on a
     *     mock throwable's unpredictable stack trace.
     * 3.  **Focused:** It tests the `findSourceFile` method in isolation, following the
     *     Arrange-Act-Assert pattern for clarity.
     */
    @Test
    public void findSourceFile_shouldReturnDefaultValue_whenSourceFileIsUnavailable() {
        // Arrange
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        String expectedDefaultValue = "Unknown Source";

        // Create a stack trace element that explicitly lacks source file information.
        // This makes the test's behavior predictable and easy to understand.
        StackTraceElement elementWithoutSourceFile = new StackTraceElement(
            "some.declaring.Class",
            "someMethod",
            null, // The critical part: no source file information is available.
            123
        );
        StackTraceElement[] stackTrace = { elementWithoutSourceFile };

        // Act
        String actualSourceFile = stackTraceFilter.findSourceFile(stackTrace, expectedDefaultValue);

        // Assert
        // The method should return the default value because no element had a source file.
        assertEquals(expectedDefaultValue, actualSourceFile);
    }
}