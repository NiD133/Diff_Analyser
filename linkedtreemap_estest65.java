package com.google.gson.internal;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link LinkedTreeMap} class.
 * This class focuses on improving the understandability of a specific, complex test case.
 */
public class LinkedTreeMapTest {

    /**
     * This test verifies that `containsKey` correctly handles keys of an incompatible type.
     *
     * <p>It sets up a map and adds an entry, which creates an internal {@code Node} object.
     * It then calls `containsKey` passing one of the map's own internal nodes as the key.
     *
     * <p>Since the {@code Node} object cannot be cast to the map's key type ({@code Integer}),
     * this should cause an internal {@code ClassCastException}. The test asserts that
     * {@code LinkedTreeMap} handles this gracefully by returning {@code false}, adhering to the
     * {@code Map} interface contract.
     *
     * <p>This refactored test corrects an assertion from the original auto-generated version,
     * which incorrectly expected {@code true}.
     */
    @Test
    public void containsKey_whenKeyIsOfIncompatibleType_returnsFalse() {
        // Arrange
        // A custom comparator is used to allow null keys, similar to the original test's setup.
        Comparator<Integer> nullSafeComparator = Comparator.nullsFirst(Integer::compareTo);
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>(nullSafeComparator, true);

        // Add an element to the map. This creates an internal Node object.
        // Using the public `put` API is cleaner than the internal `find` method used originally.
        map.put(1, "one");
        assertEquals(1, map.size());

        // Define the incompatible key to be one of the map's own internal nodes.
        // This reproduces the unusual scenario from the original test.
        Object incompatibleKey = map.root;

        // Act
        boolean result = map.containsKey(incompatibleKey);

        // Assert
        assertFalse("containsKey should return false for a key of an incompatible type (e.g., a Node).", result);
    }
}