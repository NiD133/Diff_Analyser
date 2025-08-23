package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.internal.TextUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link W3CDom}, focusing on conversion security and output string formatting.
 */
class W3CDomConversionTest {

    private static Stream<Arguments> parserProvider() {
        return Stream.of(
            Arguments.of(Parser.htmlParser()),
            Arguments.of(Parser.xmlParser())
        );
    }

    @ParameterizedTest
    @MethodSource("parserProvider")
    @DisplayName("Conversion should not expand XML entities to prevent Billion Laughs / XXE attacks")
    void conversionDoesNotExpandEntities(Parser parser) {
        // Arrange: Define an XML input with a dangerous entity declaration ("Billion Laughs" attack).
        // Jsoup is not vulnerable because it doesn't parse entities within the DOCTYPE. This test confirms that behavior.
        String billionLaughsXml =
            "<?xml version=\"1.0\"?>" +
            "<!DOCTYPE lolz [" +
            " <!ENTITY lol \"lol\">" +
            " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">" +
            "]>" +
            "<html><body><p>&lol1;</p></body></html>";

        Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);
        W3CDom w3cDom = new W3CDom();

        // Act: Convert the Jsoup document to a W3C document.
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert: Verify that the entity was not expanded.
        assertNotNull(w3cDoc);

        // 1. Check the W3C DOM structure
        org.w3c.dom.NodeList pElements = w3cDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength(), "Should find one <p> element");
        assertEquals("&lol1;", pElements.item(0).getTextContent(), "Text content should be the unexpanded entity name");

        // 2. Check the serialized string output
        String outputString = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertAll("Serialized output should not contain expanded entities",
            () -> assertFalse(outputString.contains("lololol"), "Output should not contain the expanded 'lol' entity"),
            () -> assertTrue(outputString.contains("&amp;lol1;"), "Output should contain the escaped entity reference")
        );
    }

    @Test
    @DisplayName("asString with namespaceAware=false should produce correct HTML and XML output")
    void asStringWithNamespaceAwareFalse_producesCorrectHtmlAndXml() {
        // Arrange
        String inputHtml = "<p>One</p>";
        Document jsoupDoc = Jsoup.parse(inputHtml);

        W3CDom w3cDom = new W3CDom();
        w3cDom.namespaceAware(false); // Disable namespace awareness for this test

        // Expected outputs are different for HTML and XML modes.
        // HTML output adds a <head> with a meta tag and lowercases the text node.
        String expectedHtml = "<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"></head><body><p>one</p></body></html>";
        // XML output adds an XML declaration and preserves the case of the text node.
        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head/><body><p>One</p></body></html>";

        // Act: Convert to a W3C document once, then serialize it to both HTML and XML strings.
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String actualHtml = W3CDom.asString(w3cDoc, W3CDom.OutputHtml());
        String actualXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

        // Assert: Verify that the serialized strings match the expected formats.
        // We normalize spaces to make the comparison independent of formatting differences (e.g., newlines, indentation).
        assertEquals(TextUtil.normalizeSpaces(expectedHtml), TextUtil.normalizeSpaces(actualHtml), "HTML output should be correctly formatted");
        assertEquals(TextUtil.normalizeSpaces(expectedXml), TextUtil.normalizeSpaces(actualXml), "XML output should be correctly formatted");
    }
}