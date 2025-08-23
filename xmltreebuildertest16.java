package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link XmlTreeBuilder}, focusing on its parsing behavior.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that the XML parser preserves the case of tag and attribute names,
     * which is a key difference from the default HTML parser that lowercases them.
     */
    @Test
    public void preservesCaseByDefault() {
        // Arrange
        String xmlInput = "<CHECK>One</CHECK><TEST ID=1>Check</TEST>";
        String expectedOutput = "<CHECK>One</CHECK><TEST ID=\"1\">Check</TEST>";

        // Act
        // Parse the input using the XML parser, which should be case-sensitive.
        Document doc = Jsoup.parse(xmlInput, "", Parser.xmlParser());
        String actualOutput = TextUtil.stripNewlines(doc.html());

        // Assert
        // The output should show that the tag names ('CHECK', 'TEST') and the attribute
        // name ('ID') have all retained their original case.
        assertEquals(expectedOutput, actualOutput);
    }
}