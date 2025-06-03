package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class verifies the functionality of the MessageLocalization class.
 * It focuses on setting the language and retrieving composed messages.
 */
public class MessageLocalizationTest {

    /**
     * Tests setting the language to English with an empty country code and retrieving a message
     * with an invalid message ID.
     */
    @Test
    public void testSetLanguageAndGetInvalidMessage() {
        // Arrange: Set the language to English with an empty country code.  This should succeed.
        boolean languageSetSuccessfully = MessageLocalization.setLanguage("en", "");

        // Assert: Verify that setting the language was successful.
        assertTrue(languageSetSuccessfully, "Setting the language should return true.");

        // Act: Attempt to retrieve a composed message using an invalid message ID.
        String composedMessage = MessageLocalization.getComposedMessage("", -31);

        // Assert: Verify that the method returns a default "No message found" string when the ID is invalid.
        assertEquals("No message found for ", composedMessage, "Should return the default 'No message found' message for an invalid ID.");
    }
}