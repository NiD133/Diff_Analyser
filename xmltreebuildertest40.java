package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static org.jsoup.nodes.Document.OutputSettings.Syntax;
import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.jsoup.parser.Parser.NamespaceXml;
import static org.junit.jupiter.api.Assertions.*;

public class XmlTreeBuilderTestTest40 {

    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    @Test
    void canSupplyWithHtmlTagSet() {
        // use the properties of html tag set but without HtmlTreeBuilder rules
        String xml = "<html xmlns=" + NamespaceHtml + "><div><script>a<b</script><img><p>";
        Document doc = Jsoup.parse(xml, Parser.xmlParser().tagSet(TagSet.Html()));
        doc.outputSettings().prettyPrint(true);
        String expect = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + " <div>\n" + "  <script>//<![CDATA[\n" + "a<b\n" + "//]]></script>\n" + "  <img />\n" + "  <p></p>\n" + " </div>\n" + "</html>";
        assertEquals(expect, doc.html());
        doc.outputSettings().syntax(Syntax.html);
        expect = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + " <div>\n" + "  <script>a<b</script>\n" + "  <img>\n" + "  <p></p>\n" + " </div>\n" + "</html>";
        assertEquals(expect, doc.html());
    }
}
