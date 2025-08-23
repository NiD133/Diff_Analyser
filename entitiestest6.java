package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntitiesTestTest6 {

    @Test
    public void getByName() {
        assertEquals("≫⃒", Entities.getByName("nGt"));
        assertEquals("fj", Entities.getByName("fjlig"));
        assertEquals("≫", Entities.getByName("gg"));
        assertEquals("©", Entities.getByName("copy"));
    }
}
