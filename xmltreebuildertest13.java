package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the XmlTreeBuilder.
 */
public class XmlTreeBuilderTest {

    @Test
    void parsesProcessingInstruction() {
        // Arrange
        // An XML string containing a standard XML declaration followed by a processing instruction.
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<?myProcessingInstruction My Processing instruction.?>";

        // Act
        Document doc = Jsoup.parse(xml, "", Parser.xmlParser());

        // Assert
        // The parsed document's children are:
        // 1. An XmlDeclaration for the `<?xml...` tag.
        // 2. A TextNode for the newline character `\n`.
        // 3. An XmlDeclaration for the processing instruction `<?my...`.
        Node node = doc.childNode(2);
        assertInstanceOf(XmlDeclaration.class, node, "The third child node should be an XmlDeclaration representing the processing instruction.");

        XmlDeclaration procInstruction = (XmlDeclaration) node;

        // Jsoup uses the XmlDeclaration class to represent processing instructions.
        // The content of the PI is parsed to extract its name and data.
        assertAll("Processing Instruction properties",
            () -> assertEquals("myProcessingInstruction", procInstruction.name(), "The name should be the target of the processing instruction."),
            () -> assertTrue(procInstruction.hasAttr("My"), "The first word of the PI's content is treated as an attribute key."),
            () -> assertEquals("<?myProcessingInstruction My Processing instruction.?>", procInstruction.outerHtml(), "The outer HTML should match the original PI string.")
        );
    }
}