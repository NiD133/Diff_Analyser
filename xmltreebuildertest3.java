package org.jsoup.parser;

import org.jsoup.TextUtil;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the {@link XmlTreeBuilder}.
 */
public class XmlTreeBuilderTest {

    @Test
    void parsesAndPreservesDoctypeAndComment() {
        // Arrange
        String inputXml = "<!DOCTYPE HTML><!-- a comment -->One <qux />Two";
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // Act
        Document doc = builder.parse(inputXml, "http://foo.com/");
        String parsedOutput = TextUtil.stripNewlines(doc.html());

        // Assert
        // Verifies that the parser correctly handles and preserves doctypes, comments, text, and elements.
        assertEquals(inputXml, parsedOutput);
    }
}