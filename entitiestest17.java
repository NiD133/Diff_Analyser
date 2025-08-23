package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for HTML entity escaping behavior in jsoup.
 */
public class EntitiesTest {

    /**
     * Verifies that less-than ('<') and greater-than ('>') characters are always escaped when they appear within an
     * attribute's value, regardless of the configured {@link EscapeMode}. This is crucial for generating valid and
     * secure HTML, preventing unescaped HTML tags in attributes from breaking the document structure.
     *
     * @see <a href="https://github.com/jhy/jsoup/issues/2337">GitHub Issue #2337</a>
     * @param escapeMode the escape mode to test against (runs for all modes in the enum).
     */
    @ParameterizedTest(name = "Mode: {0}")
    @EnumSource(EscapeMode.class)
    void ltAndGtInAttributeValuesAreAlwaysEscaped(EscapeMode escapeMode) {
        // Arrange
        final String htmlWithUnescapedAttribute = "<a title='<p>One</p>'>One</a>";
        final String expectedEscapedHtml = "<a title=\"&lt;p&gt;One&lt;/p&gt;\">One</a>";

        Document doc = Jsoup.parse(htmlWithUnescapedAttribute);
        Element linkElement = doc.selectFirst("a");
        doc.outputSettings().escapeMode(escapeMode);

        // Act
        String actualHtml = linkElement.outerHtml();

        // Assert
        assertEquals(expectedEscapedHtml, actualHtml);
    }
}