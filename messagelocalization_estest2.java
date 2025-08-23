package com.itextpdf.text.error_messages;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * Tests for the {@link MessageLocalization} class, focusing on exception handling.
 */
public class MessageLocalizationTest {

    /**
     * Verifies that setMessages() throws an IOException when attempting to read from a closed Reader.
     */
    @Test(expected = IOException.class)
    public void setMessages_shouldThrowIOException_whenReaderIsClosed() throws IOException {
        // Arrange: Create a reader and immediately close it to simulate a closed stream.
        // The content of the reader is irrelevant for this test.
        StringReader closedReader = new StringReader("");
        closedReader.close();

        // Act: Attempt to set messages using the closed reader.
        // This call is expected to throw an IOException, which is handled by the @Test annotation.
        MessageLocalization.setMessages(closedReader);
    }
}