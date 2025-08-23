package org.apache.commons.io.output;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link XmlStreamWriter}.
 * This class focuses on behavior after the writer has been closed.
 */
public class XmlStreamWriterTest {

    /**
     * Verifies that attempting to write to an XmlStreamWriter after it has been
     * closed throws an IOException.
     */
    @Test
    public void writeToClosedWriterThrowsIOException() throws IOException {
        // Arrange: Create a writer, write some initial data, and then close it.
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final XmlStreamWriter writer = new XmlStreamWriter(outputStream);
        final char[] data = new char[]{'a', 'b', 'c'};

        writer.write(data);
        writer.close();

        // Act & Assert: Verify that a subsequent write operation throws an IOException.
        assertThrows(IOException.class, () -> writer.write(data));
    }
}