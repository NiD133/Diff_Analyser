package org.apache.ibatis.mapping;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link CacheBuilder}.
 */
@DisplayName("CacheBuilder Tests")
class CacheBuilderTest {

    /**
     * Verifies that build() throws a CacheException when a class is added as a decorator
     * but lacks the required constructor that accepts a Cache instance.
     *
     * Cache decorators must wrap another Cache, so they need a constructor like `public MyDecorator(Cache delegate)`.
     * The `PerpetualCache` class is a base cache implementation, not a decorator, and does not have this constructor.
     */
    @Test
    @DisplayName("Should throw exception when adding a decorator that lacks the required cache constructor")
    void shouldThrowExceptionWhenDecoratorLacksRequiredConstructor() {
        // Arrange: Create a CacheBuilder and add an invalid decorator.
        CacheBuilder cacheBuilder = new CacheBuilder("test-cache");
        Class<? extends Cache> invalidDecoratorClass = PerpetualCache.class;
        cacheBuilder.addDecorator(invalidDecoratorClass);

        // Act & Assert: Verify that building the cache throws a CacheException.
        CacheException exception = assertThrows(CacheException.class, cacheBuilder::build,
            "Building with an invalid decorator should throw CacheException.");

        // Further Assert: Check if the exception message is informative.
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("Invalid cache decorator"), "Message should identify the error type.");
        assertTrue(actualMessage.contains("PerpetualCache"), "Message should mention the invalid class.");
        assertTrue(actualMessage.contains("must have a constructor that takes a Cache instance"),
            "Message should explain the required constructor.");
    }
}