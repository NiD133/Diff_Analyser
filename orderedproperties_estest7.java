package org.apache.commons.collections4.properties;

import org.junit.Test;

import java.util.HashMap;
import java.util.NoSuchElementException;

import static org.junit.Assert.fail;

/**
 * This test class contains tests for the {@link OrderedProperties} class.
 * This particular test focuses on the behavior of cloned instances.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that a cloned OrderedProperties instance becomes inconsistent if the original
     * instance is modified after cloning.
     *
     * <p><b>Scenario:</b> The {@code clone()} method inherited from {@code Hashtable}
     * performs a shallow copy. This means the internal {@code LinkedHashSet} that tracks
     * key order is shared between the original and the clone. Modifying the original
     * by removing a key also removes it from the shared set, but not from the clone's
     * internal hash table. This inconsistency causes subsequent operations on the clone,
     * such as {@code toString()}, to fail.</p>
     */
    @Test(timeout = 4000)
    public void testCloneBecomesInconsistentWhenOriginalIsModified() {
        // Arrange: Create an OrderedProperties instance, add an element, and then clone it.
        OrderedProperties original = new OrderedProperties();
        Integer key = 123;
        HashMap<String, String> value = new HashMap<>();
        original.put(key, value);

        // The clone shares the internal 'orderedKeys' set with the original.
        OrderedProperties clone = (OrderedProperties) original.clone();

        // Act: Remove the key from the original instance. This modification affects the
        // shared 'orderedKeys' set, putting the clone into an inconsistent state.
        original.remove(key);

        // Assert: Calling toString() on the inconsistent clone is expected to fail.
        // The method relies on the 'orderedKeys' set to iterate, but the clone's
        // underlying hash table state no longer matches, leading to an error.
        try {
            clone.toString();
            fail("Expected a NoSuchElementException due to the clone's inconsistent state.");
        } catch (NoSuchElementException e) {
            // This exception is expected, confirming the inconsistent state causes a failure.
        }
    }
}