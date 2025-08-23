package org.apache.commons.jxpath;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link BasicNodeSet} class.
 */
public class BasicNodeSetTest {

    /**
     * Verifies that a BasicNodeSet can correctly handle the addition and
     * removal of a null Pointer. The test ensures that these operations
     * do not cause exceptions and that the set's size is updated correctly.
     */
    @Test
    public void addAndRemoveNullPointerShouldBeHandledGracefully() {
        // Arrange: Create an empty BasicNodeSet.
        BasicNodeSet nodeSet = new BasicNodeSet();
        assertTrue("A new node set should be empty.", nodeSet.getPointers().isEmpty());

        // Act: Add a null pointer to the set.
        // The cast to (Pointer) is necessary to resolve method ambiguity.
        nodeSet.add((Pointer) null);

        // Assert: The set should now contain one element.
        assertEquals("The size of the node set should be 1 after adding a null pointer.", 1, nodeSet.getPointers().size());

        // Act: Remove the null pointer from the set.
        nodeSet.remove((Pointer) null);

        // Assert: The set should be empty again.
        assertTrue("The node set should be empty after removing the null pointer.", nodeSet.getPointers().isEmpty());
    }
}