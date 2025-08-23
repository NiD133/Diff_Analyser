package org.apache.commons.collections4.properties;

import org.junit.Test;

import java.util.Set;
import java.util.function.BiFunction;

/**
 * Contains tests for the {@link OrderedProperties} class, focusing on edge cases.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that a compute operation causes a StackOverflowError if the map contains
     * a self-referencing key.
     *
     * <p>This scenario is created by adding a map's own key set as a key to itself.
     * When {@code compute} is called with the map as the key, it triggers a hashCode
     * calculation on the map. This, in turn, requires calculating the hashCode of its
     * keys. When it encounters the self-referencing key set, the hashCode calculation
     * enters an infinite recursion, leading to a {@link StackOverflowError}.</p>
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void computeWithSelfReferencingKeyShouldCauseStackOverflow() {
        // Arrange: Create a properties map where its own key set is also a key.
        // This creates a circular reference.
        OrderedProperties properties = new OrderedProperties();
        Set<Object> keySet = properties.keySet();

        // By adding the keySet as a key, the keySet now contains itself.
        properties.put(keySet, "some-value");

        // A remapping function is required by the compute method's signature.
        // It will not be invoked because the error occurs during the key lookup.
        BiFunction<Object, Object, Object> remappingFunction = (key, value) -> "new-value";

        // Act & Assert:
        // Calling compute with the properties object itself as the key triggers a
        // recursive hashCode() calculation on the self-referencing keySet, which
        // results in a StackOverflowError. The @Test(expected=...) annotation
        // asserts that this specific error is thrown.
        properties.compute(properties, remappingFunction);
    }
}