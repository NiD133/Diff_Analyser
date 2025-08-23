package org.apache.ibatis.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.ibatis.builder.InitializingObject;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link CacheBuilder} focusing on initialization behavior.
 */
@DisplayName("CacheBuilder")
class CacheBuilderTest {

  /**
   * A test-specific cache implementation that simulates a failure during its
   * initialization phase. This is used to verify the exception handling of the
   * {@link CacheBuilder}.
   */
  private static class InitializingFailureCache extends PerpetualCache implements InitializingObject {

    public InitializingFailureCache(String id) {
      super(id);
    }

    @Override
    public void initialize() {
      throw new IllegalStateException("Simulating an initialization error.");
    }
  }

  @Test
  @DisplayName("should throw CacheException when cache initialization fails")
  void buildShouldThrowCacheExceptionWhenInitializationFails() {
    // Arrange: A cache builder configured with an implementation that is known to fail on initialization.
    CacheBuilder builder = new CacheBuilder("test-cache")
        .implementation(InitializingFailureCache.class);

    // Act: Attempt to build the cache and capture the expected exception.
    CacheException thrown = assertThrows(CacheException.class,
        builder::build,
        "CacheBuilder should wrap initialization errors in a CacheException.");

    // Assert: Verify that the exception message clearly indicates the cause of the failure.
    assertThat(thrown.getMessage())
        .isEqualTo("Failed cache initialization for 'test-cache' on '" + InitializingFailureCache.class.getName() + "'");
  }
}