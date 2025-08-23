package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import java.nio.charset.Charset;
import java.util.Arrays;
import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

public class TokeniserTestTest13 {

    @Test
    void tokenDataToString() {
        TokenData data = new TokenData();
        assertEquals("", data.toString());
        data.set("abc");
        assertEquals("abc", data.toString());
        data.append("def");
        assertEquals("abcdef", data.toString());
    }
}
