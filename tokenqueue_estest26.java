package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the {@link TokenQueue} class, focusing on its exception handling.
 */
public class TokenQueueTest {

    /**
     * Verifies that the {@code matchesAny(char...)} method throws a NullPointerException
     * when the input character array is null. This ensures the method correctly handles
     * invalid arguments.
     */
    @Test(expected = NullPointerException.class)
    public void matchesAnyShouldThrowNullPointerExceptionForNullInput() {
        // Arrange: Create a TokenQueue instance. The initial content is not relevant for this test.
        TokenQueue tokenQueue = new TokenQueue("some data");

        // Act: Call the method under test with a null argument.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        tokenQueue.matchesAny((char[]) null);
    }
}