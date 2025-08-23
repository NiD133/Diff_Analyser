package org.apache.commons.io.function;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link Uncheck}.
 * This class focuses on the Uncheck.compare() method.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#compare(IOComparator, Object, Object)}
     * correctly delegates the call to the provided IOComparator and returns its result.
     * This test ensures that the wrapper function works as expected for a successful comparison.
     */
    @Test(timeout = 4000)
    public void testCompareDelegatesToIOComparator() throws IOException {
        // Arrange
        final int expectedResult = 553;
        final String argument1 = "first_string";
        final String argument2 = "second_string";

        // Create a mock IOComparator.
        // The type is explicitly provided to handle generics with Mockito.
        @SuppressWarnings("unchecked")
        final IOComparator<String> mockComparator = mock(IOComparator.class);

        // Configure the mock to return a specific value when its 'compare' method is called.
        // We must use doReturn().when() because IOComparator.compare() is declared with 'throws IOException'.
        doReturn(expectedResult).when(mockComparator).compare(argument1, argument2);

        // Act
        final int actualResult = Uncheck.compare(mockComparator, argument1, argument2);

        // Assert
        // 1. Check that the returned value is the one we configured in the mock.
        assertEquals(expectedResult, actualResult);

        // 2. Verify that the 'compare' method on our mock was called exactly once with the correct arguments.
        verify(mockComparator).compare(argument1, argument2);
    }
}