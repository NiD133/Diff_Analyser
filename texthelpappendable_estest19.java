package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TextHelpAppendable}.
 * This class contains tests for setting and getting text style properties.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that the setIndent() method correctly updates the indent value,
     * including when a negative value is provided.
     */
    @Test
    public void setIndentShouldUpdateIndentValue() {
        // Arrange
        // The systemOut() factory method is a convenient way to get an instance for testing.
        final TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();
        final int expectedIndent = -84;

        // Act
        helpAppendable.setIndent(expectedIndent);
        final int actualIndent = helpAppendable.getIndent();

        // Assert
        assertEquals("The indent value should be updated to the set value.",
                expectedIndent, actualIndent);
    }
}