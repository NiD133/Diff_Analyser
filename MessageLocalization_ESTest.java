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

/**
 * Test suite for MessageLocalization class functionality.
 * Tests message retrieval, language setting, and error handling.
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class MessageLocalization_ESTest extends MessageLocalization_ESTest_scaffolding {

    // Test data constants
    private static final String SAMPLE_MESSAGE_KEY = "you.can.only.add.cells.to.rows.no.objects.of.type.1";
    private static final String EXPECTED_MESSAGE = "You can only add cells to rows, no objects of type {1}";
    private static final String NONEXISTENT_KEY = "X72rI";
    private static final String EMPTY_KEY = "";
    private static final String VALID_LANGUAGE = "en";
    private static final String INVALID_LANGUAGE = "Xs";

    @Test(timeout = 4000)
    public void testSetMessages_WithNullReader_ThrowsNullPointerException() throws Throwable {
        // Given: A null Reader
        Reader nullReader = null;
        
        // When & Then: Setting messages with null reader should throw NullPointerException
        try { 
            MessageLocalization.setMessages(nullReader);
            fail("Expected NullPointerException when setting messages with null reader");
        } catch(NullPointerException e) {
            verifyException("java.io.Reader", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetMessages_WithClosedReader_ThrowsIOException() throws Throwable {
        // Given: A closed StringReader
        StringReader closedReader = new StringReader("sample content");
        closedReader.close();
        
        // When & Then: Setting messages with closed reader should throw IOException
        try { 
            MessageLocalization.setMessages(closedReader);
            fail("Expected IOException when setting messages with closed reader");
        } catch(IOException e) {
            assertEquals("Stream closed", e.getMessage());
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetComposedMessage_WithNonexistentKey_ReturnsNotFoundMessage() throws Throwable {
        // Given: A non-existent message key and parameters
        Object[] parameters = new Object[4];
        parameters[0] = "Xs";
        
        // When: Getting composed message for non-existent key
        String result = MessageLocalization.getComposedMessage(NONEXISTENT_KEY, parameters);
        
        // Then: Should return "No message found" message
        assertEquals("No message found for " + NONEXISTENT_KEY, result);
    }

    @Test(timeout = 4000)
    public void testGetMessage_WithValidKey_ReturnsExpectedMessage() throws Throwable {
        // When: Getting message for valid key with fallback enabled
        String result = MessageLocalization.getMessage(SAMPLE_MESSAGE_KEY, true);
        
        // Then: Should return the expected localized message
        assertEquals(EXPECTED_MESSAGE, result);
    }

    @Test(timeout = 4000)
    public void testGetMessage_WithEmptyKey_ReturnsNotFoundMessage() throws Throwable {
        // When: Getting message for empty key with fallback enabled
        String result = MessageLocalization.getMessage(EMPTY_KEY, true);
        
        // Then: Should return "No message found" message
        assertNotNull(result);
        assertEquals("No message found for " + EMPTY_KEY, result);
    }

    @Test(timeout = 4000)
    public void testSetLanguage_WithValidLanguage_ReturnsTrue() throws Throwable {
        // Given: Valid language and country parameters
        String testMessage = "No message found for #N.value.1.is.not.supported";
        
        // When: Setting language to English
        boolean languageSet = MessageLocalization.setLanguage(VALID_LANGUAGE, testMessage);
        
        // Then: Should successfully set language
        assertTrue("Should successfully set valid language", languageSet);
        
        // And: Should be able to retrieve messages
        String result = MessageLocalization.getMessage(testMessage, false);
        assertEquals("No message found for " + testMessage, result);
        assertNotNull(result);
    }

    @Test(timeout = 4000)
    public void testGetMessage_WithHashPrefixedKey_ReturnsNotFoundMessage() throws Throwable {
        // Given: A message key with hash prefix
        String keyWithHash = "#you.can.t.add.a.1.to.a.section";
        
        // When: Getting message for hash-prefixed key
        String result = MessageLocalization.getMessage(keyWithHash);
        
        // Then: Should return "No message found" message
        assertNotNull(result);
        assertEquals("No message found for " + keyWithHash, result);
    }

    @Test(timeout = 4000)
    public void testSetLanguage_WithValidLanguageAndNullCountry_ReturnsTrue() throws Throwable {
        // When: Setting language with null country
        boolean result = MessageLocalization.setLanguage(VALID_LANGUAGE, null);
        
        // Then: Should successfully set language
        assertTrue("Should successfully set language even with null country", result);
    }

    @Test(timeout = 4000)
    public void testSetLanguage_WithInvalidLanguage_ReturnsFalse() throws Throwable {
        // When: Setting invalid language
        boolean result = MessageLocalization.setLanguage(INVALID_LANGUAGE, null);
        
        // Then: Should fail to set invalid language
        assertFalse("Should fail to set invalid language", result);
    }

    @Test(timeout = 4000)
    public void testSetLanguage_WithNullLanguage_ThrowsIllegalArgumentException() throws Throwable {
        // When & Then: Setting null language should throw IllegalArgumentException
        try { 
            MessageLocalization.setLanguage(null, "#wrong.number.of.columns");
            fail("Expected IllegalArgumentException when setting null language");
        } catch(IllegalArgumentException e) {
            assertEquals("The language cannot be null.", e.getMessage());
            verifyException("com.itextpdf.text.error_messages.MessageLocalization", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetLanguage_WithInvalidLanguageAndCountry_ReturnsFalse() throws Throwable {
        // When: Setting invalid language and country combination
        boolean result = MessageLocalization.setLanguage("#N.value.1.is.not.supported", "Z+[q![/n\".7YLF#");
        
        // Then: Should fail to set invalid language/country
        assertFalse("Should fail to set invalid language and country", result);
    }

    @Test(timeout = 4000)
    public void testGetComposedMessage_WithNullParameters_ReturnsNotFoundMessage() throws Throwable {
        // When: Getting composed message with null key and parameters
        String result = MessageLocalization.getComposedMessage(null, (Object[]) null);
        
        // Then: Should return "No message found" message
        assertEquals("No message found for null", result);
        assertNotNull(result);
    }

    @Test(timeout = 4000)
    public void testGetComposedMessage_WithIntParameter_ReturnsMessage() throws Throwable {
        // When: Getting composed message with integer parameter
        String result = MessageLocalization.getComposedMessage(SAMPLE_MESSAGE_KEY, -83190818);
        
        // Then: Should return a message (content may vary based on localization)
        assertNotNull("Should return a message with integer parameter", result);
    }

    @Test(timeout = 4000)
    public void testSetMessages_WithValidReader_UpdatesMessages() throws Throwable {
        // Given: A StringReader with sample content
        StringReader reader = new StringReader("W=EJ%T_\"");
        reader.read(); // Consume first character to simulate partial reading
        
        // When: Setting messages from reader
        MessageLocalization.setMessages(reader);
        
        // Then: Should update messages and be retrievable
        String result = MessageLocalization.getMessage(EMPTY_KEY, false);
        assertEquals("EJ%T_\"", result);
    }
}