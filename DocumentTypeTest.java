package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the DocumentType node.
 */
public class DocumentTypeTest {

    @Test
    public void testConstructorAllowsBlankName() {
        // Test that a DocumentType can be created with a blank name
        new DocumentType("", "", "");
    }

    @Test
    public void testConstructorThrowsExceptionOnNulls() {
        // Test that a DocumentType constructor throws an exception when publicId or systemId is null
        assertThrows(IllegalArgumentException.class, () -> new DocumentType("html", null, null));
    }

    @Test
    public void testConstructorAllowsBlankPublicAndSystemIds() {
        // Test that a DocumentType can be created with blank public and system IDs
        new DocumentType("html", "", "");
    }

    @Test
    public void testOuterHtmlGeneration() {
        // Test the outerHtml generation for different DocumentType configurations

        // HTML5 doctype
        DocumentType html5 = new DocumentType("html", "", "");
        assertEquals("<!doctype html>", html5.outerHtml());

        // Public doctype
        DocumentType publicDocType = new DocumentType("html", "-//IETF//DTD HTML//", "");
        assertEquals("<!DOCTYPE html PUBLIC \"-//IETF//DTD HTML//\">", publicDocType.outerHtml());

        // System doctype
        DocumentType systemDocType = new DocumentType("html", "", "http://www.ibm.com/data/dtd/v11/ibmxhtml1-transitional.dtd");
        assertEquals("<!DOCTYPE html SYSTEM \"http://www.ibm.com/data/dtd/v11/ibmxhtml1-transitional.dtd\">", systemDocType.outerHtml());

        // Combined public and system doctype
        DocumentType combo = new DocumentType("notHtml", "--public", "--system");
        assertEquals("<!DOCTYPE notHtml PUBLIC \"--public\" \"--system\">", combo.outerHtml());
        assertEquals("notHtml", combo.name());
        assertEquals("--public", combo.publicId());
        assertEquals("--system", combo.systemId());
    }

    @Test
    public void testRoundTripParsing() {
        // Test round-trip parsing of DOCTYPE declarations

        // Basic HTML5 doctype
        String base = "<!DOCTYPE html>";
        assertEquals("<!doctype html>", parseHtmlOutput(base));
        assertEquals(base, parseXmlOutput(base));

        // Public doctype
        String publicDoc = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        assertEquals(publicDoc, parseHtmlOutput(publicDoc));
        assertEquals(publicDoc, parseXmlOutput(publicDoc));

        // System doctype
        String systemDoc = "<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\">";
        assertEquals(systemDoc, parseHtmlOutput(systemDoc));
        assertEquals(systemDoc, parseXmlOutput(systemDoc));

        // Legacy doctype
        String legacyDoc = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        assertEquals(legacyDoc, parseHtmlOutput(legacyDoc));
        assertEquals(legacyDoc, parseXmlOutput(legacyDoc));
    }

    private String parseHtmlOutput(String input) {
        // Parse the input string as HTML and return the outer HTML of the first child node
        DocumentType type = (DocumentType) Jsoup.parse(input).childNode(0);
        return type.outerHtml();
    }

    private String parseXmlOutput(String input) {
        // Parse the input string as XML and return the outer HTML of the first child node
        return Jsoup.parse(input, "", Parser.xmlParser()).childNode(0).outerHtml();
    }

    @Test
    public void testAttributes() {
        // Test attribute retrieval from DocumentType nodes

        // HTML5 doctype
        Document doc = Jsoup.parse("<!DOCTYPE html>");
        DocumentType doctype = doc.documentType();
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.name());
        assertEquals("html", doctype.attr("name"));
        assertEquals("", doctype.publicId());
        assertEquals("", doctype.systemId());

        // Custom doctype with public and system IDs
        doc = Jsoup.parse("<!DOCTYPE notHtml PUBLIC \"--public\" \"--system\">");
        doctype = doc.documentType();
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("nothtml", doctype.name());
        assertEquals("nothtml", doctype.attr("name"));
        assertEquals("--public", doctype.publicId());
        assertEquals("--system", doctype.systemId());
    }
}