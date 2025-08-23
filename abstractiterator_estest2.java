package com.google.common.collect;

import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import java.util.LinkedList;
import java.util.Queue;
import org.junit.Test;

/**
 * Tests for {@link AbstractIterator}.
 */
public class AbstractIteratorTest {

    /**
     * A simple implementation of AbstractIterator that wraps and consumes a queue.
     * This is used as a concrete class to test the behavior of the abstract AbstractIterator.
     */
    private static class ConsumingQueueIterator<T> extends AbstractIterator<T> {
        private final Queue<T> queue;

        ConsumingQueueIterator(Queue<T> queue) {
            this.queue = Preconditions.checkNotNull(queue);
        }

        @Override
        protected T computeNext() {
            // If the queue is empty, signal that the iteration is complete.
            if (queue.isEmpty()) {
                return endOfData();
            }
            // Otherwise, return the next element from the queue.
            return queue.poll();
        }
    }

    @Test
    public void hasNext_whenIteratorIsNotEmpty_returnsTrue() {
        // Arrange: Create a non-empty queue and an iterator for it.
        Queue<String> backingQueue = new LinkedList<>();
        backingQueue.add("first element");
        AbstractIterator<String> iterator = new ConsumingQueueIterator<>(backingQueue);

        // Act & Assert: hasNext() should return true because the iterator has not been advanced yet
        // and the underlying queue is not empty.
        assertTrue("Expected hasNext() to be true for an iterator with elements.", iterator.hasNext());
    }
}