package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    /**
     * Verifies that the TokenQueue constructor throws a NullPointerException
     * when initialized with a null input string, as this is an invalid state.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionForNullInput() {
        new TokenQueue(null);
    }
}