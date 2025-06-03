package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageLocalizationTest {

    @Test
    void testGetComposedMessageWithEmptyKeyAndNullArguments() {
        // Arrange: We are testing the behavior of getComposedMessage
        // when the key is an empty string and the arguments are null.

        // Act: Call the method with an empty key and null arguments.
        String result = MessageLocalization.getComposedMessage("", (Object[]) null);

        // Assert: Verify that the method returns the expected default message
        // when no message is found for the empty key.
        assertEquals("No message found for ", result);
    }
}