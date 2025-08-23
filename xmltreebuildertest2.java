package org.jsoup.parser;

import org.jsoup.TextUtil;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link XmlTreeBuilder}, focusing on its parsing and tree construction logic.
 */
public class XmlTreeBuilderTest {

    @Test
    @DisplayName("popStackToClose should ignore a closing tag that has no matching open element")
    public void popStackToCloseIgnoresMismatchedEndTag() {
        // Arrange
        // This XML contains a </bar> closing tag which has no corresponding opening <bar> tag.
        // The parser is expected to ignore </bar> and correctly close the inner <val> tag.
        String malformedXml = "<doc><val>One<val>Two</val></bar>Three</doc>";
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();

        // Act
        Document parsedDoc = treeBuilder.parse(malformedXml, "http://example.com/");
        String actualHtml = TextUtil.stripNewlines(parsedDoc.html());

        // Assert
        String expectedHtml = "<doc><val>One<val>Two</val>Three</val></doc>";
        String failureMessage = "A mismatched closing tag </bar> should be ignored, and its subsequent text 'Three' should be appended to the parent <val> element.";

        assertEquals(expectedHtml, actualHtml, failureMessage);
    }
}