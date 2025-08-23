package org.apache.commons.io.function;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@code Uncheck.compare} method.
 */
public class UncheckTest {

    /**
     * Tests that Uncheck.compare successfully invokes the underlying IOComparator
     * and returns its result when no IOException is thrown.
     */
    @Test
    public void compareShouldReturnResultFromIOComparatorOnSuccess() throws IOException {
        // Arrange
        // 1. Create a mock IOComparator for String comparison.
        @SuppressWarnings("unchecked")
        final IOComparator<String> mockComparator = mock(IOComparator.class);

        // 2. Define test inputs and the expected outcome.
        final String left = "alpha";
        final String right = "beta";
        final int expectedResult = -1;

        // 3. Configure the mock to return the expected result when called with the test inputs.
        // The 'compare' method of IOComparator can throw an IOException, which is why
        // this test method also declares 'throws IOException'.
        when(mockComparator.compare(left, right)).thenReturn(expectedResult);

        // Act
        // Call the method under test with the mock and inputs.
        final int actualResult = Uncheck.compare(mockComparator, left, right);

        // Assert
        // Verify that the returned value is the one provided by the mock.
        assertEquals("The result from Uncheck.compare should match the result from the underlying IOComparator.",
                     expectedResult, actualResult);
    }
}