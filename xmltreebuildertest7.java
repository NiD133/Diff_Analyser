package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Tests for the {@link XmlTreeBuilder}.
 */
public class XmlTreeBuilderTest {

    @Test
    void parsingXmlInputShouldCorrectlyIdentifyDeclarationAndCommentNodes() {
        // Arrange: Define the input XML and the expected serialized output.
        // The input uses single quotes, but the standard output normalizes to double quotes.
        String xml = "<?xml encoding='UTF-8' ?><body>One</body><!-- comment -->";
        String expectedOutput = "<?xml encoding=\"UTF-8\"?><body>One</body><!-- comment -->";

        // Act: Parse the XML string using the XML parser.
        Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
        List<Node> childNodes = doc.childNodes();

        // Assert: Verify the structure and content of the parsed document.

        // 1. Check the complete serialized output.
        assertEquals(expectedOutput, doc.outerHtml(), "The serialized output should be correctly formatted.");

        // 2. Verify the number and types of the top-level nodes.
        assertEquals(3, childNodes.size(), "Should be three top-level nodes: declaration, element, and comment.");

        // 3. Inspect each node individually.
        Node firstNode = childNodes.get(0);
        assertInstanceOf(XmlDeclaration.class, firstNode, "The first node should be an XmlDeclaration.");
        assertEquals("#declaration", firstNode.nodeName());

        Node secondNode = childNodes.get(1);
        assertInstanceOf(Element.class, secondNode, "The second node should be an Element.");
        assertEquals("body", ((Element) secondNode).tagName());

        Node thirdNode = childNodes.get(2);
        assertInstanceOf(Comment.class, thirdNode, "The third node should be a Comment.");
        assertEquals("#comment", thirdNode.nodeName());
    }
}