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
    // Arrange: Create a CacheBuilder and build an InitializingCache
    InitializingCache cache = extractDelegateCache(
        new CacheBuilder("test")
            .implementation(InitializingCache.class)
            .build()
    );

    // Assert: Verify that the cache is initialized
    Assertions.assertThat(cache.isInitialized()).isTrue();
  }

  @Test
  void shouldThrowExceptionWhenCacheInitializationFails() {
    // Act: Attempt to build a cache that fails during initialization
    when(() -> new CacheBuilder("test")
        .implementation(InitializingFailureCache.class)
        .build()
    );

    // Assert: Verify that a CacheException is thrown with the expected message
    then(caughtException())
        .isInstanceOf(CacheException.class)
        .hasMessage("Failed cache initialization for 'test' on 'org.apache.ibatis.mapping.CacheBuilderTest$InitializingFailureCache'");
  }

  /**
   * Extracts the delegate cache from a given Cache instance.
   *
   * @param cache the Cache instance to extract from
   * @param <T>   the type of the delegate cache
   * @return the delegate cache
   */
  @SuppressWarnings("unchecked")
  private <T> T extractDelegateCache(Cache cache) {
    try {
      Field delegateField = cache.getClass().getDeclaredField("delegate");
      delegateField.setAccessible(true);
      return (T) delegateField.get(cache);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalStateException("Failed to extract delegate cache", e);
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