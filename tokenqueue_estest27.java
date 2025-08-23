package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    /**
     * Verifies that calling the matches(String) method with a null argument
     * correctly throws a NullPointerException. This is the expected behavior
     * as the underlying CharacterReader does not accept null inputs.
     */
    @Test(expected = NullPointerException.class)
    public void matchesWithNullStringThrowsNullPointerException() {
        // Arrange: Create a TokenQueue. The initial content is not relevant for this test.
        TokenQueue tokenQueue = new TokenQueue("test data");

        // Act: Attempt to match against a null string.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        tokenQueue.matches((String) null);
    }
}