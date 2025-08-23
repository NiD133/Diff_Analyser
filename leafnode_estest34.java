package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the LeafNode abstract class, using TextNode as a concrete implementation.
 */
public class LeafNodeTest {

    /**
     * Verifies that shallowClone() creates a new, distinct instance of a LeafNode
     * with the same core value and a deep copy of its attributes.
     */
    @Test
    public void shallowCloneCreatesDistinctInstanceWithSameValueAndAttributes() {
        // Arrange: Create a TextNode (a type of LeafNode) and give it an attribute.
        // Adding an attribute ensures we test the cloning logic for a node that has
        // an initialized Attributes object.
        TextNode originalNode = new TextNode("Sample Text");
        originalNode.attr("id", "1");

        // Act: Perform a shallow clone of the node.
        TextNode clonedNode = (TextNode) originalNode.shallowClone();

        // Assert: Verify the properties of the cloned node.

        // 1. The clone must be a new object instance, not a reference to the original.
        assertNotSame("The cloned node should be a new instance.", originalNode, clonedNode);

        // 2. The clone should be semantically equal to the original.
        assertEquals("The cloned node should be equal to the original.", originalNode, clonedNode);

        // 3. The clone's text content should match the original's.
        assertEquals("The cloned node's text should match the original.",
            "Sample Text", clonedNode.getWholeText());

        // 4. The clone's attributes should match the original's.
        assertEquals("The cloned node's attributes should match the original.",
            originalNode.attributes(), clonedNode.attributes());

        // 5. Importantly, the Attributes object itself should be a new instance (a deep copy).
        assertNotSame("The Attributes object in the clone should be a new instance.",
            originalNode.attributes(), clonedNode.attributes());

        // 6. A shallow clone should not have a parent.
        assertNull("A shallow-cloned node should not have a parent.", clonedNode.parent());
    }
}