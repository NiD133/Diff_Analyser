package org.jsoup.parser;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Test suite for parsing XML fragments using the XmlTreeBuilder.
 * This class focuses on verifying the correct handling of mixed content (elements and text)
 * and attribute URL resolution.
 */
@DisplayName("XML Fragment Parsing")
public class XmlFragmentParsingTest {

    @Test
    @DisplayName("Should correctly parse a fragment with mixed content and resolve attribute URLs")
    void parsesXmlFragmentWithMixedContentAndResolvesUrls() {
        // Arrange: Define an XML fragment containing a self-closing tag with a relative URL,
        // a text node, and a nested tag. Also, define the base URI for URL resolution.
        String xmlFragment = "<one src='/foo/' />Two<three><four /></three>";
        String baseUri = "http://example.com/";

        // Act: Parse the XML fragment using the method under test.
        List<Node> parsedNodes = Parser.parseXmlFragment(xmlFragment, baseUri);

        // Assert: Verify the structure and content of the parsed nodes.
        // The fragment should be parsed into three top-level nodes: <one>, "Two", and <three>.
        assertEquals(3, parsedNodes.size(), "The parser should identify three top-level nodes.");

        // 1. Validate the first node: the <one> element.
        Node firstNode = parsedNodes.get(0);
        assertInstanceOf(Element.class, firstNode, "The first node should be an Element.");
        Element firstElement = (Element) firstNode;

        assertEquals("one", firstElement.nodeName(), "The first element's tag name should be 'one'.");
        assertEquals(
                "http://example.com/foo/",
                firstElement.absUrl("src"),
                "The 'src' attribute's relative URL should be resolved against the base URI."
        );

        // 2. Validate the second node: the "Two" text node.
        Node secondNode = parsedNodes.get(1);
        assertInstanceOf(TextNode.class, secondNode, "The second node should be a TextNode.");
        TextNode textNode = (TextNode) secondNode;

        assertEquals("Two", textNode.text(), "The text content of the second node should be 'Two'.");

        // 3. Validate the third node: the <three> element.
        Node thirdNode = parsedNodes.get(2);
        assertInstanceOf(Element.class, thirdNode, "The third node should be an Element.");
        Element thirdElement = (Element) thirdNode;

        assertEquals("three", thirdElement.nodeName(), "The third element's tag name should be 'three'.");
    }
}