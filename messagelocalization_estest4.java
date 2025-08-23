package com.itextpdf.text.error_messages;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link MessageLocalization} class.
 */
public class MessageLocalizationTest {

    /**
     * Verifies that getMessage() correctly retrieves a message from the default
     * language resource file when a valid key is provided.
     */
    @Test
    public void getMessage_withValidKey_returnsCorrespondingMessageFromDefaultLanguage() {
        // Arrange: Define the message key and the expected output.
        // This key corresponds to an error message in the default (English) language properties.
        String messageKey = "you.can.only.add.cells.to.rows.no.objects.of.type.1";
        String expectedMessage = "You can only add cells to rows, no objects of type {1}";

        // Act: Call the method under test.
        // The 'true' parameter ensures that the default language is used as a fallback.
        String actualMessage = MessageLocalization.getMessage(messageKey, true);

        // Assert: Verify that the actual message matches the expected message.
        assertEquals("The retrieved message should match the expected text.", expectedMessage, actualMessage);
    }
}