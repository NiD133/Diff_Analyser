package org.apache.commons.cli.help;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link TextHelpAppendable}.
 *
 * Note: This class is a cleaned-up and more understandable version of the
 * auto-generated test 'TextHelpAppendable_ESTestTest16'.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that the TextStyle.Builder provided by TextHelpAppendable
     * remains scalable after a new style configuration is applied.
     */
    @Test
    public void getTextStyleBuilderShouldRemainScalableAfterSettingStyle() {
        // Arrange: Create a TextHelpAppendable and get its style builder.
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        TextStyle.Builder styleBuilder = textHelpAppendable.getTextStyleBuilder();

        // Act: Apply a default style configuration to the builder.
        styleBuilder.setTextStyle(TextStyle.DEFAULT);

        // Assert: The builder should still report itself as scalable. This ensures
        // that modifying the style does not alter fundamental builder properties.
        assertTrue("The builder's 'scalable' property should remain true after setting a style.",
                   styleBuilder.isScalable());
    }
}