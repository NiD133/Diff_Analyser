package org.apache.commons.jxpath;

import org.junit.Test;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link BasicNodeSet} class.
 */
public class BasicNodeSetTest {

    /**
     * Tests that a newly created BasicNodeSet returns an empty, non-null list of nodes.
     */
    @Test
    public void getNodesOnNewNodeSetShouldReturnEmptyList() {
        // Arrange: Create a new instance of BasicNodeSet.
        BasicNodeSet nodeSet = new BasicNodeSet();

        // Act: Retrieve the list of nodes.
        List<?> nodes = nodeSet.getNodes();

        // Assert: Verify that the returned list is not null and is empty.
        assertNotNull("The getNodes() method should never return null.", nodes);
        assertTrue("A new BasicNodeSet should have an empty list of nodes.", nodes.isEmpty());
    }
}