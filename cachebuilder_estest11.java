package org.apache.ibatis.mapping;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Test suite for {@link CacheBuilder}.
 */
public class CacheBuilderTest {

    /**
     * Verifies that the CacheBuilder maintains its fluent interface by returning the same instance
     * when a null decorator class is provided. This ensures that adding a null decorator is a
     * safe no-op.
     */
    @Test
    public void addDecoratorShouldReturnSameInstanceForFluentChainingWhenGivenNull() {
        // Arrange: Create a CacheBuilder instance.
        CacheBuilder cacheBuilder = new CacheBuilder("test_cache");

        // Act: Attempt to add a null decorator.
        // The cast to (Class<? extends Cache>) is required to resolve the correct method overload.
        CacheBuilder returnedBuilder = cacheBuilder.addDecorator(null);

        // Assert: The method should return the same instance to allow method chaining.
        assertSame("The builder instance returned should be the same as the original.", cacheBuilder, returnedBuilder);
    }
}