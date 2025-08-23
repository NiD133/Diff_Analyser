package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceXml;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link XmlTreeBuilder}, focusing on custom tag configurations.
 */
public class XmlTreeBuilderTest {

    @Test
    void customTagMarkedAsDataIsParsedAsRawText() {
        // Arrange: Define content that includes characters that would normally be parsed as markup (<foo>)
        // or entities (&quot;). When inside a data tag, this should be treated as literal text.
        String contentWithUnescapedMarkup = "Blah\nblah\n<foo></foo>&quot;";
        String xml = "<x><y><z>" + contentWithUnescapedMarkup + "</z></y></x><x><z id=2></z>";

        // Create a custom tag set where 'z' is defined as a data tag.
        TagSet customTagSet = new TagSet();
        Tag customDataTag = customTagSet.valueOf("z", NamespaceXml, ParseSettings.preserveCase);
        customDataTag.set(Tag.Data);

        // Create a parser with this custom tag set.
        Parser xmlParser = Parser.xmlParser().tagSet(customTagSet);

        // Act: Parse the full XML string.
        Document doc = Jsoup.parse(xml, xmlParser);
        Element dataTagElement = doc.expectFirst("z");

        // Assert: Verify the initial parse treated the 'z' tag's content as a single DataNode.
        // The parser uses a copy of the tag, so it shouldn't be the same instance, but it should be equal.
        assertNotSame(customDataTag, dataTagElement.tag(), "Parser should use a copy of the custom tag, not the same instance.");
        assertEquals(customDataTag, dataTagElement.tag(), "The element's tag should be equal to the custom data tag definition.");
        
        assertEquals(1, dataTagElement.childNodeSize(), "Element should have exactly one child node.");
        Node child = dataTagElement.childNode(0);
        assertTrue(child instanceof DataNode, "The child node should be a DataNode.");
        assertEquals(contentWithUnescapedMarkup, ((DataNode) child).getWholeData(), "DataNode content should be the raw, unparsed string.");
        assertEquals(contentWithUnescapedMarkup, dataTagElement.data(), "The element's data() method should return the raw content.");

        // Now, test that the data tag behavior is also applied during a fragment parse.
        // Arrange: Get the second 'z' element, which should also be recognized as a data tag.
        Element secondDataTagElement = doc.expectFirst("#2");

        // Act: Set its content using html(), which triggers a fragment parse.
        secondDataTagElement.html(contentWithUnescapedMarkup);

        // Assert: Verify the fragment parse also created a DataNode.
        assertEquals(1, secondDataTagElement.childNodeSize(), "Fragment parse should also result in one child node.");
        Node secondChild = secondDataTagElement.childNode(0);
        assertTrue(secondChild instanceof DataNode, "Child of fragment-parsed element should also be a DataNode.");
        assertEquals(contentWithUnescapedMarkup, ((DataNode) secondChild).getWholeData(), "DataNode content from fragment parse should match.");
        assertEquals(contentWithUnescapedMarkup, secondDataTagElement.data(), "Element's data() from fragment parse should match.");
    }
}