package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AttributeTest {

    @Test
    public void testHtmlEncoding() {
        Attribute attr = new Attribute("key", "value &");
        String expectedHtml = "key=\"value &amp;\"";
        assertEquals(expectedHtml, attr.html(), "HTML encoding of attribute value failed");
        assertEquals(attr.html(), attr.toString(), "HTML and toString representations should match");
    }

    @Test
    public void testHtmlWithSpecialCharacters() {
        Attribute attr = new Attribute("key", "<value>");
        String expectedHtml = "key=\"&lt;value&gt;\"";
        assertEquals(expectedHtml, attr.html(), "HTML encoding of special characters failed");
    }

    @Test
    public void testSupplementaryCharacterInKeyAndValue() {
        String supplementaryChar = new String(Character.toChars(135361));
        Attribute attr = new Attribute(supplementaryChar, "A" + supplementaryChar + "B");
        String expectedHtml = supplementaryChar + "=\"A" + supplementaryChar + "B\"";
        assertEquals(expectedHtml, attr.html(), "Handling of supplementary characters failed");
        assertEquals(attr.html(), attr.toString(), "HTML and toString representations should match");
    }

    @Test
    public void testKeyValidationNotEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute(" ", "Check"), "Empty key should throw IllegalArgumentException");
    }

    @Test
    public void testKeyValidationNotEmptyViaSet() {
        assertThrows(IllegalArgumentException.class, () -> {
            Attribute attr = new Attribute("One", "Check");
            attr.setKey(" ");
        }, "Setting an empty key should throw IllegalArgumentException");
    }

    @Test
    public void testBooleanAttributesHaveEmptyStringValues() {
        Document doc = Jsoup.parse("<div hidden>");
        Attributes attributes = doc.body().child(0).attributes();
        assertEquals("", attributes.get("hidden"), "Boolean attribute should have an empty string value");

        Attribute first = attributes.iterator().next();
        assertEquals("hidden", first.getKey(), "Attribute key mismatch");
        assertEquals("", first.getValue(), "Boolean attribute value should be empty");
        assertFalse(first.hasDeclaredValue(), "Boolean attribute should not have a declared value");
        assertTrue(Attribute.isBooleanAttribute(first.getKey()), "Attribute should be recognized as boolean");
    }

    @Test
    public void testSettersOnOrphanAttribute() {
        Attribute attr = new Attribute("one", "two");
        attr.setKey("three");
        String oldVal = attr.setValue("four");
        assertEquals("two", oldVal, "Old value mismatch");
        assertEquals("three", attr.getKey(), "Key mismatch after setting new key");
        assertEquals("four", attr.getValue(), "Value mismatch after setting new value");
        assertNull(attr.parent, "Orphan attribute should not have a parent");
    }

    @Test
    public void testSettersAfterParentRemoval() {
        Attributes attrs = new Attributes();
        attrs.put("foo", "bar");
        Attribute attr = attrs.attribute("foo");
        assertNotNull(attr, "Attribute should not be null");
        attrs.remove("foo");
        assertEquals("foo", attr.getKey(), "Key mismatch after parent removal");
        assertEquals("bar", attr.getValue(), "Value mismatch after parent removal");
        attr.setKey("new");
        attr.setValue("newer");
        assertEquals("new", attr.getKey(), "Key mismatch after setting new key");
        assertEquals("newer", attr.getValue(), "Value mismatch after setting new value");
    }

    @Test
    public void testHasValue() {
        Attribute a1 = new Attribute("one", "");
        Attribute a2 = new Attribute("two", null);
        Attribute a3 = new Attribute("thr", "thr");

        assertTrue(a1.hasDeclaredValue(), "Attribute with empty string should have a declared value");
        assertFalse(a2.hasDeclaredValue(), "Attribute with null value should not have a declared value");
        assertTrue(a3.hasDeclaredValue(), "Attribute with non-empty value should have a declared value");
    }

    @Test
    public void testSetValueToNull() {
        Attribute attr = new Attribute("one", "val");
        String oldVal = attr.setValue(null);
        assertEquals("one", attr.html(), "HTML representation mismatch after setting value to null");
        assertEquals("val", oldVal, "Old value mismatch after setting value to null");

        oldVal = attr.setValue("foo");
        assertEquals("", oldVal, "Old value should be empty string after setting new value");
    }

    @Test
    public void testBooleanAttributesCaseInsensitivity() {
        assertTrue(Attribute.isBooleanAttribute("required"), "Boolean attribute should be recognized");
        assertTrue(Attribute.isBooleanAttribute("REQUIRED"), "Boolean attribute should be case insensitive");
        assertTrue(Attribute.isBooleanAttribute("rEQUIREd"), "Boolean attribute should be case insensitive");
        assertFalse(Attribute.isBooleanAttribute("random string"), "Non-boolean attribute should not be recognized");

        String html = "<a href=autofocus REQUIRED>One</a>";
        Document doc = Jsoup.parse(html);
        assertEquals("<a href=\"autofocus\" required>One</a>", doc.selectFirst("a").outerHtml(), "HTML parsing mismatch");

        Document doc2 = Jsoup.parse(html, Parser.htmlParser().settings(ParseSettings.preserveCase));
        assertEquals("<a href=\"autofocus\" REQUIRED>One</a>", doc2.selectFirst("a").outerHtml(), "HTML parsing with preserved case mismatch");
    }

    @Test
    public void testOrphanNamespace() {
        Attribute attr = new Attribute("one", "two");
        assertEquals("", attr.namespace(), "Orphan attribute should have an empty namespace");
    }
}