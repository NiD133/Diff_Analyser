package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the DocumentType node, which represents HTML/XML DOCTYPE declarations.
 * 
 * @author Jonathan Hedley, http://jonathanhedley.com/
 */
public class DocumentTypeTest {

    // Constructor validation tests
    
    @Test
    public void shouldAllowBlankNameInConstructor() {
        // Should not throw exception when name is empty
        new DocumentType("", "", "");
    }

    @Test
    public void shouldThrowExceptionWhenPublicIdOrSystemIdIsNull() {
        // Constructor should validate that publicId and systemId are not null
        assertThrows(IllegalArgumentException.class, 
            () -> new DocumentType("html", null, null));
    }

    @Test
    public void shouldAllowBlankPublicAndSystemIds() {
        // Should not throw exception when publicId and systemId are empty strings
        new DocumentType("html", "", "");
    }

    // HTML output generation tests

    @Test 
    public void shouldGenerateCorrectOuterHtmlForDifferentDocumentTypes() {
        // HTML5 doctype (no public/system IDs)
        DocumentType html5DocType = new DocumentType("html", "", "");
        assertEquals("<!doctype html>", html5DocType.outerHtml());

        // Doctype with public ID only
        DocumentType publicOnlyDocType = new DocumentType("html", "-//IETF//DTD HTML//", "");
        assertEquals("<!DOCTYPE html PUBLIC \"-//IETF//DTD HTML//\">", 
            publicOnlyDocType.outerHtml());

        // Doctype with system ID only
        DocumentType systemOnlyDocType = new DocumentType("html", "", 
            "http://www.ibm.com/data/dtd/v11/ibmxhtml1-transitional.dtd");
        assertEquals("<!DOCTYPE html SYSTEM \"http://www.ibm.com/data/dtd/v11/ibmxhtml1-transitional.dtd\">", 
            systemOnlyDocType.outerHtml());

        // Doctype with both public and system IDs
        DocumentType fullDocType = new DocumentType("notHtml", "--public", "--system");
        assertEquals("<!DOCTYPE notHtml PUBLIC \"--public\" \"--system\">", 
            fullDocType.outerHtml());
        
        // Verify individual components are accessible
        assertEquals("notHtml", fullDocType.name());
        assertEquals("--public", fullDocType.publicId());
        assertEquals("--system", fullDocType.systemId());
    }

    // Parser round-trip tests

    @Test 
    public void shouldPreserveDocumentTypesThroughParsingAndSerialization() {
        // HTML5 doctype
        String html5Doctype = "<!DOCTYPE html>";
        assertEquals("<!doctype html>", parseAsHtmlAndGetOutput(html5Doctype));
        assertEquals(html5Doctype, parseAsXmlAndGetOutput(html5Doctype));

        // XHTML transitional doctype with public and system IDs
        String xhtmlTransitionalDoctype = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        assertEquals(xhtmlTransitionalDoctype, parseAsHtmlAndGetOutput(xhtmlTransitionalDoctype));
        assertEquals(xhtmlTransitionalDoctype, parseAsXmlAndGetOutput(xhtmlTransitionalDoctype));

        // System-only doctype
        String systemOnlyDoctype = "<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\">";
        assertEquals(systemOnlyDoctype, parseAsHtmlAndGetOutput(systemOnlyDoctype));
        assertEquals(systemOnlyDoctype, parseAsXmlAndGetOutput(systemOnlyDoctype));

        // Legacy compatibility doctype
        String legacyDoctype = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        assertEquals(legacyDoctype, parseAsHtmlAndGetOutput(legacyDoctype));
        assertEquals(legacyDoctype, parseAsXmlAndGetOutput(legacyDoctype));
    }

    // Attribute access tests

    @Test 
    void shouldProvideAccessToDocumentTypeAttributesAndProperties() {
        // Test simple HTML5 doctype
        Document html5Document = Jsoup.parse("<!DOCTYPE html>");
        DocumentType html5Doctype = html5Document.documentType();
        
        assertEquals("#doctype", html5Doctype.nodeName());
        assertEquals("html", html5Doctype.name());
        assertEquals("html", html5Doctype.attr("name"));
        assertEquals("", html5Doctype.publicId());
        assertEquals("", html5Doctype.systemId());

        // Test complex doctype with public and system IDs
        Document complexDocument = Jsoup.parse("<!DOCTYPE notHtml PUBLIC \"--public\" \"--system\">");
        DocumentType complexDoctype = complexDocument.documentType();

        assertEquals("#doctype", complexDoctype.nodeName());
        assertEquals("nothtml", complexDoctype.name()); // Note: parser normalizes to lowercase
        assertEquals("nothtml", complexDoctype.attr("name"));
        assertEquals("--public", complexDoctype.publicId());
        assertEquals("--system", complexDoctype.systemId());
    }

    // Helper methods for parsing and output generation

    private String parseAsHtmlAndGetOutput(String doctypeString) {
        DocumentType parsedDoctype = (DocumentType) Jsoup.parse(doctypeString).childNode(0);
        return parsedDoctype.outerHtml();
    }

    private String parseAsXmlAndGetOutput(String doctypeString) {
        DocumentType parsedDoctype = (DocumentType) Jsoup.parse(doctypeString, "", Parser.xmlParser()).childNode(0);
        return parsedDoctype.outerHtml();
    }
}