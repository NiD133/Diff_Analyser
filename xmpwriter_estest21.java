package com.itextpdf.text.xml.xmp;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Verifies that the XmpWriter can be successfully instantiated using a PdfDictionary
     * even when the provided OutputStream is null.
     */
    @Test
    public void constructorWithPdfDictionary_shouldCreateInstanceSuccessfully() throws DocumentException, IOException {
        // Arrange: Create a PdfDictionary, which is a required argument for the constructor.
        // The easiest way to get a valid PdfDictionary is from a PdfWriter instance.
        // Note: PdfWriter.getInstance allows a null OutputStream for this setup phase.
        PdfDocument document = new PdfDocument();
        PdfWriter writer = PdfWriter.getInstance(document, (OutputStream) null);
        PdfDictionary infoDictionary = writer.getInfo();

        // Act: Instantiate the XmpWriter with a null output stream and the info dictionary.
        // The core behavior being tested is that this constructor call completes without error.
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null, infoDictionary);

        // Assert: Confirm that the XmpWriter object was created.
        assertNotNull("The XmpWriter instance should not be null after construction.", xmpWriter);
    }
}