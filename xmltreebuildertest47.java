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

public class XmlTreeBuilderTestTest47 {

    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    @Test
    void selfClosingOK() {
        // In XML, all tags can be self-closing regardless of tag type
        Parser parser = Parser.xmlParser().setTrackErrors(10);
        String xml = "<div id='1'/><p/><div>Foo</div><div></div><foo></foo>";
        Document doc = Jsoup.parse(xml, "", parser);
        ParseErrorList errors = parser.getErrors();
        assertEquals(0, errors.size());
        assertEquals("<div id=\"1\" /><p /><div>Foo</div><div /><foo></foo>", TextUtil.stripNewlines(doc.outerHtml()));
        // we infer that empty els can be represented with self-closing if seen in parse
    }
}
