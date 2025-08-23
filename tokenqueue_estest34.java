package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the {@link TokenQueue#consumeToAny(String...)} method.
 */
public class TokenQueueTest {

    /**
     * Verifies that calling consumeToAny with a null array of sequences
     * throws a NullPointerException. This ensures the method correctly handles
     * invalid null input for its varargs parameter.
     */
    @Test(expected = NullPointerException.class)
    public void consumeToAnyShouldThrowNullPointerExceptionWhenSequencesArrayIsNull() {
        // Arrange: Create a TokenQueue instance. The initial data is not relevant for this test.
        TokenQueue tokenQueue = new TokenQueue("some-data");

        // Act: Call the method under test with a null argument.
        // The cast to (String[]) is necessary to resolve ambiguity for the varargs method.
        tokenQueue.consumeToAny((String[]) null);

        // Assert: The test passes if a NullPointerException is thrown, as declared
        // by the @Test(expected) annotation.
    }
}