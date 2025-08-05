package com.itextpdf.text.xml.xmp;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.*;
import com.itextpdf.xmp.XMPException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Test suite for XmpWriter class to verify PDF metadata creation and manipulation.
 */
public class XmpWriterTest {

    private static final String OUTPUT_DIRECTORY = "./target/com/itextpdf/text/xml/xmp/";
    private static final String COMPARE_DIRECTORY = "./src/test/resources/com/itextpdf/text/xml/xmp/";

    @Before
    public void setUp() {
        new File(OUTPUT_DIRECTORY).mkdirs();
    }

    /**
     * Test creating a PDF with custom XMP metadata.
     */
    @Test
    public void testCreatePdfWithCustomXmpMetadata() throws IOException, DocumentException, XMPException {
        String outputFileName = "xmp_metadata.pdf";

        // Initialize document and writer
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_DIRECTORY + outputFileName));

        // Create XMP metadata
        ByteArrayOutputStream metadataStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(metadataStream);
        addCustomXmpMetadata(xmpWriter);
        xmpWriter.close();

        // Attach XMP metadata to the PDF
        writer.setXmpMetadata(metadataStream.toByteArray());

        // Add content to the PDF
        document.open();
        document.add(new Paragraph("Hello World"));
        document.close();

        // Verify the XMP metadata
        CompareTool compareTool = new CompareTool();
        Assert.assertNull(compareTool.compareXmp(OUTPUT_DIRECTORY + outputFileName, COMPARE_DIRECTORY + outputFileName, true));
    }

    /**
     * Test creating a PDF with automatic XMP metadata.
     */
    @Test
    public void testCreatePdfWithAutomaticXmpMetadata() throws IOException, DocumentException {
        String outputFileName = "xmp_metadata_automatic.pdf";

        // Initialize document and writer
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_DIRECTORY + outputFileName));

        // Add document metadata
        addAutomaticDocumentMetadata(document);

        // Create automatic XMP metadata
        writer.createXmpMetadata();

        // Add content to the PDF
        document.open();
        document.add(new Paragraph("Hello World"));
        document.close();

        // Verify the XMP metadata
        CompareTool compareTool = new CompareTool();
        Assert.assertNull(compareTool.compareXmp(OUTPUT_DIRECTORY + outputFileName, COMPARE_DIRECTORY + outputFileName, true));
    }

    /**
     * Test manipulating an existing PDF to add XMP metadata.
     */
    @Test
    public void testManipulatePdfToAddXmpMetadata() throws IOException, DocumentException {
        String outputFileName = "xmp_metadata_added.pdf";

        // Read existing PDF and prepare for stamping
        PdfReader reader = new PdfReader(COMPARE_DIRECTORY + "pdf_metadata.pdf");
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(OUTPUT_DIRECTORY + outputFileName));

        // Create and add XMP metadata
        ByteArrayOutputStream metadataStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(metadataStream, reader.getInfo());
        xmpWriter.close();
        stamper.setXmpMetadata(metadataStream.toByteArray());

        // Close stamper and reader
        stamper.close();
        reader.close();

        // Verify the XMP metadata
        CompareTool compareTool = new CompareTool();
        Assert.assertNull(compareTool.compareXmp(OUTPUT_DIRECTORY + outputFileName, COMPARE_DIRECTORY + outputFileName, true));
    }

    /**
     * Test manipulating an existing PDF to add custom XMP metadata using a different method.
     */
    @Test
    public void testManipulatePdfToAddCustomXmpMetadata() throws IOException, DocumentException, XMPException {
        String outputFileName = "xmp_metadata_added2.pdf";

        // Read existing PDF and prepare for stamping
        PdfReader reader = new PdfReader(COMPARE_DIRECTORY + "pdf_metadata.pdf");
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(OUTPUT_DIRECTORY + outputFileName));

        // Create and add custom XMP metadata
        stamper.createXmpMetadata();
        XmpWriter xmpWriter = stamper.getXmpWriter();
        addCustomXmpMetadata(xmpWriter);

        // Close stamper and reader
        stamper.close();
        reader.close();

        // Verify the XMP metadata
        CompareTool compareTool = new CompareTool();
        Assert.assertNull(compareTool.compareXmp(OUTPUT_DIRECTORY + outputFileName, COMPARE_DIRECTORY + outputFileName, true));
    }

    /**
     * Test creating a PDF with deprecated logic for adding XMP metadata.
     */
    @Test
    public void testCreatePdfWithDeprecatedXmpLogic() throws IOException, DocumentException {
        String outputFileName = "xmp_metadata_deprecated.pdf";

        // Initialize document and writer
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_DIRECTORY + outputFileName));

        // Create XMP metadata using deprecated logic
        ByteArrayOutputStream metadataStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(metadataStream);
        addDeprecatedXmpMetadata(xmpWriter);
        xmpWriter.close();

        // Attach XMP metadata to the PDF
        writer.setXmpMetadata(metadataStream.toByteArray());

        // Add content to the PDF
        document.open();
        document.add(new Paragraph("Hello World"));
        document.close();

        // Verify the XMP metadata
        CompareTool compareTool = new CompareTool();
        Assert.assertNull(compareTool.compareXmp(OUTPUT_DIRECTORY + outputFileName, COMPARE_DIRECTORY + "xmp_metadata.pdf", true));
    }

    /**
     * Adds custom XMP metadata to the XmpWriter.
     */
    private void addCustomXmpMetadata(XmpWriter xmpWriter) throws XMPException {
        DublinCoreProperties.addSubject(xmpWriter.getXmpMeta(), "Hello World");
        DublinCoreProperties.addSubject(xmpWriter.getXmpMeta(), "XMP & Metadata");
        DublinCoreProperties.addSubject(xmpWriter.getXmpMeta(), "Metadata");

        PdfProperties.setKeywords(xmpWriter.getXmpMeta(), "Hello World, XMP & Metadata, Metadata");
        PdfProperties.setVersion(xmpWriter.getXmpMeta(), "1.4");
    }

    /**
     * Adds automatic document metadata to the Document.
     */
    private void addAutomaticDocumentMetadata(Document document) {
        document.addTitle("Hello World example");
        document.addSubject("This example shows how to add metadata & XMP");
        document.addKeywords("Metadata, iText, step 3");
        document.addCreator("My program using 'iText'");
        document.addAuthor("Bruno Lowagie & Paulo Soares");
    }

    /**
     * Adds deprecated XMP metadata to the XmpWriter.
     */
    private void addDeprecatedXmpMetadata(XmpWriter xmpWriter) throws IOException {
        XmpSchema dcSchema = new DublinCoreSchema();
        XmpArray subjectArray = new XmpArray(XmpArray.UNORDERED);
        subjectArray.add("Hello World");
        subjectArray.add("XMP & Metadata");
        subjectArray.add("Metadata");
        dcSchema.setProperty(DublinCoreSchema.SUBJECT, subjectArray);
        xmpWriter.addRdfDescription(dcSchema.getXmlns(), dcSchema.toString());

        PdfSchema pdfSchema = new PdfSchema();
        pdfSchema.setProperty(PdfSchema.KEYWORDS, "Hello World, XMP & Metadata, Metadata");
        pdfSchema.setProperty(PdfSchema.VERSION, "1.4");
        xmpWriter.addRdfDescription(pdfSchema);
    }
}