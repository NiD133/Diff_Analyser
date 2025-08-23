package org.apache.commons.io.output;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Unit tests for the {@link XmlStreamWriter} class, focusing on invalid argument handling.
 */
public class XmlStreamWriterTest {

    /**
     * Verifies that the write(char[], int, int) method throws a NullPointerException
     * when the input character array is null.
     */
    @Test(expected = NullPointerException.class)
    public void writeWithNullBufferThrowsNullPointerException() throws IOException {
        // Arrange: Create an XmlStreamWriter with a dummy output stream.
        // The stream's content is irrelevant as the method should fail before writing.
        final XmlStreamWriter writer = new XmlStreamWriter(new ByteArrayOutputStream());

        // Act: Attempt to write from a null buffer.
        // The offset and length arguments are arbitrary since the null check on the buffer
        // should occur first.
        writer.write(null, 0, 0);

        // Assert: The @Test(expected) annotation handles the exception verification.
        // The test will fail if a NullPointerException is not thrown.
    }
}