package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import java.nio.charset.Charset;
import java.util.Arrays;
import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

public class TokeniserTestTest1 {

    @Test
    public void bufferUpInAttributeVal() {
        // https://github.com/jhy/jsoup/issues/967
        // check each double, singlem, unquoted impls
        String[] quotes = { "\"", "'", "" };
        for (String quote : quotes) {
            String preamble = "<img src=" + quote;
            String tail = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
            StringBuilder sb = new StringBuilder(preamble);
            final int charsToFillBuffer = BufferSize - preamble.length();
            for (int i = 0; i < charsToFillBuffer; i++) {
                sb.append('a');
            }
            // First character to cross character buffer boundary
            sb.append('X');
            sb.append(tail).append(quote).append(">\n");
            String html = sb.toString();
            Document doc = Jsoup.parse(html);
            String src = doc.select("img").attr("src");
            assertTrue(src.contains("X"), "Handles for quote " + quote);
            assertTrue(src.contains(tail));
        }
    }
}
