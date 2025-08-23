package org.apache.commons.collections4.properties;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link OrderedProperties} class.
 */
public class OrderedPropertiesTest {

    @Test
    public void testRemoveKeyValueSucceedsForExistingEntry() {
        // Arrange
        OrderedProperties properties = new OrderedProperties();
        
        // Use two distinct but equal Integer objects to ensure the methods operate
        // based on the equals() method, not object identity (==).
        Integer keyForAddition = -1107;
        Integer keyForRemoval = new Integer(-1107); // Explicitly create a new object
        Integer value = -1107;

        // Act: Add an entry to the properties using computeIfAbsent.
        // This should add the key/value pair since the key is not present.
        properties.computeIfAbsent(keyForAddition, k -> value);

        // Assert: Verify the initial state after addition.
        assertEquals("Properties should contain one entry after addition.", 1, properties.size());
        assertEquals("The value should be correctly mapped to the key.", value, properties.get(keyForAddition));

        // Act: Remove the entry using the other key object and the correct value.
        boolean wasRemoved = properties.remove(keyForRemoval, value);

        // Assert: Verify the entry was removed and the properties object is now empty.
        assertTrue("remove(key, value) should return true for an existing entry.", wasRemoved);
        assertTrue("Properties should be empty after removing the entry.", properties.isEmpty());
    }
}