package org.apache.ibatis.mapping;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.decorators.SynchronizedCache;
import org.junit.Test;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link CacheBuilder} class.
 */
public class CacheBuilderTest {

    private static final String CACHE_ID = "test-cache";

    /**
     * Verifies that CacheBuilder#build() fails when the base cache implementation
     * does not have a constructor that accepts a single String ID.
     *
     * A decorator class like SynchronizedCache is used here as an example of a class
     * that lacks the required constructor, proving the validation works correctly.
     */
    @Test
    public void buildShouldThrowExceptionForImplementationWithoutIdConstructor() {
        // Arrange: Set up a CacheBuilder with an implementation that is invalid as a
        // base cache because it lacks a constructor with a single String parameter.
        CacheBuilder cacheBuilder = new CacheBuilder(CACHE_ID);
        Class<? extends Cache> invalidBaseImplementation = SynchronizedCache.class;
        cacheBuilder.implementation(invalidBaseImplementation);

        // Act & Assert
        try {
            cacheBuilder.build();
            fail("Expected a CacheException to be thrown because the base implementation is invalid.");
        } catch (CacheException e) {
            // Assert that the exception message clearly explains the problem.
            String message = e.getMessage();
            assertTrue("Exception message should indicate an invalid implementation.",
                message.contains("Invalid base cache implementation"));
            assertTrue("Exception message should mention the problematic class.",
                message.contains(invalidBaseImplementation.getName()));
            assertTrue("Exception message should explain the constructor requirement.",
                message.contains("must have a constructor that takes a String id as a parameter"));

            // Assert that the cause is the expected underlying exception.
            assertInstanceOf("The cause of the exception should be NoSuchMethodException.",
                NoSuchMethodException.class, e.getCause());
        }
    }
}