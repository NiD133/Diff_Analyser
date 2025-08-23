package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Date;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CacheKey} focusing on its equality and hash code contracts.
 */
class CacheKeyTest {

    @Test
    void shouldBeUnequalForDifferentDateComponents() {
        // Arrange: Create two Date objects with distinct timestamps.
        // This is a deterministic and reliable way to ensure the dates are different,
        // avoiding the need for Thread.sleep().
        Date date1 = new Date(10000L);
        Date date2 = new Date(20000L);

        // Act: Create two CacheKey instances that are identical except for the date component.
        CacheKey key1 = new CacheKey(new Object[] { 1, "hello", null, date1 });
        CacheKey key2 = new CacheKey(new Object[] { 1, "hello", null, date2 });

        // Assert: Verify that the keys are not equal and have different hash codes and string representations.
        assertNotEquals(key1, key2,
            "CacheKeys with different Date components should not be equal.");
        assertNotEquals(key1.hashCode(), key2.hashCode(),
            "Hash codes should differ for unequal CacheKeys to ensure correct behavior in hash-based collections.");
        assertNotEquals(key1.toString(), key2.toString(),
            "String representation should reflect the difference in content.");
    }
}