package org.apache.commons.io.function;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link Uncheck} utility class, focusing on the successful execution path
 * of the {@code apply} method for an {@link IOTriFunction}.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#apply(IOTriFunction, Object, Object, Object)}
     * correctly calls the underlying function and returns its result when no
     * IOException is thrown.
     */
    @Test
    public void testApplyForTriFunctionShouldReturnResultOnSuccess() throws IOException {
        // Arrange: Set up the mock function and test data.
        @SuppressWarnings("unchecked")
        final IOTriFunction<String, String, String, String> mockIoTriFunction = mock(IOTriFunction.class);

        final String expectedResult = "Function executed successfully";
        final String arg1 = "first-input";
        final String arg2 = "second-input";
        final String arg3 = "third-input";

        // Configure the mock to return the expected result when called with our specific arguments.
        when(mockIoTriFunction.apply(arg1, arg2, arg3)).thenReturn(expectedResult);

        // Act: Call the method under test.
        final String actualResult = Uncheck.apply(mockIoTriFunction, arg1, arg2, arg3);

        // Assert: Verify that the result from the Uncheck.apply call matches the expected result.
        assertEquals("The result from the underlying function should be returned.", expectedResult, actualResult);
    }
}