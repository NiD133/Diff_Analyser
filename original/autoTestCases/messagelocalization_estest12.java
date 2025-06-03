package org.example;

import org.junit.jupiter.api.Test; // Updated import for JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Updated import for JUnit 5

public class MessageLocalizationTest {

    @Test
    void testGetMessageWithNullKeyReturnsDefaultMessage() {
        // Arrange:  Define the input (null key).
        String key = null;
        boolean shouldLocalize = false; // Localization is disabled for this test.

        // Act: Call the method under test.
        String message = MessageLocalization.getMessage(key, shouldLocalize);

        // Assert: Verify the result.  We expect a specific default message
        //         when the key is null and localization is off.
        assertEquals("No message found for null", message, "Should return the default 'No message found' message when key is null.");
    }
}