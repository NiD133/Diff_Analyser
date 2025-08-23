package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Test suite for the {@link SerializedCache} decorator.
 */
public class SerializedCacheTest {

    /**
     * Verifies that calling hashCode() on a SerializedCache with a null delegate
     * throws a NullPointerException. This is expected because the decorator should
     * forward the call to its delegate.
     */
    @Test
    public void hashCodeShouldThrowNullPointerExceptionWhenDelegateIsNull() {
        // Arrange: Create a SerializedCache with a null delegate cache.
        // The 'delegate' field in SerializedCache will be null.
        Cache serializedCache = new SerializedCache(null);

        // Act & Assert: Verify that calling hashCode() on this instance
        // throws a NullPointerException. The lambda expression concisely
        // represents the action that is expected to fail.
        assertThrows(NullPointerException.class, serializedCache::hashCode);
    }
}