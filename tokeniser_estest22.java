package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Tests the XML parser's robustness when handling malformed character references.
 */
public class XmlParserErrorHandlingTest {

    @Test
    public void xmlParserShouldHandleMalformedCharacterReferencesGracefully() throws IOException {
        // Arrange
        // This input string contains multiple malformed or incomplete character references
        // (e.g., "&gt=" instead of "&gt;"). This test verifies that the parser processes
        // such input without crashing and correctly identifies the errors.
        final String malformedXmlFragment = "amp=12;1&gt=1q;3&lt=1o;2&quot=y;0&";
        final String baseUri = "https://jsoup.org/";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        Parser parser = new Parser(builder);
        
        // We expect multiple errors, so we must enable error tracking.
        // The limit should be at least the number of expected errors.
        final int maxErrors = 5;
        parser.setTrackErrors(maxErrors);

        // Act
        Document doc = parser.parse(new StringReader(malformedXmlFragment), baseUri);

        // Assert
        // 1. The parser should not crash and should produce a valid document object.
        assertNotNull(doc);
        assertEquals(baseUri, doc.location());

        // 2. The parser should have logged an error for each malformed entity.
        List<ParseError> errors = parser.getErrors();
        assertEquals("Expected four parse errors for the malformed entities", 4, errors.size());

        // 3. The parsed content should reflect Jsoup's specific handling of these
        // malformations in XML mode. Unrecognized entities are treated as literal text,
        // and the final lone ampersand is escaped in the output.
        String expectedBodyHtml = "amp=12;1&gt;=1q;3&lt;=1o;2\"=y;0&amp;";
        assertEquals(expectedBodyHtml, doc.body().html());
    }
}