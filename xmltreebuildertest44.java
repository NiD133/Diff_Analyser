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

public class XmlTreeBuilderTestTest44 {

    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    @Test
    void emptyDefault() {
        String xml = "<?xml version='1.0'?>\n" + "<Beers>\n" + "  <!-- the default namespace inside tables is that of HTML -->\n" + "  <table xmlns='http://www.w3.org/1999/xhtml'>\n" + "   <th><td>Name</td><td>Origin</td><td>Description</td></th>\n" + "   <tr> \n" + "     <!-- no default namespace inside table cells -->\n" + "     <td><brandName xmlns=\"\">Huntsman</brandName></td>\n" + "     <td><origin xmlns=\"\">Bath, UK</origin></td>\n" + "     <td>\n" + "       <details xmlns=\"\"><class>Bitter</class><hop>Fuggles</hop>\n" + "         <pro>Wonderful hop, light alcohol, good summer beer</pro>\n" + "         <con>Fragile; excessive variance pub to pub</con>\n" + "         </details>\n" + "        </td>\n" + "      </tr>\n" + "    </table>\n" + "  </Beers>";
        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        Element beers = doc.expectFirst("Beers");
        assertEquals(NamespaceXml, beers.tag().namespace());
        Element td = doc.expectFirst("td");
        assertEquals(NamespaceHtml, td.tag().namespace());
        Element origin = doc.expectFirst("origin");
        assertEquals("", origin.tag().namespace());
        Element pro = doc.expectFirst("pro");
        assertEquals("", pro.tag().namespace());
    }
}
