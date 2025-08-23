package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for edge-case behaviors of {@link SerializedCache},
 * particularly those involving complex interactions with decorated caches.
 */
public class SerializedCacheReentrancyTest {

    /**
     * Verifies that an {@link IllegalStateException} is thrown when a re-entrant call
     * occurs on an underlying {@link BlockingCache} during a {@code removeObject} operation.
     *
     * <p>This specific scenario is engineered as follows:
     * <ol>
     *   <li>A {@link SerializedCache} decorates a {@link BlockingCache}.</li>
     *   <li>The key provided to {@code removeObject} is another cache decorator
     *       (a {@link TransactionalCache}) that also wraps the <strong>same</strong>
     *       {@code BlockingCache} instance.</li>
     *   <li>When {@code removeObject} is called, the underlying cache implementation
     *       (e.g., a HashMap inside {@link PerpetualCache}) invokes {@code hashCode()}
     *       or {@code equals()} on the key to find it.</li>
     *   <li>The key's {@code hashCode/equals} methods delegate the call back to the
     *       {@code BlockingCache}, which is already locked by the initial
     *       {@code removeObject} call.</li>
     *   <li>This re-entrant call corrupts the lock state of the {@code BlockingCache},
     *       leading to an attempt to release a lock that is not held, thus throwing
     *       an {@code IllegalStateException}.</li>
     * </ol>
     */
    @Test
    public void removeObjectWithReentrantKeyThrowsIllegalStateException() {
        // Arrange: Set up a chain of cache decorators to trigger a re-entrant call.
        PerpetualCache perpetualCache = new PerpetualCache("reentrancy-test-cache");
        BlockingCache blockingCache = new BlockingCache(perpetualCache);

        // The SerializedCache under test, which decorates the BlockingCache.
        SerializedCache serializedCache = new SerializedCache(blockingCache);

        // A key that is itself a cache decorator wrapping the *same* BlockingCache.
        // This is the crucial element that will cause the re-entrant call.
        Cache reentrantKey = new TransactionalCache(blockingCache);

        // Act & Assert: Attempting to remove an object with the re-entrant key
        // should cause a locking failure in the underlying BlockingCache.
        try {
            serializedCache.removeObject(reentrantKey);
            fail("Expected an IllegalStateException due to re-entrant lock access, but no exception was thrown.");
        } catch (IllegalStateException e) {
            // Verify that the exception is the one expected from BlockingCache's locking mechanism.
            String expectedMessage = "Detected an attempt at releasing unacquired lock. This should never happen.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}