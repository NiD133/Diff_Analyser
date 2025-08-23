package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntitiesTestTest3 {

    @Test
    public void escapedSupplementary() {
        String text = "\uD835\uDD59";
        String escapedAscii = Entities.escape(text, new OutputSettings().charset("ascii").escapeMode(base));
        assertEquals("&#x1d559;", escapedAscii);
        String escapedAsciiFull = Entities.escape(text, new OutputSettings().charset("ascii").escapeMode(extended));
        assertEquals("&hopf;", escapedAsciiFull);
        String escapedUtf = Entities.escape(text, new OutputSettings().charset("UTF-8").escapeMode(extended));
        assertEquals(text, escapedUtf);
    }
}
