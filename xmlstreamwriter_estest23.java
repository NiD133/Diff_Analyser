package org.apache.commons.io.output;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Tests for {@link XmlStreamWriter}.
 */
public class XmlStreamWriterTest {

    // The internal buffer size used by XmlStreamWriter to detect encoding.
    private static final int XML_PROLOG_BUFFER_SIZE = 4096;

    /**
     * Tests that the writer flushes its buffer using the default encoding (UTF-8)
     * when the amount of written data exceeds the internal buffer size before an
     * XML encoding can be determined.
     */
    @Test
    public void testPrologBufferOverflowUsesDefaultEncoding() throws IOException {
        // Arrange: Set up a writer and prepare data that will overflow its internal prolog buffer.
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // The writer uses UTF-8 as the default encoding if not specified.
        final XmlStreamWriter writer = new XmlStreamWriter(outputStream);

        final String incompleteXmlProlog = "<?xml";
        // Create a large chunk of data that, when combined with the prolog, exceeds the buffer.
        final String largeData = "a".repeat(XML_PROLOG_BUFFER_SIZE);

        // Act:
        // 1. Write an incomplete XML prolog. This will be held in the internal buffer.
        writer.write(incompleteXmlProlog);

        // Verify that nothing has been written to the underlying stream yet.
        assertEquals("Data should be buffered before the buffer is full", 0, outputStream.size());

        // 2. Write the large data chunk. This causes the internal buffer to overflow.
        // The writer should now give up on encoding detection, use the default encoding,
        // and flush all buffered content to the output stream.
        writer.write(largeData);
        writer.close(); // Closing ensures all data is flushed.

        // Assert: Verify that the writer used the default encoding and wrote all data correctly.
        final String expectedContent = incompleteXmlProlog + largeData;

        // The writer should have defaulted to UTF-8 after the buffer overflowed.
        assertEquals("UTF-8", writer.getEncoding());

        // The output stream should contain the complete, correctly encoded string.
        assertEquals(expectedContent, outputStream.toString(StandardCharsets.UTF_8.name()));

        // Since UTF-8 uses one byte for ASCII characters, the size should match the string length.
        assertEquals(expectedContent.length(), outputStream.size());
    }
}