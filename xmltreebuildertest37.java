package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceXml;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the XmlTreeBuilder, focusing on custom tag configurations.
 */
public class XmlTreeBuilderTest {

    // This helper is not used in the test below but is kept as it may be used by other tests in the suite.
    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    /**
     * Tests that a custom tag can be configured as an RCDATA tag in the XML parser.
     * RCDATA (Raw Character Data) tags treat their contents as raw text, but still decode character entities.
     * This means any nested tags (like {@code <foo>}) should be treated as literal text, not parsed as elements.
     */
    @Test
    void canSetCustomRcdataTag() {
        // --- Arrange ---

        // The content to be placed inside our custom RCDATA tag.
        // It includes a nested tag <foo> and an entity &quot;
        String contentWithNestedTag = "Blah\nblah\n<foo></foo>&quot;";

        // The expected text after parsing: <foo> is preserved as text, and &quot; is decoded to ".
        String expectedText = "Blah\nblah\n<foo></foo>\"";

        String xml = "<x><y><z>" + contentWithNestedTag + "</z></y></x><x><z id=2></z>";

        // Create a custom TagSet where 'z' is defined as an RCDATA tag.
        TagSet customTagSet = new TagSet();
        Tag zTag = customTagSet.valueOf("z", NamespaceXml, ParseSettings.preserveCase);
        zTag.set(Tag.RcData); // This is the key configuration for the test.

        Parser customXmlParser = Parser.xmlParser().tagSet(customTagSet);

        // --- Act & Assert: Full Document Parse ---

        // 1. Test parsing a full document.
        Document doc = Jsoup.parse(xml, customXmlParser);
        Element parsedZElement = doc.expectFirst("z");

        // The tag on the parsed element should be an equal copy of our custom tag, not the same instance.
        assertNotSame(zTag, parsedZElement.tag(), "Tag should be a copy, not the same instance.");
        assertEquals(zTag, parsedZElement.tag(), "Tag should be equal to the custom defined tag.");

        // The <z> element should contain a single TextNode because it was parsed as RCDATA.
        assertEquals(1, parsedZElement.childNodeSize());
        Node child = parsedZElement.childNode(0);
        assertInstanceOf(TextNode.class, child, "Child node should be a TextNode because 'z' is an RCDATA tag.");

        // The text content should have the nested <foo> tag as-is, and the &quot; entity decoded.
        assertEquals(expectedText, ((TextNode) child).getWholeText());

        // --- Act & Assert: HTML Fragment Parse ---

        // 2. Test that the RCDATA behavior also applies when parsing a fragment into an element.
        Element secondZElement = doc.expectFirst("#2");
        secondZElement.html(contentWithNestedTag);

        // The whole text should match the expected output, confirming RCDATA rules were used for the fragment.
        assertEquals(expectedText, secondZElement.wholeText());
    }
}