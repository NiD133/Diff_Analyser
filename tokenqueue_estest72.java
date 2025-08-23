package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link TokenQueue} class.
 * This class contains a specific test case for the consume() method.
 */
public class TokenQueueTest {

    @Test
    public void consumeEmptyStringOnEmptyQueueShouldSucceed() {
        // Arrange: Create a TokenQueue with an empty string.
        TokenQueue queue = new TokenQueue("");

        // Act: Attempt to consume an empty string. This action should be
        // a no-op and not throw an exception.
        queue.consume("");

        // Assert: Verify that the queue remains empty after the operation.
        assertTrue("The queue should remain empty after consuming an empty string.", queue.isEmpty());
    }
}