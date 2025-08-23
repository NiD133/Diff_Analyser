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

public class XmlTreeBuilderTestTest28 {

    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    @Test
    public void xmlSyntaxAlwaysEscapesLtAndGtInAttributeValues() {
        // https://github.com/jhy/jsoup/issues/2337
        Document doc = Jsoup.parse("<p one='&lt;two&gt;'>Three</p>", "", Parser.xmlParser());
        doc.outputSettings().escapeMode(Entities.EscapeMode.extended);
        assertEquals(doc.outputSettings().syntax(), Syntax.xml);
        assertEquals("<p one=\"&lt;two&gt;\">Three</p>", doc.html());
    }
}
