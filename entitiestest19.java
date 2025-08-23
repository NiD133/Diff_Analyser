package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntitiesTestTest19 {

    @Test
    public void escapeByClonedOutputSettings() {
        OutputSettings outputSettings = new OutputSettings();
        String text = "Hello &<> Å å π 新 there ¾ © »";
        OutputSettings clone1 = outputSettings.clone();
        OutputSettings clone2 = outputSettings.clone();
        String escaped1 = assertDoesNotThrow(() -> Entities.escape(text, clone1));
        String escaped2 = assertDoesNotThrow(() -> Entities.escape(text, clone2));
        assertEquals(escaped1, escaped2);
    }
}
