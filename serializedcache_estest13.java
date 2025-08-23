package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

/**
 * Test suite for the {@link SerializedCache} decorator.
 */
public class SerializedCacheTest {

    /**
     * Verifies that removeObject() throws a NullPointerException if the underlying
     * delegate cache is null.
     *
     * A SerializedCache must be constructed with a valid, non-null delegate. This
     * test ensures that invoking methods on an improperly configured instance fails fast.
     */
    @Test(expected = NullPointerException.class)
    public void removeObjectShouldThrowNullPointerExceptionWhenDelegateIsNull() {
        // Arrange: Create a SerializedCache with a null delegate.
        SerializedCache serializedCache = new SerializedCache(null);

        // Act: Attempt to remove an object. This should fail because the call
        // will be forwarded to the null delegate.
        serializedCache.removeObject("any-key");

        // Assert: The test expects a NullPointerException, as declared in the @Test annotation.
    }
}