package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link SerializedCache}.
 */
public class SerializedCacheTest {

    /**
     * Verifies that attempting to put a non-serializable object into a SerializedCache
     * results in a CacheException. The SerializedCache must ensure that all cached
     * items are serializable to function correctly.
     */
    @Test
    public void shouldThrowExceptionWhenPuttingNonSerializableObject() {
        // Arrange: Create a SerializedCache and a value that cannot be serialized.
        Cache delegateCache = new PerpetualCache("test-delegate");
        SerializedCache serializedCache = new SerializedCache(delegateCache);

        // A PerpetualCache instance itself is not serializable, making it a good test case.
        Object nonSerializableValue = new PerpetualCache("non-serializable-value");
        String key = "anyKey";

        // Act & Assert: Expect a CacheException when putting the non-serializable value.
        try {
            serializedCache.putObject(key, nonSerializableValue);
            fail("Expected a CacheException to be thrown for a non-serializable value.");
        } catch (CacheException e) {
            // Verify that the exception message clearly indicates the problem.
            String expectedMessage = "SharedCache failed to make a copy of a non-serializable object";
            assertThat(e.getMessage(), containsString(expectedMessage));
        }
    }
}