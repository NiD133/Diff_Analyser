package com.itextpdf.text.xml.xmp;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Tests the functionality of the XmpWriter class, specifically its ability
 * to convert a PDF's classic info dictionary into XMP metadata.
 */
public class XmpWriterTestTest3 {

    private static final String OUTPUT_FOLDER = "./target/com/itextpdf/text/xml/xmp/";
    private static final String RESOURCE_FOLDER = "./src/test/resources/com/itextpdf/text/xml/xmp/";

    private static final String INPUT_PDF = "pdf_metadata.pdf";
    private static final String OUTPUT_AND_REFERENCE_PDF = "xmp_metadata_added.pdf";

    @Before
    public void setUp() {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    /**
     * Verifies that XMP metadata generated from a PDF's info dictionary
     * is correctly created and can be stamped onto a new PDF, matching a
     * pre-defined reference file.
     */
    @Test
    public void createXmpFromInfoDictionary_andStampToPdf_producesCorrectMetadata() throws IOException, DocumentException {
        // ARRANGE: Define file paths for input, output, and comparison.
        String sourcePdfPath = RESOURCE_FOLDER + INPUT_PDF;
        String outputPdfPath = OUTPUT_FOLDER + OUTPUT_AND_REFERENCE_PDF;
        String referencePdfPath = RESOURCE_FOLDER + OUTPUT_AND_REFERENCE_PDF;

        // ACT: Read a PDF, generate XMP from its info dictionary, and stamp it onto a new PDF.
        // Use try-with-resources to ensure all I/O streams are closed automatically.
        try (PdfReader reader = new PdfReader(sourcePdfPath);
             FileOutputStream fos = new FileOutputStream(outputPdfPath);
             PdfStamper stamper = new PdfStamper(reader, fos)) {

            // 1. Get the classic info dictionary from the source PDF.
            HashMap<String, String> info = reader.getInfo();

            // 2. Use XmpWriter to convert the info dictionary into an XMP byte array.
            // The XMP data is written to the stream when the XmpWriter is closed.
            byte[] xmpMetadata;
            try (ByteArrayOutputStream xmpOutputStream = new ByteArrayOutputStream()) {
                XmpWriter xmpWriter = new XmpWriter(xmpOutputStream, info);
                xmpWriter.close(); // This serializes the XMP data into the stream.
                xmpMetadata = xmpOutputStream.toByteArray();
            }

            // 3. Add the generated XMP metadata to the PDF via the stamper.
            stamper.setXmpMetadata(xmpMetadata);
        } // The stamper closes here, writing the final PDF to disk.

        // ASSERT: Verify that the XMP metadata in the generated file matches the reference file.
        CompareTool compareTool = new CompareTool();
        String differences = compareTool.compareXmp(outputPdfPath, referencePdfPath, true);

        Assert.assertNull(
                "Generated XMP metadata should match the reference file's metadata.",
                differences);
    }
}