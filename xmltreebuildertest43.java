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

public class XmlTreeBuilderTestTest43 {

    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    @Test
    void unprefixedDefaults() {
        String xml = "<?xml version=\"1.0\"?>\n" + "<!-- elements are in the HTML namespace, in this case by default -->\n" + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "  <head><title>Frobnostication</title></head>\n" + "  <body><p>Moved to \n" + "    <a href=\"http://frob.example.com\">here</a>.</p></body>\n" + "</html>";
        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        Element html = doc.expectFirst("html");
        assertEquals(NamespaceHtml, html.tag().namespace());
        Element a = doc.expectFirst("a");
        assertEquals(NamespaceHtml, a.tag().namespace());
    }
}
