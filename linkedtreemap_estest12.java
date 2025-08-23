package com.google.gson.internal;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * This test class focuses on the behavior of LinkedTreeMap's get() method
 * when using a custom comparator.
 */
public class LinkedTreeMap_ESTestTest12 {

    /**
     * Tests that `get()` can find an entry using a key that is different from the one
     * used for insertion, provided the custom comparator considers them equal.
     */
    @Test
    public void getShouldReturnAssociatedValueWhenComparatorTreatsAllKeysAsEqual() {
        // Arrange
        // 1. Create a mock comparator that considers any two objects to be equal.
        @SuppressWarnings("unchecked")
        Comparator<Object> alwaysEqualComparator = (Comparator<Object>) mock(Comparator.class);
        doReturn(0).when(alwaysEqualComparator).compare(any(), any());

        // 2. Create a LinkedTreeMap using this custom comparator.
        LinkedTreeMap<Object, String> map = new LinkedTreeMap<>(alwaysEqualComparator, true);

        // 3. Insert an entry with a specific key and value.
        String insertedKey = "key1";
        String insertedValue = "value1";
        map.put(insertedKey, insertedValue);

        // 4. Define a completely different object to use for the lookup.
        Object lookupKey = new Object();

        // Act
        // Attempt to retrieve the value using the different lookup key.
        String retrievedValue = map.get(lookupKey);

        // Assert
        // The map should find the entry because the comparator reports that
        // 'lookupKey' and 'insertedKey' are equal.
        assertEquals("Map size should remain 1 after the get operation.", 1, map.size());
        assertSame("The retrieved value should be the exact instance that was inserted.",
                insertedValue, retrievedValue);
    }
}