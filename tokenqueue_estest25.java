package org.jsoup.parser;

import org.junit.Test;

/**
 * This test class focuses on the behavior of the TokenQueue class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class TokenQueue_ESTestTest25 extends TokenQueue_ESTest_scaffolding {

    /**
     * Verifies that attempting to use a TokenQueue after it has been closed
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void matchesWhitespaceShouldThrowNullPointerExceptionAfterClose() {
        // Arrange: Create a TokenQueue and then immediately close it to simulate
        // an invalid state for subsequent operations. The initial content is irrelevant.
        TokenQueue tokenQueue = new TokenQueue("some data");
        tokenQueue.close();

        // Act & Assert: Attempt to call a method on the closed queue.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        tokenQueue.matchesWhitespace();
    }
}