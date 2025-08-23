package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntitiesTestTest4 {

    @Test
    public void unescapeMultiChars() {
        // gg is not combo, but 8811 could conflict with NestedGreaterGreater or others
        String text = "&NestedGreaterGreater; &nGg; &nGt; &nGtv; &Gt; &gg;";
        String un = "≫ ⋙̸ ≫⃒ ≫̸ ≫ ≫";
        assertEquals(un, Entities.unescape(text));
        String escaped = Entities.escape(un, new OutputSettings().charset("ascii").escapeMode(extended));
        assertEquals("&Gt; &Gg;&#x338; &Gt;&#x20d2; &Gt;&#x338; &Gt; &Gt;", escaped);
        assertEquals(un, Entities.unescape(escaped));
    }
}
