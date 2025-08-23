package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import java.nio.charset.Charset;
import java.util.Arrays;
import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

public class TokeniserTestTest5 {

    @Test
    public void handleLargeComment() {
        StringBuilder sb = new StringBuilder(BufferSize);
        do {
            sb.append("Quite a comment ");
        } while (sb.length() < BufferSize);
        String comment = sb.toString();
        String html = "<p><!-- " + comment + " --></p>";
        Document doc = Jsoup.parse(html);
        Elements els = doc.select("p");
        assertEquals(1, els.size());
        Element el = els.first();
        assertNotNull(el);
        Comment child = (Comment) el.childNode(0);
        assertEquals(" " + comment + " ", child.getData());
    }
}
