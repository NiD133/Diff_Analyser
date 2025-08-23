package org.apache.commons.cli;

import org.junit.Test;

/**
 * Tests for the {@link PosixParser} class, focusing on edge cases and invalid inputs.
 */
public class PosixParserTest {

    /**
     * Verifies that the flatten method throws a NullPointerException when passed null
     * for the required 'options' and 'arguments' parameters. This ensures the method
     * correctly handles invalid null inputs.
     */
    @Test(expected = NullPointerException.class)
    public void flattenShouldThrowNullPointerExceptionWhenOptionsAndArgumentsAreNull() {
        // Arrange
        PosixParser parser = new PosixParser();

        // Act & Assert
        // Calling flatten with null for the first two arguments should trigger the exception.
        parser.flatten(null, null, true);
    }
}