package com.itextpdf.text.xml.xmp;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Focused, readable tests for XmpWriter.
 *
 * These tests cover the most important behaviors of XmpWriter:
 * - Construction with defaults and specific inputs (PdfDictionary, Map)
 * - Basic property setting methods
 * - Array helpers for common namespaces
 * - Serialization behavior (writing to the constructor stream or an external stream)
 * - Error paths for invalid input
 *
 * The goal is to keep the tests small, deterministic, and free of unrelated framework noise.
 */
public class XmpWriterTest {

    @Test
    public void createWithDefaults_andClose_writesPacket() throws Exception {
        // Creates a writer with default UTF-8 encoding and default padding
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XmpWriter writer = new XmpWriter(out);

        // When closing, the XMP packet should be serialized to the provided stream
        writer.close();

        assertTrue("Expected some XMP bytes to be written", out.size() > 0);
    }

    @Test
    public void getXmpMeta_isNotNull() throws Exception {
        XmpWriter writer = new XmpWriter(new ByteArrayOutputStream());
        assertNotNull("XMP metadata should be initialized on construction", writer.getXmpMeta());
    }

    @Test
    public void addDocInfoProperty_withPdfName_doesNotThrow() throws Exception {
        XmpWriter writer = new XmpWriter(new ByteArrayOutputStream());
        writer.addDocInfoProperty(PdfName.TITLE, "My Title");
        // No assertion needed; absence of exception is the behavior under test.
    }

    @Test
    public void addDocInfoProperty_withStringKey_doesNotThrow() throws Exception {
        XmpWriter writer = new XmpWriter(new ByteArrayOutputStream());
        writer.addDocInfoProperty("Author", "Alice");
    }

    @Test
    public void constructor_withPdfDictionary_readsStringEntries() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfDictionary info = new PdfDictionary();
        info.put(PdfName.TITLE, new PdfString("My Title"));
        info.put(PdfName.AUTHOR, new PdfString("Alice"));

        // Building from a PdfDictionary should pull in string values
        XmpWriter writer = new XmpWriter(out, info);
        writer.close();

        String xml = out.toString("UTF-8");
        assertTrue("Expected Title value in XMP output", xml.contains("My Title"));
        assertTrue("Expected Author value in XMP output", xml.contains("Alice"));
    }

    @Test
    public void constructor_withMap_readsEntries_andIgnoresNulls() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Map<String, String> info = new HashMap<>();
        info.put("Title", "My Title");
        info.put("Author", null); // should be ignored

        XmpWriter writer = new XmpWriter(out, info);
        writer.close();

        String xml = out.toString("UTF-8");
        assertTrue("Expected Title value in XMP output", xml.contains("My Title"));
        // We don't assert for 'Author' key name because the actual property name in XMP differs,
        // but we do verify output exists and didn't fail.
        assertTrue("Expected some XMP output", xml.length() > 0);
    }

    @Test
    public void setProperty_withEmptySchema_throwsXmpException() throws Exception {
        XmpWriter writer = new XmpWriter(new ByteArrayOutputStream());

        assertThrows(XMPException.class, () -> writer.setProperty(null, "prop", "value"));
        assertThrows(XMPException.class, () -> writer.setProperty("", "prop", "value"));
    }

    @Test
    public void appendArrayItem_withRegisteredNamespace_succeeds() throws Exception {
        XmpWriter writer = new XmpWriter(new ByteArrayOutputStream());

        // Use a well-known, registered namespace (Dublin Core)
        writer.appendArrayItem(XMPConst.NS_DC, DublinCoreProperties.SUBJECT, "pdf");
    }

    @Test
    public void appendArrayItem_withUnknownNamespace_throwsXmpException() throws Exception {
        XmpWriter writer = new XmpWriter(new ByteArrayOutputStream());

        assertThrows(XMPException.class, () ->
                writer.appendArrayItem("urn:unknown:namespace", "arrayName", "value"));
    }

    @Test
    public void serialize_toExternalStream_writesEvenIfConstructedWithNullStream() throws Exception {
        // Construct with no underlying stream
        XmpWriter writer = new XmpWriter(null);

        // But serialize to an explicitly provided stream
        ByteArrayOutputStream external = new ByteArrayOutputStream();
        writer.serialize(external);

        assertTrue("Expected bytes written to the external stream", external.size() > 0);
    }

    @Test
    public void addRdfDescription_withInvalidXml_throwsIOException() throws Exception {
        XmpWriter writer = new XmpWriter(new ByteArrayOutputStream());

        // Invalid content should fail XML parsing
        assertThrows(IOException.class, () ->
                writer.addRdfDescription("http://purl.org/dc/elements/1.1/", "<not-closed"));
    }
}