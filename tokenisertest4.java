package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import java.nio.charset.Charset;
import java.util.Arrays;
import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

public class TokeniserTestTest4 {

    @Test
    public void handleLargeText() {
        StringBuilder sb = new StringBuilder(BufferSize);
        do {
            sb.append("A Large Amount of Text");
        } while (sb.length() < BufferSize);
        String text = sb.toString();
        String html = "<p>" + text + "</p>";
        Document doc = Jsoup.parse(html);
        Elements els = doc.select("p");
        assertEquals(1, els.size());
        Element el = els.first();
        assertNotNull(el);
        assertEquals(text, el.text());
    }
}
