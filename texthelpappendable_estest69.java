package org.apache.commons.cli.help;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * This test suite contains tests for the {@link TextHelpAppendable} class.
 */
public class TextHelpAppendable_ESTestTest69 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Tests that calling {@code appendHeader} with an extremely large level value
     * results in an {@link OutOfMemoryError}. This is expected because the method
     * will likely attempt to allocate memory for indentation based on the level,
     * and a value like Integer.MAX_VALUE will exceed available heap space.
     */
    @Test
    public void appendHeaderWithExtremelyLargeLevelThrowsOutOfMemoryError() {
        // Arrange: Create a TextHelpAppendable instance and define test parameters.
        final TextHelpAppendable textHelp = TextHelpAppendable.systemOut();
        final String headerText = "A sample header";
        final int extremelyLargeLevel = Integer.MAX_VALUE;

        // The original test also resized the text style builder to a large width.
        // This step is preserved here for faithfulness to the original test's logic,
        // although the OutOfMemoryError is primarily caused by the 'extremelyLargeLevel'.
        final TextStyle.Builder styleBuilder = textHelp.getTextStyleBuilder();
        textHelp.resize(styleBuilder, 700.0);

        // Act & Assert: Verify that an OutOfMemoryError is thrown when calling
        // appendHeader with an impossibly large level.
        assertThrows(OutOfMemoryError.class, () -> {
            textHelp.appendHeader(extremelyLargeLevel, headerText);
        });
    }
}