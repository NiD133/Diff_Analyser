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

public class XmpWriterTestTest4 {

    public static final String OUT_FOLDER = "./target/com/itextpdf/text/xml/xmp/";

    public static final String CMP_FOLDER = "./src/test/resources/com/itextpdf/text/xml/xmp/";

    @Before
    public void init() {
        new File(OUT_FOLDER).mkdirs();
    }

    @Test
    public void manipulatePdf2Test() throws IOException, DocumentException, XMPException {
        String fileName = "xmp_metadata_added2.pdf";
        PdfReader reader = new PdfReader(CMP_FOLDER + "pdf_metadata.pdf");
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(OUT_FOLDER + fileName));
        stamper.createXmpMetadata();
        XmpWriter xmp = stamper.getXmpWriter();
        DublinCoreProperties.addSubject(xmp.getXmpMeta(), "Hello World");
        DublinCoreProperties.addSubject(xmp.getXmpMeta(), "XMP & Metadata");
        DublinCoreProperties.addSubject(xmp.getXmpMeta(), "Metadata");
        PdfProperties.setVersion(xmp.getXmpMeta(), "1.4");
        stamper.close();
        reader.close();
        CompareTool ct = new CompareTool();
        Assert.assertNull(ct.compareXmp(OUT_FOLDER + fileName, CMP_FOLDER + fileName, true));
    }
}
