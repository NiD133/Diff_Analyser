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

public class XmlTreeBuilderTestTest36 {

    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    @Test
    void xmlHeaderIsValid() {
        // https://github.com/jhy/jsoup/issues/2298
        String xml = "<?xml version=\"1.0\"?>\n<root></root>";
        String expect = xml;
        Document doc = Jsoup.parse(xml, Parser.xmlParser().setTrackErrors(10));
        assertEquals(0, doc.parser().getErrors().size());
        assertEquals(expect, doc.html());
        xml = "<?xml version=\"1.0\" ?>\n<root></root>";
        doc = Jsoup.parse(xml, Parser.xmlParser().setTrackErrors(10));
        assertEquals(0, doc.parser().getErrors().size());
        assertEquals(expect, doc.html());
    }
}