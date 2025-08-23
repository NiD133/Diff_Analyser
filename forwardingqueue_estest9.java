package com.google.common.collect;

import org.junit.Test;

import java.util.Queue;

/**
 * Tests for {@link ForwardingQueue}, using {@link EvictingQueue} as a concrete implementation.
 */
public class ForwardingQueueTest {

    /**
     * Verifies that attempting to offer a null element to the queue
     * results in a NullPointerException, as per the collection contract.
     */
    @Test(expected = NullPointerException.class)
    public void offer_whenElementIsNull_throwsNullPointerException() {
        // Arrange: Create a concrete instance of a ForwardingQueue implementation.
        // EvictingQueue is used here as it's a non-abstract subclass.
        Queue<Object> queue = EvictingQueue.create(2);

        // Act: Attempt to offer a null element, which should trigger the exception.
        queue.offer(null);
    }
}