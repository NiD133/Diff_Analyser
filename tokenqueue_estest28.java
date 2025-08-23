package org.jsoup.parser;

import org.junit.Test;

/**
 * This test verifies the behavior of the TokenQueue class when methods are called after
 * the queue has been closed.
 */
public class TokenQueue_ESTestTest28 extends TokenQueue_ESTest_scaffolding {

    /**
     * Verifies that invoking the matches(char) method on a TokenQueue after it has been closed
     * results in a NullPointerException. The close() method is expected to release internal
     * resources, rendering subsequent operations invalid.
     */
    @Test(expected = NullPointerException.class)
    public void callingMatchesOnClosedQueueShouldThrowNullPointerException() {
        // Arrange: Create a TokenQueue and immediately close it.
        // The initial content is irrelevant as we are testing the closed state.
        TokenQueue queue = new TokenQueue("some data");
        queue.close();

        // Act & Assert: Attempt to use the closed queue.
        // This call is expected to throw a NullPointerException, which is handled
        // by the @Test(expected=...) annotation.
        queue.matches('A');
    }
}