package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CacheKey} focusing on its equality behavior.
 */
class CacheKeyTest {

    @Test
    @DisplayName("Keys with the same elements in a different order should not be equal")
    void keysWithSameElementsInDifferentOrderAreNotEqual() {
        // Arrange: Create two CacheKeys with the same elements but in a different order.
        // The order of "hello" and null is swapped.
        CacheKey key1 = new CacheKey(new Object[]{1, "hello", null});
        CacheKey key2 = new CacheKey(new Object[]{1, null, "hello"});

        // Assert: The order of elements is significant for equality. Therefore, the keys,
        // their hash codes, and string representations must not be equal.
        assertAll("A CacheKey must be sensitive to the order of its elements",
            () -> assertNotEquals(key1, key2, "Keys should not be equal if element order differs."),
            () -> assertNotEquals(key2, key1, "Inequality check should be symmetric."),
            () -> assertNotEquals(key1.hashCode(), key2.hashCode(), "Hash codes should differ for non-equal keys."),
            () -> assertNotEquals(key1.toString(), key2.toString(), "String representations should reflect the different order.")
        );
    }
}