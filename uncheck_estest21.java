package org.apache.commons.io.function;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class, focusing on the apply method
 * for {@link IOQuadFunction}.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#apply(IOQuadFunction, Object, Object, Object, Object)}
     * correctly calls the provided function and returns its result when no
     * exception is thrown.
     */
    @Test
    public void testApplyWithIOQuadFunctionReturnsResult() throws IOException {
        // Arrange
        final String expectedResult = "FunctionResult";
        final String arg1 = "Input1";
        final String arg2 = "Input2";
        final String arg3 = "Input3";
        final String arg4 = "Input4";

        // Create a mock IOQuadFunction that can throw IOException.
        @SuppressWarnings("unchecked")
        final IOQuadFunction<String, String, String, String, String> mockFunction = mock(IOQuadFunction.class);

        // Configure the mock to return a specific result for our test inputs.
        // We use doReturn().when() because the apply() method declares a checked exception.
        doReturn(expectedResult).when(mockFunction).apply(arg1, arg2, arg3, arg4);

        // Act
        // The Uncheck.apply method should invoke our mock function and return its result.
        final String actualResult = Uncheck.apply(mockFunction, arg1, arg2, arg3, arg4);

        // Assert
        // Verify that the result from Uncheck.apply is the one we expect.
        assertEquals(expectedResult, actualResult);

        // Also, verify that the mock function was called exactly once with the correct arguments.
        verify(mockFunction).apply(arg1, arg2, arg3, arg4);
    }
}