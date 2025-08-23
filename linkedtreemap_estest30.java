package com.google.gson.internal;

import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link LinkedTreeMap.KeySet} class, focusing on its remove behavior.
 */
public class LinkedTreeMapKeySetTest {

    /**
     * Tests that attempting to remove an object of an incompatible type from the key set
     * returns false and does not modify the set, as per the contract of Set.remove(Object).
     */
    @Test
    public void removeIncompatibleTypeFromKeySetShouldReturnFalse() {
        // Arrange: Create an empty map and get its key set.
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();
        Set<Integer> keySet = map.keySet();

        // An object that is not of type Integer and thus cannot be in the key set.
        Object incompatibleObject = new Object();

        // Act: Attempt to remove the incompatible object from the key set.
        boolean wasRemoved = keySet.remove(incompatibleObject);

        // Assert: The remove operation should fail, and the set should remain unchanged.
        assertFalse("remove() should return false for an object of an incompatible type.", wasRemoved);
        assertEquals("The key set should remain empty after the failed removal attempt.", 0, keySet.size());
    }
}