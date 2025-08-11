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
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
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
 * Tests the functionality of the {@link XmpWriter} class, covering various scenarios
 * for creating and manipulating XMP metadata in PDF documents.
 */
public class XmpWriterTest {
    public static final String OUT_FOLDER = "./target/com/itextpdf/text/xml/xmp/";
    public static final String CMP_FOLDER = "./src/test/resources/com/itextpdf/text/xml/xmp/";

    @Before
    public void init() {
        new File(OUT_FOLDER).mkdirs();
    }

    @Test
    public void createPdfWithManualXmp_shouldProduceCorrectMetadata() throws IOException, DocumentException, XMPException {
        // Arrange
        String outPdf = OUT_FOLDER + "xmp_metadata.pdf";
        String cmpPdf = CMP_FOLDER + "xmp_metadata.pdf";

        // Act
        try (Document document = new Document();
             FileOutputStream fos = new FileOutputStream(outPdf);
             PdfWriter writer = PdfWriter.getInstance(document, fos);
             ByteArrayOutputStream xmpOutputStream = new ByteArrayOutputStream()) {

            // Create XMP metadata stream manually
            try (XmpWriter xmpWriter = new XmpWriter(xmpOutputStream)) {
                DublinCoreProperties.addSubject(xmpWriter.getXmpMeta(), "Hello World");
                DublinCoreProperties.addSubject(xmpWriter.getXmpMeta(), "XMP & Metadata");
                DublinCoreProperties.addSubject(xmpWriter.getXmpMeta(), "Metadata");
                PdfProperties.setKeywords(xmpWriter.getXmpMeta(), "Hello World, XMP & Metadata, Metadata");
                PdfProperties.setVersion(xmpWriter.getXmpMeta(), "1.4");
            } // xmpWriter.close() serializes data to xmpOutputStream

            writer.setXmpMetadata(xmpOutputStream.toByteArray());

            document.open();
            document.add(new Paragraph("Hello World"));
        }

        // Assert
        assertXmpEquals(outPdf, cmpPdf);
    }

    @Test
    public void createPdfWithAutomaticXmp_shouldProduceCorrectMetadata() throws IOException, DocumentException {
        // Arrange
        String outPdf = OUT_FOLDER + "xmp_metadata_automatic.pdf";
        String cmpPdf = CMP_FOLDER + "xmp_metadata_automatic.pdf";

        // Act
        try (Document document = new Document();
             FileOutputStream fos = new FileOutputStream(outPdf);
             PdfWriter writer = PdfWriter.getInstance(document, fos)) {

            document.addTitle("Hello World example");
            document.addSubject("This example shows how to add metadata & XMP");
            document.addKeywords("Metadata, iText, step 3");
            document.addCreator("My program using 'iText'");
            document.addAuthor("Bruno Lowagie & Paulo Soares");

            // Automatically generate XMP from document properties
            writer.createXmpMetadata();

            document.open();
            document.add(new Paragraph("Hello World"));
        }

        // Assert
        assertXmpEquals(outPdf, cmpPdf);
    }

    @Test
    public void manipulatePdfByConvertingInfoDict_shouldProduceCorrectMetadata() throws IOException, DocumentException {
        // Arrange
        String outPdf = OUT_FOLDER + "xmp_metadata_added.pdf";
        String cmpPdf = CMP_FOLDER + "xmp_metadata_added.pdf";
        String sourcePdf = CMP_FOLDER + "pdf_metadata.pdf";

        // Act
        try (PdfReader reader = new PdfReader(sourcePdf);
             FileOutputStream fos = new FileOutputStream(outPdf);
             PdfStamper stamper = new PdfStamper(reader, fos)) {

            HashMap<String, String> info = reader.getInfo();
            byte[] xmpBytes;
            try (ByteArrayOutputStream xmpOutputStream = new ByteArrayOutputStream()) {
                // Create XMP from the existing info dictionary
                XmpWriter xmpWriter = new XmpWriter(xmpOutputStream, info);
                xmpWriter.close();
                xmpBytes = xmpOutputStream.toByteArray();
            }
            stamper.setXmpMetadata(xmpBytes);
        }

        // Assert
        assertXmpEquals(outPdf, cmpPdf);
    }

    @Test
    public void manipulatePdfByModifyingAutomaticXmp_shouldProduceCorrectMetadata() throws IOException, DocumentException, XMPException {
        // Arrange
        String outPdf = OUT_FOLDER + "xmp_metadata_added2.pdf";
        String cmpPdf = CMP_FOLDER + "xmp_metadata_added2.pdf";
        String sourcePdf = CMP_FOLDER + "pdf_metadata.pdf";

        // Act
        try (PdfReader reader = new PdfReader(sourcePdf);
             FileOutputStream fos = new FileOutputStream(outPdf);
             PdfStamper stamper = new PdfStamper(reader, fos)) {

            stamper.createXmpMetadata();
            XmpWriter xmpWriter = stamper.getXmpWriter();

            // Add new properties to the automatically created XMP
            DublinCoreProperties.addSubject(xmpWriter.getXmpMeta(), "Hello World");
            DublinCoreProperties.addSubject(xmpWriter.getXmpMeta(), "XMP & Metadata");
            DublinCoreProperties.addSubject(xmpWriter.getXmpMeta(), "Metadata");
            PdfProperties.setVersion(xmpWriter.getXmpMeta(), "1.4");
        }

        // Assert
        assertXmpEquals(outPdf, cmpPdf);
    }

    @Test
    public void createPdfWithDeprecatedApi_shouldProduceCorrectMetadata() throws IOException, DocumentException {
        // Arrange
        String outPdf = OUT_FOLDER + "xmp_metadata_deprecated.pdf";
        String cmpPdf = CMP_FOLDER + "xmp_metadata.pdf"; // Output is identical to the first test

        // Act
        try (Document document = new Document();
             FileOutputStream fos = new FileOutputStream(outPdf);
             PdfWriter writer = PdfWriter.getInstance(document, fos);
             ByteArrayOutputStream xmpOutputStream = new ByteArrayOutputStream()) {

            // This test verifies the deprecated way of building XMP metadata.
            try (XmpWriter xmpWriter = new XmpWriter(xmpOutputStream)) {
                XmpSchema dc = new com.itextpdf.text.xml.xmp.DublinCoreSchema();
                XmpArray subject = new XmpArray(XmpArray.UNORDERED);
                subject.add("Hello World");
                subject.add("XMP & Metadata");
                subject.add("Metadata");
                dc.setProperty(DublinCoreSchema.SUBJECT, subject);
                xmpWriter.addRdfDescription(dc.getXmlns(), dc.toString());

                PdfSchema pdf = new PdfSchema();
                pdf.setProperty(PdfSchema.KEYWORDS, "Hello World, XMP & Metadata, Metadata");
                pdf.setProperty(PdfSchema.VERSION, "1.4");
                xmpWriter.addRdfDescription(pdf);
            }

            writer.setXmpMetadata(xmpOutputStream.toByteArray());

            document.open();
            document.add(new Paragraph("Hello World"));
        }

        // Assert
        assertXmpEquals(outPdf, cmpPdf);
    }

    /**
     * Helper method to compare the XMP metadata of a generated PDF with a reference PDF.
     *
     * @param actualPdfPath   Path to the generated PDF file.
     * @param expectedPdfPath Path to the reference (comparison) PDF file.
     * @throws IOException If an I/O error occurs during comparison.
     */
    private void assertXmpEquals(String actualPdfPath, String expectedPdfPath) throws IOException {
        CompareTool compareTool = new CompareTool();
        String comparisonResult = compareTool.compareXmp(actualPdfPath, expectedPdfPath, true);
        Assert.assertNull("XMP data in " + actualPdfPath + " should match " + expectedPdfPath, comparisonResult);
    }
}