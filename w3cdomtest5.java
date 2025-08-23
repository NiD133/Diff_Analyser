package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.stream.Stream;

import static org.jsoup.nodes.Document.OutputSettings.Syntax.xml;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("W3C DOM Conversion Edge Cases")
public class W3CDomEdgeCasesTest {

    // Note: The original test class contained several helper methods (e.g., parseXml, xpath)
    // that were not used by the tests within this file. They have been removed for clarity
    // and to focus the class on its specific responsibilities.

    @DisplayName("Should not expand XML entities during conversion to prevent attacks")
    @ParameterizedTest(name = "with {0}")
    @MethodSource("parserProvider")
    void doesNotExpandEntities(Parser parser) {
        // GIVEN an XML input with a "Billion Laughs" style entity declaration.
        // This test confirms that Jsoup's parser, by not processing DTD entities,
        // prevents entity expansion attacks from affecting the W3C DOM conversion.
        String billionLaughsXml =
            "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE lolz [\n" +
            " <!ENTITY lol \"lol\">\n" +
            " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
            "]>\n" +
            "<html><body><p>&lol1;</p></body></html>";

        // WHEN parsing with Jsoup and converting to a W3C Document
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);
        Document w3cDoc = W3CDom.convert(jsoupDoc);

        // THEN the W3C document is created successfully
        assertNotNull(w3cDoc);

        // AND the entity within the <p> tag remains unexpanded
        NodeList pElements = w3cDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength());
        Node pElement = pElements.item(0);
        assertEquals("&lol1;", pElement.getTextContent());

        // AND the serialized output does not contain the expanded entity
        String outputXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertAll("Serialized output should be safe",
            () -> assertFalse(outputXml.contains("lololol"), "Should not contain expanded 'lol' string"),
            () -> assertTrue(outputXml.contains("&amp;lol1;"), "Should contain escaped entity '&lol1;'")
        );
    }

    private static Stream<Arguments> parserProvider() {
        return Stream.of(
            Arguments.of(Parser.htmlParser()),
            Arguments.of(Parser.xmlParser())
        );
    }

    private static final String HTML_WITH_UNICODE_ATTRIBUTES =
        "<!DOCTYPE html><html><head></head><body>" +
        "<p hành=\"1\" hình=\"2\">unicode attr names coerced</p>" +
        "</body></html>";

    // The expected output shows that the attribute 'hành' is dropped and 'hình' is normalized to 'h_nh'.
    // This is the desired behavior when the Jsoup output syntax is set to XML, which enforces
    // stricter (NCName) attribute naming rules, even if the final W3CDom output is HTML.
    private static final String EXPECTED_HTML_AFTER_XML_SYNTAX_CONVERSION =
        "<!DOCTYPE html SYSTEM \"about:legacy-compat\">" +
        "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head>" +
        "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head>" +
        "<body><p h_nh=\"2\">unicode attr names coerced</p></body></html>";

    @Test
    @DisplayName("Should normalize HTML attribute names when Jsoup output syntax is set to XML")
    void htmlAttributeNamesAreNormalizedWhenJsoupOutputSyntaxIsXml() {
        // GIVEN an HTML document with non-ASCII attribute names
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(HTML_WITH_UNICODE_ATTRIBUTES);

        // WHEN the Jsoup document's output syntax is set to XML before converting to W3C DOM,
        // and then the W3C DOM is rendered as an HTML string
        jsoupDoc.outputSettings().syntax(xml);
        Document w3cDoc = W3CDom.convert(jsoupDoc);
        String actualOutput = W3CDom.asString(w3cDoc, W3CDom.OutputHtml());

        // THEN the attribute names are normalized to be XML-compatible in the final output
        assertEquals(EXPECTED_HTML_AFTER_XML_SYNTAX_CONVERSION, TextUtil.stripNewlines(actualOutput));
    }
}