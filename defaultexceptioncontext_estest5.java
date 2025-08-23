package org.apache.commons.lang3.exception;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DefaultExceptionContext}.
 */
public class DefaultExceptionContextTest {

    /**
     * Tests that getFormattedExceptionMessage() returns the base message unchanged
     * when the context contains no values.
     */
    @Test
    public void getFormattedExceptionMessage_withEmptyContext_shouldReturnBaseMessage() {
        // Arrange
        final DefaultExceptionContext emptyContext = new DefaultExceptionContext();
        final String baseMessage = "Detail message.";

        // Act
        final String formattedMessage = emptyContext.getFormattedExceptionMessage(baseMessage);

        // Assert
        assertEquals("The formatted message should be identical to the base message for an empty context.",
                baseMessage, formattedMessage);
    }
}