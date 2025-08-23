package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CacheKey} focusing on its equality behavior with array components.
 */
class CacheKeyArrayHandlingTest {

  @Test
  @DisplayName("CacheKeys with different but content-equal byte arrays should be equal")
  void shouldBeEqualForKeysWithIdenticalByteArrayContent() {
    // Arrange: Create two distinct byte array instances with identical content.
    // This setup is crucial to verify that CacheKey's equality check is based on
    // array content, not on object reference.
    byte[] array1 = { 1, 2, 3 };
    byte[] array2 = { 1, 2, 3 };

    // Act: Create two CacheKeys, each initialized with one of the arrays.
    CacheKey key1 = new CacheKey(new Object[] { array1 });
    CacheKey key2 = new CacheKey(new Object[] { array2 });

    // Assert: The keys should be equal because their underlying array contents are equal.
    assertEquals(key1, key2);
  }
}