package org.apache.commons.collections4.properties;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link OrderedProperties}.
 */
// The original test class name "OrderedProperties_ESTestTest27" was replaced
// with a more conventional name.
public class OrderedPropertiesTest {

    /**
     * Tests that remove(key, value) returns false when the key-value pair
     * does not exist in the properties.
     */
    @Test
    public void removeKeyValuePairShouldReturnFalseWhenKeyIsAbsent() {
        // Arrange: Create an empty OrderedProperties instance.
        final OrderedProperties properties = new OrderedProperties();
        final String nonExistentKey = "non.existent.key";
        final String anyValue = "anyValue";

        // Act: Attempt to remove a key-value pair that is not present.
        final boolean wasRemoved = properties.remove(nonExistentKey, anyValue);

        // Assert: The remove method should return false, indicating nothing was removed.
        assertFalse("remove(key, value) should return false for a non-existent key.", wasRemoved);
    }
}