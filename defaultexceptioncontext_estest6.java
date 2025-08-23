package org.apache.commons.lang3.exception;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DefaultExceptionContext}.
 */
public class DefaultExceptionContextTest {

    /**
     * Tests that getFormattedExceptionMessage returns an empty string when the base message is null
     * and the context contains no values.
     */
    @Test
    public void getFormattedExceptionMessage_withNullBaseMessageAndNoContext_returnsEmptyString() {
        // Arrange
        final DefaultExceptionContext context = new DefaultExceptionContext();

        // Act
        final String formattedMessage = context.getFormattedExceptionMessage(null);

        // Assert
        assertEquals("The formatted message should be an empty string.", "", formattedMessage);
    }
}