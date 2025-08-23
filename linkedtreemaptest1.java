package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for {@link LinkedTreeMap}.
 */
public final class LinkedTreeMapTest {

    /**
     * Verifies that LinkedTreeMap iterates over its elements in insertion order,
     * which is its main behavioral difference from a standard {@code java.util.TreeMap}.
     */
    @Test
    public void iterationOrder_isInsertionOrder() {
        // Arrange
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        map.put("a", "android");
        map.put("c", "cola");
        map.put("b", "bbq");

        // Act & Assert
        // The keySet and values collections should reflect the order of insertion.
        assertThat(map.keySet()).containsExactly("a", "c", "b").inOrder();
        assertThat(map.values()).containsExactly("android", "cola", "bbq").inOrder();
    }
}