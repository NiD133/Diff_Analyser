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

public class XmlTreeBuilderTestTest37 {

    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    @Test
    void canSetCustomRcdataTag() {
        String inner = "Blah\nblah\n<foo></foo>&quot;";
        String innerText = "Blah\nblah\n<foo></foo>\"";
        String xml = "<x><y><z>" + inner + "</z></y></x><x><z id=2></z>";
        TagSet custom = new TagSet();
        Tag z = custom.valueOf("z", NamespaceXml, ParseSettings.preserveCase);
        z.set(Tag.RcData);
        Document doc = Jsoup.parse(xml, Parser.xmlParser().tagSet(custom));
        Element zEl = doc.expectFirst("z");
        // not same because we copy the tagset
        assertNotSame(z, zEl.tag());
        assertEquals(z, zEl.tag());
        assertEquals(1, zEl.childNodeSize());
        Node child = zEl.childNode(0);
        assertTrue(child instanceof TextNode);
        assertEquals(innerText, ((TextNode) child).getWholeText());
        // test fragment context parse - should parse <foo> as text
        Element z2 = doc.expectFirst("#2");
        z2.html(inner);
        assertEquals(innerText, z2.wholeText());
    }
}
