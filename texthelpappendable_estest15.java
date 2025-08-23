package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TextHelpAppendable}.
 * This class focuses on specific behaviors related to its configuration.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that setMaxWidth can accept a negative value and that getMaxWidth
     * correctly returns this negative value. This ensures that the component
     * behaves predictably even with unconventional input, although negative
     * widths may not be practically useful.
     */
    @Test
    public void shouldSetAndGetNegativeMaxWidth() {
        // Arrange
        final TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        final int negativeWidth = -2460;

        // Act
        textHelpAppendable.setMaxWidth(negativeWidth);
        final int actualWidth = textHelpAppendable.getMaxWidth();

        // Assert
        assertEquals("The max width should be the negative value that was set.",
                     negativeWidth, actualWidth);
    }
}