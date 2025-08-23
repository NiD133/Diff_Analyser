package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.apache.commons.cli.help.TextStyle.Builder;
import org.junit.Test;

/**
 * Tests for {@link TextHelpAppendable}.
 *
 * This test class is a refactoring of an auto-generated EvoSuite test case.
 */
public class TextHelpAppendable_ESTestTest14 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Tests that successive calls to getTextStyleBuilder() return the same
     * modifiable instance, ensuring that changes to the builder are persistent.
     */
    @Test
    public void getTextStyleBuilderShouldReturnSameModifiableInstance() {
        // Arrange
        final TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();
        final int newMaxWidth = 120;

        // Act
        // Retrieve the builder and modify its max width property.
        final Builder initialBuilder = helpAppendable.getTextStyleBuilder();
        initialBuilder.setMaxWidth(newMaxWidth);

        // Retrieve the builder again to check if the change is reflected.
        final Builder subsequentBuilder = helpAppendable.getTextStyleBuilder();

        // Assert
        // 1. Verify that the same builder instance is returned on each call.
        assertSame("Expected getTextStyleBuilder() to return the same instance every time.",
                initialBuilder, subsequentBuilder);

        // 2. Verify that the modification is reflected in the retrieved instance.
        assertEquals("The max width should be updated to the new value.",
                newMaxWidth, subsequentBuilder.getMaxWidth());

        // 3. Verify that other properties remain at their default values.
        assertEquals("The left pad should remain at its default value.",
                1, subsequentBuilder.getLeftPad());
        assertEquals("The indent should remain at its default value.",
                3, subsequentBuilder.getIndent());
    }
}