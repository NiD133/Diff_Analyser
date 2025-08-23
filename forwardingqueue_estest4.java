package com.google.common.collect;

import org.junit.Test;
import java.util.Queue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ForwardingQueue}.
 * This test suite uses {@link EvictingQueue} as a concrete implementation of the abstract
 * {@link ForwardingQueue} to verify its delegated behavior.
 */
public class ForwardingQueueTest {

    @Test
    public void poll_shouldRemoveAndReturnHeadOfQueue() {
        // Arrange
        // EvictingQueue extends ForwardingQueue, making it a suitable test subject.
        Queue<String> queue = EvictingQueue.create(10);
        String element = "head-of-queue";
        queue.add(element);

        // Act
        String polledElement = queue.poll();

        // Assert
        // Verify that the correct element was returned.
        assertEquals(element, polledElement);
        
        // Verify that the element is no longer in the queue.
        assertTrue("The queue should be empty after polling the only element.", queue.isEmpty());
        assertFalse("The queue should not contain the element after it has been polled.", queue.contains(element));
    }
}