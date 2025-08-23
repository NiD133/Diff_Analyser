package org.apache.commons.io.output;

import org.junit.Test;

import java.io.IOException;
import java.io.PipedOutputStream;

/**
 * Tests for {@link XmlStreamWriter} focusing on its behavior with problematic underlying streams.
 */
public class XmlStreamWriterTest {

    /**
     * Tests that XmlStreamWriter propagates an IOException from the underlying
     * stream when close() is called. The close() operation triggers a flush,
     * which attempts to write buffered data.
     */
    @Test(expected = IOException.class)
    public void testCloseThrowsIOExceptionWhenUnderlyingStreamFails() throws IOException {
        // Arrange: Create a writer with an output stream that is guaranteed to fail on write.
        // A PipedOutputStream that is not connected to a PipedInputStream will throw
        // an IOException ("Pipe not connected") on the first write attempt.
        PipedOutputStream unconnectedOutputStream = new PipedOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(unconnectedOutputStream);

        // Act: Write some data. This is buffered internally by XmlStreamWriter to
        // detect the XML encoding and is not immediately written to the underlying stream.
        writer.write("<test/>");

        // Assert: Closing the writer will flush the buffer, attempt to write to the
        // failing stream, and throw the expected IOException. The @Test(expected)
        // annotation handles the verification.
        writer.close();
    }
}