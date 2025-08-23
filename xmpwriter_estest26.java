package com.itextpdf.text.xml.xmp;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Unit tests for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Verifies that calling close() on an XmpWriter initialized with a null
     * OutputStream completes without throwing an exception.
     * <p>
     * This test ensures the close() method gracefully handles cases where no
     * output stream is available, preventing potential NullPointerExceptions.
     */
    @Test
    public void closeOnWriterWithNullStreamShouldNotThrowException() throws IOException {
        // Arrange: Create an XmpWriter, passing a null output stream.
        // The constructor is declared to throw IOException, so we include it in the signature,
        // though it is not expected to be thrown in this specific scenario.
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null);

        // Act: Call the close() method on the writer.
        xmpWriter.close();

        // Assert: The test passes if no exception is thrown during the 'Act' phase.
        // This confirms the method handles the null stream correctly.
    }
}