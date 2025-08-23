package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the XmlTreeBuilder.
 */
class XmlTreeBuilderTest {

    /**
     * This test verifies two key behaviors of the XML parser regarding attributes:
     * 1. Attribute names are case-sensitive (e.g., 'One' is distinct from 'one' and 'ONE').
     * 2. For attributes with an identical case-sensitive name, only the first occurrence is kept.
     */
    @Test
    void xmlParserRespectsAttributeCaseAndDropsDuplicates() {
        // Arrange
        // The input contains attributes with similar names but different casing,
        // as well as exact duplicates.
        // - "One" is duplicated -> the first ("One=One") should be kept.
        // - "ONE" is duplicated -> the first ("ONE=Two") should be kept.
        // - "two" is duplicated -> the first ("two=Six") should be kept.
        // - "one" and "Two" are unique and should be kept.
        String xmlInput = "<p One=One ONE=Two one=Three One=Four ONE=Five two=Six two=Seven Two=Eight>Text</p>";
        Parser xmlParser = Parser.xmlParser();

        // Act
        Document doc = xmlParser.parseInput(xmlInput, "");
        Element pElement = doc.selectFirst("p");
        String actualHtml = pElement.outerHtml();

        // Assert
        String expectedHtml = "<p One=\"One\" ONE=\"Two\" one=\"Three\" two=\"Six\" Two=\"Eight\">Text</p>";
        assertEquals(expectedHtml, actualHtml, "Should keep the first instance of case-sensitive duplicate attributes.");
    }
}