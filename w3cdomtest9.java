package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for W3CDom conversion, focusing on security, namespaces, and edge cases.
 */
@DisplayName("W3CDom Conversion")
public class W3CDomTest {

    @Nested
    @DisplayName("Security")
    class SecurityTests {
        /**
         * Provides both the HTML and XML parsers for parameterized tests.
         */
        private static Stream<Arguments> parserProvider() {
            return Stream.of(
                Arguments.of(Parser.htmlParser()),
                Arguments.of(Parser.xmlParser())
            );
        }

        @ParameterizedTest(name = "with {0}")
        @MethodSource("parserProvider")
        @DisplayName("Should not expand XML entities to prevent Billion Laughs and XXE attacks")
        void shouldNotExpandXmlEntitiesToPreventAttacks(Parser parser) {
            // Arrange
            // This XML contains a "Billion Laughs" entity expansion payload.
            // Jsoup should not expand these entities, and that behavior should be preserved
            // when converting to a W3C DOM.
            String billionLaughsXml = """
                <?xml version="1.0"?>
                <!DOCTYPE lolz [
                 <!ENTITY lol "lol">
                 <!ENTITY lol1 "&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;">
                ]>
                <html><body><p>&lol1;</p></body></html>""";

            Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);
            W3CDom w3cDom = new W3CDom();

            // Act
            org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

            // Assert
            // 1. Verify the DOM structure: the <p> tag's content should be the unexpanded entity.
            NodeList pElements = w3cDoc.getElementsByTagName("p");
            assertEquals(1, pElements.getLength(), "Should find one <p> element");
            assertEquals("&lol1;", pElements.item(0).getTextContent(), "Entity in <p> tag should not be expanded");

            // 2. Verify the string output: it should contain the escaped entity, not the expanded text.
            String outputString = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
            assertFalse(outputString.contains("lololol"), "Output string should not contain the expanded entity");
            assertTrue(outputString.contains("&amp;lol1;"), "Output string should contain the escaped entity reference");
        }
    }

    @Nested
    @DisplayName("Namespace Handling")
    class NamespaceTests {
        @Test
        @DisplayName("Should correctly handle elements with undeclared namespace prefixes")
        void shouldCorrectlyHandleElementsWithUndeclaredNamespaces() {
            // Arrange
            String htmlWithUndeclaredNamespace = "<fb:like>One</fb:like>";
            Document jsoupDoc = Jsoup.parse(htmlWithUndeclaredNamespace);

            // Act
            org.w3c.dom.Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

            // Assert
            // Jsoup wraps the fragment in <html><body>...</body></html>
            Node htmlElement = w3cDoc.getElementsByTagName("html").item(0);
            Node fbLikeElement = w3cDoc.getElementsByTagName("fb:like").item(0);

            assertNotNull(htmlElement, "<html> element should exist");
            assertAll("Root <html> element",
                () -> assertEquals("http://www.w3.org/1999/xhtml", htmlElement.getNamespaceURI(), "Namespace URI should be the default XHTML namespace"),
                () -> assertEquals("html", htmlElement.getLocalName(), "Local name should be 'html'"),
                () -> assertEquals("html", htmlElement.getNodeName(), "Node name should be 'html'")
            );

            assertNotNull(fbLikeElement, "<fb:like> element should exist");
            assertAll("Undeclared namespace <fb:like> element",
                () -> assertEquals("http://www.w3.org/1999/xhtml", fbLikeElement.getNamespaceURI(), "Should inherit the default XHTML namespace"),
                () -> assertEquals("like", fbLikeElement.getLocalName(), "Local name should be the part after the colon"),
                () -> assertEquals("fb:like", fbLikeElement.getNodeName(), "Node name should be the full qualified name")
            );
        }
    }
}