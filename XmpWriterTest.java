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

public class XmpWriterTest {

    public static final String OUT_FOLDER = "./target/com/itextpdf/text/xml/xmp/";
    public static final String CMP_FOLDER = "./src/test/resources/com/itextpdf/text/xml/xmp/";

    private static final String SAMPLE_PARAGRAPH = "Hello World";
    private static final String[] SUBJECTS = { "Hello World", "XMP & Metadata", "Metadata" };
    private static final String KEYWORDS = "Hello World, XMP & Metadata, Metadata";
    private static final String PDF_VERSION = "1.4";
    private static final String SOURCE_PDF_WITH_INFO = "pdf_metadata.pdf";

    @Before
    public void init() {
        new File(OUT_FOLDER).mkdirs();
    }

    /**
     * Writes XMP explicitly via XmpWriter and verifies it matches the reference.
     */
    @Test
    public void explicitXmpMetadataIsWrittenAndMatchesReference() throws IOException, DocumentException, XMPException {
        String fileName = "xmp_metadata.pdf";

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUT_FOLDER + fileName));

        ByteArrayOutputStream xmpBytes = new ByteArrayOutputStream();
        XmpWriter xmp = new XmpWriter(xmpBytes);

        addStandardSubjects(xmp);
        PdfProperties.setKeywords(xmp.getXmpMeta(), KEYWORDS);
        PdfProperties.setVersion(xmp.getXmpMeta(), PDF_VERSION);
        xmp.close();

        writer.setXmpMetadata(xmpBytes.toByteArray());

        document.open();
        addSampleContent(document);
        document.close();

        assertXmpEquals(fileName, fileName);
    }

    /**
     * Uses writer.createXmpMetadata() to auto-generate XMP from document info.
     */
    @Test
    public void automaticXmpMetadataMatchesReference() throws IOException, DocumentException {
        String fileName = "xmp_metadata_automatic.pdf";

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUT_FOLDER + fileName));

        document.addTitle("Hello World example");
        document.addSubject("This example shows how to add metadata & XMP");
        document.addKeywords("Metadata, iText, step 3");
        document.addCreator("My program using 'iText'");
        document.addAuthor("Bruno Lowagie & Paulo Soares");
        writer.createXmpMetadata();

        document.open();
        addSampleContent(document);
        document.close();

        assertXmpEquals(fileName, fileName);
    }

    /**
     * Reads an existing PDF's document info and writes equivalent XMP into a stamped output.
     */
    @Test
    public void stampingAddsXmpFromDocInfoAndMatchesReference() throws IOException, DocumentException {
        String fileName = "xmp_metadata_added.pdf";

        PdfReader reader = new PdfReader(CMP_FOLDER + SOURCE_PDF_WITH_INFO);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(OUT_FOLDER + fileName));

        HashMap<String, String> info = reader.getInfo();

        ByteArrayOutputStream xmpBytes = new ByteArrayOutputStream();
        XmpWriter xmp = new XmpWriter(xmpBytes, info);
        xmp.close();

        stamper.setXmpMetadata(xmpBytes.toByteArray());

        stamper.close();
        reader.close();

        assertXmpEquals(fileName, fileName);
    }

    /**
     * Creates XMP on a stamper and enriches it with subjects and version info.
     */
    @Test
    public void stampingCreatesAndEnrichesXmpAndMatchesReference() throws IOException, DocumentException, XMPException {
        String fileName = "xmp_metadata_added2.pdf";

        PdfReader reader = new PdfReader(CMP_FOLDER + SOURCE_PDF_WITH_INFO);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(OUT_FOLDER + fileName));

        stamper.createXmpMetadata();
        XmpWriter xmp = stamper.getXmpWriter();

        addStandardSubjects(xmp);
        PdfProperties.setVersion(xmp.getXmpMeta(), PDF_VERSION);

        stamper.close();
        reader.close();

        assertXmpEquals(fileName, fileName);
    }

    /**
     * Verifies deprecated XMP-creation logic still produces the same XMP as the explicit approach.
     */
    @Test
    public void deprecatedXmpCreationStillMatchesReference() throws IOException, DocumentException {
        String fileName = "xmp_metadata_deprecated.pdf";

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUT_FOLDER + fileName));

        ByteArrayOutputStream xmpBytes = new ByteArrayOutputStream();
        XmpWriter xmp = new XmpWriter(xmpBytes);

        // Build XMP using deprecated APIs to ensure backward compatibility
        XmpSchema dc = new com.itextpdf.text.xml.xmp.DublinCoreSchema();
        XmpArray subject = new XmpArray(XmpArray.UNORDERED);
        for (String s : SUBJECTS) {
            subject.add(s);
        }
        dc.setProperty(DublinCoreSchema.SUBJECT, subject);
        xmp.addRdfDescription(dc.getXmlns(), dc.toString());

        PdfSchema pdf = new PdfSchema();
        pdf.setProperty(PdfSchema.KEYWORDS, KEYWORDS);
        pdf.setProperty(PdfSchema.VERSION, PDF_VERSION);
        xmp.addRdfDescription(pdf);

        xmp.close();

        writer.setXmpMetadata(xmpBytes.toByteArray());

        document.open();
        addSampleContent(document);
        document.close();

        // Compare with the non-deprecated explicit reference file
        assertXmpEquals(fileName, "xmp_metadata.pdf");
    }

    // ---------- Helper methods ----------

    private void addStandardSubjects(XmpWriter xmp) throws XMPException {
        for (String subject : SUBJECTS) {
            DublinCoreProperties.addSubject(xmp.getXmpMeta(), subject);
        }
    }

    private void addSampleContent(Document document) throws DocumentException {
        document.add(new Paragraph(SAMPLE_PARAGRAPH));
    }

    private void assertXmpEquals(String actualFileName, String expectedFileName) throws IOException {
        CompareTool ct = new CompareTool();
        String actual = OUT_FOLDER + actualFileName;
        String expected = CMP_FOLDER + expectedFileName;
        Assert.assertNull(ct.compareXmp(actual, expected, true));
    }
}