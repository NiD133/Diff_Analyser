package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Test suite for the {@link CacheKey} class.
 */
public class CacheKeyTest {

    /**
     * Verifies that the clone() method creates a new instance that is a separate
     * object in memory but is logically equal to the original. This is a fundamental
     * part of the Java clone() contract.
     */
    @Test
    public void cloneShouldReturnDifferentInstanceWithEqualValue() throws CloneNotSupportedException {
        // Arrange: Create an original CacheKey and add some data to it.
        // Testing with a non-empty key ensures its internal state is also cloned correctly.
        CacheKey originalKey = new CacheKey();
        originalKey.update("parameter1");
        originalKey.update(12345);

        // Act: Clone the original key.
        CacheKey clonedKey = originalKey.clone();

        // Assert: Verify the clone contract.
        // 1. The clone must be a different object instance.
        assertNotSame("The cloned key should be a new instance, not a reference to the original.", originalKey, clonedKey);

        // 2. The clone must be logically equal to the original.
        assertEquals("The cloned key should be logically equal to the original.", originalKey, clonedKey);

        // 3. As per the equals/hashCode contract, their hash codes must also be equal.
        assertEquals("The hash code of the cloned key should match the original's.", originalKey.hashCode(), clonedKey.hashCode());
    }
}