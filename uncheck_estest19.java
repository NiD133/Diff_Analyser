package org.apache.commons.io.function;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for the {@link Uncheck#apply(IOTriFunction, Object, Object, Object)} method.
 */
public class UncheckTest {

    /**
     * Tests that Uncheck.apply() correctly invokes the provided IOTriFunction
     * and returns its result when the function completes normally. This test
     * specifically verifies the case where the function returns null.
     */
    @Test
    public void testApplyWithTriFunctionShouldReturnNullWhenFunctionReturnsNull() throws IOException {
        // Arrange: Create a mock IOTriFunction that returns null for any input.
        // The 'doReturn' syntax is used because the mocked 'apply' method is declared
        // to throw a checked IOException, which 'when(...).thenReturn(...)' cannot handle directly.
        @SuppressWarnings("unchecked")
        final IOTriFunction<String, String, String, String> mockFunction = mock(IOTriFunction.class);
        doReturn(null).when(mockFunction).apply(anyString(), anyString(), anyString());

        // Act: Call the method under test with the mock function and placeholder arguments.
        final String result = Uncheck.apply(mockFunction, "arg1", "arg2", "arg3");

        // Assert: Verify that the result is null, as dictated by the mock's setup.
        assertNull("The result should be null, matching the mock function's return value.", result);
    }
}