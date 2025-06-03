package org.example;

import org.junit.jupiter.api.Test; // Changed to JUnit 5 for better readability and features
import static org.junit.jupiter.api.Assertions.*; // Changed to JUnit 5 assertions
import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;

//Simplified and focused test case
class MessageLocalizationTest {

    @Test
    void testSetMessagesWithNullReaderThrowsException() {
        // Arrange: Prepare a null Reader object.
        Reader nullReader = null;

        // Act & Assert:  Verify that passing a null Reader to setMessages throws a NullPointerException.
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            MessageLocalization.setMessages(nullReader);
        });

        // Optional:  You can add an assertion here to check the exception message if it's important.
        // assertNull(exception.getMessage()); // Example: If the exception should have no message.
    }
}