package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    @Test
    public void resizeWithNegativeFractionShouldSetWidthAndIndentToZero() {
        // Arrange
        // The resize method uses Math.max(0, ...) to prevent negative sizes.
        // This test verifies that behavior by using a negative fraction.
        final double negativeFraction = -2.0;
        TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();
        TextStyle.Builder styleBuilder = helpAppendable.getTextStyleBuilder();

        // Pre-condition check (optional but good for clarity):
        // Default width and indent are expected to be positive.
        // assertEquals(TextHelpAppendable.DEFAULT_WIDTH, helpAppendable.getMaxWidth());
        // assertEquals(TextHelpAppendable.DEFAULT_INDENT, helpAppendable.getIndent());

        // Act
        // The resize method modifies the builder instance directly.
        helpAppendable.resize(styleBuilder, negativeFraction);

        // Assert
        // The resulting width and indent should be capped at 0, not a negative value.
        assertEquals("Max width should be 0 after resizing with a negative fraction", 0, helpAppendable.getMaxWidth());
        assertEquals("Indent should be 0 after resizing with a negative fraction", 0, helpAppendable.getIndent());
    }
}