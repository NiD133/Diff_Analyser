package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link ForwardingQueue}.
 */
public class ForwardingQueueTest {

    @Test
    public void element_retrievesHeadWithoutRemovingIt() {
        // Arrange: Create a queue with one element.
        // We use EvictingQueue as a concrete implementation of ForwardingQueue for this test.
        EvictingQueue<String> queue = EvictingQueue.create(10);
        String headElement = "first-in-queue";
        queue.add(headElement);

        // Act: Retrieve the head of the queue using element().
        String retrievedElement = queue.element();

        // Assert: Verify the correct element was returned and the queue remains unchanged.
        assertSame("element() should return the head of the queue.", headElement, retrievedElement);
        assertEquals("The queue size should not change after calling element().", 1, queue.size());
    }
}