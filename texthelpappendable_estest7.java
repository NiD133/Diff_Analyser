package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the resize utility method in {@link TextHelpAppendable}.
 */
public class TextHelpAppendableResizeTest {

    /**
     * This test verifies the behavior of the `resize` method when applied to a
     * TextStyle.Builder. Based on the original auto-generated test's assertions,
     * this method is expected to scale the maximum width of the builder but leave
     * the indent value unchanged.
     */
    @Test
    public void resizeShouldScaleBuilderMaxWidthWithoutAffectingIndent() {
        // Arrange
        // Create a TextHelpAppendable to access its resize method and a default builder.
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        TextStyle.Builder styleBuilder = textHelpAppendable.getTextStyleBuilder();

        // Use a simple, clear resize factor instead of an obscure decimal.
        final double resizeFraction = 10.0;

        // Capture the initial state for use in calculations.
        // The default max width is 74 and the default indent is 3.
        final int initialMaxWidth = textHelpAppendable.getMaxWidth();
        final int initialIndent = textHelpAppendable.getIndent();

        // Calculate the expected max width after resizing.
        final int expectedMaxWidth = (int) (initialMaxWidth * resizeFraction); // 74 * 10.0 = 740

        // Act
        // The resize method modifies the builder instance that is passed to it.
        // In this case, it's the internal builder of our textHelpAppendable instance.
        textHelpAppendable.resize(styleBuilder, resizeFraction);

        // Assert
        // Verify that the builder's properties were updated as expected by checking
        // the public accessors of the TextHelpAppendable instance.

        // 1. The max width should be scaled by the given fraction.
        assertEquals("Max width should be scaled by the resize fraction.",
                expectedMaxWidth, textHelpAppendable.getMaxWidth());

        // 2. The original test implies that the indent is NOT changed by the resize method.
        assertEquals("Indent should not be affected by the resize operation.",
                initialIndent, textHelpAppendable.getIndent());
    }
}