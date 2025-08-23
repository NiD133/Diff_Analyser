package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    @Test
    public void setIndent_shouldAcceptAndStoreNegativeValue() {
        // Arrange
        // The test verifies that the indent value can be set and retrieved correctly,
        // even when the value is negative.
        final TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();
        final int expectedIndent = -293;

        // Act
        helpAppendable.setIndent(expectedIndent);
        final int actualIndent = helpAppendable.getIndent();

        // Assert
        assertEquals("The retrieved indent should match the negative value that was set.",
                     expectedIndent, actualIndent);
    }
}