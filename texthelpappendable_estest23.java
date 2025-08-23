package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link TextHelpAppendable} class.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that the getLeftPad() method correctly returns the value
     * previously set by setLeftPad().
     */
    @Test
    public void shouldSetAndGetLeftPadding() {
        // Arrange
        // The Appendable can be null for this test as it's not used by the set/get methods.
        final TextHelpAppendable helpAppendable = new TextHelpAppendable(null);
        final int expectedLeftPad = -2020;

        // Act
        helpAppendable.setLeftPad(expectedLeftPad);
        final int actualLeftPad = helpAppendable.getLeftPad();

        // Assert
        assertEquals("The retrieved left pad value should match the value that was set.",
                     expectedLeftPad, actualLeftPad);
    }
}