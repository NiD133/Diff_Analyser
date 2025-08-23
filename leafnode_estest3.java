package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the abstract {@link LeafNode} class, using a concrete implementation like {@link CDataNode}.
 */
public class LeafNodeTest {

    @Test
    public void coreValueShouldUpdateTheNodeContent() {
        // Arrange: Create a CDataNode, a concrete implementation of LeafNode, with an initial value.
        CDataNode cdataNode = new CDataNode("Initial content");
        String newContent = "Updated content";

        // Act: Call the method under test to change the node's internal value.
        // Note: coreValue(String) is a package-private method on the abstract LeafNode.
        cdataNode.coreValue(newContent);

        // Assert: Verify that the node's content was successfully updated by checking its public value.
        // For a CDataNode, getWholeText() returns its content.
        assertEquals(newContent, cdataNode.getWholeText());
    }
}