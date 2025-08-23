package org.apache.commons.collections4.properties;

import org.junit.Test;
import java.util.Set;

/**
 * This test class contains tests for the OrderedProperties class.
 * This specific test focuses on the behavior of the toString() method
 * when dealing with self-referential (cyclic) data structures.
 */
public class OrderedProperties_ESTestTest8 {

    /**
     * Tests that calling toString() on an OrderedProperties instance that contains a
     * self-referential element (e.g., its own key set) results in a StackOverflowError.
     * <p>
     * This is the expected behavior for collections with cyclic dependencies, as the
     * toString() implementation would otherwise lead to infinite recursion.
     */
    @Test(expected = StackOverflowError.class, timeout = 4000)
    public void toStringShouldThrowStackOverflowErrorOnCyclicReference() {
        // Arrange: Create an OrderedProperties instance and obtain its key set.
        final OrderedProperties properties = new OrderedProperties();
        final Set<Object> keySet = properties.keySet();

        // Act: Add the key set to the properties as both a key and a value.
        // This creates a self-referential (cyclic) structure where the properties
        // map contains its own key set, which in turn refers back to the map.
        properties.putIfAbsent(keySet, keySet);

        // Assert: Calling toString() on the properties will cause infinite recursion.
        // The 'expected' attribute of the @Test annotation asserts that a
        // StackOverflowError is thrown, which is the expected outcome.
        properties.toString();
    }
}