package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

/**
 * Test suite for {@link BlockingCache}.
 */
public class BlockingCacheTest {

  /**
   * Verifies that a NullPointerException is thrown when getId() is called on a
   * BlockingCache that was initialized with a null delegate. This is the expected
   * behavior, as the call is delegated directly to the underlying cache instance.
   */
  @Test(expected = NullPointerException.class)
  public void shouldThrowNpeOnGetIdWhenDelegateIsNull() {
    // Arrange: Create a BlockingCache with a null delegate.
    // The cast to (Cache) is explicit to avoid ambiguity with other constructors.
    BlockingCache blockingCache = new BlockingCache((Cache) null);

    // Act: Attempt to get the ID. This should fail because the delegate is null.
    blockingCache.getId();

    // Assert: The test passes if a NullPointerException is thrown,
    // which is handled by the @Test(expected = ...) annotation.
  }
}