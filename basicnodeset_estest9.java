package org.apache.commons.jxpath;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Contains unit tests for the {@link BasicNodeSet} class.
 */
public class BasicNodeSetTest {

    /**
     * Tests that attempting to remove a null pointer from an empty BasicNodeSet
     * does not cause an error and leaves the set unchanged.
     */
    @Test
    public void remove_withNullPointerOnEmptySet_shouldBeIgnored() {
        // Arrange: Create an empty node set.
        BasicNodeSet nodeSet = new BasicNodeSet();

        // Act: Attempt to remove a null pointer. This action should be handled
        // gracefully without throwing an exception.
        nodeSet.remove(null);

        // Assert: The node set should remain empty.
        assertTrue("The node set should still be empty after attempting to remove null.",
                nodeSet.getPointers().isEmpty());
    }
}