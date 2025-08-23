package org.apache.commons.io.function;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.Test;

/**
 * Contains tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#apply(IOBiFunction, Object, Object)} correctly
     * invokes the underlying function and returns its result when the function
     * does not throw an exception.
     */
    @Test
    public void testApplyForBiFunctionReturnsDelegateResult() throws IOException {
        // Arrange: Create a mock IOBiFunction and define its behavior.
        @SuppressWarnings("unchecked") // A common warning with Mockito mocks of generic types.
        final IOBiFunction<String, String, String> mockBiFunction = mock(IOBiFunction.class);

        final String firstArgument = "T;ot>5MR";
        final String secondArgument = "";
        
        // Configure the mock to return null when called with the specific arguments.
        when(mockBiFunction.apply(firstArgument, secondArgument)).thenReturn(null);

        // Act: Call the method under test with the mock and arguments.
        final String actualResult = Uncheck.apply(mockBiFunction, firstArgument, secondArgument);

        // Assert: Verify that the result is what the mock was configured to return.
        assertNull("The result should be null, as returned by the mock function.", actualResult);

        // Also, verify that the 'apply' method on the mock was called exactly once.
        verify(mockBiFunction).apply(firstArgument, secondArgument);
    }
}