package com.google.common.collect;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Tests for {@link AbstractIterator}.
 */
public class AbstractIteratorTest {

    /**
     * A simple implementation of AbstractIterator that consumes elements from a Queue.
     * This helper class makes the test self-contained and easier to understand.
     * @param <T> The type of elements returned by this iterator.
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
    public void forEachRemaining_afterPeek_consumesAllElements() {
        // Arrange
        Queue<String> sourceQueue = new LinkedList<>();
        sourceQueue.add("first-element");

        AbstractIterator<String> iterator = new ConsumingQueueIterator<>(sourceQueue);

        @SuppressWarnings("unchecked") // Standard for Mockito.mock() with generic types
        Consumer<String> mockConsumer = mock(Consumer.class);

        // Act
        // First, peek at the element. This calls computeNext() internally, moving the
        // iterator state to READY, but doesn't advance the iterator's position.
        String peekedElement = iterator.peek();

        // Next, consume all remaining elements.
        iterator.forEachRemaining(mockConsumer);

        // Assert
        assertEquals("peek() should return the first element", "first-element", peekedElement);

        // Verify the consumer was called exactly once with the correct element.
        verify(mockConsumer).accept("first-element");
        verifyNoMoreInteractions(mockConsumer);

        // Verify the iterator is now exhausted.
        assertFalse("Iterator should be exhausted after forEachRemaining", iterator.hasNext());
        assertTrue("The underlying queue should be empty", sourceQueue.isEmpty());
    }
}