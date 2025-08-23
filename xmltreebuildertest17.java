package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the XmlTreeBuilder.
 * This test focuses on how the XML parser handles appending fragments, specifically regarding case sensitivity.
 */
public class XmlTreeBuilderTest {

    @Test
    @DisplayName("Appending an XML fragment should preserve the case of tags and attributes")
    public void appendXmlFragmentPreservesTagAndAttributeCase() {
        // Arrange: Create an XML document using the case-sensitive XML parser.
        String initialXml = "<One>One</One>";
        Document doc = Jsoup.parse(initialXml, "", Parser.xmlParser());
        Elements parentElement = doc.select("One");
        String fragmentToAppend = "<Two ID=2>Two</Two>";

        // Act: Append the new XML fragment to the selected element.
        parentElement.append(fragmentToAppend);

        // Assert: Verify that the tag ('Two') and attribute ('ID') cases are preserved in the final document.
        // The HTML parser, by contrast, would have lowercased them.
        String expectedXml = "<One>One<Two ID=\"2\">Two</Two></One>";
        String actualXml = TextUtil.stripNewlines(doc.html());
        assertEquals(expectedXml, actualXml);
    }
}