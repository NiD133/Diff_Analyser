package com.itextpdf.text.xml.xmp;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.xmp.XMPException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class XmpWriterTestTest1 {

    // Use descriptive constants for file paths to improve clarity and maintainability.
    private static final String OUTPUT_DIRECTORY = "./target/com/itextpdf/text/xml/xmp/";
    private static final String COMPARISON_DIRECTORY = "./src/test/resources/com/itextpdf/text/xml/xmp/";
    private static final String PDF_FILENAME = "xmp_metadata.pdf";

    private static final String GENERATED_PDF_PATH = OUTPUT_DIRECTORY + PDF_FILENAME;
    private static final String EXPECTED_PDF_PATH = COMPARISON_DIRECTORY + PDF_FILENAME;

    @Before
    public void setUp() {
        new File(OUTPUT_DIRECTORY).mkdirs();
    }

    /**
     * Tests the creation of a PDF with specific XMP metadata, including
     * Dublin Core subjects and PDF-specific properties like keywords and version.
     * The test verifies that the generated XMP block matches a pre-defined reference file.
     */
    @Test
    public void shouldCreatePdfWithCustomXmpMetadata() throws IOException, DocumentException, XMPException {
        // --- ARRANGE ---
        // 1. Create the XMP metadata in an in-memory byte array.
        ByteArrayOutputStream xmpOutputStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(xmpOutputStream);

        // Define and add the metadata properties.
        String[] subjects = {"Hello World", "XMP & Metadata", "Metadata"};
        for (String subject : subjects) {
            DublinCoreProperties.addSubject(xmpWriter.getXmpMeta(), subject);
        }
        // Note: Keywords are often a string representation of the subjects.
        PdfProperties.setKeywords(xmpWriter.getXmpMeta(), "Hello World, XMP & Metadata, Metadata");
        PdfProperties.setVersion(xmpWriter.getXmpMeta(), "1.4");

        // Finalize the XMP metadata stream and get the resulting bytes.
        xmpWriter.close();
        byte[] xmpMetadataBytes = xmpOutputStream.toByteArray();


        // --- ACT ---
        // 2. Create a PDF document and embed the prepared XMP metadata.
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(GENERATED_PDF_PATH));
        writer.setXmpMetadata(xmpMetadataBytes);

        document.open();
        document.add(new Paragraph("Hello World"));
        document.close();


        // --- ASSERT ---
        // 3. Compare the XMP of the generated PDF with a reference file.
        CompareTool compareTool = new CompareTool();
        // The compareXmp method returns null if the XMP sections are identical, and an error message otherwise.
        String comparisonResult = compareTool.compareXmp(GENERATED_PDF_PATH, EXPECTED_PDF_PATH, true);

        Assert.assertNull(
            "The XMP metadata of the generated file should match the reference file. Differences found: " + comparisonResult,
            comparisonResult
        );
    }
}