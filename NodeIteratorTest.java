package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class NodeIteratorTest {
    private static final String HTML = "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>";

    @Test
    void testNodeIteration() {
        Document document = Jsoup.parse(HTML);
        NodeIterator<Node> iterator = NodeIterator.from(document);
        assertNodeSequence(iterator, "#root;html;head;body;div#1;p;One;p;Two;div#2;p;Three;p;Four;");
        assertFalse(iterator.hasNext());

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testHasNextIsIdempotent() {
        Document document = Jsoup.parse(HTML);
        NodeIterator<Node> iterator = NodeIterator.from(document);
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertNodeSequence(iterator, "#root;html;head;body;div#1;p;One;p;Two;div#2;p;Three;p;Four;");
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIterateSubTree() {
        Document document = Jsoup.parse(HTML);

        Element firstDiv = document.expectFirst("div#1");
        NodeIterator<Node> iterator1 = NodeIterator.from(firstDiv);
        assertNodeSequence(iterator1, "div#1;p;One;p;Two;");
        assertFalse(iterator1.hasNext());

        Element secondDiv = document.expectFirst("div#2");
        NodeIterator<Node> iterator2 = NodeIterator.from(secondDiv);
        assertNodeSequence(iterator2, "div#2;p;Three;p;Four;");
        assertFalse(iterator2.hasNext());
    }

    @Test
    void testRestartIterator() {
        Document document = Jsoup.parse(HTML);

        NodeIterator<Node> iterator = NodeIterator.from(document);
        assertNodeSequence(iterator, "#root;html;head;body;div#1;p;One;p;Two;div#2;p;Three;p;Four;");

        iterator.restart(document.expectFirst("div#2"));
        assertNodeSequence(iterator, "div#2;p;Three;p;Four;");
    }

    @Test
    void testIterateSingleSibling() {
        Document document = Jsoup.parse(HTML);
        Element paragraphTwo = document.expectFirst("p:contains(Two)");
        assertEquals("Two", paragraphTwo.text());

        NodeIterator<Node> iterator = NodeIterator.from(paragraphTwo);
        assertNodeSequence(iterator, "p;Two;");

        NodeIterator<Element> elementIterator = new NodeIterator<>(paragraphTwo, Element.class);
        Element foundElement = elementIterator.next();
        assertSame(paragraphTwo, foundElement);
        assertFalse(elementIterator.hasNext());
    }

    @Test
    void testIterateFirstEmptySibling() {
        Document document = Jsoup.parse("<div><p id=1></p><p id=2>.</p><p id=3>..</p>");
        Element firstParagraph = document.expectFirst("p#1");
        assertEquals("", firstParagraph.ownText());

        NodeIterator<Node> iterator = NodeIterator.from(firstParagraph);
        assertTrue(iterator.hasNext());
        Node node = iterator.next();
        assertSame(firstParagraph, node);
        assertFalse(iterator.hasNext());
    }

    @Test
    void testRemoveNodeViaIterator() {
        String htmlContent = "<div id=out1><div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div></div><div id=out2>Out2";
        Document document = Jsoup.parse(htmlContent);

        NodeIterator<Node> iterator = NodeIterator.from(document);
        StringBuilder seenNodes = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.attr("id").equals("1")) {
                iterator.remove();
            }
            trackSeenNodes(node, seenNodes);
        }
        assertEquals("#root;html;head;body;div#out1;div#1;div#2;p;Three;p;Four;div#out2;Out2;", seenNodes.toString());
        assertDocumentContents(document, "#root;html;head;body;div#out1;div#2;p;Three;p;Four;div#out2;Out2;");

        iterator = NodeIterator.from(document);
        seenNodes = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.attr("id").equals("2")) {
                iterator.remove();
            }
            trackSeenNodes(node, seenNodes);
        }
        assertEquals("#root;html;head;body;div#out1;div#2;div#out2;Out2;", seenNodes.toString());
        assertDocumentContents(document, "#root;html;head;body;div#out1;div#out2;Out2;");
    }

    @Test
    void testRemoveNodeDirectly() {
        String htmlContent = "<div id=out1><div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div></div><div id=out2>Out2";
        Document document = Jsoup.parse(htmlContent);

        NodeIterator<Node> iterator = NodeIterator.from(document);
        StringBuilder seenNodes = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.attr("id").equals("1")) {
                node.remove();
            }
            trackSeenNodes(node, seenNodes);
        }
        assertEquals("#root;html;head;body;div#out1;div#1;div#2;p;Three;p;Four;div#out2;Out2;", seenNodes.toString());
        assertDocumentContents(document, "#root;html;head;body;div#out1;div#2;p;Three;p;Four;div#out2;Out2;");

        iterator = NodeIterator.from(document);
        seenNodes = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.attr("id").equals("2")) {
                node.remove();
            }
            trackSeenNodes(node, seenNodes);
        }
        assertEquals("#root;html;head;body;div#out1;div#2;div#out2;Out2;", seenNodes.toString());
        assertDocumentContents(document, "#root;html;head;body;div#out1;div#out2;Out2;");
    }

    @Test
    void testReplaceNode() {
        String htmlContent = "<div id=out1><div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div></div><div id=out2>Out2";
        Document document = Jsoup.parse(htmlContent);

        NodeIterator<Node> iterator = NodeIterator.from(document);
        StringBuilder seenNodes = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            trackSeenNodes(node, seenNodes);
            if (node.attr("id").equals("1")) {
                node.replaceWith(new Element("span").text("Foo"));
            }
        }
        assertEquals("#root;html;head;body;div#out1;div#1;span;Foo;div#2;p;Three;p;Four;div#out2;Out2;", seenNodes.toString());
        assertDocumentContents(document, "#root;html;head;body;div#out1;span;Foo;div#2;p;Three;p;Four;div#out2;Out2;");

        iterator = NodeIterator.from(document);
        seenNodes = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            trackSeenNodes(node, seenNodes);
            if (node.attr("id").equals("2")) {
                node.replaceWith(new Element("span").text("Bar"));
            }
        }
        assertEquals("#root;html;head;body;div#out1;span;Foo;div#2;span;Bar;div#out2;Out2;", seenNodes.toString());
        assertDocumentContents(document, "#root;html;head;body;div#out1;span;Foo;span;Bar;div#out2;Out2;");
    }

    @Test
    void testWrapNode() {
        Document document = Jsoup.parse(HTML);
        NodeIterator<Node> iterator = NodeIterator.from(document);
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
        assertDocumentContents(document, "#root;html;head;body;div#outer;div#1;p;One;p;Two;div#2;p;Three;p;Four;");
        assertTrue(sawInnerText);
    }

    @Test
    void testFilterForElements() {
        Document document = Jsoup.parse(HTML);
        NodeIterator<Element> iterator = new NodeIterator<>(document, Element.class);

        StringBuilder seenElements = new StringBuilder();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            assertNotNull(element);
            trackSeenNodes(element, seenElements);
        }

        assertEquals("#root;html;head;body;div#1;p;p;div#2;p;p;", seenElements.toString());
    }

    @Test
    void testFilterForTextNodes() {
        Document document = Jsoup.parse(HTML);
        NodeIterator<TextNode> iterator = new NodeIterator<>(document, TextNode.class);

        StringBuilder seenTextNodes = new StringBuilder();
        while (iterator.hasNext()) {
            TextNode textNode = iterator.next();
            assertNotNull(textNode);
            trackSeenNodes(textNode, seenTextNodes);
        }

        assertEquals("One;Two;Three;Four;", seenTextNodes.toString());
        assertDocumentContents(document, "#root;html;head;body;div#1;p;One;p;Two;div#2;p;Three;p;Four;");
    }

    @Test
    void testModifyFilteredElements() {
        Document document = Jsoup.parse(HTML);
        NodeIterator<Element> iterator = new NodeIterator<>(document, Element.class);

        StringBuilder seenElements = new StringBuilder();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            if (!element.ownText().isEmpty()) {
                element.text(element.ownText() + "++");
            }
            trackSeenNodes(element, seenElements);
        }

        assertEquals("#root;html;head;body;div#1;p;p;div#2;p;p;", seenElements.toString());
        assertDocumentContents(document, "#root;html;head;body;div#1;p;One++;p;Two++;div#2;p;Three++;p;Four++;");
    }

    private static <T extends Node> void assertNodeSequence(Iterator<T> iterator, String expectedSequence) {
        Node previousNode = null;
        StringBuilder actualSequence = new StringBuilder();
        while (iterator.hasNext()) {
            Node currentNode = iterator.next();
            assertNotNull(currentNode);
            assertNotSame(previousNode, currentNode);

            trackSeenNodes(currentNode, actualSequence);
            previousNode = currentNode;
        }
        assertEquals(expectedSequence, actualSequence.toString());
    }

    private static void assertDocumentContents(Element element, String expectedContents) {
        NodeIterator<Node> iterator = NodeIterator.from(element);
        assertNodeSequence(iterator, expectedContents);
    }

    private static void trackSeenNodes(Node node, StringBuilder actualSequence) {
        if (node instanceof Element) {
            Element element = (Element) node;
            actualSequence.append(element.tagName());
            if (element.hasAttr("id")) {
                actualSequence.append("#").append(element.id());
            }
        } else if (node instanceof TextNode) {
            actualSequence.append(((TextNode) node).text());
        } else {
            actualSequence.append(node.nodeName());
        }
        actualSequence.append(";");
    }
}