package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

/**
 * Test suite for the SerializedCache decorator.
 */
public class SerializedCacheTest {

    /**
     * Verifies that getObject() throws a ClassCastException if the underlying
     * delegate cache contains a value that is not a byte array.
     *
     * The SerializedCache decorator assumes that all values stored in its delegate
     * are byte arrays representing serialized objects. This test simulates a corrupt
     * or improperly-used cache state where the delegate holds a raw String.
     * The decorator should fail when it attempts to cast this String to a byte[]
     * for deserialization.
     */
    @Test(expected = ClassCastException.class)
    public void getObjectShouldThrowClassCastExceptionWhenDelegateCacheContainsNonByteArrayValue() {
        // Arrange:
        // 1. Create a delegate cache.
        Cache delegateCache = new PerpetualCache("test-delegate");

        // 2. Directly add a non-serialized (String) value to the delegate.
        //    This bypasses the SerializedCache's serialization logic, simulating
        //    a scenario where the cache holds an invalid value type.
        String key = "testKey";
        String rawValue = "this is not a serialized byte array";
        delegateCache.putObject(key, rawValue);

        // 3. Decorate the cache. The delegate now contains a value that SerializedCache
        //    is not designed to handle.
        Cache serializedCache = new SerializedCache(delegateCache);

        // Act:
        // Attempt to retrieve the object. This will fetch the raw String from the
        // delegate and try to cast it to a byte[], which is expected to fail.
        serializedCache.getObject(key);

        // Assert (implicit):
        // The @Test(expected) annotation asserts that a ClassCastException is thrown.
    }
}