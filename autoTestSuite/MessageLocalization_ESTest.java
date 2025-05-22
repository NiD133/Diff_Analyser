import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.example.MessageLocalization;

public class MessageLocalizationTest {

    @Test
    public void testSetMessagesFromStringReader() {
        // Tests that messages can be set using a StringReader.
        StringReader stringReader = new StringReader("=JmrhSB~/t[:");
        MessageLocalization.setMessages(stringReader);
    }

    @Test(expected = NullPointerException.class)
    public void testSetMessagesWithNullReaderThrowsNPE() {
        // Tests that setting messages with a null Reader throws a NullPointerException.
        MessageLocalization.setMessages(null);
    }

    @Test(expected = IOException.class)
    public void testSetMessagesWithClosedReaderThrowsIOException() throws IOException {
        // Tests that setting messages with a closed Reader throws an IOException.
        StringReader stringReader = new StringReader("D4^H:#)~mLrzX&|");
        stringReader.close();
        MessageLocalization.setMessages(stringReader);
    }

    @Test
    public void testGetComposedMessageWithArguments() {
        // Tests that getComposedMessage correctly substitutes arguments.
        Object[] args = new Object[]{"No message found for null"};
        String result = MessageLocalization.getComposedMessage("No message found for null", args);
        assertEquals("No message found for No message found for null", result);
    }

    @Test
    public void testGetMessageReturnsKeyWhenLanguageIsSetAndNotFound() {
        // Tests that getMessage returns the key when the message is not found for a set language.
        MessageLocalization.setLanguage("en", ""); // Set language to English.
        String result = MessageLocalization.getMessage("No message found for ", true);
        assertEquals("No message found for No message found for ", result);
    }

    @Test
    public void testSetLanguageWithNullCountry() {
        // Tests that setLanguage returns true when setting a language with a null country.
        boolean result = MessageLocalization.setLanguage("en", null);
        assertTrue(result);
    }

    @Test
    public void testSetLanguageWithInvalidLanguageCodeReturnsFalse() {
        // Tests that setLanguage returns false when setting an invalid language code.
        boolean result = MessageLocalization.setLanguage("&q", null);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLanguageWithNullLanguageThrowsIllegalArgumentException() {
        // Tests that setLanguage throws an IllegalArgumentException when the language is null.
        MessageLocalization.setLanguage(null, "No message found for /");
    }

    @Test
    public void testSetLanguageWithEmptyLanguageReturnsFalse() {
        // Tests that setLanguage returns false when setting an empty language.
        boolean result = MessageLocalization.setLanguage("", "");
        assertFalse(result);
    }

    @Test
    public void testGetComposedMessageWithNullArgumentsReturnsDefaultMessage() {
        // Tests that getComposedMessage returns the default message when arguments are null.
        String result = MessageLocalization.getComposedMessage("", (Object[]) null);
        assertEquals("No message found for ", result);
    }

    @Test
    public void testGetMessageReturnsLocalizedMessage() {
        // Tests that getMessage returns the localized message when it exists.
        String result = MessageLocalization.getMessage("writelength.can.only.be.called.in.a.contructed.pdfstream.inputstream.pdfwriter");
        assertEquals("writeLength() can only be called in a contructed PdfStream(InputStream,PdfWriter).", result);
    }

    @Test
    public void testGetMessageWithNullKeyReturnsDefaultMessage() {
        // Tests that getMessage returns a default message when the key is null.
        String result = MessageLocalization.getMessage(null, false);
        assertEquals("No message found for null", result);
    }

    @Test
    public void testGetComposedMessageWithIntArgument() {
        // Tests that getComposedMessage with integer arguments returns the default message.
        MessageLocalization.setLanguage("en", "");
        String result = MessageLocalization.getComposedMessage("", -31);
        assertEquals("No message found for ", result);
    }
}