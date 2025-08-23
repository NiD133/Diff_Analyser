package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Tests the QueryParser's ability to handle CSS selectors with escaped characters.
 */
@DisplayName("QueryParser: Escaped Identifiers")
public class QueryParserTestTest14 {

    private Document doc;
    private Element elementWithClass;
    private Element elementWithId;

    @BeforeEach
    void setUp() {
        // In CSS, identifiers (like class names or IDs) that start with a digit after a
        // hyphen (e.g., "-4a") must be escaped. The digit '4' can be escaped as `\34 `.
        String html = "<div class='-4a'>One</div> <div id='-4a'>Two</div>";
        doc = Jsoup.parse(html);
        elementWithClass = doc.expectFirst("div.-4a");
        elementWithId = doc.expectFirst("div#-4a");
    }

    @Nested
    @DisplayName("when parsing escaped class selectors")
    class EscapedClassSelector {
        @Test
        @DisplayName("should find element with simple escaped class selector")
        void findsWithSimpleSelector() {
            // Arrange: The selector for class="-4a" with '4' escaped.
            String escapedClassSelector = "div.-\\34 a";

            // Act
            Element found = doc.expectFirst(escapedClassSelector);

            // Assert
            assertSame(elementWithClass, found);
        }

        @Test
        @DisplayName("should find element with complex escaped class selector")
        void findsWithComplexSelector() {
            // Arrange: A more specific selector using the escaped class.
            String complexEscapedClassSelector = "html > body > div.-\\34 a";

            // Act
            Element found = doc.expectFirst(complexEscapedClassSelector);

            // Assert
            assertSame(elementWithClass, found);
        }

        @Test
        @DisplayName("should have correct internal representation for complex escaped class selector")
        void hasCorrectInternalRepresentation() {
            // Arrange: A complex selector to check the parser's internal state (white-box test).
            String complexEscapedClassSelector = "html > body > div.-\\34 a";
            String expectedSExpression = "(ImmediateParentRun (Tag 'html')(Tag 'body')(And (Tag 'div')(Class '.-4a')))";

            // Act & Assert
            assertEquals(expectedSExpression, sexpr(complexEscapedClassSelector));
        }
    }

    @Nested
    @DisplayName("when parsing escaped ID selectors")
    class EscapedIdSelector {
        @Test
        @DisplayName("should find element with simple escaped ID selector")
        void findsWithSimpleSelector() {
            // Arrange: The selector for id="-4a" with '4' escaped.
            String escapedIdSelector = "#-\\34 a";

            // Act
            Element found = doc.expectFirst(escapedIdSelector);

            // Assert
            assertSame(elementWithId, found);
        }

        @Test
        @DisplayName("should find element with complex escaped ID selector")
        void findsWithComplexSelector() {
            // Arrange: A more specific selector using the escaped ID.
            String complexEscapedIdSelector = "html > body > #-\\34 a";

            // Act
            Element found = doc.expectFirst(complexEscapedIdSelector);

            // Assert
            assertSame(elementWithId, found);
        }

        @Test
        @DisplayName("should have correct internal representation for complex escaped ID selector")
        void hasCorrectInternalRepresentation() {
            // Arrange: A complex selector to check the parser's internal state (white-box test).
            String complexEscapedIdSelector = "html > body > #-\\34 a";
            String expectedSExpression = "(ImmediateParentRun (Tag 'html')(Tag 'body')(Id '#-4a')))";

            // Act & Assert
            assertEquals(expectedSExpression, sexpr(complexEscapedIdSelector));
        }

        @Test
        @DisplayName("cssSelector() should generate a clean, unescaped ID selector")
        void generatesCleanCssSelector() {
            // This test verifies that Element.cssSelector() generates a clean, standard selector,
            // which is the inverse operation of parsing the escaped one.
            assertEquals("#-4a", elementWithId.cssSelector());
        }
    }
}