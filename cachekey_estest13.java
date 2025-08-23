package org.apache.ibatis.cache;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Tests for {@link CacheKey}.
 */
public class CacheKeyTest {

    @Test
    public void cloneShouldCreateEqualButNotSameInstance() throws CloneNotSupportedException {
        // Arrange: Create an original CacheKey and populate it with some data.
        CacheKey originalKey = new CacheKey();
        originalKey.update("first-item");
        originalKey.update(12345);

        // Act: Clone the original key.
        CacheKey clonedKey = originalKey.clone();

        // Assert: The cloned key should be a different object instance,
        // but it must be logically equal to the original.
        assertNotSame("The cloned key should be a new object instance.", originalKey, clonedKey);
        assertEquals("The cloned key should be equal to the original key.", originalKey, clonedKey);
    }
}