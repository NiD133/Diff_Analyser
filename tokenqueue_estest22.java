package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the {@link TokenQueue} class.
 * This test focuses on the behavior of a TokenQueue after it has been closed.
 */
public class TokenQueue_ESTestTest22 {

    /**
     * Verifies that calling toString() on a closed TokenQueue throws a NullPointerException.
     * The close() method is expected to release internal resources, making subsequent
     * operations on the queue invalid. This test ensures that such invalid operations fail as expected.
     */
    @Test(expected = NullPointerException.class)
    public void toStringOnClosedQueueThrowsNullPointerException() {
        // Arrange: Create a TokenQueue and then close it to invalidate its state.
        // The initial content of the queue is irrelevant for this test.
        TokenQueue queue = new TokenQueue(">'9sOEbGPT9N3{HK5");
        queue.close();

        // Act & Assert: Attempt to call toString() on the closed queue.
        // The @Test(expected = ...) annotation asserts that a NullPointerException is thrown.
        queue.toString();
    }
}