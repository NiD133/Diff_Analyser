package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntitiesTestTest12 {

    @Test
    public void prefixMatch() {
        // https://github.com/jhy/jsoup/issues/2207
        // example from https://html.spec.whatwg.org/multipage/parsing.html#character-reference-state
        String text = "I'm &notit; I tell you. I'm &notin; I tell you.";
        assertEquals("I'm ¬it; I tell you. I'm ∉ I tell you.", Entities.unescape(text, false));
        // not for attributes
        assertEquals("I'm &notit; I tell you. I'm ∉ I tell you.", Entities.unescape(text, true));
    }
}
