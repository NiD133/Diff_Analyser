package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;

/**
 * Unit tests for the {@link StackTraceFilter} class.
 *
 * <p>This class was refactored from an auto-generated test to improve
 * readability and adherence to standard testing practices.
 */
public class StackTraceFilterTest {

    /**
     * Verifies that {@link StackTraceFilter#findSourceFile(StackTraceElement[], String)}
     * throws a NullPointerException if the input array contains a null element.
     * This ensures the method is robust against malformed stack trace data.
     */
    @Test(expected = NullPointerException.class)
    public void findSourceFile_shouldThrowNPE_whenStackTraceContainsNullElement() {
        // Given
        // Note: In the version of Mockito this test was generated for,
        // StackTraceFilter is a concrete class with a public constructor.
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        StackTraceElement[] stackTraceWithNull = new StackTraceElement[] { null };
        String defaultValue = "Default.java";

        // When
        // The method is called with an array containing a null. It is expected to throw
        // an NPE when it attempts to access a method on the null element.
        stackTraceFilter.findSourceFile(stackTraceWithNull, defaultValue);

        // Then
        // A NullPointerException is thrown, as specified by the @Test(expected) annotation.
        // The test will automatically pass if this exception occurs.
    }
}