package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for the Jsoup Tokeniser.
 */
public class TokeniserTest {

    /**
     * Tests that a numeric character reference in the C1 control range (e.g., &#128;)
     * is parsed into its Windows-1252 equivalent, but also logs a parse error.
     * This behavior matches modern web browsers for compatibility.
     *
     * @see <a href="https://html.spec.whatwg.org/multipage/parsing.html#numeric-character-reference-end-state">HTML Spec: Numeric Character Reference End State</a>
     */
    @Test
    public void parsesWindows1252NumericEntityWithError() {
        // Arrange
        // The character reference &#128; is an invalid C1 control character.
        // Browsers map this to the Windows-1252 encoding for 0x80, which is the Euro sign (€).
        final String htmlWithInvalidReference = "<html><body>&#128;</body></html>";
        final String expectedParsedText = "\u20ac"; // The Euro symbol
        final int expectedErrorCount = 1;

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(expectedErrorCount);

        // Act
        Document document = parser.parseInput(htmlWithInvalidReference, "");
        String actualText = document.text();
        ParseErrorList errors = parser.getErrors();

        // Assert
        assertAll(
            () -> assertEquals(expectedParsedText, actualText, "Should parse &#128; as the Euro symbol for compatibility."),
            () -> assertEquals(expectedErrorCount, errors.size(), "Should log a parse error for the invalid character reference."),
            () -> assertFalse(errors.isEmpty(), "Error list should not be empty.")
        );
    }
}