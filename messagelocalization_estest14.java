package com.itextpdf.text.error_messages;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

// Note: The original test class name "MessageLocalization_ESTestTest14" was likely
// auto-generated. A more conventional name would be "MessageLocalizationTest".
public class MessageLocalization_ESTestTest14 {

    /**
     * Verifies that a message with an empty key can be set via a Reader
     * and then retrieved correctly.
     *
     * The original test created a StringReader with "W=EJ%T_\"" and then called
     * read() once before passing it to setMessages. This effectively tested
     * loading a message from the input "=EJ%T_\"", which corresponds to an
     * empty key. This revised test makes that intent explicit.
     */
    @Test
    public void getMessage_retrievesMessageForEmptyKey_whenSetFromReader() throws IOException {
        // Arrange
        // The message format is key=value. A line starting with '=' defines a message for an empty key.
        String emptyKey = "";
        String expectedMessage = "EJ%T_\"";
        String messageData = "=" + expectedMessage;
        StringReader messageSource = new StringReader(messageData);

        // Act
        // Load the custom messages from the reader.
        MessageLocalization.setMessages(messageSource);

        // Retrieve the message associated with the empty key.
        // The 'false' argument ensures we don't fall back to the default language.
        String actualMessage = MessageLocalization.getMessage(emptyKey, false);

        // Assert
        assertEquals("The message for the empty key should be retrieved correctly.",
                expectedMessage, actualMessage);
    }
}