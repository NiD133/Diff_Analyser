/*
 * Test suite for CacheBuilder class
 * Tests the builder pattern functionality for creating MyBatis cache instances
 */

package org.apache.ibatis.mapping;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Properties;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.BlockingCache;
import org.apache.ibatis.cache.decorators.SynchronizedCache;
import org.apache.ibatis.cache.impl.PerpetualCache;

public class CacheBuilderTest {

    private static final String VALID_CACHE_ID = "testCache";
    private static final String EMPTY_CACHE_ID = "";

    // ========== Exception Handling Tests ==========

    @Test(timeout = 4000)
    public void shouldThrowNumberFormatExceptionWhenSizePropertyIsEmpty() {
        // Given: Properties with empty size value
        Properties properties = new Properties();
        properties.setProperty("size", "");
        
        CacheBuilder cacheBuilder = new CacheBuilder(VALID_CACHE_ID)
                .properties(properties);

        // When & Then: Building cache should throw NumberFormatException
        try {
            cacheBuilder.build();
            fail("Expected NumberFormatException for empty size property");
        } catch (NumberFormatException e) {
            assertTrue("Should contain empty string message", 
                      e.getMessage().contains("For input string: \"\""));
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowRuntimeExceptionWhenUsingInvalidBaseImplementation() {
        // Given: CacheBuilder with SynchronizedCache as base implementation
        // (SynchronizedCache is a decorator, not a base implementation)
        CacheBuilder cacheBuilder = new CacheBuilder(VALID_CACHE_ID)
                .implementation(SynchronizedCache.class);

        // When & Then: Building cache should throw RuntimeException
        try {
            cacheBuilder.build();
            fail("Expected RuntimeException for invalid base cache implementation");
        } catch (RuntimeException e) {
            assertTrue("Should mention invalid base cache implementation", 
                      e.getMessage().contains("Invalid base cache implementation"));
            assertTrue("Should mention missing String constructor", 
                      e.getMessage().contains("constructor that takes a String id"));
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowRuntimeExceptionWhenCacheIdIsNull() {
        // Given: CacheBuilder with null ID
        CacheBuilder cacheBuilder = new CacheBuilder(null);

        // When & Then: Building cache should throw RuntimeException
        try {
            cacheBuilder.build();
            fail("Expected RuntimeException for null cache ID");
        } catch (RuntimeException e) {
            assertTrue("Should mention error building decorators", 
                      e.getMessage().contains("Error building standard cache decorators"));
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowStringIndexOutOfBoundsExceptionForInvalidProperty() {
        // Given: Properties with invalid property configuration
        Properties properties = new Properties();
        String invalidValue = "invalidPropertyValue";
        properties.setProperty(invalidValue, invalidValue);
        
        CacheBuilder cacheBuilder = new CacheBuilder(invalidValue)
                .properties(properties);

        // When & Then: Building cache should throw StringIndexOutOfBoundsException
        try {
            cacheBuilder.build();
            fail("Expected StringIndexOutOfBoundsException for invalid property");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowClassCastExceptionWhenPropertiesContainNonStringKeys() {
        // Given: Properties with non-String key (CacheBuilder object as key)
        CacheBuilder cacheBuilder = new CacheBuilder(VALID_CACHE_ID);
        Properties properties = new Properties();
        properties.put(cacheBuilder, VALID_CACHE_ID); // Invalid: non-String key
        
        cacheBuilder.properties(properties);

        // When & Then: Building cache should throw ClassCastException
        try {
            cacheBuilder.build();
            fail("Expected ClassCastException for non-String property key");
        } catch (ClassCastException e) {
            assertTrue("Should mention CacheBuilder cannot be cast to String", 
                      e.getMessage().contains("CacheBuilder cannot be cast"));
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowRuntimeExceptionWhenUsingInvalidDecorator() {
        // Given: CacheBuilder with PerpetualCache as decorator
        // (PerpetualCache is a base implementation, not a decorator)
        CacheBuilder cacheBuilder = new CacheBuilder(EMPTY_CACHE_ID)
                .addDecorator(PerpetualCache.class);

        // When & Then: Building cache should throw RuntimeException
        try {
            cacheBuilder.build();
            fail("Expected RuntimeException for invalid cache decorator");
        } catch (RuntimeException e) {
            assertTrue("Should mention invalid cache decorator", 
                      e.getMessage().contains("Invalid cache decorator"));
            assertTrue("Should mention missing Cache constructor", 
                      e.getMessage().contains("constructor that takes a Cache instance"));
        }
    }

    // ========== Successful Cache Building Tests ==========

    @Test(timeout = 4000)
    public void shouldBuildCacheWithEmptyProperties() {
        // Given: CacheBuilder with empty properties
        Properties properties = new Properties();
        properties.setProperty("", "");
        
        CacheBuilder cacheBuilder = new CacheBuilder(EMPTY_CACHE_ID)
                .properties(properties);

        // When: Building the cache
        Cache cache = cacheBuilder.build();

        // Then: Cache should be created with correct ID
        assertEquals("Cache should have empty ID", EMPTY_CACHE_ID, cache.getId());
    }

    @Test(timeout = 4000)
    public void shouldBuildBlockingCache() {
        // Given: CacheBuilder configured for blocking cache
        CacheBuilder cacheBuilder = new CacheBuilder("blockingCache")
                .blocking(true);

        // When: Building the cache
        Cache cache = cacheBuilder.build();

        // Then: Should return BlockingCache instance
        assertTrue("Should be BlockingCache instance", cache instanceof BlockingCache);
        BlockingCache blockingCache = (BlockingCache) cache;
        assertEquals("Blocking cache should have default timeout", 0L, blockingCache.getTimeout());
    }

    @Test(timeout = 4000)
    public void shouldBuildReadWriteCache() {
        // Given: CacheBuilder configured for read-write cache
        String cacheId = "readWriteCache";
        CacheBuilder cacheBuilder = new CacheBuilder(cacheId)
                .readWrite(true);

        // When: Building the cache
        Cache cache = cacheBuilder.build();

        // Then: Cache should be created with correct ID
        assertEquals("Cache should have correct ID", cacheId, cache.getId());
    }

    @Test(timeout = 4000)
    public void shouldBuildCacheWithSizeAndDecorator() {
        // Given: CacheBuilder with size configuration and decorator
        String cacheId = "sizedCache";
        CacheBuilder cacheBuilder = new CacheBuilder(cacheId)
                .addDecorator(SynchronizedCache.class)
                .size(-1); // Negative size should be handled gracefully

        // When: Building the cache
        Cache cache = cacheBuilder.build();

        // Then: Cache should be created with correct ID
        assertEquals("Cache should have correct ID", cacheId, cache.getId());
    }

    @Test(timeout = 4000)
    public void shouldBuildCacheWithSize() {
        // Given: CacheBuilder with positive size
        CacheBuilder cacheBuilder = new CacheBuilder(EMPTY_CACHE_ID)
                .size(1);

        // When: Building the cache
        Cache cache = cacheBuilder.build();

        // Then: Cache should be created successfully
        assertEquals("Cache should have correct ID", EMPTY_CACHE_ID, cache.getId());
    }

    @Test(timeout = 4000)
    public void shouldBuildCacheWithClearInterval() {
        // Given: CacheBuilder with clear interval
        CacheBuilder cacheBuilder = new CacheBuilder(EMPTY_CACHE_ID)
                .clearInterval(1L);

        // When: Building the cache
        Cache cache = cacheBuilder.build();

        // Then: Cache should be created successfully
        assertEquals("Cache should have correct ID", EMPTY_CACHE_ID, cache.getId());
    }

    // ========== Builder Pattern Tests ==========

    @Test(timeout = 4000)
    public void shouldReturnSameInstanceWhenAddingNullDecorator() {
        // Given: CacheBuilder instance
        CacheBuilder cacheBuilder = new CacheBuilder(EMPTY_CACHE_ID);

        // When: Adding null decorator
        CacheBuilder result = cacheBuilder.addDecorator(null);

        // Then: Should return same instance (fluent interface)
        assertSame("Should return same CacheBuilder instance", cacheBuilder, result);
    }
}