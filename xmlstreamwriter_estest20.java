package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import org.junit.Test;

/**
 * Contains tests for the {@link XmlStreamWriter} class.
 */
public class XmlStreamWriterTest {

    /**
     * Tests that writing a character array and flushing the writer correctly
     * pushes the data to the underlying output stream. The test uses the default
     * UTF-8 encoding, where single-byte ASCII characters are expected.
     */
    @Test
    public void writeAndFlushShouldPushDataToUnderlyingStream() throws IOException {
        // Arrange: Set up a PipedInputStream to capture the output from the writer.
        // The PipedOutputStream and XmlStreamWriter will be closed automatically by try-with-resources.
        final PipedInputStream inputStream = new PipedInputStream();
        try (final PipedOutputStream outputStream = new PipedOutputStream(inputStream);
             final XmlStreamWriter xmlWriter = new XmlStreamWriter(outputStream)) {

            final char[] dataToWrite = "test-data".toCharArray();
            // In the default UTF-8 encoding, each ASCII character is one byte.
            final int expectedByteCount = dataToWrite.length;

            // Act: Write the character data and flush the stream to ensure it's sent.
            xmlWriter.write(dataToWrite);
            xmlWriter.flush();

            // Assert: Verify that the correct number of bytes were written to the pipe.
            assertEquals("The number of available bytes should match the number of characters written.",
                    expectedByteCount, inputStream.available());
        }
    }
}