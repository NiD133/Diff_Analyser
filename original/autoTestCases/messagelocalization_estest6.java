package org.example;

import org.junit.jupiter.api.Test; // Changed to JUnit 5 for clarity and modern syntax
import static org.junit.jupiter.api.Assertions.*; // Updated assertions

public class MessageLocalizationTest { // Renamed class for better readability

    @Test
    void testSetLanguageWithNullCountry() { // Renamed method for better clarity
        // Arrange:  Define the expected behavior - setting the language to "en" should succeed.
        String language = "en";
        String country = null;

        // Act: Call the method under test, capturing the result.
        boolean result = MessageLocalization.setLanguage(language, country);

        // Assert: Verify that the method returns true, indicating success.
        assertTrue(result, "Setting the language to 'en' with null country should succeed."); // Added a message for better failure analysis
    }
}