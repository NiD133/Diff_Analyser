package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class NodeIteratorTest {
    private final String html = "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>";

    @Test
    void iterationTraversesEntireDocument() {
        // Arrange
        Document doc = Jsoup.parse(html);
        NodeIterator<Node> it = NodeIterator.from(doc);

        // Act
        List<String> nodes = collectNodeDescriptions(it);

        // Assert
        List<String> expected = List.of("#root", "html", "head", "body", "div#1", "p", "One", "p", "Two", "div#2", "p", "Three", "p", "Four");
        assertEquals(expected, nodes);

        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next, "Should throw exception when no elements remain.");
    }

    @Test
    void hasNextIsIdempotent() {
        // Arrange
        Document doc = Jsoup.parse(html);
        NodeIterator<Node> it = NodeIterator.from(doc);

        // Act & Assert
        assertTrue(it.hasNext());
        assertTrue(it.hasNext()); // Calling hasNext() again should not change the state

        List<String> nodes = collectNodeDescriptions(it);
        assertEquals(13, nodes.size()); // Full traversal
        assertFalse(it.hasNext());
    }

    @Test
    void iterationCanStartFromSubtree() {
        // Arrange
        Document doc = Jsoup.parse(html);

        // Act & Assert for first subtree
        Element div1 = doc.expectFirst("div#1");
        NodeIterator<Node> it1 = NodeIterator.from(div1);
        assertEquals(List.of("div#1", "p", "One", "p", "Two"), collectNodeDescriptions(it1));
        assertFalse(it1.hasNext());

        // Act & Assert for second subtree
        Element div2 = doc.expectFirst("div#2");
        NodeIterator<Node> it2 = NodeIterator.from(div2);
        assertEquals(List.of("div#2", "p", "Three", "p", "Four"), collectNodeDescriptions(it2));
        assertFalse(it2.hasNext());
    }

    @Test
    void iteratorCanBeRestarted() {
        // Arrange
        Document doc = Jsoup.parse(html);
        NodeIterator<Node> it = NodeIterator.from(doc);
        collectNodeDescriptions(it); // exhaust the iterator
        assertFalse(it.hasNext());

        // Act
        Element restartNode = doc.expectFirst("div#2");
        it.restart(restartNode);

        // Assert
        assertTrue(it.hasNext());
        assertEquals(List.of("div#2", "p", "Three", "p", "Four"), collectNodeDescriptions(it));
    }

    @Test
    void iterationFromElementWithOneTextChild() {
        // Arrange
        Document doc = Jsoup.parse(html);
        Element p2 = doc.expectFirst("p:contains(Two)");

        // Act & Assert for Node iterator
        NodeIterator<Node> nodeIt = NodeIterator.from(p2);
        assertEquals(List.of("p", "Two"), collectNodeDescriptions(nodeIt));

        // Act & Assert for typed Element iterator
        NodeIterator<Element> elIt = new NodeIterator<>(p2, Element.class);
        Element found = elIt.next();
        assertSame(p2, found);
        assertFalse(elIt.hasNext());
    }

    @Test
    void iterationFromEmptyElement() {
        // Arrange
        Document doc = Jsoup.parse("<div><p id=1></p><p id=2>.</p></div>");
        Element p1 = doc.expectFirst("p#1");
        assertTrue(p1.children().isEmpty());

        // Act
        NodeIterator<Node> it = NodeIterator.from(p1);

        // Assert
        assertTrue(it.hasNext());
        assertSame(p1, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void removeViaIteratorSkipsSubtreeAndUpdatesDocument() {
        // Arrange
        String complexHtml = "<div id=out1><div id=1><p>One</p></div><div id=2><p>Two</p></div></div>";
        Document doc = Jsoup.parse(complexHtml);

        // Act 1: remove div#1
        NodeIterator<Node> it = NodeIterator.from(doc);
        List<String> seenNodes1 = new ArrayList<>();
        while (it.hasNext()) {
            Node node = it.next();
            seenNodes1.add(nodeDescription(node));
            if ("1".equals(node.attr("id"))) {
                it.remove();
            }
        }

        // Assert 1: The traversal saw div#1 but correctly skipped its children.
        assertEquals(
            List.of("#root", "html", "head", "body", "div#out1", "div#1", "div#2", "p", "Two"),
            seenNodes1);
        // And the document was modified correctly.
        assertDocumentStructure(doc,
            "#root", "html", "head", "body", "div#out1", "div#2", "p", "Two");

        // Act 2: now remove div#2 from the modified document
        it.restart(doc);
        List<String> seenNodes2 = new ArrayList<>();
        while (it.hasNext()) {
            Node node = it.next();
            seenNodes2.add(nodeDescription(node));
            if ("2".equals(node.attr("id"))) {
                it.remove();
            }
        }

        // Assert 2: Traversal saw div#2 and skipped its children.
        assertEquals(
            List.of("#root", "html", "head", "body", "div#out1", "div#2"),
            seenNodes2);
        // And the document is now further modified.
        assertDocumentStructure(doc,
            "#root", "html", "head", "body", "div#out1");
    }

    @Test
    void removeViaNodeOnTheFlyDoesNotBreakIteration() {
        // Arrange
        String complexHtml = "<div id=out1><div id=1><p>One</p></div><div id=2><p>Two</p></div></div>";
        Document doc = Jsoup.parse(complexHtml);

        // Act
        NodeIterator<Node> it = NodeIterator.from(doc);
        List<String> seenNodes = new ArrayList<>();
        while (it.hasNext()) {
            Node node = it.next();
            seenNodes.add(nodeDescription(node));
            if ("1".equals(node.attr("id"))) {
                node.remove(); // remove the node directly, not via iterator
            }
        }

        // Assert: The traversal continued correctly, even after the node was removed.
        // Unlike removeViaIterator, the iterator had already queued the children of div#1, so they are seen.
        assertEquals(
            List.of("#root", "html", "head", "body", "div#out1", "div#1", "p", "One", "div#2", "p", "Two"),
            seenNodes);
        // The document is still modified as expected.
        assertDocumentStructure(doc,
            "#root", "html", "head", "body", "div#out1", "div#2", "p", "Two");
    }

    @Test
    void replaceNodeOnTheFlyUpdatesTraversal() {
        // Arrange
        String complexHtml = "<div id=out1><div id=1><p>One</p></div><div id=2><p>Two</p></div></div>";
        Document doc = Jsoup.parse(complexHtml);
        NodeIterator<Node> it = NodeIterator.from(doc);
        List<String> seenNodes = new ArrayList<>();

        // Act
        while (it.hasNext()) {
            Node node = it.next();
            seenNodes.add(nodeDescription(node));
            if ("1".equals(node.attr("id"))) {
                node.replaceWith(new Element("span").text("Replaced"));
            }
        }

        // Assert: The traversal saw the original div#1, then continued from the new replacement node.
        assertEquals(
            List.of("#root", "html", "head", "body", "div#out1", "div#1", "span", "Replaced", "div#2", "p", "Two"),
            seenNodes);
        // The document reflects the final replaced state.
        assertDocumentStructure(doc,
            "#root", "html", "head", "body", "div#out1", "span", "Replaced", "div#2", "p", "Two");
    }

    @Test
    void wrapNodeOnTheFlyIsCorrectlyTraversed() {
        // Arrange
        Document doc = Jsoup.parse(html);
        NodeIterator<Node> it = NodeIterator.from(doc);
        boolean sawInnerNode = false;

        // Act
        while (it.hasNext()) {
            Node node = it.next();
            if ("1".equals(node.attr("id"))) {
                node.wrap("<div id=wrapper>");
            }
            if (node instanceof TextNode && "One".equals(((TextNode) node).text())) {
                sawInnerNode = true;
            }
        }

        // Assert
        assertTrue(sawInnerNode, "Iterator should traverse into the wrapped node's children.");
        assertDocumentStructure(doc,
            "#root", "html", "head", "body", "div#wrapper", "div#1", "p", "One", "p", "Two", "div#2", "p", "Three", "p", "Four");
    }

    @Test
    void iteratorCanBeFilteredToReturnOnlyElements() {
        // Arrange
        Document doc = Jsoup.parse(html);
        NodeIterator<Element> it = new NodeIterator<>(doc, Element.class);

        // Act
        List<String> elements = collectNodeDescriptions(it);

        // Assert
        assertEquals(
            List.of("#root", "html", "head", "body", "div#1", "p", "p", "div#2", "p", "p"),
            elements);
    }

    @Test
    void iteratorCanBeFilteredToReturnOnlyTextNodes() {
        // Arrange
        Document doc = Jsoup.parse(html);
        NodeIterator<TextNode> it = new NodeIterator<>(doc, TextNode.class);

        // Act
        List<String> textNodes = collectNodeDescriptions(it);

        // Assert
        assertEquals(List.of("One", "Two", "Three", "Four"), textNodes);
    }

    @Test
    void nodesCanBeModifiedDuringFilteredIteration() {
        // Arrange
        Document doc = Jsoup.parse(html);
        NodeIterator<Element> it = new NodeIterator<>(doc, Element.class);

        // Act
        while (it.hasNext()) {
            Element el = it.next();
            if (!el.ownText().isEmpty()) {
                el.text(el.ownText() + "++");
            }
        }

        // Assert
        assertDocumentStructure(doc,
            "#root", "html", "head", "body", "div#1", "p", "One++", "p", "Two++", "div#2", "p", "Three++", "p", "Four++");
    }

    // --- Helper Methods ---

    /**
     * Creates a simple, readable string description of a Node for use in assertions.
     */
    private static String nodeDescription(Node node) {
        if (node instanceof Element) {
            Element el = (Element) node;
            String desc = el.tagName();
            if (el.hasAttr("id")) {
                desc += "#" + el.id();
            }
            return desc;
        }
        if (node instanceof TextNode) {
            return ((TextNode) node).text();
        }
        return node.nodeName();
    }

    /**
     * Consumes an iterator and collects the descriptions of each node into a List.
     */
    private static <T extends Node> List<String> collectNodeDescriptions(Iterator<T> it) {
        List<String> descriptions = new ArrayList<>();
        it.forEachRemaining(node -> descriptions.add(nodeDescription(node)));
        return descriptions;
    }

    /**
     * Asserts that the full traversal of an element matches the expected node descriptions.
     */
    private static void assertDocumentStructure(Element el, String... expectedDescriptions) {
        NodeIterator<Node> it = NodeIterator.from(el);
        List<String> actual = collectNodeDescriptions(it);
        assertEquals(List.of(expectedDescriptions), actual);
    }
}