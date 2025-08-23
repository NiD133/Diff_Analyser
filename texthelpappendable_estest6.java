package org.apache.commons.cli.help;

import org.apache.commons.cli.help.TextHelpAppendable;
import org.apache.commons.cli.help.TextStyle;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link TextHelpAppendable} class, focusing on text style manipulation.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that resizing a TextStyle.Builder with a fraction of 1.0 returns a new builder
     * with identical values and does not modify the original TextHelpAppendable instance.
     */
    @Test
    public void resizeTextStyleBuilderWithFractionOfOneShouldReturnEquivalentBuilder() {
        // Arrange
        // A TextHelpAppendable with default settings.
        final TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();
        // A TextStyle.Builder with default values (indent=0, maxWidth=Integer.MAX_VALUE).
        final TextStyle.Builder originalBuilder = TextStyle.builder();

        // Act
        // Resize the builder with a fraction of 1.0, which should result in no change.
        final TextStyle.Builder resizedBuilder = helpAppendable.resize(originalBuilder, 1.0);

        // Assert
        // 1. Verify the new builder has the same values as the original default builder.
        assertEquals("Indent should remain at its default value of 0", 0, resizedBuilder.getIndent());
        assertEquals("Max width should remain at its default value", Integer.MAX_VALUE, resizedBuilder.getMaxWidth());

        // 2. Verify the original TextHelpAppendable object was not mutated by the resize operation.
        assertEquals("Left pad should be unchanged", TextHelpAppendable.DEFAULT_LEFT_PAD, helpAppendable.getLeftPad());
        assertEquals("Max width should be unchanged", TextHelpAppendable.DEFAULT_WIDTH, helpAppendable.getMaxWidth());
        assertEquals("Indent should be unchanged", TextHelpAppendable.DEFAULT_INDENT, helpAppendable.getIndent());
    }
}