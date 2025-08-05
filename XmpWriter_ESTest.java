package com.itextpdf.text.xml.xmp;

import org.junit.Test;
import static org.junit.Assert.*;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.xml.xmp.XmpWriter;
import com.itextpdf.xmp.XMPMeta;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Test suite for XmpWriter functionality including construction, metadata manipulation,
 * and serialization operations.
 */
public class XmpWriterTest {

    // Test Data Constants
    private static final String SAMPLE_TEXT = "Sample Document";
    private static final String SAMPLE_NAMESPACE = "http://ns.adobe.com/pdf/1.3/";
    private static final String DUBLIN_CORE_NAMESPACE = "http://purl.org/dc/elements/1.1/";
    private static final String SAMPLE_PROPERTY = "TestProperty";
    private static final String SAMPLE_VALUE = "TestValue";

    // ========== Constructor Tests ==========

    @Test
    public void shouldCreateXmpWriterWithOutputStream() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // When
        XmpWriter writer = new XmpWriter(outputStream);
        
        // Then
        assertNotNull("XmpWriter should be created successfully", writer);
        XMPMeta meta = writer.getXmpMeta();
        assertNotNull("XMP metadata should be initialized", meta);
    }

    @Test
    public void shouldCreateXmpWriterWithCustomEncoding() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String encoding = XmpWriter.UTF16BE;
        int extraSpace = 1024;
        
        // When
        XmpWriter writer = new XmpWriter(outputStream, encoding, extraSpace);
        
        // Then
        assertNotNull("XmpWriter should be created with custom encoding", writer);
    }

    @Test
    public void shouldCreateXmpWriterWithPdfDictionary() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfDictionary info = createSamplePdfDictionary();
        
        // When
        XmpWriter writer = new XmpWriter(outputStream, info);
        
        // Then
        assertNotNull("XmpWriter should be created with PDF dictionary", writer);
    }

    @Test
    public void shouldCreateXmpWriterWithStringMap() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Map<String, String> info = createSampleStringMap();
        
        // When
        XmpWriter writer = new XmpWriter(outputStream, info);
        
        // Then
        assertNotNull("XmpWriter should be created with string map", writer);
    }

    @Test
    public void shouldHandleNullPdfDictionary() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // When
        XmpWriter writer = new XmpWriter(outputStream, (PdfDictionary) null);
        
        // Then
        assertNotNull("XmpWriter should handle null PDF dictionary gracefully", writer);
    }

    @Test
    public void shouldHandleNullStringMap() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // When
        XmpWriter writer = new XmpWriter(outputStream, (Map<String, String>) null);
        
        // Then
        assertNotNull("XmpWriter should handle null string map gracefully", writer);
    }

    // ========== Document Info Property Tests ==========

    @Test
    public void shouldAddDocumentTitle() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        
        // When
        writer.addDocInfoProperty(PdfName.TITLE, SAMPLE_TEXT);
        
        // Then - No exception should be thrown
        // Note: Actual verification would require access to internal XMP structure
    }

    @Test
    public void shouldAddDocumentKeywords() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        String keywords = "PDF, XMP, Metadata";
        
        // When
        writer.addDocInfoProperty(PdfName.KEYWORDS, keywords);
        
        // Then - No exception should be thrown
    }

    @Test
    public void shouldAddDocumentSubject() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        String subject = "Test Document Subject";
        
        // When
        writer.addDocInfoProperty(PdfName.SUBJECT, subject);
        
        // Then - No exception should be thrown
    }

    @Test
    public void shouldAddDocumentCreationDate() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        String creationDate = "D:20230101120000";
        
        // When
        writer.addDocInfoProperty(PdfName.CREATIONDATE, creationDate);
        
        // Then - No exception should be thrown
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenAddingNullValue() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        
        // When & Then
        writer.addDocInfoProperty(PdfName.TITLE, null);
    }

    // ========== XMP Property Tests ==========

    @Test
    public void shouldSetXmpProperty() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        
        // When
        writer.setProperty(SAMPLE_NAMESPACE, SAMPLE_PROPERTY, SAMPLE_VALUE);
        
        // Then - No exception should be thrown
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionForEmptyNamespace() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        
        // When & Then
        writer.setProperty("", SAMPLE_PROPERTY, SAMPLE_VALUE);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionForNullNamespace() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        
        // When & Then
        writer.setProperty(null, SAMPLE_PROPERTY, SAMPLE_VALUE);
    }

    // ========== Array Operations Tests ==========

    @Test
    public void shouldAppendArrayItem() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        
        // When
        writer.appendArrayItem(SAMPLE_NAMESPACE, "TestArray", SAMPLE_VALUE);
        
        // Then - No exception should be thrown
    }

    @Test
    public void shouldAppendOrderedArrayItem() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        
        // When
        writer.appendOrderedArrayItem(SAMPLE_NAMESPACE, "TestOrderedArray", SAMPLE_VALUE);
        
        // Then - No exception should be thrown
    }

    @Test
    public void shouldAppendAlternateArrayItem() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        
        // When
        writer.appendAlternateArrayItem(DUBLIN_CORE_NAMESPACE, "TestAlternateArray", SAMPLE_VALUE);
        
        // Then - No exception should be thrown
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionForUnregisteredNamespace() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        String unregisteredNamespace = "http://example.com/unregistered/";
        
        // When & Then
        writer.appendAlternateArrayItem(unregisteredNamespace, "TestArray", SAMPLE_VALUE);
    }

    // ========== Serialization Tests ==========

    @Test
    public void shouldSerializeToOutputStream() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // When
        writer.serialize(outputStream);
        
        // Then
        byte[] result = outputStream.toByteArray();
        assertTrue("Serialized XMP should not be empty", result.length > 0);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenSerializingToNullStream() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        
        // When & Then
        writer.serialize(null);
    }

    @Test
    public void shouldCloseSuccessfully() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XmpWriter writer = new XmpWriter(outputStream);
        
        // When
        writer.close();
        
        // Then - No exception should be thrown
    }

    // ========== Configuration Tests ==========

    @Test
    public void shouldSetReadOnlyMode() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        
        // When
        writer.setReadOnly();
        
        // Then - No exception should be thrown
    }

    @Test
    public void shouldSetAboutProperty() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        String about = "Test Document About";
        
        // When
        writer.setAbout(about);
        
        // Then - No exception should be thrown
    }

    @Test
    public void shouldGetXmpMeta() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        
        // When
        XMPMeta meta = writer.getXmpMeta();
        
        // Then
        assertNotNull("XMP metadata should be available", meta);
    }

    // ========== Error Handling Tests ==========

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenXmpMetaIsNull() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        writer.xmpMeta = null; // Simulate corrupted state
        
        // When & Then
        writer.setProperty(SAMPLE_NAMESPACE, SAMPLE_PROPERTY, SAMPLE_VALUE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowExceptionWhenSerializingNullXmpMeta() throws Exception {
        // Given
        XmpWriter writer = createXmpWriter();
        writer.xmpMeta = null; // Simulate corrupted state
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // When & Then
        writer.serialize(outputStream);
    }

    // ========== Helper Methods ==========

    private XmpWriter createXmpWriter() throws IOException {
        return new XmpWriter(new ByteArrayOutputStream());
    }

    private PdfDictionary createSamplePdfDictionary() {
        PdfDictionary dict = new PdfDictionary();
        dict.put(PdfName.TITLE, new PdfString(SAMPLE_TEXT));
        dict.put(PdfName.SUBJECT, new PdfString("Test Subject"));
        return dict;
    }

    private Map<String, String> createSampleStringMap() {
        Map<String, String> map = new HashMap<>();
        map.put("title", SAMPLE_TEXT);
        map.put("subject", "Test Subject");
        map.put("keywords", "test, xmp, metadata");
        return map;
    }
}