package com.itextpdf.text.error_messages;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

/**
 * Test suite for the {@link MessageLocalization} class.
 */
public class MessageLocalizationTest {

    /**
     * Verifies that setLanguage() returns false when provided with a language code
     * that does not correspond to a supported language resource file.
     */
    @Test
    public void setLanguage_shouldReturnFalse_whenLanguageDoesNotExist() throws IOException {
        // Arrange: Define a language code that is not expected to exist.
        String nonExistentLanguage = "Xs";
        String country = null; // The country code can be null.

        // Act: Attempt to set the non-existent language.
        boolean wasLanguageSet = MessageLocalization.setLanguage(nonExistentLanguage, country);

        // Assert: The method should return false, indicating the language was not found.
        assertFalse("Expected setLanguage to return false for a non-existent language.", wasLanguageSet);
    }
}