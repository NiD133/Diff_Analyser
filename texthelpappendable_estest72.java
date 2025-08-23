package org.apache.commons.cli.help;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for the {@link TextHelpAppendable} class.
 */
public class TextHelpAppendableTest {

    @Test
    public void adjustTableFormatShouldThrowNullPointerExceptionForNullInput() {
        // Arrange
        final TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();

        // Act & Assert
        // The method is expected to throw a NullPointerException because it does not
        // perform a null check on its 'table' parameter.
        assertThrows(NullPointerException.class, () -> {
            textHelpAppendable.adjustTableFormat(null);
        });
    }
}