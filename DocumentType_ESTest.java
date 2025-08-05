package org.jsoup.nodes;

import org.junit.Test;

import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link DocumentType} class.
 */
public class DocumentTypeTest {

    /**
     * Tests that the constructor correctly initializes the doctype's name, public ID, and system ID,
     * and that the corresponding getter methods return the correct values.
     */
    @Test
    public void constructorAndGettersWorkCorrectly() {
        // Arrange
        DocumentType docType = new DocumentType("html", "public-id", "system-id");

        // Assert
        assertEquals("html", docType.name());
        assertEquals("public-id", docType.publicId());
        assertEquals("system-id", docType.systemId());
    }

    /**
     * Verifies that the nodeName of a DocumentType is always "#doctype".
     */
    @Test
    public void nodeNameShouldAlwaysBeDoctype() {
        // Arrange
        DocumentType docType = new DocumentType("html", "", "");

        // Act
        String nodeName = docType.nodeName();

        // Assert
        assertEquals("#doctype", nodeName);
    }

    /**
     * Tests that a doctype with a public ID is rendered correctly in HTML.
     */
    @Test
    public void outerHtmlShouldRenderPublicDoctypeCorrectly() {
        // Arrange
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        String expectedHtml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";

        // Act & Assert
        assertEquals(expectedHtml, docType.outerHtml());
    }

    /**
     * Tests that a doctype with an empty public ID but a system ID is rendered with the SYSTEM keyword.
     */
    @Test
    public void outerHtmlShouldRenderSystemDoctypeCorrectly() {
        // Arrange
        DocumentType docType = new DocumentType("html", "", "about:legacy-compat");
        String expectedHtml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";

        // Act & Assert
        assertEquals(expectedHtml, docType.outerHtml());
    }

    /**
     * Tests that a doctype with only a name is rendered correctly.
     */
    @Test
    public void outerHtmlShouldRenderSimpleDoctypeCorrectly() {
        // Arrange
        DocumentType docType = new DocumentType("html", "", "");
        String expectedHtml = "<!DOCTYPE html>";

        // Act & Assert
        assertEquals(expectedHtml, docType.outerHtml());
    }

    /**
     * Tests rendering of a doctype with an empty name, which is a legacy format.
     */
    @Test
    public void outerHtmlShouldRenderEmptyNameDoctypeCorrectly() {
        // Arrange
        DocumentType docType = new DocumentType("", "", "");
        String expectedHtml = "<!doctype>";

        // Act & Assert
        assertEquals(expectedHtml, docType.outerHtml());
    }

    /**
     * Tests that the doctype renders correctly when the output syntax is set to XML.
     */
    @Test
    public void outerHtmlShouldRenderCorrectlyInXmlMode() {
        // Arrange
        DocumentType docType = new DocumentType("html", "public-id", "system-id");
        Document.OutputSettings settings = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml);
        StringWriter writer = new StringWriter();
        String expectedXml = "<!DOCTYPE html PUBLIC \"public-id\" \"system-id\">";

        // Act
        docType.outerHtmlHead(writer, settings);

        // Assert
        assertEquals(expectedXml, writer.toString());
    }

    /**
     * Verifies that setPubSysKey can override the doctype keyword from PUBLIC to SYSTEM.
     */
    @Test
    public void setPubSysKeyCanOverrideToSystemKeyword() {
        // Arrange
        DocumentType docType = new DocumentType("name", "pubId", "sysId");
        assertEquals("<!DOCTYPE name PUBLIC \"pubId\" \"sysId\">", docType.outerHtml());

        // Act
        docType.setPubSysKey("SYSTEM");

        // Assert
        assertEquals("<!DOCTYPE name SYSTEM \"sysId\">", docType.outerHtml());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionForNullPublicId() {
        new DocumentType("name", null, "systemId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionForNullSystemId() {
        new DocumentType("name", "publicId", null);
    }

    @Test(expected = NullPointerException.class)
    public void outerHtmlHeadShouldThrowExceptionForNullAppendable() {
        // Arrange
        DocumentType docType = new DocumentType("name", "", "");
        Document.OutputSettings settings = new Document.OutputSettings();

        // Act
        docType.outerHtmlHead(null, settings);
    }

    @Test(expected = BufferOverflowException.class)
    public void outerHtmlHeadShouldThrowExceptionForSmallBuffer() {
        // Arrange
        DocumentType docType = new DocumentType("name", "publicId", "systemId");
        CharBuffer smallBuffer = CharBuffer.allocate(10); // Buffer is too small for the output
        Document.OutputSettings settings = new Document.OutputSettings();

        // Act
        docType.outerHtmlHead(smallBuffer, settings);
    }

    /**
     * This test validates that an infinite recursion (StackOverflowError) occurs if a node's attribute
     * is modified when it has been set as its own parent. This is an edge case that tests the node's
     * attribute update propagation mechanism.
     */
    @Test(expected = StackOverflowError.class)
    public void setPubSysKeyShouldThrowStackOverflowOnCircularParentage() {
        // Arrange
        DocumentType docType = new DocumentType("", "", "");
        docType.setParentNode(docType); // Create a circular reference

        // Act
        // setPubSysKey modifies an attribute, which triggers a notification to the parent node.
        // Since the node is its own parent, this leads to infinite recursion.
        docType.setPubSysKey("SYSTEM");
    }
}