package org.apache.ibatis.cache;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the {@link CacheKey} class, focusing on its equality and update behavior.
 */
public class CacheKeyTest {

    /**
     * Verifies that updating a CacheKey modifies its state, causing it to no longer be
     * equal to an identical key created before the update.
     */
    @Test
    public void updateShouldModifyKeyMakingItUnequalToOriginal() {
        // Arrange: Create two identical, empty cache keys.
        CacheKey originalKey = new CacheKey();
        CacheKey keyToUpdate = new CacheKey();

        // Sanity check: Ensure the keys are equal and have the same hash code initially.
        assertEquals("Initially, two empty keys should be equal.", originalKey, keyToUpdate);
        assertEquals("Initially, hash codes of two empty keys should be equal.", originalKey.hashCode(), keyToUpdate.hashCode());

        // Act: Update one of the keys. This should change its internal state.
        // The original test updated the key with a reference to itself, which is a valid, if unusual, use case.
        keyToUpdate.update(keyToUpdate);

        // Assert: The updated key should no longer be equal to the original,
        // and their hash codes should now differ.
        assertNotEquals("After update, the key should not be equal to the original.", originalKey, keyToUpdate);
        assertNotEquals("After update, the key's hash code should not equal the original's.", originalKey.hashCode(), keyToUpdate.hashCode());
    }
}