package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the resize functionality in {@link TextHelpAppendable}.
 */
public class TextHelpAppendable_ESTestTest24 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Verifies that resizing a TextStyle with a negative fraction correctly floors
     * the max width and indent values to zero, preventing invalid negative dimensions.
     * The resize operation calculates a new size by multiplying the original by the fraction,
     * and the implementation ensures the result is never less than zero.
     */
    @Test
    public void resizeWithNegativeFractionShouldSetWidthAndIndentToZero() {
        // Arrange
        // Using a simple negative value makes the intent clear.
        final double negativeFraction = -2.5;
        
        // TextHelpAppendable.systemOut() creates an instance with default positive width and indent.
        TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();
        TextStyle.Builder styleBuilder = helpAppendable.getTextStyleBuilder();

        // Act
        // Resize the style using the negative fraction. This modifies the builder,
        // which in turn affects the state of the helpAppendable instance.
        helpAppendable.resize(styleBuilder, negativeFraction);

        // Assert
        // The resize operation should produce a negative result, which the implementation
        // is expected to cap at a minimum of 0.
        assertEquals("Max width should be floored at 0 after resizing with a negative fraction.", 0, helpAppendable.getMaxWidth());
        assertEquals("Indent should be floored at 0 after resizing with a negative fraction.", 0, helpAppendable.getIndent());
    }
}