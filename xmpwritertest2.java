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

public class XmpWriterTestTest2 {

    public static final String OUT_FOLDER = "./target/com/itextpdf/text/xml/xmp/";

    public static final String CMP_FOLDER = "./src/test/resources/com/itextpdf/text/xml/xmp/";

    @Before
    public void init() {
        new File(OUT_FOLDER).mkdirs();
    }

    @Test
    public void createPdfAutomaticTest() throws IOException, DocumentException {
        String fileName = "xmp_metadata_automatic.pdf";
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUT_FOLDER + fileName));
        document.addTitle("Hello World example");
        document.addSubject("This example shows how to add metadata & XMP");
        document.addKeywords("Metadata, iText, step 3");
        document.addCreator("My program using 'iText'");
        document.addAuthor("Bruno Lowagie & Paulo Soares");
        writer.createXmpMetadata();
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hello World"));
        // step 5
        document.close();
        CompareTool ct = new CompareTool();
        Assert.assertNull(ct.compareXmp(OUT_FOLDER + fileName, CMP_FOLDER + fileName, true));
    }
}
