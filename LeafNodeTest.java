package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the lazy initialization of Attributes in Nodes.
 *
 * The core concept is that a Node (whether an Element or a LeafNode) should not create an
 * Attributes object in memory until an attribute is actually written to it. This is an
 * important memory optimization.
 *
 * These tests verify this behavior on both Elements (like <p>) and LeafNodes (like Comment)
 * to ensure the optimization holds.
 */
public class LeafNodeTest {

    private Document doc;
    private Element p;
    private Comment comment;
    private TextNode textNode;

    @BeforeEach
    public void setUp() {
        String html = "<p>One <!-- Two --> Three</p>";
        doc = Jsoup.parse(html);
        p = doc.selectFirst("p");
        assertNotNull(p);
        
        // Get direct references to the leaf nodes for specific testing
        textNode = (TextNode) p.childNode(0);
        comment = (Comment) p.childNode(1);
    }

    @Test
    public void nodesDoNotHaveAttributesUponCreation() {
        // Assert that nodes parsed from HTML do not have an Attributes object by default.
        assertFalse(p.hasAttributes(), "Element should not have attributes after parsing");
        assertFalse(textNode.hasAttributes(), "TextNode should not have attributes after parsing");
        assertFalse(comment.hasAttributes(), "Comment should not have attributes after parsing");
    }

    @Test
    public void readOnlyAttributeOperationsDoNotCreateAttributes() {
        // Arrange: Verify the initial state is no attributes.
        assertFalse(p.hasAttributes());
        assertFalse(comment.hasAttributes());

        // Act & Assert: A series of read-only operations should not trigger attribute creation.
        p.id();
        assertFalse(p.hasAttributes(), "Reading id should not create attributes");

        p.hasClass("some-class");
        assertFalse(p.hasAttributes(), "Checking a class should not create attributes");

        p.attr("title");
        assertFalse(p.hasAttributes(), "Getting an attribute should not create attributes");

        comment.attr("id");
        assertFalse(comment.hasAttributes(), "Getting an attribute on a LeafNode should not create attributes");
    }

    @Test
    public void writeAttributeOperationCreatesAttributes() {
        // Arrange: Start with a node that has no attributes.
        assertFalse(p.hasAttributes());

        // Act: Add a class, which is a write operation.
        p.addClass("foo");

        // Assert: The Attributes object should now exist.
        assertTrue(p.hasAttributes(), "Element should have attributes after a class was added");
        assertTrue(p.hasClass("foo"));
    }
    
    @Test
    public void settingAttributeOnLeafNodeCreatesAttributes() {
        // Arrange: Start with a leaf node that has no attributes.
        assertFalse(comment.hasAttributes());

        // Act: Set an attribute, which is a write operation.
        comment.attr("id", "123");

        // Assert: The Attributes object should now exist on the leaf node.
        assertTrue(comment.hasAttributes(), "LeafNode should have attributes after one was set");
        assertEquals("123", comment.attr("id"));
    }

    @Test
    public void clearAttributesRemovesAttributesObject() {
        // Arrange: Create attributes on an element.
        p.addClass("foo");
        assertTrue(p.hasAttributes(), "Pre-condition: Element should have attributes");

        // Act: Clear the attributes.
        p.clearAttributes();

        // Assert: The node should revert to a state of not having attributes.
        assertFalse(p.hasAttributes(), "Element should not have attributes after clearing");
    }

    @Test
    public void documentWithBaseUriHasAttributesOnCreation() {
        // Arrange: Parsing with a base URI is a special case.
        Document docWithBaseUri = Jsoup.parse("<p>Hi</p>", "https://example.com/");

        // Assert: The document node itself should have attributes to store the base URI.
        assertTrue(docWithBaseUri.hasAttributes());
        assertEquals("https://example.com/", docWithBaseUri.baseUri());
    }
}