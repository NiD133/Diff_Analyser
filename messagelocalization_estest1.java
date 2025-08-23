package com.itextpdf.text.error_messages;

import org.junit.Test;

import java.io.IOException;

/**
 * Test suite for the {@link MessageLocalization} class.
 */
public class MessageLocalizationTest {

    /**
     * Verifies that calling {@link MessageLocalization#setMessages(java.io.Reader)}
     * with a null Reader argument throws a {@link NullPointerException}.
     * This is the expected behavior as the method requires a valid reader to process messages.
     */
    @Test(expected = NullPointerException.class)
    public void setMessagesWithNullReaderShouldThrowNullPointerException() throws IOException {
        // Act: Call the method under test with a null argument.
        // The @Test(expected) annotation handles the assertion that a NullPointerException is thrown.
        MessageLocalization.setMessages(null);
    }
}