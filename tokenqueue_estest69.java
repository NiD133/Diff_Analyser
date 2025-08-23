package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class contains the refactored test case from the original
 * EvoSuite-generated TokenQueue_ESTestTest69.
 */
public class TokenQueue_ESTestTest69 extends TokenQueue_ESTest_scaffolding {

    /**
     * Verifies that consumeToAny() returns an empty string when the delimiter
     * is the first character in the remaining queue.
     * It also confirms that the delimiter itself is not consumed.
     */
    @Test
    public void consumeToAnyShouldReturnEmptyStringWhenDelimiterIsAtHead() {
        // Arrange: Create a queue where the first character 'F' is a delimiter.
        TokenQueue queue = new TokenQueue("F%cdg`,");
        String[] delimiters = new String[]{"X", "Y", "F"};

        // Act: Consume characters up to any of the specified delimiters.
        // Since 'F' is at the start, no characters should be consumed.
        String consumedData = queue.consumeToAny(delimiters);

        // Assert: The result should be an empty string, and the queue should be unchanged.
        assertEquals("Should consume nothing as the delimiter is at the start", "", consumedData);
        assertEquals("The queue's remainder should be unchanged", "F%cdg`,", queue.remainder());
    }
}