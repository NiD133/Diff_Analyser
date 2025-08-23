package org.apache.commons.cli.help;

import static org.junit.Assert.fail;

import org.junit.Test;

public class TextHelpAppendable_ESTestTest45 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Tests that makeColumnQueue throws an IndexOutOfBoundsException when processing
     * a CharSequence that contains a form-feed character ('\f'), which is a defined
     * break character.
     *
     * <p>This test case reproduces a bug where the method fails to correctly handle
     * break characters within the input, leading to an out-of-bounds access attempt.</p>
     */
    @Test
    public void makeColumnQueueWithBreakCharacterNearEndThrowsException() {
        // Arrange
        // The output destination is irrelevant for this test, so we pass null.
        final TextHelpAppendable helpFormatter = new TextHelpAppendable(null);
        final TextStyle defaultStyle = TextStyle.DEFAULT;

        // The original test created a CharBuffer from a byte array: {0, 0, 0, 12, 0}.
        // The byte 12 corresponds to the form-feed character ('\f'). This complex setup
        // is simplified here by using a direct string representation. This specific
        // sequence of characters is known to trigger the bug.
        final String inputTextWithBreakChar = "\0\0\0\f\0";

        // Act & Assert
        // We expect the method to throw an IndexOutOfBoundsException when it
        // attempts to process the problematic input.
        try {
            helpFormatter.makeColumnQueue(inputTextWithBreakChar, defaultStyle);
            fail("Expected an IndexOutOfBoundsException to be thrown, but it was not.");
        } catch (final IndexOutOfBoundsException e) {
            // Test passes: The expected exception was thrown.
            // No further assertions are needed as the original exception had no message.
        }
    }
}