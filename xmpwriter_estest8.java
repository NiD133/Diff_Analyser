package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPException;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link XmpWriter} class, focusing on exception handling during serialization.
 */
public class XmpWriterTest {

    /**
     * Verifies that the {@code serialize} method correctly propagates I/O errors
     * by throwing an {@code XMPException} when the underlying output stream is unwritable.
     *
     * @throws IOException if the XmpWriter constructor fails, which is not expected in this test.
     */
    @Test
    public void serializeShouldThrowExceptionWhenOutputStreamIsUnwritable() throws IOException {
        // Arrange: Create an XmpWriter with an output stream that is guaranteed to fail on write.
        // An unconnected PipedOutputStream will throw an IOException when any write operation is attempted.
        PipedOutputStream unwritableStream = new PipedOutputStream();
        XmpWriter xmpWriter = new XmpWriter(unwritableStream);

        // Act & Assert: Attempt to serialize and verify that the expected exception is thrown.
        try {
            xmpWriter.serialize(unwritableStream);
            fail("Expected XMPException to be thrown because the output stream is not connected.");
        } catch (XMPException e) {
            // The underlying IOException from the failed stream operation should be
            // wrapped as the cause of the XMPException.
            Throwable cause = e.getCause();
            assertNotNull("The XMPException should have an underlying cause.", cause);
            assertTrue("The cause should be an instance of IOException.", cause instanceof IOException);
            assertEquals("The IOException message should indicate the reason for failure.",
                    "Pipe not connected", cause.getMessage());
        }
    }
}