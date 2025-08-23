package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link W3CDom} conversion, focusing on security and attribute handling.
 */
@DisplayName("W3CDom Conversion")
public class W3CDomTest {

    /**
     * Provides both the HTML and XML parsers to run tests against.
     */
    private static Stream<Arguments> parserProvider() {
        return Stream.of(
            Arguments.of(Parser.htmlParser()),
            Arguments.of(Parser.xmlParser())
        );
    }

    @ParameterizedTest
    @MethodSource("parserProvider")
    @DisplayName("Should prevent Billion Laughs and XXE attacks by not expanding entities")
    void billionLaughsAttackDoesNotCauseEntityExpansion(Parser parser) {
        // This test ensures that jsoup does not expand entities defined in a DOCTYPE.
        // This prevents vulnerabilities like the "Billion Laughs" attack and XXE.
        // The expected behavior is that the entity reference (&lol1;) is treated as literal text.

        // Arrange
        String billionLaughsXml = """
            <?xml version="1.0"?>
            <!DOCTYPE lolz [
             <!ENTITY lol "lol">
             <!ENTITY lol1 "&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;">
            ]>
            <html><body><p>&lol1;</p></body></html>""";

        // Act
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        // Assert
        // 1. Check the W3C DOM text content
        NodeList pElements = w3cDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength());
        // The text content should be the unexpanded entity name, not "lololol..."
        assertEquals("&lol1;", pElements.item(0).getTextContent());

        // 2. Check the serialized string output
        String outXml = W3CDom.asString(w3cDoc);
        // The output should not contain the expanded entity.
        assertFalse(outXml.contains("lololol"));
        // It should contain the escaped entity reference, confirming it was treated as text.
        assertTrue(outXml.contains("&amp;lol1;"));
    }

    @Test
    @DisplayName("Should handle HTML attributes case-insensitively after conversion")
    void attributesInHtmlAreHandledCaseInsensitively() {
        // This test verifies that when parsing HTML, attribute keys are normalized to lowercase,
        // which is the standard behavior for HTML. This is important for reliable attribute access.
        // See: https://github.com/jhy/jsoup/issues/981

        // Arrange
        String html = """
            <html lang=en>
            <body>
              <img src="firstImage.jpg" alt="Alt one" />
              <IMG SRC="secondImage.jpg" AlT="Alt two" />
            </body>
            </html>""";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

        // Act
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        NodeList imgNodes = w3cDoc.getElementsByTagName("img");

        // Assert
        assertEquals(2, imgNodes.getLength(), "Should find two <img> elements");

        // Assertions for the first image (attributes already lowercase)
        Element firstImg = (Element) imgNodes.item(0);
        assertAll("Attributes of first <img>",
            () -> assertEquals("firstImage.jpg", firstImg.getAttribute("src")),
            () -> assertEquals("Alt one", firstImg.getAttribute("alt")),
            () -> assertEquals(2, firstImg.getAttributes().getLength())
        );

        // Assertions for the second image (attributes in mixed case in source)
        Element secondImg = (Element) imgNodes.item(1);
        assertAll("Attributes of second <IMG> (should be normalized to lowercase)",
            // Note: W3C DOM's getAttribute is case-sensitive. This test confirms that
            // jsoup's HTML parser correctly normalized 'SRC' to 'src' and 'AlT' to 'alt'.
            () -> assertEquals("secondImage.jpg", secondImg.getAttribute("src")),
            () -> assertEquals("Alt two", secondImg.getAttribute("alt")),
            () -> assertEquals(2, secondImg.getAttributes().getLength())
        );
    }
}