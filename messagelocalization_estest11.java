package com.itextpdf.text.error_messages;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link MessageLocalization} class.
 */
public class MessageLocalizationTest {

    /**
     * Verifies that setLanguage returns false when provided with language and country codes
     * that do not correspond to an existing localization resource file.
     */
    @Test
    public void setLanguage_whenLanguageDoesNotExist_shouldReturnFalse() throws IOException {
        // Arrange: Define invalid language and country codes that are highly unlikely to exist.
        String nonExistentLanguage = "xx";
        String nonExistentCountry = "YY";

        // Act: Attempt to set the non-existent language.
        boolean languageWasSet = MessageLocalization.setLanguage(nonExistentLanguage, nonExistentCountry);

        // Assert: The method should return false, indicating the language resource was not found.
        assertFalse("Expected setLanguage to return false for a non-existent language.", languageWasSet);
    }
}