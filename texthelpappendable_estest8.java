package org.apache.commons.cli.help;

import org.apache.commons.cli.help.TextStyle.Builder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link TextHelpAppendable} class, focusing on text formatting logic.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that the resize method correctly scales the max width and indent
     * properties of a TextStyle.Builder by a given negative fraction.
     * The new values are calculated by multiplying the original values by the
     * fraction and casting the result to an integer.
     */
    @Test
    public void resizeWithNegativeFractionShouldCorrectlyScaleBuilderProperties() {
        // Arrange
        // The Appendable is not used by the resize method, so null is sufficient for this test.
        TextHelpAppendable textHelp = new TextHelpAppendable(null);
        Builder styleBuilder = textHelp.getTextStyleBuilder();

        // Set initial, easy-to-understand values for width and indent.
        final int initialMaxWidth = 100;
        final int initialIndent = 20;
        styleBuilder.setMaxWidth(initialMaxWidth);
        styleBuilder.setIndent(initialIndent);

        final double resizeFraction = -1.5;

        // Act
        textHelp.resize(styleBuilder, resizeFraction);

        // Assert
        // The resize operation should update the builder's properties.
        final int expectedMaxWidth = (int) (initialMaxWidth * resizeFraction); // 100 * -1.5 = -150
        final int expectedIndent = (int) (initialIndent * resizeFraction);   // 20 * -1.5 = -30

        assertEquals("Max width should be scaled by the negative fraction.",
                expectedMaxWidth, styleBuilder.getMaxWidth());
        assertEquals("Indent should be scaled by the negative fraction.",
                expectedIndent, styleBuilder.getIndent());
    }
}