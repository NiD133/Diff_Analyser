package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import java.nio.charset.Charset;
import java.util.Arrays;
import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

public class TokeniserTestTest10 {

    @Test
    public void cp1252SubstitutionTable() {
        for (int i = 0; i < Tokeniser.win1252Extensions.length; i++) {
            String s = new String(new byte[] { (byte) (i + Tokeniser.win1252ExtensionsStart) }, Charset.forName("Windows-1252"));
            assertEquals(1, s.length());
            // some of these characters are illegal
            if (s.charAt(0) == '\ufffd') {
                continue;
            }
            assertEquals(s.charAt(0), Tokeniser.win1252Extensions[i], "At: " + i);
        }
    }
}
