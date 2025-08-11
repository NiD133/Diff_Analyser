package org.apache.ibatis.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Field;

import org.apache.ibatis.builder.InitializingObject;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CacheBuilder} focusing on initialization behavior for cache implementations
 * that implement {@link InitializingObject}.
 */
class CacheBuilderTest {

  private static final String CACHE_ID = "test";

  @Test
  @DisplayName("CacheBuilder should call initialize() on custom cache implementations")
  void shouldInitializeCustomCacheImplementation() {
    Cache built = new CacheBuilder(CACHE_ID)
        .implementation(InitializingCache.class)
        .build();

    // The built cache may be wrapped by decorators. Unwrap to the actual implementation.
    InitializingCache target = unwrapDelegate(built, InitializingCache.class);

    assertThat(target.initialized)
        .as("initialize() should have been called on the underlying cache")
        .isTrue();
  }

  @Test
  @DisplayName("CacheBuilder should wrap initialization failures in CacheException with a helpful message")
  void shouldWrapInitializationFailuresInCacheException() {
    assertThatThrownBy(() ->
        new CacheBuilder(CACHE_ID)
            .implementation(InitializingFailureCache.class)
            .build())
        .isInstanceOf(CacheException.class)
        .hasMessage(
            "Failed cache initialization for 'test' on 'org.apache.ibatis.mapping.CacheBuilderTest$InitializingFailureCache'");
  }

  /**
   * Unwraps cache decorators by following the "delegate" field until an instance of the expected type is found.
   */
  private static <T> T unwrapDelegate(Cache cache, Class<T> expectedType) {
    Cache current = cache;
    while (current != null) {
      if (expectedType.isInstance(current)) {
        return expectedType.cast(current);
      }
      Field delegateField = findDelegateField(current.getClass());
      if (delegateField == null) {
        throw new IllegalStateException(
            "Could not find 'delegate' field while unwrapping cache of type " + current.getClass().getName());
      }
      Object next = getFieldValue(delegateField, current);
      if (!(next instanceof Cache)) {
        throw new IllegalStateException(
            "'delegate' field is not a Cache in type " + current.getClass().getName());
      }
      current = (Cache) next;
    }
    throw new IllegalStateException("Reached null delegate while unwrapping to " + expectedType.getName());
  }

  private static Field findDelegateField(Class<?> type) {
    Class<?> c = type;
    while (c != null) {
      try {
        Field f = c.getDeclaredField("delegate");
        f.setAccessible(true);
        return f;
      } catch (NoSuchFieldException ignored) {
        c = c.getSuperclass();
      }
    }
    return null;
  }

  private static Object getFieldValue(Field field, Object target) {
    try {
      return field.get(target);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException("Unable to access field '" + field.getName() + "'", e);
    } finally {
      field.setAccessible(false);
    }
  }

  /**
   * A cache that records whether initialize() was called.
   */
  private static class InitializingCache extends PerpetualCache implements InitializingObject {
    private boolean initialized;

    InitializingCache(String id) {
      super(id);
    }

    @Override
    public void initialize() {
      this.initialized = true;
    }
  }

  /**
   * A cache that throws from initialize() to simulate initialization failure.
   */
  private static class InitializingFailureCache extends PerpetualCache implements InitializingObject {
    InitializingFailureCache(String id) {
      super(id);
    }

    @Override
    public void initialize() {
      throw new IllegalStateException("error");
    }
  }
}