package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link LeafNode} abstract class, using {@link TextNode} as a concrete implementation.
 */
public class LeafNodeTest {

    /**
     * Verifies that the doClone() method creates a new, distinct instance of the node
     * with the same internal value.
     */
    @Test
    public void doCloneCreatesNewInstanceWithSameValue() {
        // Arrange
        TextNode originalNode = new TextNode("Sample Text");

        // Act
        // The doClone method is protected, but accessible within the same package.
        // We pass null for the parent to test cloning without re-parenting.
        LeafNode clonedNode = originalNode.doClone(null);

        // Assert
        // 1. The clone must be a new object instance.
        assertNotSame("The cloned node should be a new instance, not a reference to the original.", originalNode, clonedNode);

        // 2. The clone must be of the same concrete type.
        assertTrue("The cloned node should be an instance of TextNode.", clonedNode instanceof TextNode);

        // 3. The clone's content should match the original's.
        assertEquals("The cloned node's value should match the original.", originalNode.getWholeText(), ((TextNode) clonedNode).getWholeText());

        // 4. The clone's parent should be null, as specified in the clone call.
        assertNull("The cloned node's parent should be null.", clonedNode.parent());
    }
}