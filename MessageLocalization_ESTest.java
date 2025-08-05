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
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class MessageLocalization_ESTest extends MessageLocalization_ESTest_scaffolding {

    // ==================== setMessages() Tests ====================
    
    @Test(timeout = 4000)
    public void setMessages_withNullReader_throwsNullPointerException() {
        try {
            MessageLocalization.setMessages((Reader) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Verify exception details
            assertNull(e.getMessage());
            verifyException("java.io.Reader", e);
        }
    }

    @Test(timeout = 4000)
    public void setMessages_withClosedReader_throwsIOException() throws Throwable {
        StringReader closedReader = new StringReader("rL:MuU>kC(vmJ+!e!(>");
        closedReader.close();
        
        try {
            MessageLocalization.setMessages(closedReader);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
            verifyException("java.io.StringReader", e);
        }
    }

    // ==================== getComposedMessage() Tests ====================
    
    @Test(timeout = 4000)
    public void getComposedMessage_withNonExistingKey_returnsDefaultMessage() {
        Object[] parameters = new Object[4];
        parameters[0] = "Xs";  // First parameter
        
        String result = MessageLocalization.getComposedMessage("X72rI", parameters);
        assertEquals("No message found for X72rI", result);
    }

    @Test(timeout = 4000)
    public void getComposedMessage_withNullKey_returnsDefaultMessage() {
        String result = MessageLocalization.getComposedMessage(null, null);
        assertEquals("No message found for null", result);
    }

    @Test(timeout = 4000)
    public void getComposedMessage_withIntParameter_returnsFormattedMessage() {
        String result = MessageLocalization.getComposedMessage(
            "you.can.only.add.cells.to.rows.no.objects.of.type.1", 
            -83190818
        );
        assertNotNull(result);
        assertTrue(result.contains("-83190818"));
    }

    // ==================== getMessage() Tests ====================
    
    @Test(timeout = 4000)
    public void getMessage_existingKey_returnsLocalizedMessage() {
        String result = MessageLocalization.getMessage(
            "you.can.only.add.cells.to.rows.no.objects.of.type.1", 
            true
        );
        assertEquals(
            "You can only add cells to rows, no objects of type {1}", 
            result
        );
    }

    @Test(timeout = 4000)
    public void getMessage_emptyKey_returnsDefaultMessage() {
        String result = MessageLocalization.getMessage("", true);
        assertEquals("No message found for ", result);
    }

    @Test(timeout = 4000)
    public void getMessage_nonExistingKey_returnsDefaultMessage() {
        String result = MessageLocalization.getMessage("#you.can.t.add.a.1.to.a.section");
        assertEquals(
            "No message found for #you.can.t.add.a.1.to.a.section", 
            result
        );
    }

    @Test(timeout = 4000)
    public void getMessage_afterLanguageChange_nonExistingKey_returnsDefaultMessage() throws Throwable {
        // Set to English language
        boolean languageSet = MessageLocalization.setLanguage("en", null);
        assertTrue(languageSet);
        
        String result = MessageLocalization.getMessage(
            "No message found for #N.value.1.is.not.supported", 
            false
        );
        assertEquals(
            "No message found for No message found for #N.value.1.is.not.supported", 
            result
        );
    }

    @Test(timeout = 4000)
    public void getMessage_afterSettingCustomMessages_returnsCustomMessage() throws Throwable {
        // Prepare custom messages
        StringReader messageSource = new StringReader("W=EJ%T_\"");
        messageSource.read();  // Advance reader position
        MessageLocalization.setMessages(messageSource);
        
        String result = MessageLocalization.getMessage("", false);
        assertEquals("EJ%T_\"", result);
    }

    // ==================== setLanguage() Tests ====================
    
    @Test(timeout = 4000)
    public void setLanguage_validLanguage_returnsTrue() throws Throwable {
        boolean result = MessageLocalization.setLanguage("en", null);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void setLanguage_invalidLanguage_returnsFalse() throws Throwable {
        boolean result = MessageLocalization.setLanguage("Xs", null);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void setLanguage_withNullLanguage_throwsException() {
        try {
            MessageLocalization.setLanguage(null, "#wrong.number.of.columns");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The language cannot be null.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void setLanguage_invalidLanguageAndCountry_returnsFalse() throws Throwable {
        boolean result = MessageLocalization.setLanguage(
            "#N.value.1.is.not.supported", 
            "Z+[q![/n\".7YLF#"
        );
        assertFalse(result);
    }
}