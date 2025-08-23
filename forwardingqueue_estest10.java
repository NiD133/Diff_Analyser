package com.google.common.collect;

import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Tests for the standard helper implementations in {@link ForwardingQueue}.
 */
public class ForwardingQueueTest {

    /**
     * Verifies that {@code standardPoll()} returns {@code null} when called on an empty queue,
     * which is the expected behavior according to the {@link java.util.Queue#poll()} contract.
     */
    @Test
    public void standardPoll_onEmptyQueue_returnsNull() {
        // Arrange: Create an empty EvictingQueue.
        // We use EvictingQueue as a concrete implementation of the abstract ForwardingQueue.
        EvictingQueue<Object> queue = EvictingQueue.create(10);

        // Act: Call the standardPoll() helper method on the empty queue.
        Object result = queue.standardPoll();

        // Assert: The result should be null, as the queue is empty.
        assertNull("standardPoll() on an empty queue should return null", result);
    }
}