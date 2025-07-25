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

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.BDDAssertions.then;

import java.lang.reflect.Field;

import org.apache.ibatis.builder.InitializingObject;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CacheBuilderTest {

  @Test
  @DisplayName("Should call initialize method on cache implementation if it implements InitializingObject")
  void shouldCallInitializeOnCacheImplementation() {
    // Given
    CacheBuilder cacheBuilder = new CacheBuilder("test")
        .implementation(InitializingCache.class);

    // When
    Cache cache = cacheBuilder.build();
    InitializingCache initializingCache = unwrap(cache);

    // Then
    then(initializingCache.initialized).isTrue();
  }

  @Test
  @DisplayName("Should throw CacheException if cache implementation throws exception during initialization")
  void shouldThrowCacheExceptionIfInitializationFails() {
    // Given
    CacheBuilder cacheBuilder = new CacheBuilder("test")
        .implementation(InitializingFailureCache.class);

    // When
    when(cacheBuilder::build);

    // Then
    then(caughtException())
        .isInstanceOf(CacheException.class)
        .hasMessage(
            "Failed cache initialization for 'test' on 'org.apache.ibatis.mapping.CacheBuilderTest$InitializingFailureCache'");
  }

  /**
   * Helper method to access the delegate field of a Cache object.  This is necessary to get a hold of the
   * actual PerpetualCache instance that is wrapped by the CacheBuilder.  This is a bit of a hack, but it's
   * the easiest way to verify that the initialize method is being called correctly.
   *
   * @param cache The cache object to unwrap.
   * @param <T>   The type of the delegate object.
   * @return The delegate object.
   * @throws IllegalStateException If the delegate field cannot be accessed.
   */
  @SuppressWarnings("unchecked")
  private <T> T unwrap(Cache cache) {
    Field field;
    try {
      field = cache.getClass().getDeclaredField("delegate");
    } catch (NoSuchFieldException e) {
      throw new IllegalStateException(e);
    }
    try {
      field.setAccessible(true);
      return (T) field.get(cache);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    } finally {
      field.setAccessible(false);
    }
  }

  /**
   * A simple cache implementation that implements the InitializingObject interface.  This is used to verify
   * that the CacheBuilder is calling the initialize method on the cache implementation.
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

  }

  /**
   * A cache implementation that throws an exception during initialization. This is used to verify that the
   * CacheBuilder is handling exceptions correctly.
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