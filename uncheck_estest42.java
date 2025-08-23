package org.apache.commons.io.function;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class, focusing on the apply method.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#apply(IOFunction, Object)} correctly returns the
     * result of the given function when no IOException is thrown.
     */
    @Test
    public void applyShouldReturnResultOfIOFunction() {
        // Arrange: Create an identity function that simply returns its input.
        final IOFunction<String, String> identityFunction = IOFunction.identity();
        final String input = "http";

        // Act: Call the Uncheck.apply method with the function and input.
        final String result = Uncheck.apply(identityFunction, input);

        // Assert: Verify that the result is the same as the input.
        assertEquals(input, result);
    }
}