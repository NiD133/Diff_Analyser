package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.jsoup.nodes.Document.OutputSettings.Syntax;
import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the XmlTreeBuilder, focusing on its interaction with different tag sets and output syntaxes.
 */
public class XmlTreeBuilderTest {

    @Test
    @DisplayName("XML parser with HTML tag set should output in XML syntax by default")
    void xmlParserWithHtmlTagSet_OutputsInXmlSyntax() {
        // GIVEN an input string that resembles XML but uses tags defined in the HTML tag set (e.g., <script>, <img>).
        String xml = "<html xmlns=" + NamespaceHtml + "><div><script>a<b</script><img><p>";

        // WHEN the string is parsed using the XML parser configured with the HTML tag set.
        Document doc = Jsoup.parse(xml, Parser.xmlParser().tagSet(TagSet.Html()));
        doc.outputSettings().prettyPrint(true);

        // THEN the output should adhere to XML syntax rules.
        // Specifically, <script> content is wrapped in <![CDATA[...]]> and empty tags like <img> are self-closing.
        String expectedXmlOutput = """
                <html xmlns="http://www.w3.org/1999/xhtml">
                 <div>
                  <script>//<![CDATA[
                a<b
                //]]></script>
                  <img />
                  <p></p>
                 </div>
                </html>""";
        assertEquals(expectedXmlOutput, doc.html(), "The output should be formatted as XML.");
    }

    @Test
    @DisplayName("XML parser output should switch to HTML syntax when configured")
    void xmlParserWithHtmlTagSet_CanSwitchOutputToHtmlSyntax() {
        // GIVEN a document parsed with the XML parser.
        String xml = "<html xmlns=" + NamespaceHtml + "><div><script>a<b</script><img><p>";
        Document doc = Jsoup.parse(xml, Parser.xmlParser().tagSet(TagSet.Html()));
        doc.outputSettings().prettyPrint(true);

        // WHEN the document's output syntax is explicitly set to HTML.
        doc.outputSettings().syntax(Syntax.html);

        // THEN the output should adhere to HTML syntax rules.
        // Specifically, <script> content is not wrapped in <![CDATA[...]]> and empty tags like <img> are not self-closing.
        String expectedHtmlOutput = """
                <html xmlns="http://www.w3.org/1999/xhtml">
                 <div>
                  <script>a<b</script>
                  <img>
                  <p></p>
                 </div>
                </html>""";
        assertEquals(expectedHtmlOutput, doc.html(), "The output should be formatted as HTML.");
    }
}