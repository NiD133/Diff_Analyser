package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntitiesTestTest7 {

    @Test
    public void escapeSupplementaryCharacter() {
        String text = new String(Character.toChars(135361));
        String escapedAscii = Entities.escape(text, new OutputSettings().charset("ascii").escapeMode(base));
        assertEquals("&#x210c1;", escapedAscii);
        String escapedUtf = Entities.escape(text, new OutputSettings().charset("UTF-8").escapeMode(base));
        assertEquals(text, escapedUtf);
    }
}
