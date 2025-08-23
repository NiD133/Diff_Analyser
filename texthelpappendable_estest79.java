package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Contains tests for the {@link TextHelpAppendable#resize(TextStyle.Builder, double)} method.
 */
public class TextHelpAppendable_ESTestTest79 {

    @Test
    public void resizeWithFractionOfOneShouldNotChangeTextStyleValues() {
        // Arrange
        // Create a formatter, which initializes a TextStyle.Builder with default values.
        // The default indent is 3 (TextHelpAppendable.DEFAULT_INDENT).
        TextHelpAppendable helpFormatter = TextHelpAppendable.systemOut();
        TextStyle.Builder styleBuilder = helpFormatter.getTextStyleBuilder();

        // Set a custom max width to verify it's also handled by the resize operation.
        styleBuilder.setMaxWidth(1);

        // Act
        // Resizing with a fraction of 1.0 should have no effect on the values.
        helpFormatter.resize(styleBuilder, 1.0);

        // Assert
        // The max width should remain at the value we set.
        assertEquals("Max width should be unchanged after resizing by a factor of 1", 1, styleBuilder.getMaxWidth());

        // The default indent is 3. The resize calculation is (int)(3 * 1.0), which results in 3.
        // The original generated test incorrectly asserted 0.
        assertEquals("Indent should be unchanged from the default value", 3, helpFormatter.getIndent());
    }
}