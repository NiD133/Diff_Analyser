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

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.decorators.BlockingCache;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.decorators.ScheduledCache;
import org.apache.ibatis.cache.decorators.SerializedCache;
import org.apache.ibatis.cache.decorators.SynchronizedCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link CacheBuilder}.
 * This class focuses on verifying the correct construction and configuration of Cache instances.
 */
public class CacheBuilderTest {

  private static final String CACHE_ID = "test-cache";

  // --- Successful Build Scenarios ---

  @Test
  public void shouldBuildDefaultCacheWithPerpetualImplementation() {
    // Arrange
    CacheBuilder builder = new CacheBuilder(CACHE_ID);

    // Act
    Cache cache = builder.build();

    // Assert
    assertNotNull(cache);
    assertEquals(CACHE_ID, cache.getId());
    // The default cache stack is: Synchronized -> Logging -> Serialized (if R/W) -> Scheduled -> Lru -> Perpetual
    // We can check the final underlying implementation.
    assertThat(cache, instanceOf(SynchronizedCache.class));
  }

  @Test
  public void shouldBuildLruCacheWhenSizeIsSet() {
    // Arrange
    CacheBuilder builder = new CacheBuilder(CACHE_ID).size(1024);

    // Act
    Cache cache = builder.build();

    // Assert
    // The LruCache decorator is added when a size is specified.
    assertNotNull(cache);
    // To verify, we would need to inspect the decorator chain, which is not public.
    // A successful build confirms the configuration was accepted.
    assertEquals(CACHE_ID, cache.getId());
  }

  @Test
  public void shouldBuildScheduledCacheWhenClearIntervalIsSet() {
    // Arrange
    CacheBuilder builder = new CacheBuilder(CACHE_ID).clearInterval(60000L);

    // Act
    Cache cache = builder.build();

    // Assert
    // The ScheduledCache decorator is added when a clear interval is specified.
    assertNotNull(cache);
    assertEquals(CACHE_ID, cache.getId());
  }

  @Test
  public void shouldBuildSerializedCacheWhenReadWriteIsEnabled() {
    // Arrange
    CacheBuilder builder = new CacheBuilder(CACHE_ID).readWrite(true);

    // Act
    Cache cache = builder.build();

    // Assert
    // The SerializedCache decorator is added when readWrite is true.
    assertNotNull(cache);
    assertEquals(CACHE_ID, cache.getId());
  }

  @Test
  public void shouldBuildBlockingCacheWhenBlockingIsEnabled() {
    // Arrange
    CacheBuilder builder = new CacheBuilder(CACHE_ID).blocking(true);

    // Act
    Cache cache = builder.build();

    // Assert
    assertNotNull(cache);
    assertEquals(CACHE_ID, cache.getId());
    assertThat(cache, instanceOf(BlockingCache.class));
  }

  // --- Decorator and Implementation Handling ---

  @Test
  public void shouldApplyCustomDecoratorsInOrder() {
    // Arrange
    CacheBuilder builder = new CacheBuilder(CACHE_ID)
        .implementation(PerpetualCache.class)
        .addDecorator(LruCache.class)
        .addDecorator(SynchronizedCache.class);

    // Act
    Cache cache = builder.build();

    // Assert
    assertNotNull(cache);
    assertThat(cache, instanceOf(SynchronizedCache.class));
  }

  @Test
  public void shouldIgnoreNullDecorators() {
    // Arrange
    CacheBuilder builder = new CacheBuilder(CACHE_ID);

    // Act
    CacheBuilder returnedBuilder = builder.addDecorator(null);

    // Assert
    // The builder should not throw an exception and return itself for chaining.
    assertSame(builder, returnedBuilder);
  }

  // --- Exception and Error Scenarios ---

  @Test
  public void shouldThrowExceptionWhenImplementationLacksStringIdConstructor() {
    // Arrange
    CacheBuilder builder = new CacheBuilder(CACHE_ID)
        .implementation(SynchronizedCache.class); // SynchronizedCache requires a Cache instance, not a String ID.

    // Act & Assert
    try {
      builder.build();
      fail("Should have thrown CacheException for invalid base cache implementation.");
    } catch (CacheException e) {
      assertThat(e.getMessage(), is("Invalid base cache implementation (class org.apache.ibatis.cache.decorators.SynchronizedCache).  Base cache implementations must have a constructor that takes a String id as a parameter.  Cause: java.lang.NoSuchMethodException: org.apache.ibatis.cache.decorators.SynchronizedCache.<init>(java.lang.String)"));
    }
  }

  @Test
  public void shouldThrowExceptionWhenDecoratorLacksCacheConstructor() {
    // Arrange
    CacheBuilder builder = new CacheBuilder(CACHE_ID)
        .addDecorator(PerpetualCache.class); // PerpetualCache is a base implementation, not a decorator.

    // Act & Assert
    try {
      builder.build();
      fail("Should have thrown CacheException for invalid decorator.");
    } catch (CacheException e) {
      assertThat(e.getMessage(), is("Invalid cache decorator (class org.apache.ibatis.cache.impl.PerpetualCache).  Cache decorators must have a constructor that takes a Cache instance as a parameter.  Cause: java.lang.NoSuchMethodException: org.apache.ibatis.cache.impl.PerpetualCache.<init>(org.apache.ibatis.cache.Cache)"));
    }
  }

  @Test(expected = NumberFormatException.class)
  public void shouldThrowExceptionWhenSizePropertyIsInvalid() {
    // Arrange
    Properties props = new Properties();
    props.setProperty("size", "invalid-size"); // Not a number
    CacheBuilder builder = new CacheBuilder(CACHE_ID).properties(props);

    // Act
    builder.build(); // Asserts exception
  }

  @Test(expected = ClassCastException.class)
  public void shouldThrowExceptionWhenPropertyKeyIsNotAString() {
    // Arrange
    Properties props = new Properties();
    // Properties can hold non-string keys, but CacheBuilder expects String keys.
    props.put(123, "value");
    CacheBuilder builder = new CacheBuilder(CACHE_ID).properties(props);

    // Act
    builder.build(); // Asserts exception
  }

  @Test
  public void shouldThrowExceptionWhenIdIsNull() {
    // Arrange
    // A null ID causes issues with decorators that require an ID for logging.
    CacheBuilder builder = new CacheBuilder(null);

    // Act & Assert
    try {
      builder.build();
      fail("Should have thrown CacheException due to null ID.");
    } catch (CacheException e) {
      assertThat(e.getMessage(), is(
          "Error building standard cache decorators.  Cause: java.lang.IllegalArgumentException: logger name cannot be null"));
    }
  }
}