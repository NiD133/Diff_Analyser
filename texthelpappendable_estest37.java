package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that attempting to print with a negative left padding value
     * results in a {@link NegativeArraySizeException}. This occurs because an
     * internal utility tries to create a padding array with a negative size.
     */
    @Test(expected = NegativeArraySizeException.class)
    public void printWrappedShouldThrowExceptionWhenLeftPadIsNegative() throws IOException {
        // Arrange: Create a TextHelpAppendable and set an invalid, negative left padding.
        StringWriter writer = new StringWriter();
        TextHelpAppendable helpAppendable = new TextHelpAppendable(writer);
        helpAppendable.setLeftPad(-1);

        // Act: Attempt to print, which should trigger the exception.
        helpAppendable.printWrapped("");

        // Assert: The test is successful if a NegativeArraySizeException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}