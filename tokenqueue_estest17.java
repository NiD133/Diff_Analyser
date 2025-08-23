package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    /**
     * Verifies that the current() method correctly peeks at the first character
     * in the queue without consuming it or advancing the queue's position.
     */
    @Test
    public void currentShouldReturnFirstCharacterWithoutAdvancingPosition() {
        // Arrange: Set up the test data and the object under test.
        String input = "Did not find balanced marker at '";
        TokenQueue queue = new TokenQueue(input);

        // Act: Call the method being tested.
        char currentChar = queue.current();

        // Assert: Verify the results.
        // 1. The returned character should be the first character of the input string.
        assertEquals('D', currentChar);

        // 2. The queue's internal position should not have changed.
        //    Calling remainder() should still return the full original string.
        assertEquals(input, queue.remainder());
    }
}