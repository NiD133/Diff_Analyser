package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import java.nio.charset.Charset;
import java.util.Arrays;
import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

public class TokeniserTestTest6 {

    @Test
    public void handleLargeCdata() {
        StringBuilder sb = new StringBuilder(BufferSize);
        do {
            sb.append("Quite a lot of CDATA <><><><>");
        } while (sb.length() < BufferSize);
        String cdata = sb.toString();
        String html = "<p><![CDATA[" + cdata + "]]></p>";
        Document doc = Jsoup.parse(html);
        Elements els = doc.select("p");
        assertEquals(1, els.size());
        Element el = els.first();
        assertNotNull(el);
        TextNode child = (TextNode) el.childNode(0);
        assertEquals(cdata, el.text());
        assertEquals(cdata, child.getWholeText());
    }
}
