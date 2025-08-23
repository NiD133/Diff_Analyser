package org.apache.commons.io.function;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that Uncheck.apply() correctly handles a null input when using
     * an identity function, which should simply return the null input.
     */
    @Test
    public void testApplyWithIdentityFunctionShouldReturnNullForNullInput() {
        // Arrange: Create an identity function that returns its input.
        final IOFunction<String, String> identityFunction = IOFunction.identity();
        final String input = null;

        // Act: Apply the function to the null input using the Uncheck utility.
        final String result = Uncheck.apply(identityFunction, input);

        // Assert: The result should be null, as that was the input.
        assertNull("The result of applying the identity function to null should be null.", result);
    }
}