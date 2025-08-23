package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the NodeIterator's behavior when the document's structure is modified during iteration.
 * It ensures the iterator can gracefully handle node replacements and continue traversal correctly.
 */
@DisplayName("NodeIterator with DOM modifications")
public class NodeIteratorModificationTest {

    // The HTML structure used for testing node replacements during iteration.
    // Using well-formed HTML with quoted attributes for clarity.
    private static final String HTML =
        "<div id='out1'>" +
            "<div id='1'><p>One</p><p>Two</p></div>" +
            "<div id='2'><p>Three</p><p>Four</p></div>" +
        "</div>" +
        "<div id='out2'>Out2</div>";

    @Test
    @DisplayName("should continue traversal correctly after a middle node is replaced")
    void replacesMiddleNodeAndContinuesIteration() {
        // Arrange
        Document doc = Jsoup.parse(HTML);
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        StringBuilder traversalPath = new StringBuilder();

        // The iterator should see the node to be replaced (div#1), but not its children.
        // It then sees the replacement (span) and its content, and continues to the next sibling (div#2).
        String expectedTraversalPath =
            "#root;html;head;body;" +
            "div#out1;" +
            "div#1;" +              // Visited, then replaced
            "span;Foo;" +           // The replacement node and its text are visited next
            "div#2;p;Three;p;Four;" + // Traversal continues to the next sibling
            "div#out2;Out2;";

        // The final document structure should reflect the replacement.
        String expectedFinalStructure =
            "#root;html;head;body;" +
            "div#out1;" +
            "span;Foo;" +           // div#1 and its children are gone
            "div#2;p;Three;p;Four;" +
            "div#out2;Out2;";

        // Act
        while (iterator.hasNext()) {
            Node node = iterator.next();
            trackSeen(node, traversalPath);
            if ("1".equals(node.attr("id"))) {
                node.replaceWith(new Element("span").text("Foo"));
            }
        }

        // Assert
        assertEquals(expectedTraversalPath, traversalPath.toString(),
            "Iterator should see the replaced node, then the new node, and continue correctly.");
        assertContents(doc, expectedFinalStructure);
    }

    @Test
    @DisplayName("should continue traversal correctly after a later node is replaced")
    void replacesLaterNodeAndContinuesIteration() {
        // Arrange
        Document doc = Jsoup.parse(HTML);
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        StringBuilder traversalPath = new StringBuilder();

        // The iterator sees all nodes up to and including div#2.
        // After div#2 is replaced, it sees the new span, then continues to the next node (div#out2).
        String expectedTraversalPath =
            "#root;html;head;body;" +
            "div#out1;" +
            "div#1;p;One;p;Two;" +
            "div#2;" +              // Visited, then replaced
            "span;Bar;" +           // The replacement node and its text are visited next
            "div#out2;Out2;";       // Traversal continues to the next element

        // The final document structure should reflect the replacement.
        String expectedFinalStructure =
            "#root;html;head;body;" +
            "div#out1;" +
            "div#1;p;One;p;Two;" +
            "span;Bar;" +           // div#2 and its children are gone
            "div#out2;Out2;";

        // Act
        while (iterator.hasNext()) {
            Node node = iterator.next();
            trackSeen(node, traversalPath);
            if ("2".equals(node.attr("id"))) {
                node.replaceWith(new Element("span").text("Bar"));
            }
        }

        // Assert
        assertEquals(expectedTraversalPath, traversalPath.toString(),
            "Iterator should see the replaced node, then the new node, and continue correctly.");
        assertContents(doc, expectedFinalStructure);
    }

    // --- Helper Methods (unchanged from original, but included for context) ---

    /**
     * Asserts that the final structure of an element, when traversed by a new NodeIterator,
     * matches the expected string representation.
     * @param el the element to traverse
     * @param expected the expected string representation of the traversal
     */
    private static void assertContents(Element el, String expected) {
        NodeIterator<Node> it = NodeIterator.from(el);
        assertIterates(it, expected);
    }

    /**
     * Iterates through all nodes, asserts basic sanity, and builds a string representation of the traversed nodes.
     * @param it the iterator to test
     * @param expected the expected string representation
     */
    private static <T extends Node> void assertIterates(Iterator<T> it, String expected) {
        Node previous = null;
        StringBuilder actual = new StringBuilder();
        while (it.hasNext()) {
            Node node = it.next();
            assertNotNull(node);
            assertNotSame(previous, node);
            trackSeen(node, actual);
            previous = node;
        }
        assertEquals(expected, actual.toString());
    }

    /**
     * Creates a specific string representation of a node for tracking traversal paths.
     * - Element: tagName#id;
     * - TextNode: text;
     * - Other: nodeName;
     * @param node the node to track
     * @param actual the StringBuilder to append to
     */
    private static void trackSeen(Node node, StringBuilder actual) {
        if (node instanceof Element) {
            Element el = (Element) node;
            actual.append(el.tagName());
            if (el.hasAttr("id"))
                actual.append("#").append(el.id());
        } else if (node instanceof TextNode) {
            actual.append(((TextNode) node).text());
        } else {
            actual.append(node.nodeName());
        }
        actual.append(";");
    }
}