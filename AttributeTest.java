package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class AttributeTest {

    @Test
    @DisplayName("Attribute.html() should return properly HTML-escaped string")
    void html() {
        Attribute attribute = new Attribute("key", "value &");
        String expectedHtml = "key=\"value &amp;\"";

        assertEquals(expectedHtml, attribute.html());
        assertEquals(attribute.html(), attribute.toString(), "html() and toString() should return the same value");
    }

    @Test
    @DisplayName("Attribute.html() should escape less than and greater than symbols")
    void htmlWithLtAndGtInValue() {
        Attribute attribute = new Attribute("key", "<value>");
        String expectedHtml = "key=\"&lt;value&gt;\"";

        assertEquals(expectedHtml, attribute.html());
    }

    @Test
    @DisplayName("Attribute should handle supplementary characters in key and value")
    void testWithSupplementaryCharacterInAttributeKeyAndValue() {
        String supplementaryChar = new String(Character.toChars(135361));
        Attribute attribute = new Attribute(supplementaryChar, "A" + supplementaryChar + "B");
        String expectedHtml = supplementaryChar + "=\"A" + supplementaryChar + "B\"";

        assertEquals(expectedHtml, attribute.html());
        assertEquals(attribute.html(), attribute.toString(), "html() and toString() should return the same value");
    }

    @Test
    @DisplayName("Constructor should throw IllegalArgumentException when key is empty")
    void validatesKeysNotEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute(" ", "Check"),
            "Should throw exception when key is empty");
    }

    @Test
    @DisplayName("setKey() should throw IllegalArgumentException when key is empty")
    void validatesKeysNotEmptyViaSet() {
        assertThrows(IllegalArgumentException.class, () -> {
            Attribute attribute = new Attribute("One", "Check");
            attribute.setKey(" ");
        }, "Should throw exception when key is set to empty");
    }

    @Test
    @DisplayName("Boolean attributes should have empty string values")
    void booleanAttributesAreEmptyStringValues() {
        Document document = Jsoup.parse("<div hidden>");
        Attributes attributes = document.body().child(0).attributes();

        assertEquals("", attributes.get("hidden"), "Boolean attribute should have empty string value");

        Attribute firstAttribute = attributes.iterator().next();
        assertEquals("hidden", firstAttribute.getKey());
        assertEquals("", firstAttribute.getValue());
        assertFalse(firstAttribute.hasDeclaredValue());
        assertTrue(Attribute.isBooleanAttribute(firstAttribute.getKey()));
    }

    @Test
    @DisplayName("Setters on orphan attribute should work correctly")
    void settersOnOrphanAttribute() {
        Attribute attribute = new Attribute("one", "two");

        attribute.setKey("three");
        String oldValue = attribute.setValue("four");

        assertEquals("two", oldValue, "Old value should be returned");
        assertEquals("three", attribute.getKey());
        assertEquals("four", attribute.getValue());
        assertNull(attribute.parent, "Parent should be null");
    }

    @Test
    @DisplayName("Setters after parent removal should work correctly")
    void settersAfterParentRemoval() {
        Attributes attributes = new Attributes();
        attributes.put("foo", "bar");
        Attribute attribute = attributes.attribute("foo");
        assertNotNull(attribute);

        attributes.remove("foo");

        assertEquals("foo", attribute.getKey());
        assertEquals("bar", attribute.getValue());

        attribute.setKey("new");
        attribute.setValue("newer");

        assertEquals("new", attribute.getKey());
        assertEquals("newer", attribute.getValue());
    }

    @Test
    @DisplayName("hasValue() should return correct value based on attribute value")
    void hasValue() {
        Attribute attribute1 = new Attribute("one", "");
        Attribute attribute2 = new Attribute("two", null);
        Attribute attribute3 = new Attribute("thr", "thr");

        assertTrue(attribute1.hasDeclaredValue(), "Empty string should be considered as having a value");
        assertFalse(attribute2.hasDeclaredValue(), "Null should be considered as not having a value");
        assertTrue(attribute3.hasDeclaredValue());
    }

    @Test
    @DisplayName("canSetValueToNull() should set value to null and return the old value")
    void canSetValueToNull() {
        Attribute attribute = new Attribute("one", "val");
        String oldValue = attribute.setValue(null);

        assertEquals("one", attribute.html());
        assertEquals("val", oldValue, "Old value should be returned");

        oldValue = attribute.setValue("foo");
        assertEquals("", oldValue, "Empty string should be returned for null values");
    }

    @Test
    @DisplayName("booleanAttributesAreNotCaseSensitive() - Boolean attributes should be case-insensitive")
    void booleanAttributesAreNotCaseSensitive() {
        assertTrue(Attribute.isBooleanAttribute("required"));
        assertTrue(Attribute.isBooleanAttribute("REQUIRED"));
        assertTrue(Attribute.isBooleanAttribute("rEQUIREd"));
        assertFalse(Attribute.isBooleanAttribute("random string"));

        String html = "<a href=autofocus REQUIRED>One</a>";
        Document document = Jsoup.parse(html);
        assertEquals("<a href=\"autofocus\" required>One</a>", document.selectFirst("a").outerHtml());

        Document document2 = Jsoup.parse(html, Parser.htmlParser().settings(ParseSettings.preserveCase));
        assertEquals("<a href=\"autofocus\" REQUIRED>One</a>", document2.selectFirst("a").outerHtml());
    }

    @Test
    @DisplayName("orphanNamespace() - Namespace should be empty for an orphan attribute")
    void orphanNamespace() {
        Attribute attribute = new Attribute("one", "two");
        assertEquals("", attribute.namespace());
    }
}