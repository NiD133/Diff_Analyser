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

public class XmlTreeBuilderTestTest39 {

    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    @Test
    void canSetCustomVoid() {
        String ns = "custom";
        String xml = "<x xmlns=custom><foo><link><meta>";
        TagSet custom = new TagSet();
        custom.valueOf("link", ns).set(Tag.Void);
        custom.valueOf("meta", ns).set(Tag.Void);
        // ns doesn't match, won't impact
        custom.valueOf("foo", "other").set(Tag.Void);
        Document doc = Jsoup.parse(xml, Parser.xmlParser().tagSet(custom));
        String expect = "<x xmlns=\"custom\"><foo><link /><meta /></foo></x>";
        assertEquals(expect, doc.html());
    }
}