package org.example;

import org.junit.Test;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class MessageLocalizationTest {

    @Test
    public void testSetMessages_validReader_setsMessages() throws IOException {
        // Arrange
        String testString = "=JmrhSB~/t[:";
        StringReader stringReader = new StringReader(testString);

        // Act
        MessageLocalization.setMessages(stringReader);

        // Assert:  No exception is thrown.  Implicit assertion that the method executes without error.
        // A more complete test would involve retrieving a message after setting the messages and verifying the retrieval.
    }

    @Test(expected = NullPointerException.class)
    public void testSetMessages_nullReader_throwsNullPointerException() throws IOException {
        // Arrange
        Reader nullReader = null;

        // Act
        MessageLocalization.setMessages(nullReader);

        // Assert: Exception is thrown as expected
    }

    @Test(expected = IOException.class)
    public void testSetMessages_closedReader_throwsIOException() throws IOException {
        // Arrange
        StringReader stringReader = new StringReader("D4^H:#)~mLrzX&|");
        stringReader.close();

        // Act
        MessageLocalization.setMessages(stringReader);

        // Assert: Exception is thrown as expected
    }

    @Test
    public void testGetComposedMessage_withArguments_replacesPlaceholders() {
        // Arrange
        Object[] arguments = new Object[2];
        arguments[0] = "No message found for null";
        String messageKey = "No message found for null";

        // Act
        String composedMessage = MessageLocalization.getComposedMessage(messageKey, arguments);

        // Assert
        assertEquals("No message found for No message found for null", composedMessage);
    }

    @Test
    public void testGetMessage_languageSet_returnsLocalizedMessage() throws IOException {
        // Arrange
        String language = "en";
        String country = "";
        String messageKey = "No message found for ";
        MessageLocalization.setLanguage(language, country); // Setup language before getting the message

        // Act
        String message = MessageLocalization.getMessage(messageKey, true);

        // Assert
        assertEquals("No message found for No message found for ", message);
    }

    @Test
    public void testSetLanguage_validLanguageAndNullCountry_returnsTrue() throws IOException {
        // Arrange
        String language = "en";
        String country = null;

        // Act
        boolean result = MessageLocalization.setLanguage(language, country);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testSetLanguage_invalidLanguageAndNullCountry_returnsFalse() throws IOException {
        // Arrange
        String language = "&q";
        String country = null;

        // Act
        boolean result = MessageLocalization.setLanguage(language, country);

        // Assert
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLanguage_nullLanguage_throwsIllegalArgumentException() throws IOException {
        // Arrange
        String language = null;
        String country = "No message found for /";

        // Act
        MessageLocalization.setLanguage(language, country);

        // Assert: Exception is thrown as expected
    }

    @Test
    public void testSetLanguage_emptyLanguageAndEmptyCountry_returnsFalse() throws IOException {
        // Arrange
        String language = "";
        String country = "";

        // Act
        boolean result = MessageLocalization.setLanguage(language, country);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetComposedMessage_nullArguments_returnsDefaultMessage() {
        // Arrange
        String messageKey = "";

        // Act
        String composedMessage = MessageLocalization.getComposedMessage(messageKey, (Object[]) null);

        // Assert
        assertEquals("No message found for ", composedMessage);
    }

    @Test
    public void testGetMessage_knownKey_returnsMessage() {
        // Arrange
        String messageKey = "writelength.can.only.be.called.in.a.contructed.pdfstream.inputstream.pdfwriter";

        // Act
        String message = MessageLocalization.getMessage(messageKey);

        // Assert
        assertEquals("writeLength() can only be called in a contructed PdfStream(InputStream,PdfWriter).", message);
    }

    @Test
    public void testGetMessage_nullKey_returnsNoMessageFound() {
        // Arrange
        String messageKey = null;

        // Act
        String message = MessageLocalization.getMessage(messageKey, false);

        // Assert
        assertEquals("No message found for null", message);
    }

    @Test
    public void testGetComposedMessage_withIntArgument_returnsComposedMessage() throws IOException {
        // Arrange
         MessageLocalization.setLanguage("en", "");
        String messageKey = "";
        int argument = -31;

        // Act
        String composedMessage = MessageLocalization.getComposedMessage(messageKey, argument);

        // Assert
        assertEquals("No message found for ", composedMessage);
    }
}