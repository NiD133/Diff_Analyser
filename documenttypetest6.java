package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DocumentTypeTestTest6 {

    private String htmlOutput(String in) {
        DocumentType type = (DocumentType) Jsoup.parse(in).childNode(0);
        return type.outerHtml();
    }

    private String xmlOutput(String in) {
        return Jsoup.parse(in, "", Parser.xmlParser()).childNode(0).outerHtml();
    }

    @Test
    void attributes() {
        Document doc = Jsoup.parse("<!DOCTYPE html>");
        DocumentType doctype = doc.documentType();
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.name());
        assertEquals("html", doctype.attr("name"));
        assertEquals("", doctype.publicId());
        assertEquals("", doctype.systemId());
        doc = Jsoup.parse("<!DOCTYPE notHtml PUBLIC \"--public\" \"--system\">");
        doctype = doc.documentType();
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("nothtml", doctype.name());
        assertEquals("nothtml", doctype.attr("name"));
        assertEquals("--public", doctype.publicId());
        assertEquals("--system", doctype.systemId());
    }
}
