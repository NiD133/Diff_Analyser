package com.google.common.collect;

import org.junit.Test;
import java.util.Queue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ForwardingQueue}.
 *
 * <p>This test uses {@link EvictingQueue} as a concrete implementation of the abstract
 * {@link ForwardingQueue} to verify its forwarding behavior.
 */
public class ForwardingQueueTest {

    /**
     * Verifies that the remove() method successfully retrieves and removes the head of the queue,
     * leaving the queue empty.
     */
    @Test
    public void remove_whenElementExists_returnsElementAndRemovesItFromQueue() {
        // Arrange: Create a queue with a single element.
        // We use EvictingQueue as a concrete implementation of ForwardingQueue.
        Queue<String> queue = EvictingQueue.create(5);
        String element = "test-element";
        queue.add(element);

        // Act: Remove the element from the queue.
        String removedElement = queue.remove();

        // Assert: Verify the correct element was returned and the queue is now empty.
        assertEquals("The removed element should be the one that was added.", element, removedElement);
        assertTrue("The queue should be empty after the element is removed.", queue.isEmpty());
        assertFalse("The queue should no longer contain the removed element.", queue.contains(element));
    }
}