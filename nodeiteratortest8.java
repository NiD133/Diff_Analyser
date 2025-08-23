package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for structural modifications to the DOM tree while using a {@link NodeIterator}.
 */
public class NodeIteratorModificationTest {

    /**
     * This test verifies that the NodeIterator can handle nodes being removed from the tree during iteration.
     * It performs two separate traversals and removals on the same document to ensure the iterator's state
     * management is robust.
     */
    @Test
    void canRemoveNodesAcrossSeparateIterations() {
        // Arrange: Set up a document with several nested elements.
        String html = "<div id=out1>" +
                          "<div id=1><p>One</p><p>Two</p></div>" +
                          "<div id=2><p>Three</p><p>Four</p></div>" +
                      "</div>" +
                      "<div id=out2>Out2</div>";
        Document doc = Jsoup.parse(html);

        // === First Pass: Remove div#1 ===

        // Act: Iterate through the document and remove the node with id=1.
        StringBuilder nodesSeenOnFirstPass = new StringBuilder();
        NodeIterator<Node> it = NodeIterator.from(doc);
        while (it.hasNext()) {
            Node node = it.next();
            trackSeen(node, nodesSeenOnFirstPass); // Track the node before potential removal
            if (node.attr("id").equals("1")) {
                node.remove();
            }
        }

        // Assert: Check the nodes seen during the first pass.
        // The iterator correctly returns the node to be removed (`div#1`) and then continues traversal
        // from its next sibling (`div#2`), not its children.
        String expectedSeen1 =
            "#root;html;head;body;" +
            "div#out1;" +
            "div#1;" + // This node was returned by next() and then removed.
            "div#2;p;Three;p;Four;" + // Traversal continues at the next sibling.
            "div#out2;Out2;";
        assertEquals(expectedSeen1, nodesSeenOnFirstPass.toString());

        // Assert: Verify the final state of the document after the first removal.
        String expectedDocState1 =
            "#root;html;head;body;" +
            "div#out1;" +
            // div#1 is now gone.
            "div#2;p;Three;p;Four;" +
            "div#out2;Out2;";
        assertContents(doc, expectedDocState1);


        // === Second Pass: Remove div#2 from the modified document ===

        // Act: Create a new iterator and remove the node with id=2.
        StringBuilder nodesSeenOnSecondPass = new StringBuilder();
        it = NodeIterator.from(doc); // Re-initialize iterator on the modified document
        while (it.hasNext()) {
            Node node = it.next();
            trackSeen(node, nodesSeenOnSecondPass);
            if (node.attr("id").equals("2")) {
                node.remove();
            }
        }

        // Assert: Check the nodes seen during the second pass.
        String expectedSeen2 =
            "#root;html;head;body;" +
            "div#out1;" +
            "div#2;" + // This node was returned by next() and then removed.
            "div#out2;Out2;"; // Traversal continues at the next sibling.
        assertEquals(expectedSeen2, nodesSeenOnSecondPass.toString());

        // Assert: Verify the final state of the document after the second removal.
        String expectedDocState2 =
            "#root;html;head;body;" +
            "div#out1;" +
            // div#2 is now gone.
            "div#out2;Out2;";
        assertContents(doc, expectedDocState2);
    }

    // --- Helper Methods ---
    // These helpers are used to verify the sequence of nodes visited by the iterator.

    /**
     * Asserts that a new iterator over the given element produces a specific sequence of nodes.
     * @param el the element to iterate over.
     * @param expected the expected string representation of the node sequence.
     */
    private static void assertContents(Element el, String expected) {
        NodeIterator<Node> it = NodeIterator.from(el);
        assertIterates(it, expected);
    }

    /**
     * Iterates through the iterator, building a string representation of the nodes,
     * and asserts it matches the expected string.
     */
    private static <T extends Node> void assertIterates(Iterator<T> it, String expected) {
        Node previous = null;
        StringBuilder actual = new StringBuilder();
        while (it.hasNext()) {
            Node node = it.next();
            assertNotNull(node);
            assertNotSame(previous, node, "Iterator should not return the same node consecutively.");
            trackSeen(node, actual);
            previous = node;
        }
        assertEquals(expected, actual.toString());
    }

    /**
     * Creates a simple string representation of a Node for tracking purposes.
     * - Element: `tagName#id;` or `tagName;`
     * - TextNode: `text;`
     * - Other: `nodeName;`
     */
    private static void trackSeen(Node node, StringBuilder actual) {
        if (node instanceof Element) {
            Element el = (Element) node;
            actual.append(el.tagName());
            if (el.hasAttr("id"))
                actual.append("#").append(el.id());
        } else if (node instanceof TextNode)
            actual.append(((TextNode) node).text());
        else
            actual.append(node.nodeName());
        actual.append(";");
    }
}