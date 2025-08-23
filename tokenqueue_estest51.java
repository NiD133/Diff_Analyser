package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest51 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void consumeCssIdentifierShouldThrowExceptionIfQueueDoesNotStartWithValidIdentifier() {
        // Arrange: Create a queue that starts with a character ('+') which is not a valid
        // start for a CSS identifier.
        TokenQueue queue = new TokenQueue("+");

        // Act & Assert
        try {
            queue.consumeCssIdentifier();
            fail("Expected an IllegalArgumentException because the queue does not start with a valid CSS identifier.");
        } catch (IllegalArgumentException e) {
            // The implementation throws this specific message when it cannot find an identifier at the current position.
            String expectedMessage = "CSS identifier expected, but end of input found";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}