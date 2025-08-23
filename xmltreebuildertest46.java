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

public class XmlTreeBuilderTestTest46 {

    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    @Test
    void elementsViaAppendHtmlAreNamespaced() {
        // tests that when elements / attributes are added via a fragment parse, they inherit the namespace stack, and can still override
        String xml = "<out xmlns='/out'><bk:book xmlns:bk='/books' xmlns:edi='/edi'><bk:title>Test</bk:title><li edi:foo='bar'></bk:book></out>";
        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        // insert some parsed xml, inherit bk and edi, and with an inner node override bk
        Element book = doc.expectFirst("bk|book");
        book.append("<bk:content edi:foo=qux>Content</bk:content>");
        Element out = doc.expectFirst("out");
        assertEquals("/out", out.tag().namespace());
        Element content = book.expectFirst("bk|content");
        assertEquals("bk:content", content.tag().name());
        assertEquals("/books", content.tag().namespace());
        assertEquals("/edi", content.attribute("edi:foo").namespace());
        content.append("<data>Data</data><html xmlns='/html' xmlns:bk='/update'><p>Foo</p><bk:news>News</bk:news></html>");
        // p should be in /html, news in /update
        Element p = content.expectFirst("p");
        assertEquals("/html", p.tag().namespace());
        Element news = content.expectFirst("bk|news");
        assertEquals("/update", news.tag().namespace());
        Element data = content.expectFirst("data");
        assertEquals("/out", data.tag().namespace());
    }
}
