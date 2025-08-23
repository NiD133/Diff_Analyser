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

public class XmlTreeBuilderTestTest31 {

    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    @Test
    void customTagsAreFlyweights() {
        String xml = "<foo>Foo</foo><foo>Foo</foo><FOO>FOO</FOO><FOO>FOO</FOO>";
        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        Elements els = doc.children();
        Tag t1 = els.get(0).tag();
        Tag t2 = els.get(1).tag();
        Tag t3 = els.get(2).tag();
        Tag t4 = els.get(3).tag();
        assertEquals("foo", t1.getName());
        assertEquals("FOO", t3.getName());
        assertSame(t1, t2);
        assertSame(t3, t4);
    }
}
