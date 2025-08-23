package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntitiesTestTest20 {

    @Test
    void parseHtmlEncodedEmojiMultipoint() {
        // 💯
        String emoji = Parser.unescapeEntities("&#55357;&#56495;", false);
        assertEquals("\uD83D\uDCAF", emoji);
    }
}
