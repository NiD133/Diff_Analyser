package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Elements#traverse(NodeVisitor)} method.
 */
public class ElementsTest {

    @Test
    void traverseShouldVisitAllNodesWithinEachSelectedElement() {
        // Arrange
        String html = "<div><p>Hello</p></div><div>There</div>";
        Document doc = Jsoup.parse(html);

        // The expected string represents a depth-first traversal of both selected 'div' elements.
        // The NodeVisitor builds this log by appending node names during the head and tail visits.
        String expectedLog = "<div><p><#text></#text></p></div><div><#text></#text></div>";

        final StringBuilder traversalLog = new StringBuilder();
        NodeVisitor loggingVisitor = new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                traversalLog.append("<").append(node.nodeName()).append(">");
            }

            @Override
            public void tail(Node node, int depth) {
                traversalLog.append("</").append(node.nodeName()).append(">");
            }
        };

        Elements selectedElements = doc.select("div");

        // Act
        selectedElements.traverse(loggingVisitor);

        // Assert
        assertEquals(expectedLog, traversalLog.toString());
    }
}