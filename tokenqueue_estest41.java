package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the TokenQueue class.
 */
public class TokenQueueTest {

    /**
     * Verifies that attempting to use a TokenQueue after it has been closed
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void chompBalancedShouldThrowExceptionWhenQueueIsClosed() {
        // Arrange: Create a TokenQueue and then close it.
        TokenQueue queue = new TokenQueue("some data");
        queue.close();

        // Act: Attempt to perform an operation on the closed queue.
        queue.chompBalanced('(', ')');

        // Assert: The test passes if a NullPointerException is thrown,
        // as specified by the 'expected' attribute of the @Test annotation.
    }
}