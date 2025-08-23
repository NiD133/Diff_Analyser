package org.apache.commons.collections4.properties;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit tests for the {@link OrderedProperties} class.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that calling the clear() method on an already empty OrderedProperties instance
     * correctly results in the instance remaining empty. This verifies the behavior
     * for the edge case of clearing a collection that contains no elements.
     */
    @Test
    public void testClearOnEmptyPropertiesLeavesItEmpty() {
        // Arrange: Create a new, empty OrderedProperties instance.
        final OrderedProperties properties = new OrderedProperties();

        // Act: Call the clear() method on the empty instance.
        properties.clear();

        // Assert: Verify that the properties instance is still empty.
        assertTrue("Calling clear() on an empty properties object should leave it empty.", properties.isEmpty());
    }
}