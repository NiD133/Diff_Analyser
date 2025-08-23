package com.google.common.collect;

import org.junit.Test;

import java.util.Queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ForwardingQueue}.
 * Note: Since ForwardingQueue is an abstract class, we test its functionality
 * through a concrete implementation, {@link EvictingQueue}.
 */
public class ForwardingQueueTest {

    @Test
    public void offer_whenQueueHasCapacity_shouldAddElementAndReturnTrue() {
        // Arrange
        // EvictingQueue extends ForwardingQueue, allowing us to test the forwarding behavior.
        Queue<String> queue = EvictingQueue.create(5);
        String elementToAdd = "test-element";

        // Act
        boolean wasAdded = queue.offer(elementToAdd);

        // Assert
        assertTrue("offer() should return true when an element is successfully added.", wasAdded);
        assertEquals("The queue size should increase by one.", 1, queue.size());
        assertEquals("The head of the queue should be the newly added element.", elementToAdd, queue.peek());
    }
}