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
     * Tests that calling getValues() on a newly created, empty BasicNodeSet
     * returns an empty list.
     */
    @Test
    public void getValuesShouldReturnEmptyListForNewNodeSet() {
        // Arrange: Create a new, empty BasicNodeSet.
        BasicNodeSet nodeSet = new BasicNodeSet();

        // Act: Retrieve the list of values from the new node set.
        List<?> values = nodeSet.getValues();

        // Assert: The returned list should be non-null and empty.
        assertNotNull("The values list should not be null.", values);
        assertTrue("A new BasicNodeSet should have an empty list of values.", values.isEmpty());
    }
}