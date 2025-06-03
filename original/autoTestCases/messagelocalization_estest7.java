package org.example;

import org.junit.jupiter.api.Test; // Changed import for clarity

import static org.junit.jupiter.api.Assertions.*; // Using JUnit 5 assertions

public class MessageLocalizationTest {  // Renamed class to be more descriptive

    @Test
    public void testSetLanguage_InvalidLanguageCode_ReturnsFalse() { // Descriptive method name
        // Arrange:  Set up the scenario (if needed - in this case, no setup is required).

        // Act: Call the method being tested.
        boolean result = MessageLocalization.setLanguage("&q", null);

        // Assert: Verify the expected outcome.
        assertFalse(result, "Setting language with an invalid code and null country should return false.");
    }
}