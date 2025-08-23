package com.itextpdf.text.error_messages;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

/**
 * Tests for the {@link MessageLocalization} class.
 */
public class MessageLocalizationTest {

    /**
     * Verifies that getMessage() returns a default "not found" message
     * when the requested message key does not exist in the loaded language file.
     */
    @Test
    public void getMessage_whenKeyNotFound_returnsDefaultNotFoundMessage() throws IOException {
        // Arrange
        // Set the language to English. Using a non-existent country code ("XX")
        // ensures the system falls back to loading the base "en.lng" file.
        boolean wasLanguageSet = MessageLocalization.setLanguage("en", "XX");
        assertTrue("The base English language file should be found and loaded.", wasLanguageSet);

        final String nonExistentKey = "a.message.key.that.does.not.exist";
        final String expectedMessage = "No message found for " + nonExistentKey;

        // Act
        // Request a message for a key that is guaranteed not to exist.
        String actualMessage = MessageLocalization.getMessage(nonExistentKey, false);

        // Assert
        assertEquals("Should return a default 'not found' message for a non-existent key.",
                expectedMessage, actualMessage);
    }
}