package org.jsoup.nodes;

import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for the abstract {@link LeafNode} class.
 * Tests are conducted using concrete subclasses like {@link TextNode}, {@link CDataNode}, and {@link Comment}.
 */
public class LeafNodeTest {

    // A concrete LeafNode implementation for general testing.
    private final TextNode textNode = new TextNode("Sample text");

    @Test
    public void childNodeSizeIsAlwaysZero() {
        // Arrange: LeafNodes cannot have children.
        // Act
        int size = textNode.childNodeSize();

        // Assert
        assertEquals(0, size);
    }

    @Test
    public void ensureChildNodesReturnsAnEmptyList() {
        // Arrange: LeafNodes cannot have children.
        // Act & Assert
        assertTrue("ensureChildNodes should return an empty list for a leaf node.", textNode.ensureChildNodes().isEmpty());
    }

    @Test
    public void emptyIsANoOpAndReturnsSameInstance() {
        // Arrange
        Element parent = new Element("div");
        parent.appendChild(textNode);

        // Act
        Node result = textNode.empty();

        // Assert
        assertSame("empty() should return the same node instance.", textNode, result);
        assertTrue("Parent should not be changed.", textNode.hasParent());
    }

    @Test
    public void hasAttributesIsFalseOnNewNode() {
        // Arrange: A new node has no attributes.
        // Act
        boolean hasAttributes = textNode.hasAttributes();

        // Assert
        assertFalse(hasAttributes);
    }



    @Test
    public void hasAttributesIsTrueAfterAddingAttribute() {
        // Arrange
        textNode.attr("id", "1");

        // Act
        boolean hasAttributes = textNode.hasAttributes();

        // Assert
        assertTrue(hasAttributes);
    }

    @Test
    public void attrSetterAndGetter() {
        // Arrange
        String key = "data-test";
        String value = "foo";

        // Act
        Node returnedNode = textNode.attr(key, value);

        // Assert
        assertSame("attr() should return the same node instance.", textNode, returnedNode);
        assertTrue(textNode.hasAttr(key));
        assertEquals(value, textNode.attr(key));
    }

    @Test
    public void attrUpdatesExistingAttributeValue() {
        // Arrange
        String key = "id";
        textNode.attr(key, "1");

        // Act
        textNode.attr(key, "2");

        // Assert
        assertEquals("Attribute value should be updated.", "2", textNode.attr(key));
    }

    @Test
    public void attrGetterForNonExistentKeyReturnsEmptyString() {
        // Arrange: Node has no attributes.
        // Act
        String value = textNode.attr("non-existent-key");

        // Assert
        assertEquals("", value);
    }



    @Test
    public void removeAttrRemovesAttribute() {
        // Arrange
        String key = "id";
        textNode.attr(key, "1");
        assertTrue("Attribute should exist before removal.", textNode.hasAttr(key));

        // Act
        Node returnedNode = textNode.removeAttr(key);

        // Assert
        assertSame("removeAttr() should return the same node instance.", textNode, returnedNode);
        assertFalse("Attribute should not exist after removal.", textNode.hasAttr(key));
    }

    @Test
    public void hasAttrReturnsTrueForNodeNameAsAttribute() {
        // Arrange
        CDataNode cdata = new CDataNode("cdata content");
        TextNode text = new TextNode("text content");

        // Act & Assert
        assertTrue("CDataNode should virtually have a '#cdata' attribute.", cdata.hasAttr("#cdata"));
        assertTrue("TextNode should virtually have a '#text' attribute.", text.hasAttr("#text"));
        assertFalse("Should not have an arbitrary attribute.", text.hasAttr("foo"));
    }

    @Test
    public void attrGetterForNodeNameKeyReturnsCoreValue() {
        // Arrange
        CDataNode cdata = new CDataNode("cdata content");
        TextNode text = new TextNode("text content");

        // Act & Assert
        assertEquals("cdata content", cdata.attr("#cdata"));
        assertEquals("text content", text.attr("#text"));
    }

    @Test
    public void attrSetterWithNodeNameKeyUpdatesCoreValue() {
        // Arrange
        textNode.attr("#text", "new content");

        // Act
        String coreValue = textNode.getWholeText(); // or textNode.coreValue()

        // Assert
        assertEquals("new content", coreValue);
        assertFalse("Setting virtual attribute should not create a real one.", textNode.attributes().hasKey("#text"));
    }

    @Test
    public void baseUriIsEmptyStringForStandaloneNode() {
        // Arrange: A node with no parent and no explicit base URI.
        // Act
        String baseUri = textNode.baseUri();

        // Assert
        assertEquals("", baseUri);
    }

    @Test
    public void baseUriFallsBackToParentsBaseUri() {
        // Arrange
        String parentBaseUri = "https://example.com/";
        Document doc = new Document(parentBaseUri);
        doc.body().appendChild(textNode);

        // Act
        String baseUri = textNode.baseUri();

        // Assert
        assertEquals(parentBaseUri, baseUri);
    }

    @Test
    public void absUrlResolvesUrlAgainstBaseUri() {
        // Arrange
        String baseUri = "https://example.com/path/";
        textNode.doSetBaseUri(baseUri);
        textNode.attr("href", "page.html");
        textNode.attr("src", "/image.jpg");
        textNode.attr("data-abs", "https://jsoup.org/");

        // Act & Assert
        assertEquals("https://example.com/path/page.html", textNode.absUrl("href"));
        assertEquals("https://example.com/image.jpg", textNode.absUrl("src"));
        assertEquals("https://jsoup.org/", textNode.absUrl("data-abs"));
    }

    @Test
    public void absUrlForNonExistentAttributeReturnsEmptyString() {
        // Arrange
        textNode.doSetBaseUri("https://example.com");

        // Act
        String absUrl = textNode.absUrl("non-existent-key");

        // Assert
        assertEquals("", absUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void absUrlWithEmptyKeyThrowsException() {
        // Arrange, Act, Assert
        textNode.absUrl("");
    }

    @Test
    public void shallowCloneCopiesValueAndAttributesButNotParent() {
        // Arrange
        Element parent = new Element("div");
        parent.appendChild(textNode);
        textNode.attr("id", "1");

        // Act
        Node clone = textNode.shallowClone();

        // Assert
        assertNotSame("Clone should be a new object.", textNode, clone);
        assertFalse("Clone should not have a parent.", clone.hasParent());
        assertEquals("Core value should be cloned.", ((TextNode) textNode).getWholeText(), ((TextNode) clone).getWholeText());
        assertEquals("Attributes should be cloned.", "1", clone.attr("id"));
    }
}