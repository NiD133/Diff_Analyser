package org.jsoup.nodes;

import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Focused, readable tests for common LeafNode behaviors using concrete subclasses.
 *
 * These tests avoid poking internal state and instead exercise public API that all LeafNodes share:
 * - children (none)
 * - attributes lifecycle (none by default, set/remove, chainable)
 * - baseUri and absUrl handling
 * - special pseudo-attribute behavior for CDataNode (#cdata)
 * - defensive argument validation
 * - no-op empty() semantics
 */
public class LeafNodeTest {

    @Test
    public void leafNodesHaveNoChildren() {
        CDataNode node = new CDataNode("content");

        assertEquals(0, node.childNodeSize());

        // ensureChildNodes is a protected API but visible in-package; should be empty for all leaf nodes
        List<Node> children = node.ensureChildNodes();
        assertTrue(children.isEmpty());
    }

    @Test
    public void attributes_areEmptyByDefault_andToggleWithSetRemove() {
        TextNode node = new TextNode("hello");

        assertFalse(node.hasAttributes());

        node.attr("data-flag", "on");
        assertTrue(node.hasAttributes());
        assertEquals("on", node.attributes().get("data-flag"));

        // remove returns the same instance (fluent/chainable)
        Node same = node.removeAttr("data-flag");
        assertSame(node, same);
        assertFalse(node.hasAttributes());
    }

    @Test
    public void removeAttrOnMissingKey_isNoOpAndChainable() {
        Comment node = new Comment("note");

        Node same = node.removeAttr("does-not-exist");
        assertSame(node, same);
        assertFalse(node.hasAttributes());
    }

    @Test
    public void baseUri_isInheritedFromParentDocument() {
        // Create a document with a base URI and attach a leaf node to it
        String base = "https://example.org/base/";
        Document doc = Parser.parseBodyFragment("", base);
        TextNode text = new TextNode("hi");
        doc.body().appendChild(text);

        assertEquals(base, text.baseUri());
    }

    @Test
    public void baseUri_isEmptyWhenDetached() {
        CDataNode node = new CDataNode("content");
        assertEquals("", node.baseUri());
    }

    @Test
    public void absUrl_missingAttributeReturnsEmptyString() {
        // Leaf nodes can hold attributes but absUrl on a missing key should return empty string
        Comment node = new Comment("note");
        assertEquals("", node.absUrl("href"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void absUrl_rejectsEmptyKey() {
        new CDataNode("x").absUrl("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void attr_rejectsNullKey() {
        new Comment("x").attr(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeAttr_rejectsNullKey() {
        new DataNode("x").removeAttr(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hasAttr_rejectsNullKey() {
        new TextNode("x").hasAttr(null);
    }

    @Test
    public void empty_isNoOpAndReturnsSameInstance() {
        DataNode node = new DataNode("payload");
        Node same = node.empty();

        assertSame(node, same);
        assertEquals(0, node.childNodeSize());
        assertFalse(node.hasAttributes());
    }

    @Test
    public void cdata_exposesContentViaPseudoAttribute() {
        CDataNode node = new CDataNode("raw-xml");
        assertTrue(node.hasAttr("#cdata"));
        assertEquals("raw-xml", node.attr("#cdata"));
    }
}