package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class NodeIteratorTest {
    
    // Test HTML structure: Two divs, each containing two paragraphs with text
    private static final String TEST_HTML = "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>";

    @Test 
    void shouldIterateThroughAllNodesInDocumentOrder() {
        Document doc = Jsoup.parse(TEST_HTML);
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        
        // Should traverse: #root -> html -> head -> body -> div#1 -> p -> "One" -> p -> "Two" -> div#2 -> p -> "Three" -> p -> "Four"
        String expectedTraversal = "#root;html;head;body;div#1;p;One;p;Two;div#2;p;Three;p;Four;";
        assertIteratorTraversesNodes(iterator, expectedTraversal);
        
        assertIteratorIsExhausted(iterator);
    }

    @Test 
    void shouldNotChangeStateWhenCallingHasNextMultipleTimes() {
        Document doc = Jsoup.parse(TEST_HTML);
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        
        // Calling hasNext() multiple times should not advance the iterator
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        
        // Iterator should still traverse all nodes correctly
        String expectedTraversal = "#root;html;head;body;div#1;p;One;p;Two;div#2;p;Three;p;Four;";
        assertIteratorTraversesNodes(iterator, expectedTraversal);
        assertFalse(iterator.hasNext());
    }

    @Test 
    void shouldIterateOnlyWithinSpecifiedSubtree() {
        Document doc = Jsoup.parse(TEST_HTML);

        // Test iteration starting from first div
        Element firstDiv = doc.expectFirst("div#1");
        NodeIterator<Node> firstDivIterator = NodeIterator.from(firstDiv);
        assertIteratorTraversesNodes(firstDivIterator, "div#1;p;One;p;Two;");
        assertFalse(firstDivIterator.hasNext());

        // Test iteration starting from second div
        Element secondDiv = doc.expectFirst("div#2");
        NodeIterator<Node> secondDivIterator = NodeIterator.from(secondDiv);
        assertIteratorTraversesNodes(secondDivIterator, "div#2;p;Three;p;Four;");
        assertFalse(secondDivIterator.hasNext());
    }

    @Test 
    void shouldAllowRestartingFromDifferentNode() {
        Document doc = Jsoup.parse(TEST_HTML);

        NodeIterator<Node> iterator = NodeIterator.from(doc);
        // First, iterate through entire document
        String fullDocumentTraversal = "#root;html;head;body;div#1;p;One;p;Two;div#2;p;Three;p;Four;";
        assertIteratorTraversesNodes(iterator, fullDocumentTraversal);

        // Restart from second div and verify it only iterates that subtree
        iterator.restart(doc.expectFirst("div#2"));
        assertIteratorTraversesNodes(iterator, "div#2;p;Three;p;Four;");
    }

    @Test 
    void shouldIterateSingleNodeWithChildren() {
        Document doc = Jsoup.parse(TEST_HTML);
        Element paragraphWithText = doc.expectFirst("p:contains(Two)");
        assertEquals("Two", paragraphWithText.text());

        // Iterator should traverse the paragraph element and its text content
        NodeIterator<Node> nodeIterator = NodeIterator.from(paragraphWithText);
        assertIteratorTraversesNodes(nodeIterator, "p;Two;");

        // Element-only iterator should return just the paragraph element
        NodeIterator<Element> elementIterator = new NodeIterator<>(paragraphWithText, Element.class);
        Element foundElement = elementIterator.next();
        assertSame(paragraphWithText, foundElement);
        assertFalse(elementIterator.hasNext());
    }

    @Test 
    void shouldIterateEmptyElementCorrectly() {
        Document doc = Jsoup.parse("<div><p id=1></p><p id=2>.</p><p id=3>..</p>");
        Element emptyParagraph = doc.expectFirst("p#1");
        assertEquals("", emptyParagraph.ownText());

        NodeIterator<Node> iterator = NodeIterator.from(emptyParagraph);
        assertTrue(iterator.hasNext());
        Node foundNode = iterator.next();
        assertSame(emptyParagraph, foundNode);
        assertFalse(iterator.hasNext());
    }

    @Test 
    void shouldSupportRemovalViaIteratorRemoveMethod() {
        String htmlWithNestedDivs = "<div id=out1><div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div></div><div id=out2>Out2";
        Document doc = Jsoup.parse(htmlWithNestedDivs);

        // Remove div with id=1 using iterator.remove()
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        StringBuilder visitedNodes = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if ("1".equals(node.attr("id"))) {
                iterator.remove();
            }
            recordVisitedNode(node, visitedNodes);
        }
        
        String expectedVisitedNodes = "#root;html;head;body;div#out1;div#1;div#2;p;Three;p;Four;div#out2;Out2;";
        assertEquals(expectedVisitedNodes, visitedNodes.toString());
        
        String expectedRemainingStructure = "#root;html;head;body;div#out1;div#2;p;Three;p;Four;div#out2;Out2;";
        assertDocumentStructure(doc, expectedRemainingStructure);

        // Remove div with id=2 using iterator.remove()
        iterator = NodeIterator.from(doc);
        visitedNodes = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if ("2".equals(node.attr("id"))) {
                iterator.remove();
            }
            recordVisitedNode(node, visitedNodes);
        }
        
        assertEquals("#root;html;head;body;div#out1;div#2;div#out2;Out2;", visitedNodes.toString());
        assertDocumentStructure(doc, "#root;html;head;body;div#out1;div#out2;Out2;");
    }

    @Test 
    void shouldSupportRemovalViaNodeRemoveMethod() {
        String htmlWithNestedDivs = "<div id=out1><div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div></div><div id=out2>Out2";
        Document doc = Jsoup.parse(htmlWithNestedDivs);

        // Remove div with id=1 using node.remove()
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        StringBuilder visitedNodes = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if ("1".equals(node.attr("id"))) {
                node.remove();
            }
            recordVisitedNode(node, visitedNodes);
        }
        
        String expectedVisitedNodes = "#root;html;head;body;div#out1;div#1;div#2;p;Three;p;Four;div#out2;Out2;";
        assertEquals(expectedVisitedNodes, visitedNodes.toString());
        
        String expectedRemainingStructure = "#root;html;head;body;div#out1;div#2;p;Three;p;Four;div#out2;Out2;";
        assertDocumentStructure(doc, expectedRemainingStructure);

        // Remove div with id=2 using node.remove()
        iterator = NodeIterator.from(doc);
        visitedNodes = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if ("2".equals(node.attr("id"))) {
                node.remove();
            }
            recordVisitedNode(node, visitedNodes);
        }
        
        assertEquals("#root;html;head;body;div#out1;div#2;div#out2;Out2;", visitedNodes.toString());
        assertDocumentStructure(doc, "#root;html;head;body;div#out1;div#out2;Out2;");
    }

    @Test 
    void shouldSupportNodeReplacementDuringIteration() {
        String htmlWithNestedDivs = "<div id=out1><div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div></div><div id=out2>Out2";
        Document doc = Jsoup.parse(htmlWithNestedDivs);

        // Replace div with id=1 with a span containing "Foo"
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        StringBuilder visitedNodes = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            recordVisitedNode(node, visitedNodes);
            if ("1".equals(node.attr("id"))) {
                node.replaceWith(new Element("span").text("Foo"));
            }
        }
        
        // Note: We see the original div#1, then the replacement span, but not the original children of div#1
        String expectedVisitedNodes = "#root;html;head;body;div#out1;div#1;span;Foo;div#2;p;Three;p;Four;div#out2;Out2;";
        assertEquals(expectedVisitedNodes, visitedNodes.toString());
        
        String expectedFinalStructure = "#root;html;head;body;div#out1;span;Foo;div#2;p;Three;p;Four;div#out2;Out2;";
        assertDocumentStructure(doc, expectedFinalStructure);

        // Replace div with id=2 with a span containing "Bar"
        iterator = NodeIterator.from(doc);
        visitedNodes = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            recordVisitedNode(node, visitedNodes);
            if ("2".equals(node.attr("id"))) {
                node.replaceWith(new Element("span").text("Bar"));
            }
        }
        
        assertEquals("#root;html;head;body;div#out1;span;Foo;div#2;span;Bar;div#out2;Out2;", visitedNodes.toString());
        assertDocumentStructure(doc, "#root;html;head;body;div#out1;span;Foo;span;Bar;div#out2;Out2;");
    }

    @Test 
    void shouldSupportNodeWrappingDuringIteration() {
        Document doc = Jsoup.parse(TEST_HTML);
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        boolean foundInnerTextNode = false;
        
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if ("1".equals(node.attr("id"))) {
                node.wrap("<div id=outer>");
            }
            if (node instanceof TextNode && "One".equals(((TextNode) node).text())) {
                foundInnerTextNode = true;
            }
        }
        
        String expectedStructureAfterWrapping = "#root;html;head;body;div#outer;div#1;p;One;p;Two;div#2;p;Three;p;Four;";
        assertDocumentStructure(doc, expectedStructureAfterWrapping);
        assertTrue(foundInnerTextNode, "Should have found the 'One' text node during iteration");
    }

    @Test 
    void shouldFilterForElementsOnly() {
        Document doc = Jsoup.parse(TEST_HTML);
        NodeIterator<Element> elementIterator = new NodeIterator<>(doc, Element.class);

        StringBuilder visitedElements = new StringBuilder();
        while (elementIterator.hasNext()) {
            Element element = elementIterator.next();
            assertNotNull(element);
            recordVisitedNode(element, visitedElements);
        }

        // Should only see elements, not text nodes
        String expectedElements = "#root;html;head;body;div#1;p;p;div#2;p;p;";
        assertEquals(expectedElements, visitedElements.toString());
    }

    @Test 
    void shouldFilterForTextNodesOnly() {
        Document doc = Jsoup.parse(TEST_HTML);
        NodeIterator<TextNode> textIterator = new NodeIterator<>(doc, TextNode.class);

        StringBuilder visitedTextNodes = new StringBuilder();
        while (textIterator.hasNext()) {
            TextNode textNode = textIterator.next();
            assertNotNull(textNode);
            recordVisitedNode(textNode, visitedTextNodes);
        }

        // Should only see text content, not elements
        assertEquals("One;Two;Three;Four;", visitedTextNodes.toString());
        
        // Verify original document structure is unchanged
        String expectedFullStructure = "#root;html;head;body;div#1;p;One;p;Two;div#2;p;Three;p;Four;";
        assertDocumentStructure(doc, expectedFullStructure);
    }

    @Test 
    void shouldAllowModificationOfFilteredElements() {
        Document doc = Jsoup.parse(TEST_HTML);
        NodeIterator<Element> elementIterator = new NodeIterator<>(doc, Element.class);

        StringBuilder visitedElements = new StringBuilder();
        while (elementIterator.hasNext()) {
            Element element = elementIterator.next();
            // Add "++" to any element that has its own text content
            if (!element.ownText().isEmpty()) {
                element.text(element.ownText() + "++");
            }
            recordVisitedNode(element, visitedElements);
        }

        assertEquals("#root;html;head;body;div#1;p;p;div#2;p;p;", visitedElements.toString());
        
        // Verify that text content was modified
        String expectedModifiedStructure = "#root;html;head;body;div#1;p;One++;p;Two++;div#2;p;Three++;p;Four++;";
        assertDocumentStructure(doc, expectedModifiedStructure);
    }

    // Helper methods for better readability

    private static <T extends Node> void assertIteratorTraversesNodes(Iterator<T> iterator, String expectedTraversal) {
        Node previousNode = null;
        StringBuilder actualTraversal = new StringBuilder();
        
        while (iterator.hasNext()) {
            Node currentNode = iterator.next();
            assertNotNull(currentNode);
            assertNotSame(previousNode, currentNode, "Iterator should not return the same node twice");

            recordVisitedNode(currentNode, actualTraversal);
            previousNode = currentNode;
        }
        
        assertEquals(expectedTraversal, actualTraversal.toString());
    }

    private static void assertIteratorIsExhausted(NodeIterator<Node> iterator) {
        assertFalse(iterator.hasNext());

        assertThrows(NoSuchElementException.class, iterator::next, 
            "Iterator should throw NoSuchElementException when next() is called after exhaustion");
    }

    private static void assertDocumentStructure(Element element, String expectedStructure) {
        NodeIterator<Node> iterator = NodeIterator.from(element);
        assertIteratorTraversesNodes(iterator, expectedStructure);
    }

    /**
     * Records a visited node in a standardized format for test assertions.
     * Elements are recorded as "tagname#id", text nodes as their text content, others as node name.
     */
    private static void recordVisitedNode(Node node, StringBuilder record) {
        if (node instanceof Element) {
            Element element = (Element) node;
            record.append(element.tagName());
            if (element.hasAttr("id")) {
                record.append("#").append(element.id());
            }
        } else if (node instanceof TextNode) {
            record.append(((TextNode) node).text());
        } else {
            record.append(node.nodeName());
        }
        record.append(";");
    }
}