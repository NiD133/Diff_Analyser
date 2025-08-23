package org.apache.ibatis.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Field;
import org.apache.ibatis.builder.InitializingObject;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link CacheBuilder} class, focusing on its handling
 * of cache implementations that require initialization.
 */
@DisplayName("CacheBuilder")
class CacheBuilderTest {

  private static final String CACHE_ID = "test-cache";
  private CacheBuilder builder;

  @BeforeEach
  void setUp() {
    builder = new CacheBuilder(CACHE_ID);
  }

  @Nested
  @DisplayName("when building a cache that implements InitializingObject")
  class WhenBuildingInitializableCache {

    @Test
    @DisplayName("should call the initialize() method upon successful creation")
    void shouldCallInitializeOnSuccessfulCreation() {
      // Arrange
      builder.implementation(InitializableCache.class);

      // Act
      Cache cache = builder.build();

      // Assert
      InitializableCache underlyingCache = getUnderlyingCacheImplementation(cache);
      assertThat(underlyingCache.isInitialized()).isTrue();
    }

    @Test
    @DisplayName("should throw CacheException if initialize() method fails")
    void shouldThrowCacheExceptionWhenInitializationFails() {
      // Arrange
      builder.implementation(FailingInitializableCache.class);

      // Act & Assert
      assertThatThrownBy(() -> builder.build())
          .isInstanceOf(CacheException.class)
          .hasMessageContaining("Failed to initialize cache " + CACHE_ID)
          .hasRootCauseInstanceOf(IllegalStateException.class)
          .hasRootCauseMessage("Initialization failed");
    }
  }

  // --- Helper Classes for Testing ---

  /**
   * A test cache implementation that tracks if its initialize() method has been called.
   */
  static class InitializableCache extends PerpetualCache implements InitializingObject {
    private boolean initialized = false;

    public InitializableCache(String id) {
      super(id);
    }

    @Override
    public void initialize() {
      this.initialized = true;
    }

    public boolean isInitialized() {
      return initialized;
    }
  }

  /**
   * A test cache implementation whose initialize() method always throws an exception.
   */
  static class FailingInitializableCache extends PerpetualCache implements InitializingObject {
    public FailingInitializableCache(String id) {
      super(id);
    }

    @Override
    public void initialize() {
      throw new IllegalStateException("Initialization failed");
    }
  }

  /**
   * The CacheBuilder wraps the base cache implementation with decorators.
   * This helper uses reflection to access the underlying (delegate) cache
   * instance to verify its state.
   *
   * @param cache The potentially decorated cache instance.
   * @return The unwrapped, underlying cache implementation.
   */
  @SuppressWarnings("unchecked")
  private <T> T getUnderlyingCacheImplementation(Cache cache) {
    try {
      Field delegateField = cache.getClass().getDeclaredField("delegate");
      delegateField.setAccessible(true);
      return (T) delegateField.get(cache);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalStateException("Failed to get 'delegate' field from cache for testing", e);
    }
  }
}