package com.itextpdf.text.xml.xmp;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.itextpdf.awt.AsianFontMapper;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.xml.xmp.XmpWriter;
import com.itextpdf.xmp.XMPMeta;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import javax.swing.DebugGraphics;
import javax.swing.DropMode;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;

import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockPrintStream;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class XmpWriter_ESTest extends XmpWriter_ESTest_scaffolding {

    private static final String UTF_16 = "UTF-16";
    private static final String UTF_16BE = "UTF-16BE";
    private static final String PDF_NAMESPACE = "http://ns.adobe.com/pdf/1.3/";
    private static final String TEST_STRING = "VM}5IU\"rA,G:V\"]1D";

    @Test(timeout = 4000)
    public void testAddDocInfoProperty() throws Throwable {
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null);
        PdfName pdfName = PdfName.KEYWORDS;
        xmpWriter.addDocInfoProperty(pdfName, TEST_STRING);
        assertFalse(pdfName.isNull());
    }

    @Test(timeout = 4000)
    public void testSetProperty() throws Throwable {
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("qTF+8");
        MockPrintStream mockPrintStream = new MockPrintStream(mockFileOutputStream, true);
        AsianFontMapper asianFontMapper = new AsianFontMapper("s'E=)qP", "Producer");
        HashMap<String, String> aliases = asianFontMapper.getAliases();
        XmpWriter xmpWriter = new XmpWriter(mockPrintStream, aliases);
        xmpWriter.setProperty(PDF_NAMESPACE, "UniGB-UCS2-H", "</rdf:Description></rdf:RDF>\n");
    }

    @Test(timeout = 4000)
    public void testCloseXmpWriter() throws Throwable {
        MockPrintStream mockPrintStream = new MockPrintStream("SpaceBefore");
        XmpWriter xmpWriter = new XmpWriter(mockPrintStream, "SpaceBefore", 6229);
        xmpWriter.close();
    }

    @Test(timeout = 4000)
    public void testAppendOrderedArrayItem() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        XmpWriter xmpWriter = new XmpWriter(pipedOutputStream, "rPSJQGnS", -1800);
        xmpWriter.appendOrderedArrayItem(PDF_NAMESPACE, UTF_16BE, PDF_NAMESPACE);
    }

    @Test(timeout = 4000)
    public void testAddRdfDescription() throws Throwable {
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("Z>h7mej", true);
        MockPrintStream mockPrintStream = new MockPrintStream(mockFileOutputStream);
        DefaultFontMapper defaultFontMapper = new DefaultFontMapper();
        HashMap<String, String> aliases = defaultFontMapper.getAliases();
        XmpWriter xmpWriter = new XmpWriter(mockPrintStream, aliases);
        xmpWriter.addRdfDescription("", "");
    }

    @Test(timeout = 4000)
    public void testGetXmpMetaReturnsNull() throws Throwable {
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null, "kZE9PB", -1915564823);
        xmpWriter.xmpMeta = null;
        XMPMeta xmpMeta = xmpWriter.getXmpMeta();
        assertNull(xmpMeta);
    }

    @Test(timeout = 4000)
    public void testSetPropertyThrowsNullPointerException() throws Throwable {
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null, "kZE9PB", -1915564823);
        xmpWriter.xmpMeta = null;
        try {
            xmpWriter.setProperty("kZE9PB", "kZE9PB", (Object) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.xml.xmp.XmpWriter", e);
        }
    }

    @Test(timeout = 4000)
    public void testSerializeThrowsException() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        XmpWriter xmpWriter = new XmpWriter(pipedOutputStream);
        try {
            xmpWriter.serialize(pipedOutputStream);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("com.itextpdf.xmp.impl.XMPSerializerRDF", e);
        }
    }

    @Test(timeout = 4000)
    public void testSerializeWithNullOutputStream() throws Throwable {
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null, "3Y'G95KH", 10633);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        xmpWriter.serialize(byteArrayOutputStream);
    }

    @Test(timeout = 4000)
    public void testSerializeThrowsUnsupportedOperationException() throws Throwable {
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null);
        xmpWriter.xmpMeta = null;
        try {
            xmpWriter.serialize((OutputStream) null);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("com.itextpdf.xmp.XMPMetaFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testSerializeThrowsNullPointerException() throws Throwable {
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null);
        try {
            xmpWriter.serialize((OutputStream) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.xmp.impl.CountOutputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testCloseThrowsIOException() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PdfAction pdfAction = PdfAction.gotoRemotePage("zhcsaI", "", true, false);
        XmpWriter xmpWriter = new XmpWriter(pipedOutputStream, pdfAction);
        try {
            xmpWriter.close();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.itextpdf.text.xml.xmp.XmpWriter", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddDocInfoPropertyWithNullValueThrowsNullPointerException() throws Throwable {
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null);
        PdfName pdfName = PdfName.KEYWORDS;
        try {
            xmpWriter.addDocInfoProperty(pdfName, (String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.xml.xmp.XmpWriter", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddDocInfoPropertyWithEmptyString() throws Throwable {
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null, "E9vMPB", -3497);
        PdfName pdfName = PdfName.KEYWORDS;
        xmpWriter.addDocInfoProperty(pdfName, "");
        assertEquals(8, PdfObject.NULL);
    }

    @Test(timeout = 4000)
    public void testAddDocInfoPropertyWithSubject() throws Throwable {
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null, "Subject", 158);
        xmpWriter.addDocInfoProperty("Subject", "P:Z|Bx");
    }

    @Test(timeout = 4000)
    public void testAddDocInfoPropertyWithTitle() throws Throwable {
        HashMap<String, String> hashMap = new HashMap<>();
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null, hashMap);
        PdfName pdfName = PdfName.TITLE;
        xmpWriter.addDocInfoProperty(pdfName, "UnicodeBig");
        assertEquals(4, PdfObject.NAME);
    }

    @Test(timeout = 4000)
    public void testCloseXmpWriterWithoutException() throws Throwable {
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null);
        xmpWriter.close();
    }

    @Test(timeout = 4000)
    public void testAppendAlternateArrayItem() throws Throwable {
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null);
        xmpWriter.appendAlternateArrayItem("http://purl.org/dc/elements/1.1/", UTF_16, "gBP]3L}");
    }

    @Test(timeout = 4000)
    public void testAppendOrderedArrayItemThrowsException() throws Throwable {
        HashMap<String, String> hashMap = new HashMap<>();
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null, hashMap);
        try {
            xmpWriter.appendOrderedArrayItem("Y}161VLEk*R", "Y}161VLEk*R", "flags");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("com.itextpdf.xmp.impl.xpath.XMPPathParser", e);
        }
    }
}