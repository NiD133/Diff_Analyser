package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import org.junit.Test;

/**
 * Tests for the resize functionality in {@link TextHelpAppendable}.
 */
public class TextHelpAppendableResizeTest {

    /**
     * The resize method calculates a new indent by multiplying the original indent
     * by a given fraction. This test verifies that if this calculation results
     * in a negative number, the indent is clamped to zero.
     */
    @Test
    public void resizeWithNegativeFractionShouldClampIndentToZero() {
        // Arrange
        // Use a StringWriter for test isolation instead of System.out
        TextHelpAppendable helpAppendable = new TextHelpAppendable(new StringWriter());
        TextStyle.Builder styleBuilder = helpAppendable.getTextStyleBuilder();
        
        // The default indent is 3, which is the value we expect to be resized.
        final int initialIndent = 3;
        assertEquals("Initial indent should be the default value.", initialIndent, helpAppendable.getIndent());

        // A negative fraction will cause the resized indent to be negative.
        final double negativeFraction = -2.0;

        // Act
        // Resize the style, which should update the indent on the helpAppendable instance.
        helpAppendable.resize(styleBuilder, negativeFraction);

        // Assert
        // The new indent should be 0, not a negative value.
        assertEquals("Indent should be clamped to 0 after resizing with a negative fraction.",
                     0, helpAppendable.getIndent());
    }
}