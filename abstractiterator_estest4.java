package com.google.common.collect;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Test suite for {@link AbstractIterator}.
 * This test was improved for understandability.
 */
// The original test extended a scaffolding class. For a standalone, understandable
// test, this is often not necessary.
public class AbstractIterator_ESTestTest4 {

    /**
     * A test-local implementation of AbstractIterator that consumes elements from a queue.
     * This is assumed to be the role of the original `ConsumingQueueIterator`.
     */
    private static class ConsumingQueueIterator<E> extends AbstractIterator<E> {
        private final Queue<E> queue;

        ConsumingQueueIterator(Queue<E> queue) {
            this.queue = queue;
        }

        @Override
        protected E computeNext() {
            if (queue.isEmpty()) {
                return endOfData();
            }
            return queue.poll();
        }
    }

    @Test
    public void hasNextShouldReturnFalseAfterExhaustingAnEmptyIterator() {
        // Arrange: Create an iterator that is empty from the start.
        // ConsumingQueueIterator is a simple AbstractIterator implementation for testing.
        Queue<Object> emptyBackendQueue = new ArrayDeque<>();
        AbstractIterator<Object> iterator = new ConsumingQueueIterator<>(emptyBackendQueue);

        @SuppressWarnings("unchecked") // A common warning when mocking generic types.
        Consumer<Object> mockConsumer = mock(Consumer.class);

        // Act: Exhaust the iterator by calling forEachRemaining. On an empty iterator,
        // this should do nothing and complete immediately.
        iterator.forEachRemaining(mockConsumer);

        // Assert: Verify the iterator is in the correct, exhausted state.
        // First, confirm that no elements were ever processed.
        verify(mockConsumer, never()).accept(any());

        // Second, confirm that hasNext() correctly reports the iterator is exhausted.
        assertFalse("hasNext() must return false after the iterator has been exhausted.", iterator.hasNext());
    }
}