package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Queue;

/**
 * This test class contains tests for ForwardingQueue.
 * This specific test was improved for clarity.
 */
public class ForwardingQueue_ESTestTest15 extends ForwardingQueue_ESTest_scaffolding {

    /**
     * Verifies that calling poll() on an empty queue returns null,
     * as specified by the Queue interface contract.
     */
    @Test
    public void poll_onEmptyQueue_shouldReturnNull() {
        // Arrange: Create an empty EvictingQueue, which is a concrete
        // implementation that uses ForwardingQueue. The capacity and element
        // type are not important for this test.
        Queue<String> emptyQueue = EvictingQueue.create(10);

        // Act: Attempt to retrieve an element from the empty queue.
        String result = emptyQueue.poll();

        // Assert: The poll() method should return null when the queue is empty.
        assertNull("Expected poll() on an empty queue to return null.", result);
    }
}