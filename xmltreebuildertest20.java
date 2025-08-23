package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link XmlTreeBuilder}.
 */
public class XmlTreeBuilderTest {

    @Test
    void xmlParserWithHtmlSettingsNormalizesTagCase() {
        // Arrange
        String htmlWithMixedCaseTags = "<div>test</DIV><p></p>";
        String expectedHtml = "<div>test</div><p></p>";

        // This is a specific edge case: using an XML parser, but with HTML-style settings
        // that normalize tag names to lowercase.
        Parser parser = Parser.xmlParser().settings(ParseSettings.htmlDefault);

        // Act
        Document document = Jsoup.parse(htmlWithMixedCaseTags, "", parser);
        String actualHtml = document.html();

        // Assert
        assertEquals(expectedHtml, actualHtml, "The mixed-case end tag </DIV> should be normalized to </div>");
    }
}