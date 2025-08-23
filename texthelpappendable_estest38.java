package org.apache.commons.cli.help;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * The printWrapped method relies on the internal TextStyle, which defines the line width.
     * This test ensures that if the width is set to a negative value (via the resize method),
     * printWrapped will throw an IllegalArgumentException, preventing invalid operations.
     */
    @Test
    public void printWrappedShouldThrowExceptionWhenWidthIsNegative() throws IOException {
        // Arrange
        // Use a StringBuilder for an isolated test environment, avoiding console output.
        TextHelpAppendable helpAppendable = new TextHelpAppendable(new StringBuilder());
        TextStyle.Builder styleBuilder = helpAppendable.getTextStyleBuilder();
        double negativeFraction = -2.0;

        // The resize method will calculate a new width for the style builder.
        // A negative fraction results in a negative width.
        helpAppendable.resize(styleBuilder, negativeFraction);

        // Assert: Expect an exception with a specific message.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Width must be greater than 0");

        // Act: This call should fail because the internal style now has a negative width.
        helpAppendable.printWrapped("Attempting to wrap text with an invalid width.");
    }
}