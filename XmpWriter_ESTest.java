package com.itextpdf.text.xml.xmp;

import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.awt.AsianFontMapper;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.xml.xmp.XmpWriter;
import com.itextpdf.xmp.XMPMeta;
import java.io.*;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import javax.swing.*;
import javax.swing.tree.TreeModel;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class XmpWriter_ESTest extends XmpWriter_ESTest_scaffolding {

    // ================= Constructor Tests =================
    @Test(timeout = 4000)
    public void testConstructorWithPdfDictionary() throws Throwable {
        PdfDocument pdfDocument = new PdfDocument();
        PdfWriter pdfWriter = PdfWriter.getInstance(pdfDocument, (OutputStream) null);
        PdfDictionary infoDictionary = pdfWriter.getInfo();
        new XmpWriter((OutputStream) null, infoDictionary);
    }

    @Test(timeout = 4000)
    public void testConstructorWithMap() throws Throwable {
        Map<String, String> zoneMap = ZoneId.SHORT_IDS;
        new XmpWriter((OutputStream) null, zoneMap);
    }

    @Test(timeout = 4000)
    public void testConstructorWithUtf16Encoding() throws Throwable {
        new XmpWriter((OutputStream) null, "UTF-16", -1912709396);
    }

    @Test(timeout = 4000)
    public void testConstructorWithUtf16LeEncoding() throws Throwable {
        new XmpWriter((OutputStream) null, "UTF-16LE", -1912709396);
    }

    @Test(timeout = 4000)
    public void testConstructorWithUtf16BeEncoding() throws Throwable {
        new XmpWriter((OutputStream) null, "UTF-16BE", -4);
    }

    // ================= addDocInfoProperty() Tests =================
    @Test(timeout = 4000)
    public void testAddDocInfoPropertyWithPdfNameKeywordsSuccess() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null, "E9vMPB", -3497);
        writer.addDocInfoProperty(PdfName.KEYWORDS, "");
    }

    @Test(timeout = 4000)
    public void testAddDocInfoPropertyWithSubjectSuccess() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null, "Subject", 158);
        writer.addDocInfoProperty("Subject", "P:Z|Bx");
    }

    @Test(timeout = 4000)
    public void testAddDocInfoPropertyWithPdfNameTitleSuccess() throws Throwable {
        HashMap<String, String> aliases = new HashMap<>();
        XmpWriter writer = new XmpWriter((OutputStream) null, aliases);
        writer.addDocInfoProperty(PdfName.TITLE, "UnicodeBig");
    }

    @Test(timeout = 4000)
    public void testAddDocInfoPropertyWithProducerSuccess() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null, "Producer", -1);
        writer.addDocInfoProperty("Producer", "illegal.p.value");
    }

    @Test(timeout = 4000)
    public void testAddDocInfoPropertyWithPdfNameCreationDateSuccess() throws Throwable {
        PrintStream logStream = DebugGraphics.logStream();
        XmpWriter writer = new XmpWriter(logStream);
        writer.addDocInfoProperty(PdfName.CREATIONDATE, "W<t@4^");
    }

    @Test(timeout = 4000)
    public void testAddDocInfoPropertyWithPdfNamePagesSuccess() throws Throwable {
        PipedOutputStream pipedOutput = new PipedOutputStream();
        PdfAction pdfAction = PdfAction.gotoRemotePage("zhcsaI", "", true, false);
        XmpWriter writer = new XmpWriter(pipedOutput, pdfAction);
        writer.addDocInfoProperty(pdfAction.PAGES, "PDF");
    }

    @Test(expected = NullPointerException.class, timeout = 4000)
    public void testAddDocInfoPropertyThrowsNullPointerException() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null);
        writer.addDocInfoProperty(PdfName.KEYWORDS, null);
    }

    // ================= setProperty() Tests =================
    @Test(timeout = 4000)
    public void testSetPropertySuccess() throws Throwable {
        MockFileOutputStream fileOutput = new MockFileOutputStream("qTF+8");
        MockPrintStream printStream = new MockPrintStream(fileOutput, true);
        AsianFontMapper fontMapper = new AsianFontMapper("s'E=)qP", "Prducer");
        HashMap<String, String> aliases = fontMapper.getAliases();
        XmpWriter writer = new XmpWriter(printStream, aliases);
        writer.setProperty("http://ns.adobe.com/pdf/1.3/", "UniGB-UCS2-H", "</rdf:Description></rdf:RDF>\n");
    }

    @Test(expected = Exception.class, timeout = 4000)
    public void testSetPropertyWithEmptySchemaThrowsException() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null, "Producer", -1);
        JTree tree = new JTree((TreeModel) null);
        DropMode dropMode = tree.getDropMode();
        writer.setProperty(null, null, dropMode);
    }

    @Test(expected = NullPointerException.class, timeout = 4000)
    public void testSetPropertyWhenMetaIsNullThrowsException() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null, "kZE9PB", -1915564823);
        writer.xmpMeta = null;
        writer.setProperty("kZE9PB", "kZE9PB", null);
    }

    // ================= appendArrayItem() Tests =================
    @Test(timeout = 4000)
    public void testAppendArrayItemSuccess() throws Throwable {
        MockFileOutputStream fileOutput = new MockFileOutputStream("qTF+8");
        MockPrintStream printStream = new MockPrintStream(fileOutput, true);
        AsianFontMapper fontMapper = new AsianFontMapper("s'E=)qP", "Prducer");
        HashMap<String, String> aliases = fontMapper.getAliases();
        XmpWriter writer = new XmpWriter(printStream, aliases);
        writer.appendArrayItem("http://ns.adobe.com/pdf/1.3/", "UniGB-UCS2-V", "UniGB-UCS2-V");
    }

    @Test(expected = Exception.class, timeout = 4000)
    public void testAppendArrayItemWithEmptySchemaThrowsException() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null);
        writer.appendArrayItem(null, null, null);
    }

    @Test(expected = NullPointerException.class, timeout = 4000)
    public void testAppendArrayItemWhenMetaIsNullThrowsException() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null);
        writer.xmpMeta = null;
        writer.appendArrayItem("HW0?X1pNW.`syp5", "HW0?X1pNW.`syp5", "");
    }

    // ================= appendOrderedArrayItem() Tests =================
    @Test(timeout = 4000)
    public void testAppendOrderedArrayItemSuccess() throws Throwable {
        PipedOutputStream pipedOutput = new PipedOutputStream();
        XmpWriter writer = new XmpWriter(pipedOutput, "rPSJQGnS", -1800);
        writer.appendOrderedArrayItem("http://ns.adobe.com/pdf/1.3/", "UTF-16BE", "http://ns.adobe.com/pdf/1.3/");
    }

    @Test(expected = Exception.class, timeout = 4000)
    public void testAppendOrderedArrayItemWithUnregisteredSchemaThrowsException() throws Throwable {
        HashMap<String, String> aliases = new HashMap<>();
        XmpWriter writer = new XmpWriter((OutputStream) null, aliases);
        writer.appendOrderedArrayItem("Y}161VLEk*R", "Y}161VLEk*R", "flags");
    }

    @Test(expected = NullPointerException.class, timeout = 4000)
    public void testAppendOrderedArrayItemWhenMetaIsNullThrowsException() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null, "tZE9PB", 2097159);
        writer.xmpMeta = null;
        writer.appendOrderedArrayItem("UTF-16BE", "UTF-16BE", "UTF-16");
    }

    // ================= appendAlternateArrayItem() Tests =================
    @Test(timeout = 4000)
    public void testAppendAlternateArrayItemWithValidNamespaceSuccess() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null);
        writer.appendAlternateArrayItem("http://purl.org/dc/elements/1.1/", "UTF-16LE", "gBP]3L}");
    }

    @Test(expected = Exception.class, timeout = 4000)
    public void testAppendAlternateArrayItemWithUnregisteredSchemaThrowsException() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null);
        writer.appendAlternateArrayItem("UTF-16", "UTF-16", "UTF-16");
    }

    // ================= addRdfDescription() Tests =================
    @Test(timeout = 4000)
    public void testAddRdfDescriptionWithEmptyStringsSuccess() throws Throwable {
        MockFileOutputStream fileOutput = new MockFileOutputStream("Z>h7mej", true);
        MockPrintStream printStream = new MockPrintStream(fileOutput);
        DefaultFontMapper fontMapper = new DefaultFontMapper();
        HashMap<String, String> aliases = fontMapper.getAliases();
        XmpWriter writer = new XmpWriter(printStream, aliases);
        writer.addRdfDescription("", "");
    }

    @Test(expected = IOException.class, timeout = 4000)
    public void testAddRdfDescriptionWithInvalidDataThrowsException() throws Throwable {
        Map<String, String> zoneMap = ZoneId.SHORT_IDS;
        XmpWriter writer = new XmpWriter((OutputStream) null, zoneMap);
        writer.addRdfDescription("UTF-16BE", "com.itextpdf.text.io.FileChannelRandomAccessSource");
    }

    @Test(expected = NullPointerException.class, timeout = 4000)
    public void testAddRdfDescriptionWhenMetaIsNullThrowsException() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null);
        writer.xmpMeta = null;
        writer.addRdfDescription("|A", "|A");
    }

    // ================= serialize() Tests =================
    @Test(timeout = 4000)
    public void testSerializeWithObjectOutputStreamSuccess() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null);
        MockPrintStream mockStream = new MockPrintStream("UTF-16");
        ObjectOutputStream objectOutput = new ObjectOutputStream(mockStream);
        writer.serialize(objectOutput);
    }

    @Test(expected = Exception.class, timeout = 4000)
    public void testSerializeWithPipedOutputStreamThrowsException() throws Throwable {
        PipedOutputStream pipedOutput = new PipedOutputStream();
        XmpWriter writer = new XmpWriter(pipedOutput);
        writer.serialize(pipedOutput);
    }

    @Test(expected = NullPointerException.class, timeout = 4000)
    public void testSerializeWithNullOutputStreamThrowsException() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null);
        writer.serialize((OutputStream) null);
    }

    @Test(expected = UnsupportedOperationException.class, timeout = 4000)
    public void testSerializeWhenMetaIsNullThrowsException() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null);
        writer.xmpMeta = null;
        writer.serialize((OutputStream) null);
    }

    // ================= close() Tests =================
    @Test(timeout = 4000)
    public void testCloseSuccess() throws Throwable {
        MockPrintStream printStream = new MockPrintStream("SpaceBefore");
        XmpWriter writer = new XmpWriter(printStream, "SpaceBefore", 6229);
        writer.close();
    }

    @Test(expected = IOException.class, timeout = 4000)
    public void testCloseWithPipedOutputStreamThrowsException() throws Throwable {
        PipedOutputStream pipedOutput = new PipedOutputStream();
        PdfAction pdfAction = PdfAction.gotoRemotePage("zhcsaI", "", true, false);
        XmpWriter writer = new XmpWriter(pipedOutput, pdfAction);
        writer.close();
    }

    @Test(expected = Exception.class, timeout = 4000)
    public void testCloseWithMockFileOutputStreamThrowsException() throws Throwable {
        MockFileOutputStream fileOutput = new MockFileOutputStream("({");
        XmpWriter writer = new XmpWriter(fileOutput, "({", 10796);
        writer.close();
    }

    // ================= getXmpMeta() Tests =================
    @Test(timeout = 4000)
    public void testGetXmpMetaSuccess() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null);
        XMPMeta meta = writer.getXmpMeta();
        assertNull(meta.getPacketHeader());
    }

    @Test(timeout = 4000)
    public void testGetXmpMetaWhenMetaIsNullReturnsNull() throws Throwable {
        XmpWriter writer = new XmpWriter((OutputStream) null, "kZE9PB", -1915564823);
        writer.xmpMeta = null;
        XMPMeta meta = writer.getXmpMeta();
        assertNull(meta);
    }

    // ================= setAbout() Tests =================
    @Test(timeout = 4000)
    public void testSetAboutSuccess() throws Throwable {
        PipedOutputStream pipedOutput = new PipedOutputStream();
        PdfAction pdfAction = PdfAction.gotoRemotePage("zhcsaI", "", true, false);
        XmpWriter writer = new XmpWriter(pipedOutput, pdfAction);
        writer.setAbout("|+)1]{?0s");
    }

    // ================= setReadOnly() Tests =================
    @Test(timeout = 4000)
    public void testSetReadOnlySuccess() throws Throwable {
        PipedOutputStream pipedOutput = new PipedOutputStream();
        PdfAction pdfAction = PdfAction.gotoRemotePage("zhcsaI", "", true, false);
        XmpWriter writer = new XmpWriter(pipedOutput, pdfAction);
        writer.setReadOnly();
    }

    // ================= Edge Case Tests =================
    @Test(timeout = 4000)
    public void testConstructorWithBiFunctionMap() throws Throwable {
        HashMap<String, String> map = new HashMap<>();
        BiFunction<Object, Object, String> biFunction = 
            (BiFunction<Object, Object, String>) mock(BiFunction.class, new ViolatedAssumptionAnswer());
        doReturn(null).when(biFunction).apply(any(), any());
        map.put("subject", "subject");
        map.replaceAll(biFunction);
        new XmpWriter((OutputStream) null, map);
    }
}