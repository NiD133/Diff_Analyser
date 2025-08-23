package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test suite for the {@link XmlTreeBuilder}.
 */
class XmlTreeBuilderTest {

    @Test
    @DisplayName("should correctly detect the character set from an XML declaration")
    void detectsCharsetFromXmlDeclaration() throws IOException, URISyntaxException {
        // Arrange
        // The test file 'xml-charset.xml' is encoded in ISO-8859-1 and declares this in its prolog:
        // <?xml version="1.0" encoding="ISO-8859-1"?>
        final String resourcePath = "/htmltests/xml-charset.xml";
        final URL resourceUrl = XmlTreeBuilder.class.getResource(resourcePath);
        assertNotNull(resourceUrl, "Test resource file not found: " + resourcePath);

        final File xmlFile = new File(resourceUrl.toURI());
        final String expectedCharset = "ISO-8859-1";
        // The expected output after parsing and re-serializing without pretty-printing.
        // The special characters (äöåéü) confirm the encoding was handled correctly.
        final String expectedXmlOutput = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><data>äöåéü</data>";
        final String baseUri = "http://example.com/";

        // Act
        // We pass 'null' for the charset, forcing Jsoup to detect it from the XML declaration.
        Document doc = Jsoup.parse(xmlFile, null, baseUri, Parser.xmlParser());
        doc.outputSettings().prettyPrint(false); // Use compact output for a stable string comparison.
        String actualXmlOutput = doc.html();

        // Assert
        assertEquals(expectedCharset, doc.charset().name(), "Should detect charset from XML declaration.");
        assertEquals(expectedXmlOutput, actualXmlOutput, "Parsed and re-serialized XML should match expected output.");
    }
}