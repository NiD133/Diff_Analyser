package com.itextpdf.text.error_messages;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.After;
import org.junit.Test;

/**
 * Readable tests for MessageLocalization.
 *
 * Goals:
 * - Use descriptive test names
 * - Keep tests independent and self-explanatory
 * - Avoid EvoSuite-specific runners/utilities
 * - Reset global state between tests
 */
public class MessageLocalizationReadableTest {

    /**
     * Reset global language state after each test to keep tests isolated.
     */
    @After
    public void resetLanguage() throws Exception {
        // Restore a known-good baseline
        MessageLocalization.setLanguage("en", null);
    }

    @Test
    public void setMessages_nullReader_throwsNullPointerException() throws Exception {
        try {
            MessageLocalization.setMessages(null);
            fail("Expected NullPointerException when passing a null Reader");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void setLanguage_nullLanguage_throwsIllegalArgumentException() throws Exception {
        try {
            MessageLocalization.setLanguage(null, "US");
            fail("Expected IllegalArgumentException when language is null");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void setLanguage_en_returnsTrue() throws Exception {
        assertTrue(MessageLocalization.setLanguage("en", null));
    }

    @Test
    public void setLanguage_unknownLanguage_returnsFalse() throws Exception {
        assertFalse(MessageLocalization.setLanguage("zz", null));
    }

    @Test
    public void getMessage_missingKey_returnsHelpfulFallback() {
        String key = "this.key.does.not.exist";
        String msg = MessageLocalization.getMessage(key, true);
        assertEquals("No message found for " + key, msg);
    }

    @Test
    public void getMessage_knownKey_usesDefaultEnglishWhenRequested() {
        // This key exists in the default en.lng shipped with iText
        String key = "you.can.only.add.cells.to.rows.no.objects.of.type.1";
        String msg = MessageLocalization.getMessage(key, true);

        // Do not assert the full sentence to avoid brittleness across iText versions.
        assertNotNull(msg);
        assertTrue("Expected english message to contain placeholder {1}", msg.contains("{1}"));
        assertTrue("Expected a recognizable english sentence", msg.toLowerCase().startsWith("you can only add"));
    }

    @Test
    public void getComposedMessage_replacesPlaceholders() {
        // Known key with a single placeholder {1}
        String key = "you.can.only.add.cells.to.rows.no.objects.of.type.1";

        String msg = MessageLocalization.getComposedMessage(key, "Cell");

        assertNotNull(msg);
        assertFalse("Placeholder {1} should be replaced", msg.contains("{1}"));
        assertTrue("Substitution value should be present", msg.contains("Cell"));
    }

    @Test
    public void getComposedMessage_handlesNullKeyGracefully() {
        String msg = MessageLocalization.getComposedMessage(null, (Object[]) null);
        assertEquals("No message found for null", msg);
    }

    @Test
    public void setMessages_fromCustomReader_overridesMessagesForCurrentSession() throws Exception {
        // Provide a minimal custom "language" with a single mapping
        StringReader reader = new StringReader("greeting=Hello, World!");
        MessageLocalization.setMessages(reader);

        // The custom mapping should be used when fallback is disabled
        assertEquals("Hello, World!", MessageLocalization.getMessage("greeting", false));

        // For keys not present in the custom map, enabling fallback should return default english
        String knownEnglishKey = "you.can.only.add.cells.to.rows.no.objects.of.type.1";
        String fallback = MessageLocalization.getMessage(knownEnglishKey, true);
        assertNotNull(fallback);
        assertTrue(fallback.contains("{1}"));
    }
}