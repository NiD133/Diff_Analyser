package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.Collections;
import java.util.List;

/**
 * Test suite for {@link TextHelpAppendable}.
 * This class contains the refactored test case.
 */
public class TextHelpAppendable_ESTestTest65 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Verifies that appendList throws a ReadOnlyBufferException when the underlying
     * Appendable is a read-only buffer. This ensures that exceptions from the
     * underlying Appendable are correctly propagated.
     *
     * @throws IOException if an I/O error occurs, though not expected in this test.
     */
    @Test(timeout = 4000, expected = ReadOnlyBufferException.class)
    public void testAppendListThrowsExceptionWhenUsingReadOnlyBuffer() throws IOException {
        // Arrange: Create a TextHelpAppendable with a read-only buffer as its output.
        CharBuffer readOnlyBuffer = CharBuffer.wrap("").asReadOnlyBuffer();
        TextHelpAppendable textHelpAppendable = new TextHelpAppendable(readOnlyBuffer);
        List<CharSequence> itemsToAppend = Collections.singletonList("An item to append");

        // Act & Assert: Attempting to append to the read-only buffer should throw
        // a ReadOnlyBufferException. The 'expected' parameter of the @Test annotation
        // handles the assertion.
        textHelpAppendable.appendList(false, itemsToAppend);
    }
}