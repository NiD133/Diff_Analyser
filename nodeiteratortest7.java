package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for removing nodes from the DOM tree while iterating over them using {@link NodeIterator}.
 */
public class NodeIteratorRemovalTest {

    /**
     * Creates a string representation of a node for traversal path verification.
     * Format: tagName#id; for elements with an ID, tagName; for other elements, textContent; for text nodes.
     * @param node The node to track.
     * @param actual The StringBuilder to append the node's representation to.
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

    @Test
    void removingElementDuringIterationSkipsItsChildren() {
        // Arrange
        String html = "<div id=out1><div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div></div><div id=out2>Out2";
        Document doc = Jsoup.parse(html);
        StringBuilder traversedNodes = new StringBuilder();

        // Act
        NodeIterator<Node> it = NodeIterator.from(doc);
        while (it.hasNext()) {
            Node node = it.next();
            trackSeen(node, traversedNodes); // Tracked before removal to confirm it was visited
            if ("1".equals(node.attr("id"))) {
                it.remove();
            }
        }

        // Assert
        // The node to be removed (div#1) is visited, then removed.
        // The iterator correctly skips its children ('One', 'Two') and continues from the next sibling (div#2).
        String expectedTraversal = "#root;html;head;body;div#out1;div#1;div#2;p;Three;p;Four;div#out2;Out2;";
        assertEquals(expectedTraversal, traversedNodes.toString());

        // Verify the final DOM structure is as expected after the removal.
        String expectedBodyHtml = "<div id=\"out1\"><div id=\"2\"><p>Three</p><p>Four</p></div></div><div id=\"out2\">Out2</div>";
        assertEquals(expectedBodyHtml, doc.body().html());
    }

    @Test
    void removingElementLaterInIterationDoesNotAffectPriorNodes() {
        // Arrange
        String html = "<div id=out1><div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div></div><div id=out2>Out2";
        Document doc = Jsoup.parse(html);
        StringBuilder traversedNodes = new StringBuilder();

        // Act
        NodeIterator<Node> it = NodeIterator.from(doc);
        while (it.hasNext()) {
            Node node = it.next();
            trackSeen(node, traversedNodes); // Tracked before removal
            if ("2".equals(node.attr("id"))) {
                it.remove();
            }
        }

        // Assert
        // The iterator traverses all nodes up to div#2.
        // div#2 is visited and removed, so its children ('Three', 'Four') are skipped.
        // Traversal continues normally after the removed node.
        String expectedTraversal = "#root;html;head;body;div#out1;div#1;p;One;p;Two;div#2;div#out2;Out2;";
        assertEquals(expectedTraversal, traversedNodes.toString());

        // Verify the final DOM structure.
        String expectedBodyHtml = "<div id=\"out1\"><div id=\"1\"><p>One</p><p>Two</p></div></div><div id=\"out2\">Out2</div>";
        assertEquals(expectedBodyHtml, doc.body().html());
    }
}