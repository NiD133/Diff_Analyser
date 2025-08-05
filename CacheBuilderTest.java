/*
 *    Copyright 2009-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Field;

import org.apache.ibatis.builder.InitializingObject;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.Test;

/**
 * Tests for CacheBuilder focusing on initialization behavior of cache implementations
 * that implement the InitializingObject interface.
 */
class CacheBuilderTest {

  private static final String TEST_CACHE_ID = "testCache";

  @Test
  void shouldInitializeCacheWhenImplementsInitializingObject() {
    // Given: A CacheBuilder configured with an initializing cache implementation
    CacheBuilder cacheBuilder = new CacheBuilder(TEST_CACHE_ID)
        .implementation(TestInitializingCache.class);
    
    // When: Building the cache
    Cache builtCache = cacheBuilder.build();
    TestInitializingCache actualCache = extractDelegateCache(builtCache);
    
    // Then: The cache should be properly initialized
    assertThat(actualCache.isInitialized())
        .as("Cache should be initialized after build")
        .isTrue();
  }

  @Test
  void shouldThrowCacheExceptionWhenInitializationFails() {
    // Given: A CacheBuilder configured with a cache that fails during initialization
    CacheBuilder cacheBuilder = new CacheBuilder(TEST_CACHE_ID)
        .implementation(TestFailingInitializationCache.class);
    
    // When & Then: Building the cache should throw a CacheException with descriptive message
    assertThatThrownBy(() -> cacheBuilder.build())
        .isInstanceOf(CacheException.class)
        .hasMessage("Failed cache initialization for 'testCache' on '" + 
                   TestFailingInitializationCache.class.getName() + "'");
  }

  /**
   * Extracts the delegate cache from a potentially decorated cache using reflection.
   * This is necessary because CacheBuilder may wrap the actual cache implementation
   * with decorators.
   */
  @SuppressWarnings("unchecked")
  private <T> T extractDelegateCache(Cache cache) {
    try {
      Field delegateField = cache.getClass().getDeclaredField("delegate");
      delegateField.setAccessible(true);
      try {
        return (T) delegateField.get(cache);
      } finally {
        delegateField.setAccessible(false);
      }
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalStateException("Failed to extract delegate cache", e);
    }
  }

  /**
   * Test cache implementation that successfully initializes when built.
   */
  private static class TestInitializingCache extends PerpetualCache implements InitializingObject {
    private boolean initialized = false;

    public TestInitializingCache(String id) {
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
   * Test cache implementation that throws an exception during initialization.
   */
  private static class TestFailingInitializationCache extends PerpetualCache implements InitializingObject {

    public TestFailingInitializationCache(String id) {
      super(id);
    }

    @Override
    public void initialize() {
      throw new IllegalStateException("Simulated initialization failure");
    }
  }
}