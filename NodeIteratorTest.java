package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class NodeIteratorTest {

    // Helper method: Builds a string representation of visited nodes
    static void trackSeen(Node node, StringBuilder actual) {
        if (node instanceof Element) {
            Element el = (Element) node;
            actual.append(el.tagName());
            if (el.hasAttr("id")) {
                actual.append("#").append(el.id());
            }
        } else if (node instanceof TextNode) {
            actual.append(((TextNode) node).text());
        } else {
            actual.append(node.nodeName());
        }
        actual.append(";");
    }

    // Helper method: Asserts iterator produces expected node sequence
    static <T extends Node> void assertIterates(Iterator<T> it, String expected) {
        StringBuilder actual = new StringBuilder();
        Node previous = null;
        
        while (it.hasNext()) {
            Node node = it.next();
            assertNotNull(node);
            assertNotSame(previous, node);
            trackSeen(node, actual);
            previous = node;
        }
        
        assertEquals(expected, actual.toString());
    }

    // Helper method: Asserts document contains expected node structure
    static void assertContents(Element root, String expected) {
        NodeIterator<Node> it = NodeIterator.from(root);
        assertIterates(it, expected);
    }

    @Test
    void testIterateAllNodesInDocument() {
        String html = "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>";
        Document doc = Jsoup.parse(html);
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        
        // Verify complete node sequence
        assertIterates(iterator, 
            "#root;html;head;body;div#1;p;One;p;Two;div#2;p;Three;p;Four;");
        
        // Verify iterator exhaustion
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testHasNextIsIdempotent() {
        Document doc = Jsoup.parse(
            "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>"
        );
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        
        // Multiple hasNext() calls don't affect iteration
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        
        // Verify full iteration
        assertIterates(iterator, 
            "#root;html;head;body;div#1;p;One;p;Two;div#2;p;Three;p;Four;");
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIterateSubtree() {
        Document doc = Jsoup.parse(
            "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>"
        );

        // Test subtree for first div
        Element div1 = doc.expectFirst("div#1");
        NodeIterator<Node> iterator1 = NodeIterator.from(div1);
        assertIterates(iterator1, "div#1;p;One;p;Two;");
        assertFalse(iterator1.hasNext());

        // Test subtree for second div
        Element div2 = doc.expectFirst("div#2");
        NodeIterator<Node> iterator2 = NodeIterator.from(div2);
        assertIterates(iterator2, "div#2;p;Three;p;Four;");
        assertFalse(iterator2.hasNext());
    }

    @Test
    void testRestartFromDifferentNode() {
        Document doc = Jsoup.parse(
            "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>"
        );
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        
        // First full iteration
        assertIterates(iterator, 
            "#root;html;head;body;div#1;p;One;p;Two;div#2;p;Three;p;Four;");
        
        // Restart from second div
        iterator.restart(doc.expectFirst("div#2"));
        assertIterates(iterator, "div#2;p;Three;p;Four;");
    }

    @Test
    void testIterateSingleNode() {
        Document doc = Jsoup.parse(
            "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>"
        );
        Element p2 = doc.expectFirst("p:contains(Two)");
        assertEquals("Two", p2.text());

        // Test leaf node iteration
        NodeIterator<Node> nodeIterator = NodeIterator.from(p2);
        assertIterates(nodeIterator, "p;Two;");

        // Test filtered element iteration
        NodeIterator<Element> elementIterator = new NodeIterator<>(p2, Element.class);
        Element found = elementIterator.next();
        assertSame(p2, found);
        assertFalse(elementIterator.hasNext());
    }

    @Test
    void testIterateEmptyElement() {
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
    void testRemoveViaIterator() {
        String html = "<div id=out1><div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div></div><div id=out2>Out2";
        Document doc = Jsoup.parse(html);

        // First removal: id=1
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        StringBuilder seen = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.attr("id").equals("1")) {
                iterator.remove();
            }
            trackSeen(node, seen);
        }
        assertEquals("#root;html;head;body;div#out1;div#1;div#2;p;Three;p;Four;div#out2;Out2;", seen.toString());
        assertContents(doc, "#root;html;head;body;div#out1;div#2;p;Three;p;Four;div#out2;Out2;");

        // Second removal: id=2
        iterator = NodeIterator.from(doc);
        seen = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.attr("id").equals("2")) {
                iterator.remove();
            }
            trackSeen(node, seen);
        }
        assertEquals("#root;html;head;body;div#out1;div#2;div#out2;Out2;", seen.toString());
        assertContents(doc, "#root;html;head;body;div#out1;div#out2;Out2;");
    }

    @Test
    void testRemoveViaNodeMethod() {
        String html = "<div id=out1><div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div></div><div id=out2>Out2";
        Document doc = Jsoup.parse(html);

        // First removal: id=1
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        StringBuilder seen = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.attr("id").equals("1")) {
                node.remove();
            }
            trackSeen(node, seen);
        }
        assertEquals("#root;html;head;body;div#out1;div#1;div#2;p;Three;p;Four;div#out2;Out2;", seen.toString());
        assertContents(doc, "#root;html;head;body;div#out1;div#2;p;Three;p;Four;div#out2;Out2;");

        // Second removal: id=2
        iterator = NodeIterator.from(doc);
        seen = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.attr("id").equals("2")) {
                node.remove();
            }
            trackSeen(node, seen);
        }
        assertEquals("#root;html;head;body;div#out1;div#2;div#out2;Out2;", seen.toString());
        assertContents(doc, "#root;html;head;body;div#out1;div#out2;Out2;");
    }

    @Test
    void testReplaceNode() {
        String html = "<div id=out1><div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div></div><div id=out2>Out2";
        Document doc = Jsoup.parse(html);

        // First replacement: id=1 -> span
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        StringBuilder seen = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            trackSeen(node, seen);
            if (node.attr("id").equals("1")) {
                node.replaceWith(new Element("span").text("Foo"));
            }
        }
        assertEquals(
            "#root;html;head;body;div#out1;div#1;span;Foo;div#2;p;Three;p;Four;div#out2;Out2;", 
            seen.toString()
        );
        assertContents(
            doc, 
            "#root;html;head;body;div#out1;span;Foo;div#2;p;Three;p;Four;div#out2;Out2;"
        );

        // Second replacement: id=2 -> span
        iterator = NodeIterator.from(doc);
        seen = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            trackSeen(node, seen);
            if (node.attr("id").equals("2")) {
                node.replaceWith(new Element("span").text("Bar"));
            }
        }
        assertEquals(
            "#root;html;head;body;div#out1;span;Foo;div#2;span;Bar;div#out2;Out2;", 
            seen.toString()
        );
        assertContents(
            doc, 
            "#root;html;head;body;div#out1;span;Foo;span;Bar;div#out2;Out2;"
        );
    }

    @Test
    void testWrapNode() {
        String html = "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>";
        Document doc = Jsoup.parse(html);
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        
        boolean sawTextNode = false;
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.attr("id").equals("1")) {
                node.wrap("<div id=outer>");
            }
            if (node instanceof TextNode && "One".equals(((TextNode) node).text())) {
                sawTextNode = true;
            }
        }
        
        assertTrue(sawTextNode, "Should have visited 'One' text node");
        assertContents(
            doc, 
            "#root;html;head;body;div#outer;div#1;p;One;p;Two;div#2;p;Three;p;Four;"
        );
    }

    @Test
    void testFilterForElements() {
        Document doc = Jsoup.parse(
            "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>"
        );
        NodeIterator<Element> iterator = new NodeIterator<>(doc, Element.class);
        StringBuilder seen = new StringBuilder();
        
        while (iterator.hasNext()) {
            Element element = iterator.next();
            assertNotNull(element);
            trackSeen(element, seen);
        }
        
        assertEquals("#root;html;head;body;div#1;p;p;div#2;p;p;", seen.toString());
    }

    @Test
    void testFilterForTextNodes() {
        Document doc = Jsoup.parse(
            "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>"
        );
        NodeIterator<TextNode> iterator = new NodeIterator<>(doc, TextNode.class);
        StringBuilder seen = new StringBuilder();
        
        while (iterator.hasNext()) {
            TextNode textNode = iterator.next();
            assertNotNull(textNode);
            trackSeen(textNode, seen);
        }
        
        assertEquals("One;Two;Three;Four;", seen.toString());
    }

    @Test
    void testModifyFilteredElements() {
        Document doc = Jsoup.parse(
            "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>"
        );
        NodeIterator<Element> iterator = new NodeIterator<>(doc, Element.class);
        StringBuilder seen = new StringBuilder();
        
        while (iterator.hasNext()) {
            Element element = iterator.next();
            if (!element.ownText().isEmpty()) {
                element.text(element.ownText() + "++");
            }
            trackSeen(element, seen);
        }
        
        assertEquals("#root;html;head;body;div#1;p;p;div#2;p;p;", seen.toString());
        assertContents(
            doc, 
            "#root;html;head;body;div#1;p;One++;p;Two++;div#2;p;Three++;p;Four++;"
        );
    }
}