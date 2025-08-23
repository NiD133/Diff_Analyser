package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for the BlockingCache decorator.
 */
public class BlockingCacheTest {

    /**
     * Verifies that if the delegate cache throws an exception during a getObject call,
     * the BlockingCache decorator correctly propagates that exception to the caller.
     */
    @Test
    public void shouldPropagateExceptionFromDelegateOnGet() {
        // Arrange
        // 1. Create a mock for the delegate cache, which is the dependency we want to control.
        Cache delegateCache = mock(Cache.class);
        BlockingCache blockingCache = new BlockingCache(delegateCache);

        // 2. Define a sample key and the expected exception.
        Object key = "some-key";
        CacheException expectedException = new CacheException("Error from delegate cache");

        // 3. Configure the mock to throw our defined exception when getObject is called.
        when(delegateCache.getObject(key)).thenThrow(expectedException);

        // Act & Assert
        try {
            blockingCache.getObject(key);
            fail("Expected BlockingCache to propagate CacheException from the delegate.");
        } catch (CacheException actualException) {
            // 4. Verify that the caught exception is the one we expected.
            assertEquals("The propagated exception should be the same as the one from the delegate.",
                expectedException, actualException);
        }
    }
}