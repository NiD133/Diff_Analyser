package org.example;

import org.junit.jupiter.api.Test; // Using JUnit 5 for clarity
import static org.junit.jupiter.api.Assertions.*; // JUnit 5 assertions
import java.io.IOException;
import java.io.StringReader;

public class MessageLocalizationTest {  // More descriptive class name

    @Test
    void testSetMessagesWithClosedReader() { // More descriptive method name

        // Arrange: Create a StringReader and immediately close it.
        StringReader reader = new StringReader("D4^H:#)~mLrzX&|");
        try {
            reader.close();
        } catch (IOException e) {
            fail("Unexpected IOException while closing reader: " + e.getMessage());
        }

        // Act & Assert: Attempting to set messages from the closed reader should throw an IOException.
        assertThrows(IOException.class, () -> {
            MessageLocalization.setMessages(reader);
        }, "Expected IOException due to closed reader, but no exception was thrown.");

        // (Optional) Assert specific error message. Can make the test more robust
        // try {
        //     MessageLocalization.setMessages(reader);
        //     fail("Expected IOException");
        // } catch (IOException e) {
        //     assertEquals("Stream closed", e.getMessage());
        // }
    }
}