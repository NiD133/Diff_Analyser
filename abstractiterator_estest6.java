package com.google.common.collect;

import static org.junit.Assert.assertThrows;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;
import org.junit.Test;

/**
 * Tests for {@link AbstractIterator}.
 */
public class AbstractIteratorTest {

    /**
     * A simple implementation of AbstractIterator for testing, which iterates over a queue.
     * This helper class was likely used by the original test and is included here to make
     * the test self-contained and runnable.
     */
    private static class ConsumingQueueIterator<T> extends AbstractIterator<T> {
        private final Queue<T> queue;

        ConsumingQueueIterator(Queue<T> queue) {
            this.queue = queue;
        }

        @Override
        protected T computeNext() {
            if (queue.isEmpty()) {
                return endOfData();
            }
            return queue.poll();
        }
    }

    @Test
    public void next_onEmptyIterator_throwsNoSuchElementException() {
        // Arrange: Create an iterator from an empty source.
        Queue<String> emptyQueue = new ArrayDeque<>();
        AbstractIterator<String> iterator = new ConsumingQueueIterator<>(emptyQueue);

        // Act & Assert: Verify that calling next() on an empty iterator throws the expected exception.
        assertThrows(NoSuchElementException.class, iterator::next);
    }
}