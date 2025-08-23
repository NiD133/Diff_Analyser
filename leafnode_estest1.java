package org.jsoup.nodes;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the abstract {@link LeafNode} class.
 */
public class LeafNodeTest {

    /**
     * Verifies that LeafNodes, which by definition cannot have children,
     * return an empty list from ensureChildNodes().
     */
    @Test
    public void ensureChildNodesReturnsEmptyList() {
        // Arrange: Create a concrete instance of a LeafNode. CDataNode is used here as an example.
        LeafNode leafNode = new CDataNode("some data");

        // Act: Attempt to get the list of child nodes.
        List<Node> childNodes = leafNode.ensureChildNodes();

        // Assert: The list must be empty, as LeafNodes do not have children.
        assertTrue("A LeafNode should not have any child nodes.", childNodes.isEmpty());
    }
}