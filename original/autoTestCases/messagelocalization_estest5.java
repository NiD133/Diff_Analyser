package org.example;

import org.junit.jupiter.api.Test; // Use JUnit 5 for better readability and features
import static org.junit.jupiter.api.Assertions.*; // Use JUnit 5 assertions

public class MessageLocalizationTest { // Renamed class for clarity

    @Test
    void testMessageRetrievalWithFallback() { // More descriptive test name

        // Setup:  Set the language to English (en) with an empty country code.
        // This seems to be initializing the localization system for English language with no specific region.
        boolean languageSetSuccessfully = MessageLocalization.setLanguage("en", "");

        // Assertion 1: Verify that the language was set successfully.
        assertTrue(languageSetSuccessfully, "Language should be set to 'en' successfully.");

        // Act: Attempt to retrieve a message using a key. Because no message is actually defined
        // the method will fall back to returning the key itself. In this case, the key appears to be "No message found for ".  The second argument `true` might indicate the key should be returned if no message is found.
        String retrievedMessage = MessageLocalization.getMessage("No message found for ", true);

        // Assertion 2:  Verify that the retrieved message is what we expect in the case of a fallback.
        // In this case, the expected behavior is to return the key repeated twice.
        assertEquals("No message found for No message found for ", retrievedMessage, "Message should match the fallback string.");
    }
}