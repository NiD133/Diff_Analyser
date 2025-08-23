package org.apache.ibatis.cache;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test suite for the CacheKey class.
 */
public class CacheKeyTest {

    /**
     * Verifies the equals() and hashCode() contract for CacheKey.
     *
     * This test ensures that:
     * 1. Two newly created, empty CacheKey instances are considered equal.
     * 2. After one CacheKey is updated with a new value, it is no longer equal to the
     *    original empty key.
     */
    @Test
    public void shouldBeEqualForTwoEmptyKeysAndUnequalAfterAnUpdate() {
        // Arrange: Create two identical, empty CacheKey instances.
        CacheKey keyA = new CacheKey();
        CacheKey keyB = new CacheKey();

        // Assert: Initially, the two empty keys should be equal and have the same hash code.
        assertEquals("Two new, empty CacheKeys should be equal", keyA, keyB);
        assertEquals("Hash codes of two new, empty CacheKeys should be equal", keyA.hashCode(), keyB.hashCode());

        // Act: Update one of the keys. This changes its internal state.
        keyB.update("a-new-value");

        // Assert: After the update, the keys should no longer be equal,
        // and their hash codes should differ.
        assertNotEquals("An updated CacheKey should not be equal to an unmodified one", keyA, keyB);
        assertNotEquals("Hash codes should differ after one CacheKey is updated", keyA.hashCode(), keyB.hashCode());
    }
}