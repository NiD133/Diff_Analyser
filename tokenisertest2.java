package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import java.nio.charset.Charset;
import java.util.Arrays;
import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

public class TokeniserTestTest2 {

    @Test
    public void handleSuperLargeTagNames() {
        // unlikely, but valid. so who knows.
        StringBuilder sb = new StringBuilder(BufferSize);
        do {
            sb.append("LargeTagName");
        } while (sb.length() < BufferSize);
        String tag = sb.toString();
        String html = "<" + tag + ">One</" + tag + ">";
        Document doc = Parser.htmlParser().settings(ParseSettings.preserveCase).parseInput(html, "");
        Elements els = doc.select(tag);
        assertEquals(1, els.size());
        Element el = els.first();
        assertNotNull(el);
        assertEquals("One", el.text());
        assertEquals(tag, el.tagName());
    }
}
