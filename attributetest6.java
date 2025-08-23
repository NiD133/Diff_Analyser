package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AttributeTestTest6 {

    @Test
    public void booleanAttributesAreEmptyStringValues() {
        Document doc = Jsoup.parse("<div hidden>");
        Attributes attributes = doc.body().child(0).attributes();
        assertEquals("", attributes.get("hidden"));
        Attribute first = attributes.iterator().next();
        assertEquals("hidden", first.getKey());
        assertEquals("", first.getValue());
        assertFalse(first.hasDeclaredValue());
        assertTrue(Attribute.isBooleanAttribute(first.getKey()));
    }
}
