package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the configuration of the Jsoup XML Parser.
 */
public class XmlParserSettingsTest {

    @Test
    @DisplayName("XML parser with HTML settings should normalize tag and attribute case")
    void xmlParserShouldNormalizeCaseWhenUsingHtmlSettings() {
        // Arrange
        // This test verifies that parser settings can override a parser's default behavior.
        // We provide an XML string with uppercase tags and attributes.
        String inputXmlWithUppercase = "<TEST ID=1>Check</TEST>";
        String expectedNormalizedOutput = "<test id=\"1\">Check</test>";

        // Act
        // We configure the XML parser to use HTML's default parse settings.
        // The XML parser normally preserves case, but HTML settings enforce lowercase normalization.
        Parser parser = Parser.xmlParser().settings(ParseSettings.htmlDefault);
        Document doc = Jsoup.parse(inputXmlWithUppercase, "", parser);
        String actualOutput = TextUtil.stripNewlines(doc.html());

        // Assert
        // The resulting HTML should have lowercase tag and attribute names, as dictated by the HTML settings.
        assertEquals(expectedNormalizedOutput, actualOutput);
    }
}