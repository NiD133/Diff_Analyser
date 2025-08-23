package org.jsoup.nodes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTypeTest {

    // Rendering

    @Test
    void rendersDoctypeWithPublicAndSystemIds() {
        // Given a doctype with a name, publicId, and systemId
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd");

        // When rendering
        String html = doctype.outerHtml();

        // Then expect a full, upper-case <!DOCTYPE ... PUBLIC "..."> form
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", html);
    }

    @Test
    void rendersDoctypeWithOnlySystemId() {
        // Given a doctype with only a systemId
        DocumentType doctype = new DocumentType("", "", "http://example.com/dtd.dtd");

        // When rendering
        String html = doctype.outerHtml();

        // Then expect SYSTEM form
        assertEquals("<!DOCTYPE SYSTEM \"http://example.com/dtd.dtd\">", html);
    }

    @Test
    void rendersMinimalDoctypeWhenAllFieldsEmpty() {
        // Given an "empty" doctype
        DocumentType doctype = new DocumentType("", "", "");

        // When rendering
        String html = doctype.outerHtml();

        // Then expect the minimal lowercase <!doctype>
        assertEquals("<!doctype>", html);
    }

    // Accessors

    @Test
    void gettersReturnProvidedValues() {
        DocumentType doctype = new DocumentType("QGRn=|xO8k", "PUBLIC_ID", "SYSTEM_ID");

        assertEquals("QGRn=|xO8k", doctype.name());
        assertEquals("PUBLIC_ID", doctype.publicId());
        assertEquals("SYSTEM_ID", doctype.systemId());
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    void gettersReturnEmptyStringsWhenUnset() {
        DocumentType doctype = new DocumentType("", "", "");

        assertEquals("", doctype.name());
        assertEquals("", doctype.publicId());
        assertEquals("", doctype.systemId());
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    void nodeNameIsAlwaysDoctype() {
        DocumentType doctype = new DocumentType("anything", "anything", "anything");
        assertEquals("#doctype", doctype.nodeName());
    }

    // Validation

    @Test
    void constructorRejectsNullPublicId() {
        // systemId must also be non-null to reach publicId validation deterministically
        assertThrows(IllegalArgumentException.class, () -> new DocumentType("html", null, ""));
    }

    @Test
    void constructorRejectsNullSystemId() {
        assertThrows(IllegalArgumentException.class, () -> new DocumentType("html", "", null));
    }

    // PubSysKey handling

    @Test
    void setPubSysKeyRejectsUnknownValues() {
        DocumentType doctype = new DocumentType("", "", "sys");
        // Only "PUBLIC", "SYSTEM", or null should be valid
        assertThrows(IllegalArgumentException.class, () -> doctype.setPubSysKey("optgroup"));
    }

    @Test
    void setPubSysKeyAcceptsNull() {
        DocumentType doctype = new DocumentType("", "", "sys");
        // Should not throw; null typically clears the key and allows defaulting logic
        assertDoesNotThrow(() -> doctype.setPubSysKey(null));
    }
}