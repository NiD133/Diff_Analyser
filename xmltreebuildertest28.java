package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.junit.jupiter.api.Test;

import static org.jsoup.nodes.Document.OutputSettings.Syntax;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link XmlTreeBuilder}.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that in XML syntax, the less-than ('<') and greater-than ('>') characters
     * are always escaped within attribute values, regardless of the configured escape mode.
     * This is critical to prevent generating invalid XML.
     *
     * @see <a href="https://github.com/jhy/jsoup/issues/2337">GitHub issue #2337</a>
     */
    @Test
    public void xmlSyntaxAlwaysEscapesLtAndGtInAttributeValues() {
        // Arrange
        String xmlWithEscapedCharsInAttr = "<p one='&lt;two&gt;'>Three</p>";
        String expectedOutput = "<p one=\"&lt;two&gt;\">Three</p>";

        Document doc = Jsoup.parse(xmlWithEscapedCharsInAttr, "", Parser.xmlParser());

        // This setting should NOT affect the mandatory escaping of < and > in XML attributes.
        doc.outputSettings().escapeMode(Entities.EscapeMode.extended);

        // Sanity check to confirm the document is in XML mode.
        assertEquals(Syntax.xml, doc.outputSettings().syntax(), "Document should be in XML syntax mode.");

        // Act
        String actualOutput = doc.html();

        // Assert
        assertEquals(expectedOutput, actualOutput, "Attribute values should retain &lt; and &gt; escapes in XML mode.");
    }
}