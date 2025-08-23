package com.itextpdf.text.error_messages;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link MessageLocalization} class.
 */
public class MessageLocalizationTest {

    /**
     * Verifies that getComposedMessage returns a specific fallback message
     * when the provided message key is null. This ensures graceful handling
     * of invalid input.
     */
    @Test
    public void getComposedMessage_withNullKey_returnsFallbackMessage() {
        // Arrange
        String nullKey = null;
        Object[] nullParams = null;
        String expectedMessage = "No message found for null";

        // Act
        String actualMessage = MessageLocalization.getComposedMessage(nullKey, nullParams);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }
}