package org.apache.ibatis.mapping;

import org.junit.Test;

import java.util.Properties;

/**
 * Test suite for {@link CacheBuilder}.
 */
public class CacheBuilderTest {

    /**
     * The CacheBuilder's build() method processes custom properties to configure the cache.
     * It expects property keys to be Strings. This test verifies that if a non-String key
     * is provided in the properties, the build() method correctly throws a
     * ClassCastException.
     */
    @Test(expected = ClassCastException.class)
    public void buildShouldThrowClassCastExceptionWhenPropertyKeyIsNotString() {
        // Arrange: Create a CacheBuilder and provide it with a Properties object
        // that contains a non-String key.
        CacheBuilder cacheBuilder = new CacheBuilder("test-cache");
        Properties invalidProperties = new Properties();

        // The Properties object allows non-String keys, but CacheBuilder expects them to be Strings.
        // We use a simple Object instance as the key to trigger the failure condition.
        invalidProperties.put(new Object(), "any-value");

        cacheBuilder.properties(invalidProperties);

        // Act: Attempt to build the cache. This action is expected to throw the exception
        // when it tries to cast the property key to a String.
        cacheBuilder.build();

        // Assert: The test will pass if a ClassCastException is thrown, as declared
        // by the @Test(expected = ...) annotation. No explicit assert is needed.
    }
}