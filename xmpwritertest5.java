package com.itextpdf.text.xml.xmp;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Tests the deprecated functionality of the {@link XmpWriter} class,
 * specifically focusing on the addRdfDescription methods.
 */
public class XmpWriterDeprecatedApiTest {

    private static final String OUTPUT_DIRECTORY = "./target/com/itextpdf/text/xml/xmp/";
    private static final String COMPARISON_DIRECTORY = "./src/test/resources/com/itextpdf/text/xml/xmp/";

    @Before
    public void setUp() {
        new File(OUTPUT_DIRECTORY).mkdirs();
    }

    /**
     * Verifies that XMP metadata created using the deprecated {@link XmpWriter#addRdfDescription}
     * methods is correctly serialized into a PDF file.
     */
    @Test
    public void addRdfDescription_withMultipleSchemas_shouldGenerateCorrectXmp() throws IOException, DocumentException {
        // Arrange
        String outputFileName = "xmp_metadata_deprecated.pdf";
        String outputFilePath = OUTPUT_DIRECTORY + outputFileName;
        String referenceFilePath = COMPARISON_DIRECTORY + "xmp_metadata.pdf";

        // Define metadata values to be added to the XMP schemas
        final String subject1 = "Hello World";
        final String subject2 = "XMP & Metadata";
        final String subject3 = "Metadata";
        final String keywords = String.join(", ", subject1, subject2, subject3);
        final String pdfVersion = "1.4";

        // Create XMP data using the deprecated methods.
        // This test ensures backward compatibility of this logic.
        byte[] xmpBytes;
        try (ByteArrayOutputStream xmpOutputStream = new ByteArrayOutputStream()) {
            XmpWriter xmpWriter = new XmpWriter(xmpOutputStream);

            // 1. Create and add Dublin Core schema with an unordered array
            DublinCoreSchema dcSchema = new DublinCoreSchema();
            XmpArray subjectArray = new XmpArray(XmpArray.UNORDERED);
            subjectArray.add(subject1);
            subjectArray.add(subject2);
            subjectArray.add(subject3);
            dcSchema.setProperty(DublinCoreSchema.SUBJECT, subjectArray);
            xmpWriter.addRdfDescription(dcSchema.getXmlns(), dcSchema.toString());

            // 2. Create and add PDF schema with simple properties
            PdfSchema pdfSchema = new PdfSchema();
            pdfSchema.setProperty(PdfSchema.KEYWORDS, keywords);
            pdfSchema.setProperty(PdfSchema.VERSION, pdfVersion);
            xmpWriter.addRdfDescription(pdfSchema);

            xmpWriter.close();
            xmpBytes = xmpOutputStream.toByteArray();
        }

        // Act: Create a PDF document and embed the generated XMP metadata
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputFilePath));
        writer.setXmpMetadata(xmpBytes);
        document.open();
        document.add(new Paragraph("Hello World"));
        document.close();

        // Assert: Verify the generated XMP data matches the reference file
        CompareTool compareTool = new CompareTool();
        String difference = compareTool.compareXmp(outputFilePath, referenceFilePath, true);
        Assert.assertNull("The generated XMP metadata should match the reference file, indicating no differences.", difference);
    }
}