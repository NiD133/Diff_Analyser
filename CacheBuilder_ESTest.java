package org.apache.ibatis.mapping;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Properties;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.BlockingCache;
import org.apache.ibatis.cache.decorators.SynchronizedCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class CacheBuilder_ESTest extends CacheBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testBuildCacheWithInvalidSizeProperty() throws Throwable {
        // Setup properties with an invalid size value
        Properties properties = new Properties();
        properties.setProperty("size", "");

        // Create CacheBuilder with properties
        CacheBuilder cacheBuilder = new CacheBuilder(",Fpc9HVO,j|b").properties(properties);

        // Expect NumberFormatException due to invalid size
        try {
            cacheBuilder.build();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            verifyException("java.lang.NumberFormatException", e);
        }
    }

    @Test(timeout = 4000)
    public void testBuildCacheWithInvalidImplementation() throws Throwable {
        // Create CacheBuilder with SynchronizedCache implementation
        CacheBuilder cacheBuilder = new CacheBuilder("w").implementation(SynchronizedCache.class);

        // Expect RuntimeException due to missing constructor
        try {
            cacheBuilder.build();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.mapping.CacheBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testBuildCacheWithNullId() throws Throwable {
        // Create CacheBuilder with null ID
        CacheBuilder cacheBuilder = new CacheBuilder(null);

        // Expect RuntimeException due to null ID
        try {
            cacheBuilder.build();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.mapping.CacheBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testBuildCacheWithInvalidPropertyKey() throws Throwable {
        // Setup properties with an invalid key
        Properties properties = new Properties();
        properties.setProperty("xPxjnuTbn[", "xPxjnuTbn[");

        // Create CacheBuilder with properties
        CacheBuilder cacheBuilder = new CacheBuilder("xPxjnuTbn[").properties(properties);

        // Expect StringIndexOutOfBoundsException due to invalid key
        try {
            cacheBuilder.build();
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testBuildCacheWithInvalidPropertyType() throws Throwable {
        // Setup properties with CacheBuilder as key
        CacheBuilder cacheBuilder = new CacheBuilder("F");
        Properties properties = new Properties();
        properties.put(cacheBuilder, "F");

        // Create CacheBuilder with properties
        CacheBuilder cacheBuilderWithProperties = cacheBuilder.properties(properties);

        // Expect ClassCastException due to invalid property type
        try {
            cacheBuilderWithProperties.build();
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.apache.ibatis.mapping.CacheBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testBuildCacheWithEmptyId() throws Throwable {
        // Setup properties with empty ID
        Properties properties = new Properties();
        properties.setProperty("", "");

        // Create CacheBuilder with properties
        CacheBuilder cacheBuilder = new CacheBuilder("").properties(properties);

        // Build cache and verify ID
        Cache cache = cacheBuilder.build();
        assertEquals("", cache.getId());
    }

    @Test(timeout = 4000)
    public void testBuildBlockingCache() throws Throwable {
        // Create CacheBuilder with blocking enabled
        CacheBuilder cacheBuilder = new CacheBuilder("org.apache.ibatis.mapping.CacheBuilder").blocking(true);

        // Build blocking cache and verify timeout
        BlockingCache blockingCache = (BlockingCache) cacheBuilder.build();
        assertEquals(0L, blockingCache.getTimeout());
    }

    @Test(timeout = 4000)
    public void testBuildReadWriteCache() throws Throwable {
        // Create CacheBuilder with readWrite enabled
        CacheBuilder cacheBuilder = new CacheBuilder("iFLF@Zzpqzdl1Iv-&4p").readWrite(true);

        // Build cache and verify ID
        Cache cache = cacheBuilder.build();
        assertEquals("iFLF@Zzpqzdl1Iv-&4p", cache.getId());
    }

    @Test(timeout = 4000)
    public void testBuildCacheWithDecoratorAndSize() throws Throwable {
        // Create CacheBuilder with decorator and size
        CacheBuilder cacheBuilder = new CacheBuilder("org.apache.ibatis.reflection.SystemMetaObject")
            .addDecorator(SynchronizedCache.class)
            .size(-1);

        // Build cache and verify ID
        Cache cache = cacheBuilder.build();
        assertEquals("org.apache.ibatis.reflection.SystemMetaObject", cache.getId());
    }

    @Test(timeout = 4000)
    public void testBuildCacheWithInvalidDecorator() throws Throwable {
        // Create CacheBuilder with PerpetualCache decorator
        CacheBuilder cacheBuilder = new CacheBuilder("").addDecorator(PerpetualCache.class);

        // Expect RuntimeException due to invalid decorator
        try {
            cacheBuilder.build();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.mapping.CacheBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddNullDecorator() throws Throwable {
        // Create CacheBuilder and add null decorator
        CacheBuilder cacheBuilder = new CacheBuilder("");
        CacheBuilder result = cacheBuilder.addDecorator(null);

        // Verify that the same CacheBuilder is returned
        assertSame(cacheBuilder, result);
    }

    @Test(timeout = 4000)
    public void testBuildCacheWithSize() throws Throwable {
        // Create CacheBuilder with size
        CacheBuilder cacheBuilder = new CacheBuilder("").size(1);

        // Build cache and verify ID
        Cache cache = cacheBuilder.build();
        assertEquals("", cache.getId());
    }

    @Test(timeout = 4000)
    public void testBuildCacheWithClearInterval() throws Throwable {
        // Create CacheBuilder with clearInterval
        CacheBuilder cacheBuilder = new CacheBuilder("").clearInterval(1L);

        // Build cache and verify ID
        Cache cache = cacheBuilder.build();
        assertEquals("", cache.getId());
    }
}