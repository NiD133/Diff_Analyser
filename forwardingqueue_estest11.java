package com.google.common.collect;

import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Tests for the standard helper methods in {@link ForwardingQueue}.
 */
public class ForwardingQueueTest {

    /**
     * Verifies that standardPeek() returns null for an empty queue, consistent with the
     * {@link java.util.Queue#peek()} contract.
     */
    @Test
    public void standardPeek_onEmptyQueue_returnsNull() {
        // Arrange: Create a queue that is guaranteed to be empty.
        // EvictingQueue, a subclass of ForwardingQueue, with a max size of 0 serves this purpose.
        // Note: This test must be in the `com.google.common.collect` package to access the
        // protected standardPeek() method.
        ForwardingQueue<String> emptyQueue = EvictingQueue.create(0);

        // Act: Call the method under test.
        String peekedElement = emptyQueue.standardPeek();

        // Assert: The result should be null, as the queue is empty.
        assertNull("standardPeek() should return null when called on an empty queue.", peekedElement);
    }
}