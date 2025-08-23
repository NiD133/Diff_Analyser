package org.apache.commons.io.function;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link Uncheck}.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#apply(IOQuadFunction, Object, Object, Object, Object)}
     * correctly calls the underlying function and returns its result without throwing an exception.
     */
    @Test
    public void testApplyWithQuadFunctionReturnsMockedValue() throws IOException {
        // Arrange: Create a mock IOQuadFunction and define its behavior.
        // The function is mocked to return null for any string inputs.
        @SuppressWarnings("unchecked")
        final IOQuadFunction<String, String, String, String, String> mockFunction = mock(IOQuadFunction.class);
        when(mockFunction.apply(anyString(), anyString(), anyString(), anyString())).thenReturn(null);

        // Define the arguments to be passed to the function.
        final String arg1 = "input1";
        final String arg2 = "input2";
        final String arg3 = "input3";
        final String arg4 = "input4";

        // Act: Call the Uncheck.apply method with the mock function and arguments.
        final String result = Uncheck.apply(mockFunction, arg1, arg2, arg3, arg4);

        // Assert: Verify the outcome.
        // 1. The result should be null, as defined by the mock.
        assertNull("The result of Uncheck.apply should be the value returned by the underlying function.", result);

        // 2. The mock function's apply method should have been called exactly once with the specified arguments.
        verify(mockFunction).apply(arg1, arg2, arg3, arg4);
    }
}