package com.itextpdf.text.xml.xmp;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.xmp.XMPException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Test suite for the XmpWriter class.
 */
public class XmpWriterTest {

    private static final String OUT_FOLDER = "./target/com/itextpdf/text/xml/xmp/";
    private static final String CMP_FOLDER = "./src/test/resources/com/itextpdf/text/xml/xmp/";

    private static final String INPUT_PDF = "pdf_metadata.pdf";
    private static final String OUTPUT_AND_COMPARISON_PDF = "xmp_metadata_added2.pdf";

    @Before
    public void setUp() {
        new File(OUT_FOLDER).mkdirs();
    }

    /**
     * Tests the addition of multiple XMP subject entries and a PDF version property
     * to an existing PDF's metadata.
     *
     * @throws IOException       if an I/O error occurs.
     * @throws DocumentException if a PDF processing error occurs.
     * @throws XMPException      if an XMP processing error occurs.
     */
    @Test
    public void testAddMultipleDublinCoreSubjectsAndPdfVersion() throws IOException, DocumentException, XMPException {
        // Arrange
        String outputPdfPath = OUT_FOLDER + OUTPUT_AND_COMPARISON_PDF;
        String comparisonPdfPath = CMP_FOLDER + OUTPUT_AND_COMPARISON_PDF;
        String inputPdfPath = CMP_FOLDER + INPUT_PDF;

        // Use try-with-resources for automatic resource management. [9, 10]
        try (PdfReader reader = new PdfReader(inputPdfPath);
             PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputPdfPath))) {

            // Act
            stamper.createXmpMetadata();
            XmpWriter xmpWriter = stamper.getXmpWriter();

            // Add multiple subjects to the Dublin Core schema
            DublinCoreProperties.addSubject(xmpWriter.getXmpMeta(), "Hello World");
            DublinCoreProperties.addSubject(xmpWriter.getXmpMeta(), "XMP & Metadata");
            DublinCoreProperties.addSubject(xmpWriter.getXmpMeta(), "Metadata");

            // Set the PDF version in the PDF schema
            PdfProperties.setVersion(xmpWriter.getXmpMeta(), "1.4");

        } // The stamper and reader are automatically closed here.

        // Assert
        CompareTool compareTool = new CompareTool();
        String comparisonResult = compareTool.compareXmp(outputPdfPath, comparisonPdfPath, true);

        Assert.assertNull(
                "The XMP metadata of the modified PDF should match the reference file, indicating no differences.",
                comparisonResult
        );
    }
}