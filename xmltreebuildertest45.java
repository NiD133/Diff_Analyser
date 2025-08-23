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

public class XmlTreeBuilderTestTest45 {

    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    @Test
    void namespacedAttribute() {
        String xml = "<x xmlns:edi='http://ecommerce.example.org/schema'>\n" + "  <!-- the 'taxClass' attribute's namespace is http://ecommerce.example.org/schema -->\n" + "  <lineItem edi:taxClass=\"exempt\" other=foo>Baby food</lineItem>\n" + "</x>";
        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        Element lineItem = doc.expectFirst("lineItem");
        Attribute taxClass = lineItem.attribute("edi:taxClass");
        assertNotNull(taxClass);
        assertEquals("edi", taxClass.prefix());
        assertEquals("taxClass", taxClass.localName());
        assertEquals("http://ecommerce.example.org/schema", taxClass.namespace());
        Attribute other = lineItem.attribute("other");
        assertNotNull(other);
        assertEquals("foo", other.getValue());
        assertEquals("", other.prefix());
        assertEquals("other", other.localName());
        assertEquals("", other.namespace());
    }
}
