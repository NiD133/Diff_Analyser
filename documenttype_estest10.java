package org.jsoup.nodes;

import org.junit.Test;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.nodes.Document.OutputSettings;

/**
 * Test suite for {@link DocumentType}.
 * This class focuses on improving a specific generated test case for clarity.
 */
public class DocumentTypeTest {

    /**
     * Verifies that writing a DocumentType to an Appendable with insufficient
     * capacity throws a BufferOverflowException.
     */
    @Test(expected = BufferOverflowException.class)
    public void outerHtmlHeadThrowsBufferOverflowWhenAppendableIsTooSmall() {
        // Arrange: Create a standard DocumentType. Its outer HTML representation will be
        // significantly longer than the small buffer we create below.
        DocumentType docType = new DocumentType("html",
            "-//W3C//DTD XHTML 1.0 Strict//EN",
            "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");

        // Arrange: Create an Appendable with a very small, fixed capacity.
        final int bufferCapacity = 4;
        CharBuffer smallBuffer = CharBuffer.allocate(bufferCapacity);
        QuietAppendable appendable = QuietAppendable.wrap(smallBuffer);
        
        OutputSettings settings = new OutputSettings();

        // Act: Attempt to write the doctype's HTML to the undersized appendable.
        // The @Test(expected) annotation will assert that a BufferOverflowException is thrown.
        docType.outerHtmlHead(appendable, settings);
    }
}