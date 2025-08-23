package com.google.common.collect;

import org.junit.Test;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Test suite for {@link AbstractIterator}.
 */
public class AbstractIteratorTest {

    /**
     * A helper class to create a concrete AbstractIterator for testing.
     * This is based on com.google.common.collect.ConsumingQueueIterator,
     * which is a test-only helper in Guava's own test suite. It creates an
     * iterator that consumes elements from a provided queue.
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
            return queue.remove();
        }
    }

    /**
     * Verifies that calling peek() on an iterator that is exhausted
     * (i.e., has no more elements) throws a NoSuchElementException.
     */
    @Test(expected = NoSuchElementException.class)
    public void peekOnExhaustedIterator_throwsNoSuchElementException() {
        // Arrange: Create an iterator over an empty collection.
        // This iterator is considered exhausted from the moment it's created.
        Queue<String> emptyQueue = new LinkedList<>();
        AbstractIterator<String> iterator = new ConsumingQueueIterator<>(emptyQueue);

        // Act: Attempt to peek at the next element from the exhausted iterator.
        // Assert: A NoSuchElementException is expected, as declared in the @Test annotation.
        iterator.peek();
    }
}