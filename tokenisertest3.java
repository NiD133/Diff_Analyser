package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import java.nio.charset.Charset;
import java.util.Arrays;
import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

public class TokeniserTestTest3 {

    @Test
    public void handleSuperLargeAttributeName() {
        StringBuilder sb = new StringBuilder(BufferSize);
        do {
            sb.append("LargAttributeName");
        } while (sb.length() < BufferSize);
        String attrName = sb.toString();
        String html = "<p " + attrName + "=foo>One</p>";
        Document doc = Jsoup.parse(html);
        Elements els = doc.getElementsByAttribute(attrName);
        assertEquals(1, els.size());
        Element el = els.first();
        assertNotNull(el);
        assertEquals("One", el.text());
        Attribute attribute = el.attributes().asList().get(0);
        assertEquals(attrName.toLowerCase(), attribute.getKey());
        assertEquals("foo", attribute.getValue());
    }
}
