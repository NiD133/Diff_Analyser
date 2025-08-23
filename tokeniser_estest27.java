package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This class contains tests for the Tokeniser, focusing on its behavior
 * when used by the XmlTreeBuilder.
 */
public class Tokeniser_ESTestTest27 extends Tokeniser_ESTest_scaffolding {

    /**
     * Verifies that the XML parser handles an input string containing a malformed
     * character reference (e.g., "&5;") gracefully. Instead of throwing an
     * error or misinterpreting the string, it should treat the sequence as
     * literal text.
     */
    @Test(timeout = 4000)
    public void xmlParserShouldTreatMalformedCharacterReferenceAsLiteralText() {
        // Arrange: An input string with a malformed character reference "&5;".
        // A valid reference would be numeric (e.g., "&#123;") or named (e.g., "&amp;").
        String malformedXmlInput = "mcP)?FW_&5;~\"";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // Act: Parse the input string using the XML tree builder.
        Document doc = xmlTreeBuilder.parse(malformedXmlInput, "http://example.com/");

        // Assert: The parsed document's text content should be identical to the input string.
        // This confirms that the parser correctly processed the malformed entity as plain text.
        // The original test asserted on `siblingIndex()`, which is always 0 for a Document
        // and did not validate the actual parsing behavior.
        assertEquals(malformedXmlInput, doc.text());
    }
}