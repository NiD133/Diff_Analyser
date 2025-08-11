/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS
    
    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/
    
    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Notices, as required under
    Section 5 of the GNU Affero General Public License.
    
    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.
    
    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.
    
    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
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
 * Test suite for XmpWriter functionality.
 * Tests various scenarios of creating and manipulating XMP metadata in PDF documents.
 */
public class XmpWriterTest {
    
    private static final String OUTPUT_FOLDER = "./target/com/itextpdf/text/xml/xmp/";
    private static final String EXPECTED_FOLDER = "./src/test/resources/com/itextpdf/text/xml/xmp/";
    
    // Test data constants
    private static final String SAMPLE_TEXT = "Hello World";
    private static final String[] TEST_SUBJECTS = {"Hello World", "XMP & Metadata", "Metadata"};
    private static final String TEST_KEYWORDS = "Hello World, XMP & Metadata, Metadata";
    private static final String PDF_VERSION = "1.4";

    @Before
    public void setupOutputDirectory() {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    @Test
    public void shouldCreatePdfWithManualXmpMetadata() throws IOException, DocumentException, XMPException {
        // Given
        String outputFileName = "xmp_metadata.pdf";
        
        // When
        createPdfWithManualXmpMetadata(outputFileName);
        
        // Then
        assertXmpMetadataMatches(outputFileName, "xmp_metadata.pdf");
    }

    @Test
    public void shouldCreatePdfWithAutomaticXmpMetadata() throws IOException, DocumentException {
        // Given
        String outputFileName = "xmp_metadata_automatic.pdf";
        
        // When
        createPdfWithAutomaticXmpMetadata(outputFileName);
        
        // Then
        assertXmpMetadataMatches(outputFileName, outputFileName);
    }

    @Test
    public void shouldAddXmpMetadataToExistingPdf() throws IOException, DocumentException {
        // Given
        String inputFileName = "pdf_metadata.pdf";
        String outputFileName = "xmp_metadata_added.pdf";
        
        // When
        addXmpMetadataToExistingPdf(inputFileName, outputFileName);
        
        // Then
        assertXmpMetadataMatches(outputFileName, outputFileName);
    }

    @Test
    public void shouldEnhanceExistingPdfWithAdditionalXmpMetadata() throws IOException, DocumentException, XMPException {
        // Given
        String inputFileName = "pdf_metadata.pdf";
        String outputFileName = "xmp_metadata_added2.pdf";
        
        // When
        enhanceExistingPdfWithXmpMetadata(inputFileName, outputFileName);
        
        // Then
        assertXmpMetadataMatches(outputFileName, outputFileName);
    }

    @Test
    public void shouldCreatePdfUsingDeprecatedXmpApi() throws IOException, DocumentException {
        // Given
        String outputFileName = "xmp_metadata_deprecated.pdf";
        String expectedFileName = "xmp_metadata.pdf";
        
        // When
        createPdfUsingDeprecatedXmpApi(outputFileName);
        
        // Then
        assertXmpMetadataMatches(outputFileName, expectedFileName);
    }

    // Helper methods for better test organization and readability

    private void createPdfWithManualXmpMetadata(String fileName) throws IOException, DocumentException, XMPException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_FOLDER + fileName));
        
        // Create XMP metadata manually
        ByteArrayOutputStream xmpOutputStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(xmpOutputStream);
        
        addTestSubjectsToXmp(xmpWriter);
        addTestPropertiesToXmp(xmpWriter);
        
        xmpWriter.close();
        writer.setXmpMetadata(xmpOutputStream.toByteArray());
        
        writeDocumentContent(document);
    }

    private void createPdfWithAutomaticXmpMetadata(String fileName) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_FOLDER + fileName));
        
        // Add metadata using Document methods (automatic XMP generation)
        addDocumentMetadata(document);
        writer.createXmpMetadata();
        
        writeDocumentContent(document);
    }

    private void addXmpMetadataToExistingPdf(String inputFileName, String outputFileName) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(EXPECTED_FOLDER + inputFileName);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(OUTPUT_FOLDER + outputFileName));
        
        // Extract existing metadata and create XMP from it
        HashMap<String, String> existingMetadata = reader.getInfo();
        ByteArrayOutputStream xmpOutputStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(xmpOutputStream, existingMetadata);
        
        xmpWriter.close();
        stamper.setXmpMetadata(xmpOutputStream.toByteArray());
        
        closeResources(stamper, reader);
    }

    private void enhanceExistingPdfWithXmpMetadata(String inputFileName, String outputFileName) 
            throws IOException, DocumentException, XMPException {
        PdfReader reader = new PdfReader(EXPECTED_FOLDER + inputFileName);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(OUTPUT_FOLDER + outputFileName));
        
        // Create XMP metadata and enhance it with additional properties
        stamper.createXmpMetadata();
        XmpWriter xmpWriter = stamper.getXmpWriter();
        
        addTestSubjectsToXmp(xmpWriter);
        PdfProperties.setVersion(xmpWriter.getXmpMeta(), PDF_VERSION);
        
        closeResources(stamper, reader);
    }

    private void createPdfUsingDeprecatedXmpApi(String fileName) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_FOLDER + fileName));
        
        // Create XMP metadata using deprecated API
        ByteArrayOutputStream xmpOutputStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(xmpOutputStream);
        
        addDeprecatedDublinCoreSchema(xmpWriter);
        addDeprecatedPdfSchema(xmpWriter);
        
        xmpWriter.close();
        writer.setXmpMetadata(xmpOutputStream.toByteArray());
        
        writeDocumentContent(document);
    }

    // Utility methods for common operations

    private void addTestSubjectsToXmp(XmpWriter xmpWriter) throws XMPException {
        for (String subject : TEST_SUBJECTS) {
            DublinCoreProperties.addSubject(xmpWriter.getXmpMeta(), subject);
        }
    }

    private void addTestPropertiesToXmp(XmpWriter xmpWriter) throws XMPException {
        PdfProperties.setKeywords(xmpWriter.getXmpMeta(), TEST_KEYWORDS);
        PdfProperties.setVersion(xmpWriter.getXmpMeta(), PDF_VERSION);
    }

    private void addDocumentMetadata(Document document) {
        document.addTitle("Hello World example");
        document.addSubject("This example shows how to add metadata & XMP");
        document.addKeywords("Metadata, iText, step 3");
        document.addCreator("My program using 'iText'");
        document.addAuthor("Bruno Lowagie & Paulo Soares");
    }

    private void writeDocumentContent(Document document) throws DocumentException {
        document.open();
        document.add(new Paragraph(SAMPLE_TEXT));
        document.close();
    }

    private void addDeprecatedDublinCoreSchema(XmpWriter xmpWriter) throws IOException {
        XmpSchema dublinCoreSchema = new com.itextpdf.text.xml.xmp.DublinCoreSchema();
        XmpArray subjectArray = new XmpArray(XmpArray.UNORDERED);
        
        for (String subject : TEST_SUBJECTS) {
            subjectArray.add(subject);
        }
        
        dublinCoreSchema.setProperty(DublinCoreSchema.SUBJECT, subjectArray);
        xmpWriter.addRdfDescription(dublinCoreSchema.getXmlns(), dublinCoreSchema.toString());
    }

    private void addDeprecatedPdfSchema(XmpWriter xmpWriter) throws IOException {
        PdfSchema pdfSchema = new PdfSchema();
        pdfSchema.setProperty(PdfSchema.KEYWORDS, TEST_KEYWORDS);
        pdfSchema.setProperty(PdfSchema.VERSION, PDF_VERSION);
        xmpWriter.addRdfDescription(pdfSchema);
    }

    private void closeResources(PdfStamper stamper, PdfReader reader) throws DocumentException, IOException {
        stamper.close();
        reader.close();
    }

    private void assertXmpMetadataMatches(String actualFileName, String expectedFileName) throws IOException, DocumentException {
        CompareTool compareTool = new CompareTool();
        String comparisonResult = compareTool.compareXmp(
            OUTPUT_FOLDER + actualFileName, 
            EXPECTED_FOLDER + expectedFileName, 
            true
        );
        Assert.assertNull("XMP metadata should match expected output", comparisonResult);
    }
}