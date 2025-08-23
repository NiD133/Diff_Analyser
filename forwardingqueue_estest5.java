package com.google.common.collect;

import org.junit.Test;
import java.util.Locale;
import java.util.Queue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link ForwardingQueue}.
 * This test uses {@link EvictingQueue} as a concrete implementation to test the forwarding behavior.
 */
public class ForwardingQueueTest {

    /**
     * Tests that peek() returns the head of the queue without removing it.
     */
    @Test
    public void peek_whenQueueIsNotEmpty_returnsHeadWithoutRemovingIt() {
        // Arrange: Create a queue and add a single element.
        // Using EvictingQueue as a concrete implementation of ForwardingQueue.
        Queue<Locale.Category> queue = EvictingQueue.create(3);
        Locale.Category expectedElement = Locale.Category.DISPLAY;
        queue.add(expectedElement);

        // Act: Peek at the head of the queue.
        Locale.Category peekedElement = queue.peek();

        // Assert: Verify the correct element was returned and the queue state is unchanged.
        assertSame("peek() should return the element at the head of the queue.", expectedElement, peekedElement);
        assertEquals("The queue size should not change after a peek() operation.", 1, queue.size());
    }
}