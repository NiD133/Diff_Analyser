package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link LinkedTreeMap}.
 * This class focuses on exception handling related to key comparability.
 */
// The original test class name and inheritance are preserved to maintain context.
public class LinkedTreeMap_ESTestTest59 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that attempting to insert a key that is not {@link Comparable} into a
     * {@link LinkedTreeMap} that uses natural ordering (i.e., was constructed
     * with a null comparator) results in a {@link ClassCastException}.
     */
    @Test
    public void put_whenKeyIsNotComparableAndNoComparatorIsSet_throwsClassCastException() {
        // Arrange: Create a map configured to use natural ordering by passing a null comparator.
        // The key type for this map (another LinkedTreeMap) does not implement Comparable.
        LinkedTreeMap<LinkedTreeMap<Integer, Integer>, Integer> map = new LinkedTreeMap<>();
        LinkedTreeMap<Integer, Integer> nonComparableKey = new LinkedTreeMap<>();
        Integer value = 529;

        // Act & Assert
        try {
            map.put(nonComparableKey, value);
            fail("Expected a ClassCastException to be thrown, but it was not.");
        } catch (ClassCastException e) {
            // Verify that the exception is thrown because LinkedTreeMap cannot be cast to Comparable.
            // This is the expected behavior when using natural ordering with non-comparable keys.
            String expectedMessage = "com.google.gson.internal.LinkedTreeMap cannot be cast to java.lang.Comparable";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}