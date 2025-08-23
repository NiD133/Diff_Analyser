package org.jsoup.select;

import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling dataNodes() on a collection of elements
     * that do not contain any DataNode children returns an empty list.
     */
    @Test
    public void dataNodesReturnsEmptyListWhenNoDataNodesArePresent() {
        // Arrange: Create a document with standard elements but no DataNodes (like <script> or <style>).
        // A default new Document contains <html>, <head>, and <body>.
        Document doc = new Document("");
        Elements elements = doc.getAllElements();

        // Act: Retrieve the DataNode children from the elements.
        List<DataNode> dataNodes = elements.dataNodes();

        // Assert: The resulting list should be empty.
        assertTrue("Expected an empty list as no DataNode elements were present", dataNodes.isEmpty());
    }
}