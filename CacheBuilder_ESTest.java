package org.apache.ibatis.mapping;

import static org.junit.Assert.*;

import java.util.Properties;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.BlockingCache;
import org.apache.ibatis.cache.decorators.SynchronizedCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

/**
 * Readable, behavior-oriented tests for CacheBuilder.
 *
 * Notes:
 * - Focus on observable behavior and meaningful configuration scenarios.
 * - Avoid environment-specific assumptions (e.g., EvoSuite runner, mock JVM).
 * - Use descriptive test names and keep assertions clear and minimal.
 */
public class CacheBuilderTest {

  @Test
  public void build_withEmptyId_returnsCacheWithSameId() {
    Cache cache = new CacheBuilder("").build();
    assertNotNull(cache);
    assertEquals("", cache.getId());
  }

  @Test
  public void build_withBlockingEnabled_returnsBlockingCacheAsOutermost() {
    CacheBuilder builder = new CacheBuilder("cache-blocking");
    Cache cache = builder.blocking(true).build();

    assertTrue("Expected outermost cache to be BlockingCache", cache instanceof BlockingCache);
    BlockingCache blocking = (BlockingCache) cache;
    // default timeout should be 0 unless configured
    assertEquals(0L, blocking.getTimeout());
    assertEquals("cache-blocking", cache.getId());
  }

  @Test(expected = RuntimeException.class)
  public void build_withInvalidBaseImplementation_throwsRuntimeException() {
    // SynchronizedCache is a decorator (expects a Cache in constructor), not a base implementation
    new CacheBuilder("bad-base")
        .implementation(SynchronizedCache.class)
        .build();
  }

  @Test(expected = RuntimeException.class)
  public void build_withInvalidDecoratorClass_throwsRuntimeException() {
    // PerpetualCache is a base implementation (expects String id), not a decorator
    new CacheBuilder("bad-decorator")
        .addDecorator(PerpetualCache.class)
        .build();
  }

  @Test
  public void addDecorator_withNull_isNoOpAndReturnsSameBuilder() {
    CacheBuilder builder = new CacheBuilder("noop");
    CacheBuilder same = builder.addDecorator(null);
    assertSame(builder, same);

    Cache cache = builder.build();
    assertEquals("noop", cache.getId());
  }

  @Test(expected = NumberFormatException.class)
  public void build_withEmptySizeProperty_throwsNumberFormatException() {
    // Many cache decorators (e.g., LruCache) expect 'size' to be a number
    Properties props = new Properties();
    props.setProperty("size", ""); // invalid numeric value

    new CacheBuilder("prop-error")
        .properties(props)
        .build();
  }

  @Test
  public void build_withSizeAndClearInterval_buildsSuccessfully() {
    Cache cache = new CacheBuilder("configured")
        .size(128)
        .clearInterval(60_000L)
        .build();

    assertNotNull(cache);
    assertEquals("configured", cache.getId());
  }

  @Test
  public void build_withValidDecorator_buildsSuccessfully() {
    Cache cache = new CacheBuilder("decorated")
        .addDecorator(SynchronizedCache.class)
        .build();

    assertNotNull(cache);
    assertEquals("decorated", cache.getId());
  }

  @Test
  public void build_withExplicitPerpetualBaseImplementation_buildsSuccessfully() {
    Cache cache = new CacheBuilder("explicit-base")
        .implementation(PerpetualCache.class)
        .build();

    assertNotNull(cache);
    assertEquals("explicit-base", cache.getId());
  }

  @Test
  public void build_withReadWriteEnabled_buildsSuccessfully() {
    Cache cache = new CacheBuilder("read-write")
        .readWrite(true)
        .build();

    assertNotNull(cache);
    assertEquals("read-write", cache.getId());
  }
}