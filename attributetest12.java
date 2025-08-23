package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AttributeTestTest12 {

    @Test
    void orphanNamespace() {
        Attribute attr = new Attribute("one", "two");
        assertEquals("", attr.namespace());
    }
}
