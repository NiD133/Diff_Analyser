package com.itextpdf.text.error_messages;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link MessageLocalization} class.
 * This suite verifies the loading and retrieval of localized error messages.
 */
public class MessageLocalizationTest {

    /**
     * Resets the messages to the default English set before each test.
     * This ensures test isolation, as MessageLocalization holds state statically.
     */
    @Before
    public void setUp() throws IOException {
        MessageLocalization.setLanguage("en", null);
    }

    @Test(expected = NullPointerException.class)
    public void setMessages_withNullReader_throwsNullPointerException() throws IOException {
        // Act
        MessageLocalization.setMessages(null);
    }

    @Test
    public void setMessages_withClosedReader_throwsIOException() {
        // Arrange
        Reader reader = new StringReader("key=value");
        try {
            reader.close();
        } catch (IOException e) {
            fail("Setup failed: could not close reader. " + e.getMessage());
        }

        // Act & Assert
        try {
            MessageLocalization.setMessages(reader);
            fail("Expected an IOException because the reader is closed.");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
    }

    @Test
    public void setMessages_withCustomMessages_overridesDefaultMessages() throws IOException {
        // Arrange
        String customKey = "custom.message.key";
        String customValue = "This is a custom message.";
        String properties = customKey + "=" + customValue;
        Reader reader = new StringReader(properties);

        // Act
        MessageLocalization.setMessages(reader);
        String actualMessage = MessageLocalization.getMessage(customKey);

        // Assert
        assertEquals("The custom message should be retrieved.", customValue, actualMessage);
    }

    @Test
    public void getMessage_forExistingKey_returnsLocalizedMessage() {
        // Arrange
        // This key exists in the default en.lng properties file.
        String key = "you.can.only.add.cells.to.rows.no.objects.of.type.1";
        String expectedMessage = "You can only add cells to rows, no objects of type {1}";

        // Act
        String actualMessage = MessageLocalization.getMessage(key);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getMessage_forNonExistentKey_returnsFallbackMessage() {
        // Arrange
        String nonExistentKey = "this.key.does.not.exist";
        String expectedMessage = "No message found for " + nonExistentKey;

        // Act
        String actualMessage = MessageLocalization.getMessage(nonExistentKey);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getMessage_forEmptyKey_returnsFallbackMessage() {
        // Arrange
        String expectedMessage = "No message found for ";

        // Act
        String actualMessage = MessageLocalization.getMessage("");

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getComposedMessage_forExistingKeyWithIntegerParam_returnsFormattedMessage() {
        // Arrange
        String key = "you.can.only.add.cells.to.rows.no.objects.of.type.1";
        int param = 42;
        String expectedMessage = "You can only add cells to rows, no objects of type 42";

        // Act
        String actualMessage = MessageLocalization.getComposedMessage(key, param);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getComposedMessage_forExistingKeyWithObjectParams_returnsFormattedMessage() {
        // Arrange
        String key = "1.is.not.a.valid.page.size.letter.or.legal";
        Object[] params = {"MyPageSize"};
        String expectedMessage = "MyPageSize is not a valid page size (letter or legal).";

        // Act
        String actualMessage = MessageLocalization.getComposedMessage(key, params);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getComposedMessage_forNonExistentKey_returnsFallbackMessage() {
        // Arrange
        String nonExistentKey = "this.key.does.not.exist";
        Object[] params = {"param1", 123};
        String expectedMessage = "No message found for " + nonExistentKey;

        // Act
        String actualMessage = MessageLocalization.getComposedMessage(nonExistentKey, params);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getComposedMessage_withNullKey_returnsFallbackMessage() {
        // Arrange
        String expectedMessage = "No message found for null";

        // Act
        String actualMessage = MessageLocalization.getComposedMessage(null, (Object[]) null);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void setLanguage_withValidLanguageAndNullCountry_succeeds() throws IOException {
        // Act
        boolean wasLanguageSet = MessageLocalization.setLanguage("en", null);

        // Assert
        assertTrue("Setting language should succeed for a valid language and null country.", wasLanguageSet);
    }

    @Test
    public void setLanguage_withValidLanguageAndInvalidCountry_succeedsByFallingBackToLanguage() throws IOException {
        // Arrange
        // "en" is a valid language, but "INVALID" is not a valid country.
        // The method should fall back to loading just the "en" language file, which exists.

        // Act
        boolean wasLanguageSet = MessageLocalization.setLanguage("en", "INVALID");

        // Assert
        assertTrue("Setting language should succeed by falling back to the base language.", wasLanguageSet);
    }

    @Test
    public void setLanguage_withInvalidLanguage_returnsFalse() throws IOException {
        // Arrange
        String invalidLanguage = "xx"; // Assuming 'xx' is not a supported language.

        // Act
        boolean wasLanguageSet = MessageLocalization.setLanguage(invalidLanguage, null);

        // Assert
        assertFalse("Setting an unsupported language should return false.", wasLanguageSet);
    }

    @Test
    public void setLanguage_withNullLanguage_throwsIllegalArgumentException() {
        // Arrange
        String expectedMessage = "The language cannot be null.";

        // Act & Assert
        try {
            MessageLocalization.setLanguage(null, "US");
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedMessage, e.getMessage());
        } catch (IOException e) {
            fail("Caught an unexpected IOException: " + e.getMessage());
        }
    }
}