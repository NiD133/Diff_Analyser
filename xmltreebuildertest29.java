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

public class XmlTreeBuilderTestTest29 {

    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    @Test
    void xmlOutputCorrectsInvalidAttributeNames() {
        String xml = "<body style=\"color: red\" \" name\"><div =\"\"></div></body>";
        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        assertEquals(Syntax.xml, doc.outputSettings().syntax());
        String out = doc.html();
        assertEquals("<body style=\"color: red\" _=\"\" name_=\"\"><div _=\"\"></div></body>", out);
    }
}
