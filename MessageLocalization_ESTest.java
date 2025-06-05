package org.example;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true,
    useJEE = true
)
public class MessageLocalization_ESTest extends MessageLocalization_ESTest_scaffolding {

    private static final String NO_MESSAGE_FOUND = "No message found for ";
    private static final String NULL_LANGUAGE_EXCEPTION = "The language cannot be null.";

    @Test(timeout = 4000)
    public void testSetMessagesWithValidReader() throws Throwable {
        StringReader validReader = new StringReader("=JmrhSB~/t[:");
        MessageLocalization.setMessages(validReader);
    }

    @Test(timeout = 4000)
    public void testSetMessagesWithNullReaderThrowsNullPointerException() throws Throwable {
        try {
            MessageLocalization.setMessages((Reader) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.io.Reader", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetMessagesWithClosedReaderThrowsIOException() throws Throwable {
        StringReader closedReader = new StringReader("D4^H:#)~mLrzX&|");
        closedReader.close();
        try {
            MessageLocalization.setMessages(closedReader);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetComposedMessageWithNullKey() throws Throwable {
        Object[] params = new Object[]{"No message found for null"};
        String result = MessageLocalization.getComposedMessage("No message found for null", params);
        assertEquals("No message found for No message found for null", result);
    }

    @Test(timeout = 4000)
    public void testSetLanguageToEnglish() throws Throwable {
        boolean isLanguageSet = MessageLocalization.setLanguage("en", "");
        assertTrue(isLanguageSet);

        String message = MessageLocalization.getMessage(NO_MESSAGE_FOUND, true);
        assertEquals(NO_MESSAGE_FOUND + NO_MESSAGE_FOUND, message);
    }

    @Test(timeout = 4000)
    public void testSetLanguageToEnglishWithNullCountry() throws Throwable {
        boolean isLanguageSet = MessageLocalization.setLanguage("en", null);
        assertTrue(isLanguageSet);
    }

    @Test(timeout = 4000)
    public void testSetLanguageWithInvalidLanguageCode() throws Throwable {
        boolean isLanguageSet = MessageLocalization.setLanguage("&q", null);
        assertFalse(isLanguageSet);
    }

    @Test(timeout = 4000)
    public void testSetLanguageWithNullLanguageThrowsIllegalArgumentException() throws Throwable {
        try {
            MessageLocalization.setLanguage(null, "No message found for /");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(NULL_LANGUAGE_EXCEPTION, e.getMessage());
            verifyException("org.example.MessageLocalization", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetLanguageWithEmptyStrings() throws Throwable {
        boolean isLanguageSet = MessageLocalization.setLanguage("", "");
        assertFalse(isLanguageSet);
    }

    @Test(timeout = 4000)
    public void testGetComposedMessageWithEmptyKey() throws Throwable {
        String result = MessageLocalization.getComposedMessage("", (Object[]) null);
        assertEquals(NO_MESSAGE_FOUND, result);
    }

    @Test(timeout = 4000)
    public void testGetMessageWithSpecificKey() throws Throwable {
        String result = MessageLocalization.getMessage("writelength.can.only.be.called.in.a.contructed.pdfstream.inputstream.pdfwriter");
        assertEquals("writeLength() can only be called in a contructed PdfStream(InputStream,PdfWriter).", result);
    }

    @Test(timeout = 4000)
    public void testGetMessageWithNullKey() throws Throwable {
        String result = MessageLocalization.getMessage(null, false);
        assertEquals(NO_MESSAGE_FOUND + "null", result);
    }

    @Test(timeout = 4000)
    public void testGetComposedMessageWithEmptyKeyAndNegativeParam() throws Throwable {
        boolean isLanguageSet = MessageLocalization.setLanguage("en", "");
        assertTrue(isLanguageSet);

        String result = MessageLocalization.getComposedMessage("", -31);
        assertEquals(NO_MESSAGE_FOUND, result);
    }
}