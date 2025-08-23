package org.jsoup.parser;

import org.junit.Test;

/**
 * Tests the behavior of a {@link TokenQueue} after it has been closed.
 */
public class TokenQueueClosedStateTest {

    /**
     * Verifies that attempting to call a consumption method like consumeCssIdentifier()
     * on a closed TokenQueue throws a NullPointerException. This is because closing
     * the queue nullifies its internal CharacterReader.
     */
    @Test(expected = NullPointerException.class)
    public void callingConsumeCssIdentifierOnClosedQueueShouldThrowException() {
        // Arrange: Create a TokenQueue and immediately close it. The initial content is not important.
        TokenQueue queue = new TokenQueue("some-data");
        queue.close();

        // Act: Attempt to use the queue after it has been closed.
        queue.consumeCssIdentifier();

        // Assert: A NullPointerException is expected, as specified by the @Test annotation.
    }
}