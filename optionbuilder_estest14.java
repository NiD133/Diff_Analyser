package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for the {@link OptionBuilder} class, focusing on exception scenarios for invalid inputs.
 */
public class OptionBuilderTest {

    /**
     * Verifies that calling {@code OptionBuilder.create(char)} with an illegal character
     * for an option name throws an {@code IllegalArgumentException} with a descriptive message.
     */
    @Test
    public void createWithInvalidCharacterShouldThrowIllegalArgumentException() {
        // The character ']' is not a valid short option name according to the OptionValidator.
        final char illegalOptionChar = ']';

        try {
            OptionBuilder.create(illegalOptionChar);
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (final IllegalArgumentException e) {
            // The exception is expected. Now, verify its message for correctness.
            final String expectedMessage = "Illegal option name '" + illegalOptionChar + "'.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}