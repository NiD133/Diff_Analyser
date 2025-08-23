package org.apache.commons.collections4.properties;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link OrderedProperties}.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that calling remove() with a non-existent key on an empty
     * OrderedProperties instance returns null and leaves the properties empty.
     */
    @Test
    public void removeShouldReturnNullForNonExistentKey() {
        // Arrange: Create an empty OrderedProperties instance and a key that is not present.
        final OrderedProperties properties = new OrderedProperties();
        final String nonExistentKey = "key.that.does.not.exist";

        // Act: Attempt to remove the non-existent key.
        final Object removedValue = properties.remove(nonExistentKey);

        // Assert: Verify that the returned value is null and the properties remain empty.
        assertNull("The remove() method should return null for a non-existent key.", removedValue);
        assertTrue("The properties should remain empty after the remove operation.", properties.isEmpty());
    }
}