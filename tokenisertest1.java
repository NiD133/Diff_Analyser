package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Contains tests for the {@link Tokeniser}.
 */
// Renamed from TokeniserTestTest1 for clarity and convention.
class TokeniserTest {

    /**
     * Tests that the Tokeniser correctly parses an attribute value that spans across its internal buffer boundary.
     * This is a regression test for issue #967.
     * <p>
     * The test constructs an HTML string where an attribute's value is long enough to force the parser's
     * {@link CharacterReader} to refill its buffer mid-value. It verifies that the parsed attribute is complete and correct.
     *
     * @param quote The quote character to use for the attribute value. An empty string signifies an unquoted attribute.
     * @see <a href="https://github.com/jhy/jsoup/issues/967">Issue #967</a>
     */
    @DisplayName("Should correctly parse an attribute value that spans the buffer boundary")
    @ParameterizedTest(name = "for {0} ({1})")
    @ValueSource(strings = {"\"", "'", ""})
    void attributeValueSpanningBufferBoundaryIsParsedCorrectly(String quote) {
        // 1. Arrange: Construct an HTML string where the attribute value crosses the CharacterReader's buffer boundary.
        String attributeStart = "<img src=" + quote;
        String valueTail = "rest-of-the-value-after-boundary";
        char boundaryCrossingChar = 'X';

        // Create padding to fill the buffer right up to the boundary, so `boundaryCrossingChar` is the first
        // character to be read into the new buffer.
        final int paddingLength = BufferSize - attributeStart.length();
        String padding = "a".repeat(paddingLength);

        // The full attribute value that we expect to be parsed.
        String expectedSrcValue = padding + boundaryCrossingChar + valueTail;

        // Assemble the complete HTML string.
        String html = attributeStart + expectedSrcValue + quote + ">";

        // 2. Act: Parse the HTML.
        Document doc = Jsoup.parse(html);
        String parsedSrcValue = doc.select("img").attr("src");

        // 3. Assert: Verify the parsed attribute value is complete and matches the expected string exactly.
        String testCaseDescription = getQuoteDescription(quote);
        assertEquals(expectedSrcValue, parsedSrcValue, "Attribute value should be parsed correctly for " + testCaseDescription);
    }

    /**
     * Provides a human-readable description for the type of quote being tested.
     */
    private static String getQuoteDescription(String quote) {
        switch (quote) {
            case "\"":
                return "a double-quoted attribute";
            case "'":
                return "a single-quoted attribute";
            case "":
                return "an unquoted attribute";
            default:
                return "an unknown attribute type";
        }
    }
}