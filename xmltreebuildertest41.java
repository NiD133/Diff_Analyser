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

public class XmlTreeBuilderTestTest41 {

    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    @Test
    void prettyFormatsTextInline() {
        // https://github.com/jhy/jsoup/issues/2141
        String xml = "<package><metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" + "<dc:identifier id=\"pub-id\">id</dc:identifier>\n" + "<dc:title>title</dc:title>\n" + "<dc:language>ja</dc:language>\n" + "<dc:description>desc</dc:description>\n" + "</metadata></package>";
        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        doc.outputSettings().prettyPrint(true);
        assertEquals("<package>\n" + " <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" + "  <dc:identifier id=\"pub-id\">id</dc:identifier> <dc:title>title</dc:title> <dc:language>ja</dc:language> <dc:description>desc</dc:description>\n" + " </metadata>\n" + "</package>", doc.html());
        // can customize
        Element meta = doc.expectFirst("metadata");
        Tag metaTag = meta.tag();
        metaTag.set(Tag.Block);
        // set all the inner els of meta to be blocks
        for (Element inner : meta) inner.tag().set(Tag.Block);
        assertEquals("<package>\n" + " <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" + "  <dc:identifier id=\"pub-id\">id</dc:identifier>\n" + "  <dc:title>title</dc:title>\n" + "  <dc:language>ja</dc:language>\n" + "  <dc:description>desc</dc:description>\n" + " </metadata>\n" + "</package>", doc.html());
    }
}
