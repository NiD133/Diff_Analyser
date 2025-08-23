package org.apache.commons.io.function;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link Uncheck}.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#apply(IOFunction, Object)} throws a NullPointerException
     * when the function parameter is null.
     */
    @Test
    public void applyShouldThrowNullPointerExceptionForNullFunction() {
        // Arrange: Define a test input. The function to be applied is null.
        final String testInput = "any-string";

        // Act & Assert: Verify that calling Uncheck.apply with a null function
        // throws a NullPointerException.
        assertThrows(NullPointerException.class, () -> {
            Uncheck.apply((IOFunction<String, String>) null, testInput);
        });
    }
}