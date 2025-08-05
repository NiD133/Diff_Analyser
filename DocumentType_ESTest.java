package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.internal.QuietAppendable;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class DocumentType_ESTest extends DocumentType_ESTest_scaffolding {

    private static final String EMPTY_STRING = "";
    private static final String DOCTYPE_NODE_NAME = "#doctype";

    @Test(timeout = 4000)
    public void testOuterHtmlHeadProducesCorrectOutput() throws Throwable {
        DocumentType documentType = new DocumentType("html", "PUBLIC_ID", "SYSTEM_ID");
        StringWriter stringWriter = new StringWriter();
        QuietAppendable quietAppendable = QuietAppendable.wrap(stringWriter);
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentType.outerHtmlHead(quietAppendable, outputSettings);

        assertEquals("<!DOCTYPE html PUBLIC \"PUBLIC_ID\" \"SYSTEM_ID\">", stringWriter.toString());
    }

    @Test(timeout = 4000)
    public void testEmptyDocumentTypeSystemId() throws Throwable {
        DocumentType documentType = new DocumentType(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);

        assertEquals(DOCTYPE_NODE_NAME, documentType.nodeName());
        assertEquals(EMPTY_STRING, documentType.systemId());
    }

    @Test(timeout = 4000)
    public void testDocumentTypePublicId() throws Throwable {
        DocumentType documentType = new DocumentType("SYSTEM", "SYSTEM", "SYSTEM_ID");

        assertEquals("SYSTEM", documentType.publicId());
        assertEquals(DOCTYPE_NODE_NAME, documentType.nodeName());
    }

    @Test(timeout = 4000)
    public void testDocumentTypeNodeName() throws Throwable {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId");

        assertEquals(DOCTYPE_NODE_NAME, documentType.nodeName());
    }

    @Test(timeout = 4000)
    public void testEmptyDocumentTypeName() throws Throwable {
        DocumentType documentType = new DocumentType(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);

        assertEquals(EMPTY_STRING, documentType.name());
        assertEquals(DOCTYPE_NODE_NAME, documentType.nodeName());
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testDocumentTypeWithNullSystemIdThrowsException() throws Throwable {
        new DocumentType(EMPTY_STRING, EMPTY_STRING, null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testOuterHtmlHeadWithNullAppendableThrowsException() throws Throwable {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId");
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentType.outerHtmlHead(null, outputSettings);
    }

    @Test(timeout = 4000, expected = BufferOverflowException.class)
    public void testOuterHtmlHeadWithInsufficientBufferThrowsException() throws Throwable {
        DocumentType documentType = new DocumentType(EMPTY_STRING, "<!doctype", "systemId");
        char[] charArray = new char[4];
        CharBuffer charBuffer = CharBuffer.wrap(charArray);
        QuietAppendable quietAppendable = QuietAppendable.wrap(charBuffer);
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentType.outerHtmlHead(quietAppendable, outputSettings);
    }

    @Test(timeout = 4000)
    public void testDocumentTypeOuterHtml() throws Throwable {
        DocumentType documentType = new DocumentType(EMPTY_STRING, EMPTY_STRING, "systemId");

        assertEquals("<!DOCTYPE SYSTEM \"systemId\">", documentType.outerHtml());
        assertEquals(DOCTYPE_NODE_NAME, documentType.nodeName());
    }

    @Test(timeout = 4000)
    public void testDocumentTypeWithPubSysKey() throws Throwable {
        DocumentType documentType = new DocumentType(EMPTY_STRING, EMPTY_STRING, "<!doctype");
        documentType.setPubSysKey("org.jsoup.nodes.DocumentType");

        assertEquals(DOCTYPE_NODE_NAME, documentType.nodeName());
    }

    @Test(timeout = 4000)
    public void testDocumentTypeWithNullPubSysKey() throws Throwable {
        DocumentType documentType = new DocumentType(EMPTY_STRING, EMPTY_STRING, "<!doctype");
        documentType.setPubSysKey(null);

        assertEquals(DOCTYPE_NODE_NAME, documentType.nodeName());
    }

    @Test(timeout = 4000)
    public void testDocumentTypeSystemIdRetrieval() throws Throwable {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId");

        assertEquals("systemId", documentType.systemId());
        assertEquals(DOCTYPE_NODE_NAME, documentType.nodeName());
    }

    @Test(timeout = 4000)
    public void testDocumentTypePublicIdRetrieval() throws Throwable {
        DocumentType documentType = new DocumentType(EMPTY_STRING, EMPTY_STRING, "<!doctype");

        assertEquals(EMPTY_STRING, documentType.publicId());
        assertEquals(DOCTYPE_NODE_NAME, documentType.nodeName());
    }

    @Test(timeout = 4000)
    public void testDocumentTypeNameRetrieval() throws Throwable {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId");

        assertEquals("name", documentType.name());
        assertEquals(DOCTYPE_NODE_NAME, documentType.nodeName());
    }

    @Test(timeout = 4000)
    public void testEmptyDocumentTypeOuterHtml() throws Throwable {
        DocumentType documentType = new DocumentType(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);

        assertEquals("<!doctype>", documentType.outerHtml());
        assertEquals(DOCTYPE_NODE_NAME, documentType.nodeName());
    }
}