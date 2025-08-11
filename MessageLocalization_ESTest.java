package com.itextpdf.text.error_messages;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class MessageLocalization_ESTest extends MessageLocalization_ESTest_scaffolding {

    private static final String NULL_MESSAGE = "No message found for ";
    private static final String STREAM_CLOSED_EXCEPTION = "Stream closed";
    private static final String LANGUAGE_CANNOT_BE_NULL = "The language cannot be null.";

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
        StringReader stringReader = new StringReader("rL:MuU>kC(vmJ+!e!(>");
        stringReader.close();
        try {
            MessageLocalization.setMessages(stringReader);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetComposedMessageReturnsNoMessageFound() throws Throwable {
        Object[] params = new Object[4];
        params[0] = "Xs";
        String result = MessageLocalization.getComposedMessage("X72rI", params);
        assertEquals(NULL_MESSAGE + "X72rI", result);
    }

    @Test(timeout = 4000)
    public void testGetMessageReturnsLocalizedMessage() throws Throwable {
        String result = MessageLocalization.getMessage("you.can.only.add.cells.to.rows.no.objects.of.type.1", true);
        assertEquals("You can only add cells to rows, no objects of type {1}", result);
    }

    @Test(timeout = 4000)
    public void testGetMessageWithEmptyKeyReturnsNoMessageFound() throws Throwable {
        String result = MessageLocalization.getMessage("", true);
        assertNotNull(result);
        assertEquals(NULL_MESSAGE, result);
    }

    @Test(timeout = 4000)
    public void testSetLanguageWithValidLanguageReturnsTrue() throws Throwable {
        boolean success = MessageLocalization.setLanguage("en", "No message found for #N.value.1.is.not.supported");
        assertTrue(success);

        String result = MessageLocalization.getMessage("No message found for #N.value.1.is.not.supported", false);
        assertEquals(NULL_MESSAGE + "No message found for #N.value.1.is.not.supported", result);
        assertNotNull(result);
    }

    @Test(timeout = 4000)
    public void testGetMessageWithInvalidKeyReturnsNoMessageFound() throws Throwable {
        String result = MessageLocalization.getMessage("#you.can.t.add.a.1.to.a.section");
        assertNotNull(result);
        assertEquals(NULL_MESSAGE + "#you.can.t.add.a.1.to.a.section", result);
    }

    @Test(timeout = 4000)
    public void testSetLanguageWithNullCountryReturnsTrue() throws Throwable {
        boolean success = MessageLocalization.setLanguage("en", null);
        assertTrue(success);
    }

    @Test(timeout = 4000)
    public void testSetLanguageWithInvalidLanguageReturnsFalse() throws Throwable {
        boolean success = MessageLocalization.setLanguage("Xs", null);
        assertFalse(success);
    }

    @Test(timeout = 4000)
    public void testSetLanguageWithNullLanguageThrowsIllegalArgumentException() throws Throwable {
        try {
            MessageLocalization.setLanguage(null, "#wrong.number.of.columns");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("com.itextpdf.text.error_messages.MessageLocalization", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetLanguageWithInvalidLanguageAndCountryReturnsFalse() throws Throwable {
        boolean success = MessageLocalization.setLanguage("#N.value.1.is.not.supported", "Z+[q![/n\".7YLF#");
        assertFalse(success);
    }

    @Test(timeout = 4000)
    public void testGetComposedMessageWithNullKeyReturnsNoMessageFound() throws Throwable {
        String result = MessageLocalization.getComposedMessage(null, (Object[]) null);
        assertEquals(NULL_MESSAGE + "null", result);
        assertNotNull(result);
    }

    @Test(timeout = 4000)
    public void testGetComposedMessageWithInvalidKeyAndIntParamReturnsNoMessageFound() throws Throwable {
        String result = MessageLocalization.getComposedMessage("you.can.only.add.cells.to.rows.no.objects.of.type.1", -83190818);
        assertNotNull(result);
    }

    @Test(timeout = 4000)
    public void testSetMessagesWithValidReaderUpdatesMessages() throws Throwable {
        StringReader stringReader = new StringReader("W=EJ%T_\"");
        stringReader.read();
        MessageLocalization.setMessages(stringReader);
        String result = MessageLocalization.getMessage("", false);
        assertEquals("EJ%T_\"", result);
    }
}