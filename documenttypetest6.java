package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the {@link DocumentType} node, focusing on attribute retrieval after parsing.
 */
public class DocumentTypeTest {

    @Test
    void parsesSimpleDoctypeCorrectly() {
        // Arrange: A simple HTML5 doctype string.
        String html = "<!DOCTYPE html>";

        // Act: Parse the string and get the DocumentType node.
        Document doc = Jsoup.parse(html);
        DocumentType doctype = doc.documentType();

        // Assert: Verify all attributes are as expected for a simple doctype.
        assertNotNull(doctype, "A DocumentType node should be present.");
        assertAll("Simple Doctype Attributes",
            () -> assertEquals("#doctype", doctype.nodeName()),
            () -> assertEquals("html", doctype.name()),
            () -> assertEquals("html", doctype.attr("name")),
            () -> assertEquals("", doctype.publicId(), "Public ID should be empty for a simple doctype."),
            () -> assertEquals("", doctype.systemId(), "System ID should be empty for a simple doctype.")
        );
    }

    @Test
    void parsesDoctypeWithPublicAndSystemIdsCorrectly() {
        // Arrange: A doctype string with public and system identifiers.
        String html = "<!DOCTYPE notHtml PUBLIC \"--public\" \"--system\">";

        // Act: Parse the string and get the DocumentType node.
        Document doc = Jsoup.parse(html);
        DocumentType doctype = doc.documentType();

        // Assert: Verify all attributes, including public and system IDs, are correct.
        assertNotNull(doctype, "A DocumentType node should be present.");
        assertAll("Doctype with Public and System IDs",
            () -> assertEquals("#doctype", doctype.nodeName()),
            () -> assertEquals("nothtml", doctype.name(), "Doctype name is lowercased by the parser."),
            () -> assertEquals("nothtml", doctype.attr("name")),
            () -> assertEquals("--public", doctype.publicId()),
            () -> assertEquals("--system", doctype.systemId())
        );
    }
}