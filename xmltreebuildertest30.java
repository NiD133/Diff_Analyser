package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.jsoup.nodes.Document.OutputSettings.Syntax;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the XmlTreeBuilder.
 */
public class XmlTreeBuilderTest {

    @Test
    void handlesValidAndCoercedAttributeNamesInXml() {
        // The purpose of this test is to verify that the XML parser correctly handles various attribute names.
        // Specifically, it checks that:
        // 1. A valid attribute name containing a mix of allowed characters ("bB1-_:.") is preserved.
        // 2. An attribute name with an invalid character ('!') is coerced into a valid name,
        //    where "_9!" becomes "_9_".

        // Arrange
        String inputXml = "<a bB1-_:.=foo _9!=bar xmlns:p1=qux>One</a>";
        String expectedXml = "<a bB1-_:.=\"foo\" _9_=\"bar\" xmlns:p1=\"qux\">One</a>";

        // Act
        // The base URI is empty as it's not relevant for this parsing test.
        Document doc = Jsoup.parse(inputXml, "", Parser.xmlParser());
        String actualXml = doc.html();

        // Assert
        // First, confirm the document was parsed in XML mode.
        assertEquals(Syntax.xml, doc.outputSettings().syntax());

        // Second, verify the serialized output matches the expected XML,
        // confirming the attribute name coercion.
        assertEquals(expectedXml, actualXml);
    }
}