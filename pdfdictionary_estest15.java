package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.IOException;
import java.io.PipedOutputStream;

/**
 * Test suite for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that the toPdf() method correctly propagates an IOException
     * when the provided OutputStream fails during a write operation.
     * <p>
     * This scenario is simulated by attempting to write to an unconnected
     * PipedOutputStream, which is guaranteed to throw an IOException.
     */
    @Test(expected = IOException.class)
    public void toPdf_shouldThrowIOException_whenOutputStreamFails() throws IOException {
        // Arrange: Create a PdfDictionary instance (using PdfInfo, a subclass)
        // and a stream that is guaranteed to fail on write.
        PdfDictionary pdfDictionary = new PdfDocument.PdfInfo();
        PipedOutputStream faultyOutputStream = new PipedOutputStream();

        // The toPdf method requires a PdfWriter instance. We can create a dummy
        // writer that uses our faulty stream to satisfy the method signature.
        PdfWriter writer = new FdfWriter(faultyOutputStream).wrt;

        // Act: Attempt to write the dictionary to the faulty stream.
        // This action is expected to throw an IOException.
        pdfDictionary.toPdf(writer, faultyOutputStream);

        // Assert: The test succeeds if an IOException is thrown, as specified
        // by the @Test(expected = IOException.class) annotation.
    }
}