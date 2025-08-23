package org.apache.commons.collections4.properties;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link OrderedProperties} class.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that calling compute() on a non-existent key with a remapping function
     * that returns null will result in null and not add the key to the properties.
     */
    @Test
    public void computeShouldReturnNullAndNotAddKeyWhenFunctionReturnsNullForNonExistentKey() {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();
        final String nonExistentKey = "key.that.does.not.exist";

        // Act: Call compute with a function that returns null.
        // According to the Map#compute contract, this should not add a new entry.
        final Object result = properties.compute(nonExistentKey, (key, value) -> null);

        // Assert
        assertNull("The result of compute() should be null when the remapping function returns null.", result);
        assertTrue("The properties map should remain empty because the key was not added.", properties.isEmpty());
    }
}