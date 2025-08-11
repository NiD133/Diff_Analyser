/*
 * Refactored for clarity and maintainability
 */
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Printer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
) 
public class DocumentType_ESTest extends DocumentType_ESTest_scaffolding {

    // ========================================================================
    // Constructor Tests
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testConstructorWithNullSystemIdThrowsException() {
        try {
            new DocumentType("name", "publicId", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected: systemId must not be null
        }
    }

    // ========================================================================
    // Getter Tests
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testNodeName() {
        DocumentType docType = new DocumentType("name", "pubId", "sysId");
        assertEquals("#doctype", docType.nodeName());
    }

    @Test(timeout = 4000)
    public void testNameGetter() {
        DocumentType docType = new DocumentType("html", "", "");
        assertEquals("html", docType.name());
    }

    @Test(timeout = 4000)
    public void testPublicIdGetter() {
        DocumentType docType = new DocumentType("", "PUB123", "");
        assertEquals("PUB123", docType.publicId());
    }

    @Test(timeout = 4000)
    public void testSystemIdGetter() {
        DocumentType docType = new DocumentType("", "", "SYS456");
        assertEquals("SYS456", docType.systemId());
    }

    @Test(timeout = 4000)
    public void testEmptyGetters() {
        DocumentType docType = new DocumentType("", "", "");
        assertEquals("", docType.name());
        assertEquals("", docType.publicId());
        assertEquals("", docType.systemId());
    }

    // ========================================================================
    // Setter Tests
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testSetPubSysKey() {
        DocumentType docType = new DocumentType("", "", "");
        docType.setPubSysKey("SYSTEM");
        // No getter to verify, just ensure no exception
    }

    @Test(timeout = 4000)
    public void testSetPubSysKeyToNull() {
        DocumentType docType = new DocumentType("", "", "");
        docType.setPubSysKey(null);
        // Should handle null without exception
    }

    @Test(timeout = 4000)
    public void testSetPubSysKeyWithCircularParentThrowsException() {
        DocumentType docType = new DocumentType("", "", "");
        docType.setParentNode(docType); // Create circular reference
        
        try {
            docType.setPubSysKey("key");
            fail("Expected exception due to circular parent");
        } catch (Exception e) {
            // Expected behavior
        }
    }

    // ========================================================================
    // HTML Rendering Tests
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testOuterHtmlWithPublicSystemIds() {
        DocumentType docType = new DocumentType("html", "PUB", "SYS");
        StringWriter writer = new StringWriter();
        QuietAppendable appendable = QuietAppendable.wrap(writer);
        Document.OutputSettings settings = new Document.OutputSettings();
        
        docType.outerHtmlHead(appendable, settings);
        
        assertEquals("<!DOCTYPE html PUBLIC \"PUB\" \"SYS\">", writer.toString());
    }

    @Test(timeout = 4000)
    public void testOuterHtmlWithSystemIdOnly() {
        DocumentType docType = new DocumentType("", "", "SYS");
        assertEquals("<!DOCTYPE SYSTEM \"SYS\">", docType.outerHtml());
    }

    @Test(timeout = 4000)
    public void testOuterHtmlWithAllEmpty() {
        DocumentType docType = new DocumentType("", "", "");
        assertEquals("<!doctype>", docType.outerHtml());
    }

    @Test(timeout = 4000)
    public void testOuterHtmlHeadWithNullAppendableThrowsException() {
        DocumentType docType = new DocumentType("name", "pub", "sys");
        Document.OutputSettings settings = new Document.OutputSettings();
        
        try {
            docType.outerHtmlHead(null, settings);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testOuterHtmlHeadWithSmallBufferThrowsException() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId");
        char[] smallBuffer = new char[10];
        CharBuffer buffer = CharBuffer.wrap(smallBuffer);
        QuietAppendable appendable = QuietAppendable.wrap(buffer);
        Document.OutputSettings settings = new Document.OutputSettings();
        
        try {
            docType.outerHtmlHead(appendable, settings);
            fail("Expected BufferOverflowException");
        } catch (BufferOverflowException e) {
            // Expected when buffer is too small
        }
    }

    // ========================================================================
    // Special Case Tests
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testTraverseWithPrinterInXmlMode() {
        DocumentType docType = new DocumentType("name", "pub", "sys");
        StringBuffer buffer = new StringBuffer("prefix");
        QuietAppendable appendable = QuietAppendable.wrap(buffer);
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.xml);
        Printer.Pretty printer = new Printer.Pretty(docType, appendable, settings);
        
        docType.traverse(printer);
        
        assertEquals("prefix<!DOCTYPE name PUBLIC \"pub\" \"sys\">", buffer.toString());
    }
}