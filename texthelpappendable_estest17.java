package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    @Test
    public void setLeftPadShouldUpdatePaddingValue() {
        // Arrange
        // A null Appendable is acceptable here since this test does not perform any write operations.
        TextHelpAppendable helpAppendable = new TextHelpAppendable(null);
        final int expectedLeftPad = -3110;

        // Act
        helpAppendable.setLeftPad(expectedLeftPad);
        final int actualLeftPad = helpAppendable.getLeftPad();

        // Assert
        assertEquals("The left pad value should be updated as set.", expectedLeftPad, actualLeftPad);
    }
}