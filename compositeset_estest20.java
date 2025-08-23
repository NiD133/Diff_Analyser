package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

/**
 * This class contains improved tests for CompositeSet.
 * The original test was an auto-generated test case (CompositeSet_ESTestTest20)
 * that was hard to understand. This version focuses on clarity and maintainability.
 */
public class CompositeSet_ESTestTest20 { // Retaining original class name as per request

    /**
     * Tests that calling toArray(T[] array) with an array of an incompatible
     * component type throws an ArrayStoreException.
     *
     * For example, trying to store String elements into a Long[] array.
     */
    @Test
    public void toArrayWithIncompatibleArrayTypeShouldThrowArrayStoreException() {
        // --- Arrange ---
        // 1. Create a simple component set with one element.
        final Set<String> componentSet = new HashSet<>();
        componentSet.add("test_element");

        // 2. Create the CompositeSet under test.
        final CompositeSet<String> compositeSet = new CompositeSet<>(componentSet);

        // 3. Create a destination array of a type (Long) that is incompatible with the set's elements (String).
        final Long[] incompatibleArray = new Long[1];

        // --- Act & Assert ---
        try {
            compositeSet.toArray(incompatibleArray);
            fail("Expected an ArrayStoreException because a String cannot be stored in a Long array.");
        } catch (final ArrayStoreException e) {
            // This is the expected behavior. The test passes.
            // The exception message can be optionally verified for more specific tests.
            // For example: assertEquals("java.lang.String", e.getMessage());
        }
    }
}