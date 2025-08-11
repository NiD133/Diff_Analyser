package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for NodeIterator. The tests favor readability:
 * - clear test names
 * - small, focused assertions
 * - helpers to express the expected traversal order
 * - comments describing the scenario under test
 */
public class NodeIteratorTest {

    private static final String HTML_TWO_DIVS =
        "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>";
    private static final String HTML_OUTER =
        "<div id=out1><div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div></div><div id=out2>Out2";

    // Document-order traversal of HTML_TWO_DIVS from the document root
    private static final String[] DOC_ORDER_TOKENS = {
        "#root", "html", "head", "body",
        "div#1", "p", "One", "p", "Two",
        "div#2", "p", "Three", "p", "Four"
    };

    @Test
    void iteratesWholeDocumentInDocumentOrder_thenThrowsWhenExhausted() {
        Document doc = Jsoup.parse(HTML_TWO_DIVS);

        NodeIterator<Node> iterator = NodeIterator.from(doc);
        assertIterates(iterator, DOC_ORDER_TOKENS);
        assertFalse(iterator.hasNext());

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void hasNext_doesNotAdvanceIterator() {
        Document doc = Jsoup.parse(HTML_TWO_DIVS);

        NodeIterator<Node> iterator = NodeIterator.from(doc);
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext()); // should be pure / not advance

        assertIterates(iterator, DOC_ORDER_TOKENS);
        assertFalse(iterator.hasNext());
    }

    @Test
    void iteratesSingleSubtrees_onlyWithinStartNode() {
        Document doc = Jsoup.parse(HTML_TWO_DIVS);

        Element div1 = doc.expectFirst("div#1");
        assertTree(div1, "div#1", "p", "One", "p", "Two");

        Element div2 = doc.expectFirst("div#2");
        assertTree(div2, "div#2", "p", "Three", "p", "Four");
    }

    @Test
    void restart_startsFromNewNode() {
        Document doc = Jsoup.parse(HTML_TWO_DIVS);

        NodeIterator<Node> iterator = NodeIterator.from(doc);
        assertIterates(iterator, DOC_ORDER_TOKENS);

        iterator.restart(doc.expectFirst("div#2"));
        assertIterates(iterator, "div#2", "p", "Three", "p", "Four");
    }

    @Test
    void iteratingSingleElement_returnsElementAndItsChildrenOnly() {
        Document doc = Jsoup.parse(HTML_TWO_DIVS);

        Element pTwo = doc.expectFirst("p:contains(Two)");
        assertEquals("Two", pTwo.text());

        // All node types
        NodeIterator<Node> anyNodeIterator = NodeIterator.from(pTwo);
        assertIterates(anyNodeIterator, "p", "Two");

        // Elements only
        NodeIterator<Element> elementOnlyIterator = new NodeIterator<>(pTwo, Element.class);
        Element found = elementOnlyIterator.next();
        assertSame(pTwo, found);
        assertFalse(elementOnlyIterator.hasNext());
    }

    @Test
    void iteratingEmptyElement_returnsOnlyTheElement() {
        Document doc = Jsoup.parse("<div><p id=1></p><p id=2>.</p><p id=3>..</p>");

        Element p1 = doc.expectFirst("p#1");
        assertTrue(p1.ownText().isEmpty());

        NodeIterator<Node> iterator = NodeIterator.from(p1);
        assertTrue(iterator.hasNext());
        Node node = iterator.next();
        assertSame(p1, node);
        assertFalse(iterator.hasNext());
    }

    @Test
    void supportsRemoveViaIterator() {
        // First pass: remove id=1 while traversing
        Document doc = Jsoup.parse(HTML_OUTER);

        NodeIterator<Node> iterator = NodeIterator.from(doc);
        StringBuilder seen = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.attr("id").equals("1")) {
                iterator.remove();
            }
            appendNodeSignature(node, seen);
        }

        assertEquals(
            expectedSequence("#root", "html", "head", "body", "div#out1", "div#1", "div#2", "p", "Three", "p", "Four", "div#out2", "Out2"),
            seen.toString()
        );
        assertTree(doc, "#root", "html", "head", "body", "div#out1", "div#2", "p", "Three", "p", "Four", "div#out2", "Out2");

        // Second pass: remove id=2 while traversing
        iterator = NodeIterator.from(doc);
        seen = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.attr("id").equals("2")) {
                iterator.remove();
            }
            appendNodeSignature(node, seen);
        }

        assertEquals(
            expectedSequence("#root", "html", "head", "body", "div#out1", "div#2", "div#out2", "Out2"),
            seen.toString()
        );
        assertTree(doc, "#root", "html", "head", "body", "div#out1", "div#out2", "Out2");
    }

    @Test
    void supportsRemoveViaNodeRemove() {
        // First pass: remove id=1 via node.remove()
        Document doc = Jsoup.parse(HTML_OUTER);

        NodeIterator<Node> iterator = NodeIterator.from(doc);
        StringBuilder seen = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.attr("id").equals("1")) {
                node.remove();
            }
            appendNodeSignature(node, seen);
        }

        assertEquals(
            expectedSequence("#root", "html", "head", "body", "div#out1", "div#1", "div#2", "p", "Three", "p", "Four", "div#out2", "Out2"),
            seen.toString()
        );
        assertTree(doc, "#root", "html", "head", "body", "div#out1", "div#2", "p", "Three", "p", "Four", "div#out2", "Out2");

        // Second pass: remove id=2 via node.remove()
        iterator = NodeIterator.from(doc);
        seen = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.attr("id").equals("2")) {
                node.remove();
            }
            appendNodeSignature(node, seen);
        }

        assertEquals(
            expectedSequence("#root", "html", "head", "body", "div#out1", "div#2", "div#out2", "Out2"),
            seen.toString()
        );
        assertTree(doc, "#root", "html", "head", "body", "div#out1", "div#out2", "Out2");
    }

    @Test
    void supportsReplaceWithDuringTraversal() {
        // Replace id=1 with <span>Foo</span>
        Document doc = Jsoup.parse(HTML_OUTER);

        NodeIterator<Node> iterator = NodeIterator.from(doc);
        StringBuilder seen = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            appendNodeSignature(node, seen);
            if (node.attr("id").equals("1")) {
                node.replaceWith(new Element("span").text("Foo"));
            }
        }

        // We don't see the original <p>One subtree; we do see the replacement <span>Foo</span>
        assertEquals(
            expectedSequence("#root", "html", "head", "body", "div#out1", "div#1", "span", "Foo", "div#2", "p", "Three", "p", "Four", "div#out2", "Out2"),
            seen.toString()
        );
        assertTree(doc, "#root", "html", "head", "body", "div#out1", "span", "Foo", "div#2", "p", "Three", "p", "Four", "div#out2", "Out2");

        // Now replace id=2 with <span>Bar</span>
        iterator = NodeIterator.from(doc);
        seen = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            appendNodeSignature(node, seen);
            if (node.attr("id").equals("2")) {
                node.replaceWith(new Element("span").text("Bar"));
            }
        }

        assertEquals(
            expectedSequence("#root", "html", "head", "body", "div#out1", "span", "Foo", "div#2", "span", "Bar", "div#out2", "Out2"),
            seen.toString()
        );
        assertTree(doc, "#root", "html", "head", "body", "div#out1", "span", "Foo", "span", "Bar", "div#out2", "Out2");
    }

    @Test
    void supportsWrapDuringTraversal() {
        Document doc = Jsoup.parse(HTML_TWO_DIVS);

        NodeIterator<Node> iterator = NodeIterator.from(doc);
        boolean sawInnerText = false;

        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.attr("id").equals("1")) {
                node.wrap("<div id=outer>");
            }
            if (node instanceof TextNode && ((TextNode) node).text().equals("One")) {
                sawInnerText = true;
            }
        }

        assertTree(doc, "#root", "html", "head", "body", "div#outer", "div#1", "p", "One", "p", "Two", "div#2", "p", "Three", "p", "Four");
        assertTrue(sawInnerText);
    }

    @Test
    void canFilterForElementsOnly() {
        Document doc = Jsoup.parse(HTML_TWO_DIVS);

        NodeIterator<Element> iterator = new NodeIterator<>(doc, Element.class);
        StringBuilder seen = new StringBuilder();
        while (iterator.hasNext()) {
            Element el = iterator.next();
            assertNotNull(el);
            appendNodeSignature(el, seen);
        }

        assertEquals(expectedSequence("#root", "html", "head", "body", "div#1", "p", "p", "div#2", "p", "p"), seen.toString());
    }

    @Test
    void canFilterForTextNodesOnly() {
        Document doc = Jsoup.parse(HTML_TWO_DIVS);

        NodeIterator<TextNode> iterator = new NodeIterator<>(doc, TextNode.class);
        StringBuilder seen = new StringBuilder();
        while (iterator.hasNext()) {
            TextNode tn = iterator.next();
            assertNotNull(tn);
            appendNodeSignature(tn, seen);
        }

        assertEquals(expectedSequence("One", "Two", "Three", "Four"), seen.toString());
        assertTree(doc, DOC_ORDER_TOKENS); // tree unchanged
    }

    @Test
    void modifyingFilteredElements_updatesTreeDuringTraversal() {
        Document doc = Jsoup.parse(HTML_TWO_DIVS);

        NodeIterator<Element> iterator = new NodeIterator<>(doc, Element.class);
        StringBuilder seen = new StringBuilder();
        while (iterator.hasNext()) {
            Element el = iterator.next();
            if (!el.ownText().isEmpty()) {
                el.text(el.ownText() + "++");
            }
            appendNodeSignature(el, seen);
        }

        assertEquals(expectedSequence("#root", "html", "head", "body", "div#1", "p", "p", "div#2", "p", "p"), seen.toString());
        assertTree(
            doc,
            "#root", "html", "head", "body",
            "div#1", "p", "One++", "p", "Two++",
            "div#2", "p", "Three++", "p", "Four++"
        );
    }

    // ------------------------
    // Helpers
    // ------------------------

    /**
     * Assert that an iterator yields the expected node "signatures" in order.
     * A signature is:
     * - for elements: "tagName" or "tagName#id" if an id is present
     * - for text nodes: the text content
     * - for all other nodes: nodeName()
     */
    private static void assertIterates(Iterator<? extends Node> iterator, String... expectedTokens) {
        Node previous = null;
        StringBuilder actual = new StringBuilder();

        while (iterator.hasNext()) {
            Node node = iterator.next();
            assertNotNull(node);
            assertNotSame(previous, node); // ensure progress
            appendNodeSignature(node, actual);
            previous = node;
        }
        assertEquals(expectedSequence(expectedTokens), actual.toString());
    }

    /**
     * Asserts the traversal order of the subtree rooted at 'root'.
     */
    private static void assertTree(Element root, String... expectedTokens) {
        assertIterates(NodeIterator.from(root), expectedTokens);
    }

    /**
     * Builds the canonical expected sequence string, joining tokens with ';' and appending a trailing ';'.
     */
    private static String expectedSequence(String... tokens) {
        return String.join(";", tokens) + ";";
    }

    /**
     * Append a concise signature of the node to the builder:
     * - Element: tagName[#id]
     * - TextNode: text
     * - Others: nodeName
     */
    private static void appendNodeSignature(Node node, StringBuilder out) {
        if (node instanceof Element) {
            Element el = (Element) node;
            out.append(el.tagName());
            if (el.hasAttr("id")) {
                out.append("#").append(el.id());
            }
        } else if (node instanceof TextNode) {
            out.append(((TextNode) node).text());
        } else {
            out.append(node.nodeName());
        }
        out.append(";");
    }
}