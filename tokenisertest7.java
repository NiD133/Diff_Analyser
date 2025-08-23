package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import java.nio.charset.Charset;
import java.util.Arrays;
import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

public class TokeniserTestTest7 {

    @Test
    public void handleLargeTitle() {
        StringBuilder sb = new StringBuilder(BufferSize);
        do {
            sb.append("Quite a long title");
        } while (sb.length() < BufferSize);
        String title = sb.toString();
        String html = "<title>" + title + "</title>";
        Document doc = Jsoup.parse(html);
        Elements els = doc.select("title");
        assertEquals(1, els.size());
        Element el = els.first();
        assertNotNull(el);
        TextNode child = (TextNode) el.childNode(0);
        assertEquals(title, el.text());
        assertEquals(title, child.getWholeText());
        assertEquals(title, doc.title());
    }
}
