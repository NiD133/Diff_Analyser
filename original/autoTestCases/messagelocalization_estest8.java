package org.example;

import org.junit.jupiter.api.Test; // Using JUnit 5 annotations for better readability
import static org.junit.jupiter.api.Assertions.*; // Using JUnit 5 assertions

public class MessageLocalizationTest { // Renamed class for better clarity

    @Test
    void testSetLanguageWithNullLanguageThrowsIllegalArgumentException() {
        // Arrange: Define the invalid input (null language).
        String language = null;
        String messageKey = "No message found for /";

        // Act & Assert:  Verify that an IllegalArgumentException is thrown when setting language to null.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            MessageLocalization.setLanguage(language, messageKey);
        });

        // Assert:  Verify the exception message.  Optional, but improves test clarity.
        assertEquals("The language cannot be null.", exception.getMessage());
    }
}