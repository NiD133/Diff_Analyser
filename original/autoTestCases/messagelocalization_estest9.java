package org.example;

import org.junit.jupiter.api.Test; // Updated import for JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Updated import for JUnit 5

/**
 * Test class for the MessageLocalization class.
 * This test specifically focuses on the behavior of the setLanguage method
 * when provided with empty strings for both language and country code.
 */
public class MessageLocalizationTest { // Renamed class for clarity

    /**
     * Tests the setLanguage method with empty strings for language and country.
     *
     * Expected behavior: The method should return false, indicating that
     * setting the language to an empty string is not successful.
     */
    @Test
    void testSetLanguageWithEmptyStrings() { // Renamed method for clarity
        // Arrange: No setup needed as we are directly calling a static method.

        // Act: Call the setLanguage method with empty strings.
        boolean result = MessageLocalization.setLanguage("", "");

        // Assert: Verify that the method returns false.
        assertFalse(result, "Setting language with empty strings should return false.");
    }
}