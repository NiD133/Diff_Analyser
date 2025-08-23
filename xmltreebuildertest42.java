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

public class XmlTreeBuilderTestTest42 {

    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    // namespace tests
    @Test
    void xmlns() {
        // example from the xml namespace spec https://www.w3.org/TR/xml-names/
        String xml = "<?xml version=\"1.0\"?>\n" + "<!-- both namespace prefixes are available throughout -->\n" + "<bk:book xmlns:bk=\"urn:loc.gov:books\" xmlns:isbn=\"urn:ISBN:0-395-36341-6\">\n" + "    <bk:title>Cheaper by the Dozen</bk:title>\n" + "    <isbn:number>1568491379</isbn:number>\n" + "</bk:book>";
        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        Element book = doc.expectFirst("bk|book");
        assertEquals("bk:book", book.tag().name());
        assertEquals("bk", book.tag().prefix());
        assertEquals("book", book.tag().localName());
        assertEquals("urn:loc.gov:books", book.tag().namespace());
        Element title = doc.expectFirst("bk|title");
        assertEquals("bk:title", title.tag().name());
        assertEquals("urn:loc.gov:books", title.tag().namespace());
        Element number = doc.expectFirst("isbn|number");
        assertEquals("isbn:number", number.tag().name());
        assertEquals("urn:ISBN:0-395-36341-6", number.tag().namespace());
        // and we didn't modify the dom
        assertEquals(xml, doc.html());
    }
}