package com.itextpdf.text.error_messages;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link MessageLocalization} class.
 */
public class MessageLocalizationTest {

    /**
     * Verifies that getMessage() returns a default "not found" message
     * when the requested message key does not exist in the resource files.
     */
    @Test
    public void getMessage_withNonExistentKey_returnsNotFoundMessage() {
        // Arrange: Define a key that is not expected to exist and the corresponding
        // default message that the system should return.
        String nonExistentKey = "this.key.does.not.exist";
        String expectedMessage = "No message found for " + nonExistentKey;

        // Act: Request the message for the non-existent key.
        String actualMessage = MessageLocalization.getMessage(nonExistentKey);

        // Assert: Verify that the returned message is the expected "not found" message.
        assertEquals(expectedMessage, actualMessage);
    }
}