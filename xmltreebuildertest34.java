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

public class XmlTreeBuilderTestTest34 {

    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    @Test
    void declarations() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><!DOCTYPE html\n" + "  PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" + "  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + "<!ELEMENT footnote (#PCDATA|a)*>";
        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        XmlDeclaration proc = (XmlDeclaration) doc.childNode(0);
        DocumentType doctype = (DocumentType) doc.childNode(1);
        XmlDeclaration decl = (XmlDeclaration) doc.childNode(2);
        assertEquals("xml", proc.name());
        assertEquals("1.0", proc.attr("version"));
        assertEquals("utf-8", proc.attr("encoding"));
        assertEquals("version=\"1.0\" encoding=\"utf-8\"", proc.getWholeDeclaration());
        assertEquals("<?xml version=\"1.0\" encoding=\"utf-8\"?>", proc.outerHtml());
        assertEquals("html", doctype.name());
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.attr("systemId"));
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", doctype.outerHtml());
        assertEquals("ELEMENT", decl.name());
        assertEquals("footnote (#PCDATA|a)*", decl.getWholeDeclaration());
        assertTrue(decl.hasAttr("footNote"));
        assertFalse(decl.hasAttr("ELEMENT"));
        assertEquals("<!ELEMENT footnote (#PCDATA|a)*>", decl.outerHtml());
        assertEquals("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + "<!ELEMENT footnote (#PCDATA|a)*>", doc.outerHtml());
    }
}
