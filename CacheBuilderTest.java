package org.apache.ibatis.mapping;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.BDDAssertions.then;

import java.lang.reflect.Field;

import org.apache.ibatis.builder.InitializingObject;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CacheBuilderTest {

  @Test
  void shouldInitializeCacheSuccessfully() {
    // Arrange
    CacheBuilder cacheBuilder = new CacheBuilder("test").implementation(InitializingCache.class);

    // Act
    InitializingCache cache = unwrap(cacheBuilder.build());

    // Assert
    Assertions.assertThat(cache.isInitialized()).isTrue();
  }

  @Test
  void shouldThrowExceptionOnCacheInitializationFailure() {
    // Arrange
    CacheBuilder cacheBuilder = new CacheBuilder("test").implementation(InitializingFailureCache.class);

    // Act
    when(() -> cacheBuilder.build());

    // Assert
    then(caughtException())
      .isInstanceOf(CacheException.class)
      .hasMessage("Failed cache initialization for 'test' on 'org.apache.ibatis.mapping.CacheBuilderTest$InitializingFailureCache'");
  }

  /**
   * Unwraps the underlying cache implementation from a proxy or decorator.
   *
   * @param cache The cache instance to unwrap.
   * @param <T> The type of the underlying cache implementation.
   * @return The unwrapped cache implementation.
   */
  @SuppressWarnings("unchecked")
  private <T> T unwrap(Cache cache) {
    try {
      Field delegateField = cache.getClass().getDeclaredField("delegate");
      delegateField.setAccessible(true);
      try {
        return (T) delegateField.get(cache);
      } finally {
        delegateField.setAccessible(false);
      }
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalStateException("Failed to unwrap cache", e);
    }
  }

  /**
   * A cache implementation that initializes successfully.
   */
  private static class InitializingCache extends PerpetualCache implements InitializingObject {

    private boolean initialized;

    public InitializingCache(String id) {
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
   * A cache implementation that fails during initialization.
   */
  private static class InitializingFailureCache extends PerpetualCache implements InitializingObject {

    public InitializingFailureCache(String id) {
      super(id);
    }

    @Override
    public void initialize() {
      throw new IllegalStateException("error");
    }
  }
}