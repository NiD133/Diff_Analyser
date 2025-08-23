package com.google.common.collect;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Tests for {@link ForwardingQueue}.
 */
public class ForwardingQueueTest {

    @Test(expected = NoSuchElementException.class)
    public void remove_onEmptyQueue_throwsNoSuchElementException() {
        // Arrange: Create an empty queue.
        // EvictingQueue is a concrete implementation of ForwardingQueue,
        // which is suitable for testing the abstract class's forwarding behavior.
        Queue<Object> emptyQueue = EvictingQueue.create(10);

        // Act: Attempt to remove an element from the empty queue.
        emptyQueue.remove();

        // Assert: The expected NoSuchElementException is verified by the
        // @Test(expected=...) annotation.
    }
}