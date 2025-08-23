package com.google.common.collect;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayDeque;
import java.util.Queue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the helper methods in {@link ForwardingQueue}.
 */
class ForwardingQueueTest {

    /**
     * A minimal concrete implementation of ForwardingQueue for testing its protected methods.
     * This class makes the protected {@code standardOffer} method publicly accessible for the test.
     */
    private static class TestForwardingQueue<E> extends ForwardingQueue<E> {
        private final Queue<E> delegate = new ArrayDeque<>();

        @Override
        protected Queue<E> delegate() {
            return delegate;
        }

        // Expose the protected method for testing by overriding it as public
        @Override
        public boolean standardOffer(E e) {
            return super.standardOffer(e);
        }
    }

    @Test
    @DisplayName("standardOffer() should throw NullPointerException when the element is null")
    void standardOffer_whenElementIsNull_throwsNullPointerException() {
        // Arrange
        ForwardingQueue<Object> queue = new TestForwardingQueue<>();

        // Act & Assert
        // The standardOffer method is expected to delegate to add(), which should reject nulls.
        assertThrows(NullPointerException.class, () -> queue.standardOffer(null));
    }
}