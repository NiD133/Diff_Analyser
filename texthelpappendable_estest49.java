package org.apache.commons.cli.help;

import static org.junit.Assert.assertThrows;

import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import org.junit.Test;

/**
 * Contains tests for the {@link TextHelpAppendable} class, focusing on its
 * behavior with different underlying {@link Appendable} implementations.
 */
public class TextHelpAppendable_ESTestTest49 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Verifies that attempting to write to a TextHelpAppendable instance that
     * was initialized with a read-only buffer correctly throws a
     * ReadOnlyBufferException.
     */
    @Test
    public void appendTitleShouldThrowExceptionWhenOutputBufferIsReadOnly() {
        // Arrange: Create a TextHelpAppendable with a read-only output target.
        // The static method CharBuffer.wrap(String) conveniently creates a read-only buffer.
        CharBuffer readOnlyOutputBuffer = CharBuffer.wrap("");
        TextHelpAppendable helpAppendable = new TextHelpAppendable(readOnlyOutputBuffer);
        String titleToAppend = "This title will fail to be appended";

        // Act & Assert: Verify that the append operation throws the expected exception.
        // The appendTitle method will attempt to write to the underlying read-only buffer,
        // which must result in a ReadOnlyBufferException.
        assertThrows(ReadOnlyBufferException.class, () -> {
            helpAppendable.appendTitle(titleToAppend);
        });
    }
}