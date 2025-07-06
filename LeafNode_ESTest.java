package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testLeafNodeCreation() {
        LeafNode leafNode = new TextNode("Hello");
        assertNotNull(leafNode);
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new TextNode("Hello");
        assertEquals("Hello", leafNode.coreValue());
    }

    @Test
    public void testSetCoreValue() {
        LeafNode leafNode = new TextNode("Hello");
        leafNode.coreValue("World");
        assertEquals("World", leafNode.coreValue());
    }

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new TextNode("Hello");
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new TextNode("Hello");
        assertNotNull(leafNode.attributes());
    }

    @Test
    public void testParentNode() {
        Document document = new Document("");
        LeafNode leafNode = new TextNode("Hello");
        leafNode.setParentNode(document);
        assertSame(document, leafNode.parent());
    }

    @Test
    public void testNodeValue() {
        LeafNode leafNode = new TextNode("Hello");
        assertEquals("Hello", leafNode.nodeValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new TextNode("Hello");
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new TextNode("Hello");
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new TextNode("Hello");
        leafNode.attr("key", "value");
        leafNode.removeAttr("key");
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new TextNode("Hello");
        Document document = new Document("http://example.com");
        leafNode.setParentNode(document);
        assertEquals("http://example.com", leafNode.absUrl("href"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new TextNode("Hello");
        Document document = new Document("http://example.com");
        leafNode.setParentNode(document);
        assertEquals("http://example.com", leafNode.baseUri());
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new TextNode("Hello");
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test
    public void testEmpty() {
        LeafNode leafNode = new TextNode("Hello");
        assertSame(leafNode, leafNode.empty());
    }

    @Test
    public void testDoClone() {
        LeafNode leafNode = new TextNode("Hello");
        Node parent = new DocumentType("", "", "");
        LeafNode clone = leafNode.doClone(parent);
        assertNotSame(leafNode, clone);
        assertEquals(leafNode.coreValue(), clone.coreValue());
    }

    @Test
    public void testVerifyException() {
        LeafNode leafNode = new TextNode("Hello");
        try {
            leafNode.removeAttr(null);
            fail("Expected exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testNullAttrKey() {
        LeafNode leafNode = new TextNode("Hello");
        try {
            leafNode.attr(null, "value");
            fail("Expected exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testNullAttrValue() {
        LeafNode leafNode = new TextNode("Hello");
        try {
            leafNode.attr("key", null);
            fail("Expected exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}