package com.google.common.collect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Unit tests for the standard helper methods in {@link ForwardingQueue}.
 */
public class ForwardingQueueTest {

    @Test
    public void standardPeek_onNonEmptyQueue_returnsHeadElementWithoutRemovingIt() {
        // Arrange: Create a concrete ForwardingQueue implementation (EvictingQueue)
        // and add an element to it.
        EvictingQueue<String> queue = EvictingQueue.create(5);
        String headElement = "first-element";
        queue.add(headElement);

        // Act: Call the standardPeek() method.
        String peekedElement = queue.standardPeek();

        // Assert: Verify that the returned element is the head of the queue
        // and that the queue itself has not been modified.
        assertSame(
                "standardPeek() should return the same instance that is at the head of the queue.",
                headElement,
                peekedElement);
        assertEquals("standardPeek() should not modify the queue's size.", 1, queue.size());
    }
}