package com.itextpdf.text.xml.xmp;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Tests the automatic creation of XMP metadata from the standard PDF Info dictionary.
 */
public class XmpWriterAutomaticCreationTest {

    private static final String OUT_FOLDER = "./target/test-output/com/itextpdf/text/xml/xmp/";
    private static final String CMP_FOLDER = "./src/test/resources/com/itextpdf/text/xml/xmp/";

    private static final String FILE_NAME = "xmp_metadata_automatic.pdf";
    private static final String DEST_PATH = OUT_FOLDER + FILE_NAME;
    private static final String CMP_PATH = CMP_FOLDER + FILE_NAME;

    @Before
    public void setUp() {
        new File(OUT_FOLDER).mkdirs();
    }

    /**
     * Verifies that when createXmpMetadata() is called, the document's Info dictionary
     * properties (Title, Author, etc.) are correctly converted into an XMP stream in the
     * resulting PDF.
     */
    @Test
    public void testAutomaticXmpCreationFromDocumentInfo() throws IOException, DocumentException {
        // Arrange
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DEST_PATH));

        // Add standard document metadata, which populates the PDF Info dictionary.
        document.addTitle("Hello World example");
        document.addSubject("This example shows how to add metadata & XMP");
        document.addKeywords("Metadata, iText, step 3");
        document.addCreator("My program using 'iText'");
        document.addAuthor("Bruno Lowagie & Paulo Soares");

        // This method call instructs the writer to automatically generate
        // an XMP stream from the Info dictionary metadata upon closing the document.
        writer.createXmpMetadata();

        // Act
        document.open();
        document.add(new Paragraph("Hello World"));
        document.close();

        // Assert
        CompareTool compareTool = new CompareTool();
        String comparisonResult = compareTool.compareXmp(DEST_PATH, CMP_PATH, true);

        Assert.assertNull(
            "The generated XMP metadata should match the reference file. Differences found: " + comparisonResult,
            comparisonResult
        );
    }
}