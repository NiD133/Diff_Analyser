package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntitiesTestTest18 {

    @Test
    public void controlCharactersAreEscaped() {
        // https://github.com/jhy/jsoup/issues/1556
        // escape in HTML for legibility; remove from xml
        String input = "<a foo=\"&#x1b;esc&#x7;bell\">Text &#x1b; &#x7;</a>";
        Document doc = Jsoup.parse(input);
        assertEquals(input, doc.body().html());
        Document xml = Jsoup.parse(input, "", Parser.xmlParser());
        assertEquals("<a foo=\"escbell\">Text  </a>", xml.html());
    }
}
