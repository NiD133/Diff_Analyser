package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.evosuite.runtime.mock.java.io.MockPrintStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Test suite for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that writing a PdfDictionary to a writer does not have the side effect
     * of changing the writer's current page number. This ensures that the toPdf()
     * method only serializes the dictionary object without altering the writer's state.
     * @throws IOException if an I/O error occurs during the test.
     */
    @Test
    public void toPdfShouldNotAlterWriterPageNumber() throws IOException {
        // Arrange: Create a PdfInfo object (a subclass of PdfDictionary) and a PDF writer.
        PdfDocument.PdfInfo pdfInfo = new PdfDocument.PdfInfo();
        OutputStream mockOutputStream = new MockPrintStream("dummy-output.pdf");
        
        // FdfWriter.Wrt is a concrete implementation of a PdfWriter, suitable for this test.
        FdfWriter.Wrt writer = new FdfWriter.Wrt(mockOutputStream, new FdfWriter());

        int initialPageNumber = writer.getCurrentPageNumber();
        assertEquals("Precondition: Writer's initial page number should be 1.", 1, initialPageNumber);

        // Act: Write the dictionary to the output stream using the writer.
        pdfInfo.toPdf(writer, mockOutputStream);

        // Assert: The writer's page number should remain unchanged.
        int finalPageNumber = writer.getCurrentPageNumber();
        assertEquals("Writing a dictionary must not change the writer's page number.",
                initialPageNumber, finalPageNumber);
    }
}