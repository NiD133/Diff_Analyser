package org.apache.commons.io.output;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Tests for {@link XmlStreamWriter}.
 */
public class XmlStreamWriterTest {

    /**
     * Tests that calling getEncoding() before any data has been written throws a
     * NullPointerException. The encoding is only detected upon the first write
     * operation and is null until then.
     */
    @Test(expected = NullPointerException.class)
    public void getEncodingShouldThrowNPEWhenCalledBeforeWriting() {
        // Arrange: Create an XmlStreamWriter without writing any data.
        final OutputStream outputStream = new ByteArrayOutputStream();
        final XmlStreamWriter writer = new XmlStreamWriter(outputStream);

        // Act & Assert: Calling getEncoding() should throw a NullPointerException.
        writer.getEncoding();
    }
}