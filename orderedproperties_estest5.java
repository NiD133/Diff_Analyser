package org.apache.commons.collections4.properties;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.util.Set;

/**
 * Unit tests for {@link OrderedProperties}.
 * This class focuses on testing the keySet() method.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that a key added via put() is correctly reported in the keySet().
     */
    @Test
    public void keySetShouldContainAddedKey() {
        // Arrange
        OrderedProperties properties = new OrderedProperties();
        Integer testKey = 61;
        Integer testValue = 61;

        // Act
        properties.put(testKey, testValue);
        Set<Object> keySet = properties.keySet();

        // Assert
        assertTrue("The key set should contain the key that was just added.", keySet.contains(testKey));
    }
}