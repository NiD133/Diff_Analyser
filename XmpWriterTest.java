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
    // Test directories
    private static final String OUTPUT_DIR = "./target/com/itextpdf/text/xml/xmp/";
    private static final String CMP_DIR = "./src/test/resources/com/itextpdf/text/xml/xmp/";
    
    // Test file names
    private static final String BASIC_XMP_PDF = "xmp_metadata.pdf";
    private static final String AUTO_XMP_PDF = "xmp_metadata_automatic.pdf";
    private static final String ADDED_XMP_PDF = "xmp_metadata_added.pdf";
    private static final String UPDATED_XMP_PDF = "xmp_metadata_added2.pdf";
    private static final String DEPRECATED_XMP_PDF = "xmp_metadata_deprecated.pdf";
    private static final String SOURCE_PDF = "pdf_metadata.pdf";

    @Before
    public void setup() {
        new File(OUTPUT_DIR).mkdirs();
    }

    @Test
    public void createPdfWithManualXmpMetadata_ShouldGenerateValidXmpStream() 
            throws IOException, DocumentException, XMPException {
        // Step 1: Initialize document and writer
        Document document = new Document();
        try (FileOutputStream fos = new FileOutputStream(OUTPUT_DIR + BASIC_XMP_PDF);
             PdfWriter writer = PdfWriter.getInstance(document, fos)) {
            
            // Step 2: Create XMP metadata
            try (ByteArrayOutputStream xmpStream = new ByteArrayOutputStream();
                 XmpWriter xmp = new XmpWriter(xmpStream)) {
                
                // Add Dublin Core properties
                DublinCoreProperties.addSubject(xmp.getXmpMeta(), "Hello World");
                DublinCoreProperties.addSubject(xmp.getXmpMeta(), "XMP & Metadata");
                DublinCoreProperties.addSubject(xmp.getXmpMeta(), "Metadata");
                
                // Add PDF properties
                PdfProperties.setKeywords(xmp.getXmpMeta(), "Hello World, XMP & Metadata, Metadata");
                PdfProperties.setVersion(xmp.getXmpMeta(), "1.4");
            }
            
            // Step 3: Add content and metadata
            document.open();
            document.add(new Paragraph("Hello World"));
        }

        // Step 4: Validate generated XMP
        CompareTool ct = new CompareTool();
        Assert.assertNull(
            "Manual XMP metadata differs from expected",
            ct.compareXmp(OUTPUT_DIR + BASIC_XMP_PDF, CMP_DIR + BASIC_XMP_PDF, true)
        );
    }

    @Test
    public void createPdfWithAutomaticMetadata_ShouldGenerateValidXmp() 
            throws IOException, DocumentException {
        // Step 1: Initialize document with metadata
        Document document = new Document();
        document.addTitle("Hello World example");
        document.addSubject("This example shows how to add metadata & XMP");
        document.addKeywords("Metadata, iText, step 3");
        document.addCreator("My program using 'iText'");
        document.addAuthor("Bruno Lowagie & Paulo Soares");

        // Step 2: Create PDF with automatic XMP
        try (FileOutputStream fos = new FileOutputStream(OUTPUT_DIR + AUTO_XMP_PDF);
             PdfWriter writer = PdfWriter.getInstance(document, fos)) {
            writer.createXmpMetadata();
            
            // Step 3: Add content
            document.open();
            document.add(new Paragraph("Hello World"));
        }

        // Step 4: Validate XMP
        CompareTool ct = new CompareTool();
        Assert.assertNull(
            "Automatic XMP generation differs from expected",
            ct.compareXmp(OUTPUT_DIR + AUTO_XMP_PDF, CMP_DIR + AUTO_XMP_PDF, true)
        );
    }

    @Test
    public void addXmpToExistingPdfUsingInfoDictionary_ShouldMatchExpected() 
            throws IOException, DocumentException {
        // Step 1: Read source PDF and extract info dictionary
        try (PdfReader reader = new PdfReader(CMP_DIR + SOURCE_PDF)) {
            HashMap<String, String> info = reader.getInfo();
            
            // Step 2: Create new PDF with XMP metadata
            try (FileOutputStream fos = new FileOutputStream(OUTPUT_DIR + ADDED_XMP_PDF);
                 PdfStamper stamper = new PdfStamper(reader, fos);
                 ByteArrayOutputStream xmpStream = new ByteArrayOutputStream();
                 XmpWriter xmp = new XmpWriter(xmpStream, info)) {
                
                stamper.setXmpMetadata(xmpStream.toByteArray());
            }
        }

        // Step 3: Validate XMP
        CompareTool ct = new CompareTool();
        Assert.assertNull(
            "XMP from info dictionary differs from expected",
            ct.compareXmp(OUTPUT_DIR + ADDED_XMP_PDF, CMP_DIR + ADDED_XMP_PDF, true)
        );
    }

    @Test
    public void updatePdfXmpWithDublinCore_ShouldContainNewMetadata() 
            throws IOException, DocumentException, XMPException {
        // Step 1: Read source PDF
        try (PdfReader reader = new PdfReader(CMP_DIR + SOURCE_PDF)) {
            
            // Step 2: Update XMP metadata
            try (FileOutputStream fos = new FileOutputStream(OUTPUT_DIR + UPDATED_XMP_PDF);
                 PdfStamper stamper = new PdfStamper(reader, fos)) {
                
                stamper.createXmpMetadata();
                XmpWriter xmp = stamper.getXmpWriter();
                
                // Add Dublin Core subjects
                DublinCoreProperties.addSubject(xmp.getXmpMeta(), "Hello World");
                DublinCoreProperties.addSubject(xmp.getXmpMeta(), "XMP & Metadata");
                DublinCoreProperties.addSubject(xmp.getXmpMeta(), "Metadata");
                
                // Set PDF version
                PdfProperties.setVersion(xmp.getXmpMeta(), "1.4");
            }
        }

        // Step 3: Validate XMP
        CompareTool ct = new CompareTool();
        Assert.assertNull(
            "Updated XMP with DublinCore differs from expected",
            ct.compareXmp(OUTPUT_DIR + UPDATED_XMP_PDF, CMP_DIR + UPDATED_XMP_PDF, true)
        );
    }

    @Test
    @SuppressWarnings("deprecation")
    public void createPdfUsingDeprecatedXmpSchema_ShouldMatchLegacyOutput() 
            throws IOException, DocumentException {
        // Step 1: Initialize document
        Document document = new Document();
        
        try (FileOutputStream fos = new FileOutputStream(OUTPUT_DIR + DEPRECATED_XMP_PDF);
             PdfWriter writer = PdfWriter.getInstance(document, fos)) {
            
            // Step 2: Create XMP using deprecated schema
            try (ByteArrayOutputStream xmpStream = new ByteArrayOutputStream();
                 XmpWriter xmp = new XmpWriter(xmpStream)) {
                
                // DublinCore schema (deprecated)
                DublinCoreSchema dc = new DublinCoreSchema();
                XmpArray subject = new XmpArray(XmpArray.UNORDERED);
                subject.add("Hello World");
                subject.add("XMP & Metadata");
                subject.add("Metadata");
                dc.setProperty(DublinCoreSchema.SUBJECT, subject);
                xmp.addRdfDescription(dc);
                
                // PDF schema (deprecated)
                PdfSchema pdf = new PdfSchema();
                pdf.setProperty(PdfSchema.KEYWORDS, "Hello World, XMP & Metadata, Metadata");
                pdf.setProperty(PdfSchema.VERSION, "1.4");
                xmp.addRdfDescription(pdf);
                
                writer.setXmpMetadata(xmpStream.toByteArray());
            }
            
            // Step 3: Add content
            document.open();
            document.add(new Paragraph("Hello World"));
        }

        // Step 4: Validate against legacy output
        CompareTool ct = new CompareTool();
        Assert.assertNull(
            "Deprecated XMP schema output differs from expected",
            ct.compareXmp(OUTPUT_DIR + DEPRECATED_XMP_PDF, CMP_DIR + BASIC_XMP_PDF, true)
        );
    }
}