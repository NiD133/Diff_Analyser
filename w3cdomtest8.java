package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for W3CDom conversion, focusing on security aspects and custom document handling.
 */
public class W3CDomTest {

    /**
     * Provides HTML and XML parsers for parameterized tests.
     */
    private static Stream<Arguments> parserProvider() {
        return Stream.of(
            Arguments.of(Parser.htmlParser()),
            Arguments.of(Parser.xmlParser())
        );
    }

    @ParameterizedTest
    @MethodSource("parserProvider")
    void doesNotExpandEntities(Parser parser) {
        // Arrange
        // This test ensures that the "billion laughs" and other XXE attacks are mitigated
        // because jsoup does not parse or expand entities within the DOCTYPE.
        String billionLaughsXml = """
            <?xml version="1.0"?>
            <!DOCTYPE lolz [
             <!ENTITY lol "lol">
             <!ENTITY lol1 "&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;">
            ]>
            <html><body><p>&lol1;</p></body></html>""";

        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);
        W3CDom w3cDom = new W3CDom();

        // Act
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        NodeList pElements = w3cDoc.getElementsByTagName("p");
        String w3cString = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

        // Assert
        assertNotNull(w3cDoc);

        // Ensure the entity is not expanded in the DOM text content.
        assertEquals(1, pElements.getLength());
        assertEquals("&lol1;", pElements.item(0).getTextContent());

        // Ensure the entity is not expanded in the string output, and is correctly escaped.
        assertFalse(w3cString.contains("lololol"));
        assertTrue(w3cString.contains("&amp;lol1;"));
    }

    @Test
    public void canConvertToCustomSuppliedDocument() throws ParserConfigurationException {
        // Arrange
        String html = "<html><div></div></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

        // Create a custom W3C Document to be populated by the conversion.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document targetW3cDoc = factory.newDocumentBuilder().newDocument();

        W3CDom w3cDom = new W3CDom();

        // Act
        w3cDom.convert(jsoupDoc, targetW3cDoc);
        String resultHtml = W3CDom.asString(targetW3cDoc, W3CDom.OutputHtml());

        // Assert
        // The conversion should populate the target document, adding necessary namespaces
        // and a head element if missing.
        String expectedHtml = "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                              "<head>" +
                              "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" +
                              "</head>" +
                              "<body>" +
                              "<div></div>" +
                              "</body>" +
                              "</html>";
        // Note: The exact output of the META tag can vary slightly. A more robust check
        // might parse the result and check attributes, but for a serialization test, this is adequate.
        // A simple replace normalizes the self-closing tag for consistency.
        assertEquals(expectedHtml, resultHtml.replace("...>", "... />"));
    }
}