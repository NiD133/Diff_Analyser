package org.apache.commons.io.function;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import org.junit.Test;

/**
 * Contains tests for the {@link Uncheck} utility class.
 * This class focuses on improving an auto-generated test for the Uncheck.compare() method.
 */
public class Uncheck_ESTestTest18 { // Retaining original class name for context

    /**
     * Tests that {@link Uncheck#compare(IOComparator, Object, Object)} correctly
     * delegates the comparison to the provided IOComparator and returns its result
     * when no exception is thrown.
     */
    @Test
    public void testCompareDelegatesToIOComparatorAndReturnsResult() throws IOException {
        // Arrange
        final int expectedComparisonResult = -502;
        final String leftOperand = "any string";
        final String rightOperand = "another string";

        // Create a mock for the IOComparator functional interface.
        // The type is suppressed because mock() returns a raw type.
        @SuppressWarnings("unchecked")
        final IOComparator<String> mockComparator = mock(IOComparator.class);

        // Configure the mock to return a specific result.
        // We use doReturn().when() because IOComparator.compare() is declared
        // to throw a checked IOException, and this syntax avoids a compile-time error.
        doReturn(expectedComparisonResult).when(mockComparator).compare(anyString(), anyString());

        // Act
        final int actualResult = Uncheck.compare(mockComparator, leftOperand, rightOperand);

        // Assert
        assertEquals("The result from Uncheck.compare should match the one from the mocked IOComparator.",
                expectedComparisonResult, actualResult);
    }
}