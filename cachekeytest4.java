package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for CacheKey equality and hash code generation.
 * This suite focuses on how the key's state changes with updates, particularly with null values.
 */
@DisplayName("CacheKey Test")
class CacheKeyTest {

  @Nested
  @DisplayName("Equality and HashCode Contract")
  class EqualityAndHashCode {

    @Test
    @DisplayName("Two empty keys should be equal")
    void shouldBeEqualForTwoEmptyKeys() {
      // Arrange
      CacheKey key1 = new CacheKey();
      CacheKey key2 = new CacheKey();

      // Assert: Two default-constructed CacheKeys must be equal.
      assertEquals(key1, key2);
      assertEquals(key1.hashCode(), key2.hashCode(), "Hash codes must be equal for equal objects");
    }

    @Test
    @DisplayName("Keys updated with the same null value should be equal")
    void shouldBeEqualWhenBothKeysAreUpdatedWithNull() {
      // Arrange
      CacheKey key1 = new CacheKey();
      CacheKey key2 = new CacheKey();

      // Act
      key1.update(null);
      key2.update(null);

      // Assert: Two keys updated with the same sequence (a single null) must be equal.
      assertEquals(key1, key2);
      assertEquals(key1.hashCode(), key2.hashCode(), "Hash codes must be equal for equal objects");
    }

    @Test
    @DisplayName("An empty key and an updated key should not be equal")
    void shouldNotBeEqualWhenOneKeyIsEmptyAndOtherIsUpdated() {
      // Arrange
      CacheKey emptyKey = new CacheKey();
      CacheKey updatedKey = new CacheKey();

      // Act
      updatedKey.update(null);

      // Assert: An empty key should not be equal to a key that has been updated.
      assertNotEquals(emptyKey, updatedKey);
    }
  }
}