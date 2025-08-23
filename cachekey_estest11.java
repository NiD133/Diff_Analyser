package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link CacheKey} class.
 */
public class CacheKeyTest {

    /**
     * Verifies that a CacheKey created via the default constructor is considered
     * equal to a CacheKey initialized with an empty object array. Both represent
     * an empty key and should be interchangeable.
     */
    @Test
    public void shouldConsiderTwoEmptyKeysAsEqual() {
        // Arrange: Create two CacheKey instances that should be logically equivalent.
        // One is created with the default constructor.
        CacheKey keyFromDefaultConstructor = new CacheKey();
        // The other is created with an empty array of objects.
        CacheKey keyFromEmptyArray = new CacheKey(new Object[0]);

        // Act & Assert: The two keys should be equal.
        // We use assertEquals to check for logical equality.
        assertEquals(keyFromDefaultConstructor, keyFromEmptyArray);

        // As per the Java contract for equals(), if two objects are equal,
        // their hash codes must also be equal.
        assertEquals(keyFromDefaultConstructor.hashCode(), keyFromEmptyArray.hashCode());
    }
}