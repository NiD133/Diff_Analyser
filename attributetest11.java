package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the handling of boolean attributes in jsoup.
 * This includes both the static utility method {@link Attribute#isBooleanAttribute(String)}
 * and the parsing behavior of such attributes.
 *
 * @see <a href="https://github.com/jhy/jsoup/issues/1656">GitHub Issue #1656</a>
 */
@DisplayName("Boolean Attribute Handling")
class BooleanAttributeTest {

    @Test
    @DisplayName("isBooleanAttribute() should identify boolean attributes case-insensitively")
    void isBooleanAttributeIsCaseInsensitive() {
        // Assert that the method correctly identifies a boolean attribute regardless of its case.
        assertTrue(Attribute.isBooleanAttribute("required"), "Lowercase");
        assertTrue(Attribute.isBooleanAttribute("REQUIRED"), "Uppercase");
        assertTrue(Attribute.isBooleanAttribute("rEQUIREd"), "Mixed case");

        // Assert that a non-boolean attribute is not identified as one.
        assertFalse(Attribute.isBooleanAttribute("not-a-boolean-attribute"), "Non-boolean attribute");
    }

    @Nested
    @DisplayName("During HTML Parsing")
    class ParsingBehavior {

        @Test
        @DisplayName("should normalize boolean attribute names to lowercase by default")
        void parserNormalizesBooleanAttributeCaseByDefault() {
            // Arrange
            String htmlWithUppercaseBooleanAttribute = "<a href=autofocus REQUIRED>One</a>";
            String expectedHtml = "<a href=\"autofocus\" required>One</a>";

            // Act
            Document doc = Jsoup.parse(htmlWithUppercaseBooleanAttribute);
            Element linkElement = doc.selectFirst("a");

            // Assert
            // By default, jsoup normalizes attribute names to lowercase, as per HTML standards.
            assertEquals(expectedHtml, linkElement.outerHtml());
        }

        @Test
        @DisplayName("should preserve boolean attribute case when configured")
        void parserPreservesBooleanAttributeCaseWhenConfigured() {
            // Arrange
            String htmlWithUppercaseBooleanAttribute = "<a href=autofocus REQUIRED>One</a>";
            String expectedHtml = "<a href=\"autofocus\" REQUIRED>One</a>";

            // To preserve attribute case, we need a parser with that setting enabled.
            Parser casePreservingParser = Parser.htmlParser().settings(ParseSettings.preserveCase);

            // Act
            // Note: The original test had a call to a non-existent Jsoup.parse(String, Parser) method.
            // The correct way to use a custom parser is with Jsoup.parse(html, baseUri, parser).
            Document doc = Jsoup.parse(htmlWithUppercaseBooleanAttribute, "", casePreservingParser);
            Element linkElement = doc.selectFirst("a");

            // Assert
            // With case preservation enabled, the attribute's original case is maintained.
            assertEquals(expectedHtml, linkElement.outerHtml());
        }
    }
}