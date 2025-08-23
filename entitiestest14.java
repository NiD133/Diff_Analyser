package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntitiesTestTest14 {

    @Test
    public void quoteReplacements() {
        String escaped = "&#92; &#36;";
        String unescaped = "\\ $";
        assertEquals(unescaped, Entities.unescape(escaped));
    }
}
