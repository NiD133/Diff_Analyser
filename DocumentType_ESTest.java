package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Printer;

/**
 * Test suite for DocumentType class functionality.
 * Tests DOCTYPE node creation, attribute access, and HTML output generation.
 */
public class DocumentTypeTest {

    // Test data constants for better readability
    private static final String HTML_DOCTYPE_NAME = "html";
    private static final String HTML5_PUBLIC_ID = "";
    private static final String HTML5_SYSTEM_ID = "";
    
    private static final String XHTML_PUBLIC_ID = "-//W3C//DTD XHTML 1.0 Strict//EN";
    private static final String XHTML_SYSTEM_ID = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";

    @Test
    public void shouldGenerateCorrectHtmlForPublicDoctype() {
        // Given: A DOCTYPE with public and system identifiers
        DocumentType doctype = new DocumentType("html", XHTML_PUBLIC_ID, XHTML_SYSTEM_ID);
        
        // When: Converting to HTML
        StringWriter output = new StringWriter();
        QuietAppendable appendable = QuietAppendable.wrap(output);
        Document.OutputSettings settings = new Document.OutputSettings();
        doctype.outerHtmlHead(appendable, settings);
        
        // Then: Should generate proper PUBLIC DOCTYPE declaration
        String expectedHtml = "<!DOCTYPE html PUBLIC \"" + XHTML_PUBLIC_ID + "\" \"" + XHTML_SYSTEM_ID + "\">";
        assertEquals("DOCTYPE with PUBLIC and SYSTEM IDs should format correctly", 
                    expectedHtml, output.toString());
    }

    @Test
    public void shouldReturnEmptySystemIdForHtml5Doctype() {
        // Given: An HTML5 DOCTYPE (no system ID)
        DocumentType html5Doctype = new DocumentType(HTML_DOCTYPE_NAME, HTML5_PUBLIC_ID, HTML5_SYSTEM_ID);
        
        // When: Getting system ID
        String systemId = html5Doctype.systemId();
        
        // Then: Should return empty string
        assertEquals("HTML5 DOCTYPE should have empty system ID", "", systemId);
        assertEquals("Node name should always be #doctype", "#doctype", html5Doctype.nodeName());
    }

    @Test
    public void shouldReturnCorrectPublicId() {
        // Given: A DOCTYPE with specific public ID
        DocumentType doctype = new DocumentType("test", "PUBLIC_ID_VALUE", "system_id");
        
        // When: Getting public ID
        String publicId = doctype.publicId();
        
        // Then: Should return the correct public ID
        assertEquals("Should return the public ID that was set", "PUBLIC_ID_VALUE", publicId);
        assertEquals("Node name should always be #doctype", "#doctype", doctype.nodeName());
    }

    @Test
    public void shouldAlwaysReturnDoctypeAsNodeName() {
        // Given: Any DocumentType instance
        DocumentType doctype = new DocumentType("custom_name", "pub", "sys");
        
        // When: Getting node name
        String nodeName = doctype.nodeName();
        
        // Then: Should always return "#doctype"
        assertEquals("Node name should always be #doctype regardless of DOCTYPE name", 
                    "#doctype", nodeName);
    }

    @Test
    public void shouldReturnCorrectDoctypeName() {
        // Given: A DOCTYPE with specific name
        DocumentType doctype = new DocumentType("custom_doctype", "", "");
        
        // When: Getting the name
        String name = doctype.name();
        
        // Then: Should return the name that was set
        assertEquals("Should return the DOCTYPE name that was set", "custom_doctype", name);
        assertEquals("Node name should always be #doctype", "#doctype", doctype.nodeName());
    }

    @Test
    public void shouldAllowSettingPubSysKeyWhenParentExists() {
        // Given: A DocumentType with a parent node
        DocumentType doctype = new DocumentType("", "", "");
        doctype.setParentNode(doctype); // Self-reference for testing
        
        // When: Setting pub/sys key
        // Then: Should not throw exception (this appears to be testing internal state)
        doctype.setPubSysKey("optgroup");
        
        // Test passes if no exception is thrown
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenOutputtingToNullAppendable() {
        // Given: A DocumentType and null appendable
        DocumentType doctype = new DocumentType("test", "pub", "sys");
        Document.OutputSettings settings = new Document.OutputSettings();
        
        // When: Trying to output to null appendable
        // Then: Should throw NullPointerException
        doctype.outerHtmlHead(null, settings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNullSystemId() {
        // Given: Null system ID
        // When: Creating DocumentType with null system ID
        // Then: Should throw IllegalArgumentException
        new DocumentType("", "", null);
    }

    @Test
    public void shouldGenerateSystemOnlyDoctypeWhenPublicIdEmpty() {
        // Given: A DOCTYPE with empty public ID but non-empty system ID
        DocumentType doctype = new DocumentType("", "", "system_id_value");
        
        // When: Converting to HTML
        String html = doctype.outerHtml();
        
        // Then: Should generate SYSTEM-only DOCTYPE
        assertEquals("Empty public ID should result in SYSTEM-only DOCTYPE", 
                    "<!DOCTYPE SYSTEM \"system_id_value\">", html);
        assertEquals("Node name should always be #doctype", "#doctype", doctype.nodeName());
    }

    @Test(expected = BufferOverflowException.class)
    public void shouldThrowBufferOverflowWhenAppendableIsTooSmall() {
        // Given: A DocumentType that will generate long output
        DocumentType doctype = new DocumentType("", "public_id", "system_id");
        
        // And: A very small buffer
        char[] smallBuffer = new char[4]; // Too small for DOCTYPE output
        CharBuffer charBuffer = CharBuffer.wrap(smallBuffer);
        QuietAppendable appendable = QuietAppendable.wrap(charBuffer);
        Document.OutputSettings settings = new Document.OutputSettings();
        
        // When: Trying to output to small buffer
        // Then: Should throw BufferOverflowException
        doctype.outerHtmlHead(appendable, settings);
    }

    @Test
    public void shouldGenerateCorrectXmlOutput() {
        // Given: A DocumentType and XML output settings
        DocumentType doctype = new DocumentType("root", "public_id", "system_id");
        StringBuffer output = new StringBuffer("prefix_");
        QuietAppendable appendable = QuietAppendable.wrap(output);
        
        Document.OutputSettings xmlSettings = new Document.OutputSettings()
                .syntax(Document.OutputSettings.Syntax.xml);
        
        // When: Traversing with XML printer
        Printer.Pretty printer = new Printer.Pretty(doctype, appendable, xmlSettings);
        doctype.traverse(printer);
        
        // Then: Should generate correct XML DOCTYPE
        String result = output.toString();
        assertTrue("Should contain original prefix", result.startsWith("prefix_"));
        assertTrue("Should contain DOCTYPE declaration", result.contains("<!DOCTYPE root PUBLIC"));
        assertTrue("Should contain public ID", result.contains("\"public_id\""));
        assertTrue("Should contain system ID", result.contains("\"system_id\""));
    }

    @Test
    public void shouldAllowSettingPubSysKeyToValidValue() {
        // Given: A DocumentType
        DocumentType doctype = new DocumentType("", "", "test_system");
        
        // When: Setting pub/sys key to valid value
        doctype.setPubSysKey("SYSTEM");
        
        // Then: Should not throw exception
        assertEquals("Node name should remain #doctype", "#doctype", doctype.nodeName());
    }

    @Test
    public void shouldAllowSettingPubSysKeyToNull() {
        // Given: A DocumentType
        DocumentType doctype = new DocumentType("", "", "test_system");
        
        // When: Setting pub/sys key to null
        doctype.setPubSysKey(null);
        
        // Then: Should not throw exception
        assertEquals("Node name should remain #doctype", "#doctype", doctype.nodeName());
    }

    @Test
    public void shouldReturnCorrectSystemId() {
        // Given: A DocumentType with specific system ID
        DocumentType doctype = new DocumentType("name", "pub", "specific_system_id");
        
        // When: Getting system ID
        String systemId = doctype.systemId();
        
        // Then: Should return the correct system ID
        assertEquals("Should return the system ID that was set", "specific_system_id", systemId);
        assertEquals("Node name should always be #doctype", "#doctype", doctype.nodeName());
    }

    @Test
    public void shouldReturnEmptyPublicIdWhenNotSet() {
        // Given: A DocumentType with empty public ID
        DocumentType doctype = new DocumentType("", "", "system");
        
        // When: Getting public ID
        String publicId = doctype.publicId();
        
        // Then: Should return empty string
        assertEquals("Empty public ID should return empty string", "", publicId);
        assertEquals("Node name should always be #doctype", "#doctype", doctype.nodeName());
    }

    @Test
    public void shouldReturnCorrectName() {
        // Given: A DocumentType with specific name
        DocumentType doctype = new DocumentType("specific_name", "pub", "sys");
        
        // When: Getting the name
        String name = doctype.name();
        
        // Then: Should return the correct name
        assertEquals("Should return the name that was set", "specific_name", name);
        assertEquals("Node name should always be #doctype", "#doctype", doctype.nodeName());
    }

    @Test
    public void shouldGenerateMinimalDoctypeWhenAllValuesEmpty() {
        // Given: A DocumentType with all empty values
        DocumentType doctype = new DocumentType("", "", "");
        
        // When: Converting to HTML
        String html = doctype.outerHtml();
        
        // Then: Should generate minimal DOCTYPE
        assertEquals("All empty values should generate minimal DOCTYPE", 
                    "<!doctype>", html);
        assertEquals("Node name should always be #doctype", "#doctype", doctype.nodeName());
    }
}