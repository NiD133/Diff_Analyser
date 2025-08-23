package org.apache.commons.jxpath;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link BasicNodeSet} class, focusing on collection-like operations.
 */
public class BasicNodeSetTest {

    /**
     * Tests that adding an empty BasicNodeSet to itself is a safe, no-op operation.
     * This verifies that the set correctly handles the edge case of self-addition
     * without causing infinite recursion or unexpected modifications.
     */
    @Test
    public void testAddEmptyNodeSetToItselfShouldNotChangeTheSet() {
        // Arrange: Create an empty node set.
        BasicNodeSet emptyNodeSet = new BasicNodeSet();

        // Act: Add the node set to itself.
        emptyNodeSet.add(emptyNodeSet);

        // Assert: Verify that the node set remains empty after the operation.
        assertTrue("Adding an empty set to itself should not add any pointers.",
                emptyNodeSet.getPointers().isEmpty());
        assertTrue("The list of nodes should remain empty.",
                emptyNodeSet.getNodes().isEmpty());
        assertTrue("The list of values should remain empty.",
                emptyNodeSet.getValues().isEmpty());
    }
}