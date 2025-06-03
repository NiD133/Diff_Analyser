package org.example;

import org.junit.jupiter.api.Test; // Using JUnit 5's @Test
import static org.junit.jupiter.api.Assertions.assertEquals; // Using JUnit 5's assertions

public class MessageLocalizationTest { // Renamed class for clarity

    @Test
    void testGetMessage_ReturnsExpectedString() { // More descriptive test name
        // Arrange: Define the key for the message we want to retrieve.
        String messageKey = "writelength.can.only.be.called.in.a.contructed.pdfstream.inputstream.pdfwriter";

        // Act: Retrieve the message using the MessageLocalization class.
        String actualMessage = MessageLocalization.getMessage(messageKey);

        // Assert: Verify that the retrieved message matches the expected message.
        String expectedMessage = "writeLength() can only be called in a contructed PdfStream(InputStream,PdfWriter).";
        assertEquals(expectedMessage, actualMessage, "The retrieved message should match the expected message."); // Added a message for better error reporting
    }
}