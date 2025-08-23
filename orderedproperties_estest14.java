package org.apache.commons.collections4.properties;

import org.junit.Test;
import java.util.Set;

/**
 * Tests edge cases for the {@link OrderedProperties} class.
 * This test focuses on behavior inherited from {@link java.util.Hashtable}
 * concerning self-referential collections.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that a StackOverflowError occurs when using a self-referential keySet as a key.
     *
     * <p>This behavior is inherited from {@link java.util.Hashtable}. When a map's keySet
     * is used as a key within that same map, it creates a self-referential loop.
     * Any subsequent operation that triggers the key's {@code hashCode()} method (like another
     * {@code put}) will result in infinite recursion, leading to a {@link StackOverflowError}.</p>
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void putWithSelfReferentialKeySetShouldCauseStackOverflow() {
        // Arrange: Create a properties map and get a reference to its keySet.
        // The keySet is a "view" of the keys, not a separate copy.
        OrderedProperties properties = new OrderedProperties();
        Set<Object> keySet = properties.keySet();

        // Arrange: Add the keySet to the map *as a key*. This creates a
        // self-referential structure where the map's keySet contains itself.
        properties.put(keySet, "initial value");

        // Act: Attempt to update the value for the self-referential key.
        // This 'put' operation requires calculating the hashCode of the key (the keySet).
        // Because the keySet contains itself, the hashCode calculation enters an
        // infinite recursion, which is expected to throw a StackOverflowError.
        properties.put(keySet, "new value");

        // Assert: The test passes if a StackOverflowError is thrown.
    }
}