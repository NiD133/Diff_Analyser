package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;

import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;

/**
 * Tests the behavior of the Entities.escape() method when writing to a read-only buffer.
 */
public class Entities_ESTestTest14 extends Entities_ESTest_scaffolding {

    /**
     * Verifies that calling {@link Entities#escape(QuietAppendable, String, Document.OutputSettings, int)}
     * with an appendable that wraps a read-only buffer correctly throws a {@link ReadOnlyBufferException}.
     */
    @Test(expected = ReadOnlyBufferException.class)
    public void escapeToReadOnlyBufferThrowsException() {
        // Arrange: Create a read-only CharBuffer. CharBuffer.wrap(CharSequence) produces one.
        String content = "http://www.w3.org/000/svg";
        CharBuffer readOnlyBuffer = CharBuffer.wrap(content);
        QuietAppendable readOnlyAppendable = QuietAppendable.wrap(readOnlyBuffer);
        Document.OutputSettings settings = new Document.OutputSettings();
        int noEscapeOptions = 0;

        // Act & Assert: Attempting to escape the string into the read-only appendable
        // should throw a ReadOnlyBufferException. The assertion is handled by the
        // 'expected' parameter of the @Test annotation.
        Entities.escape(readOnlyAppendable, content, settings, noEscapeOptions);
    }
}