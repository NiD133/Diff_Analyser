package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AttributeTest {

    // Test HTML representation of an attribute with special characters
    @Test
    public void testHtmlRepresentationWithSpecialCharacters() {
        Attribute attr = new Attribute("key", "value &");
        assertEquals("key=\"value &amp;\"", attr.html());
        assertEquals(attr.html(), attr.toString());
    }

    // Test HTML representation of an attribute with < and > in its value
    @Test
    public void testHtmlWithLtAndGtInValue() {
        Attribute attr = new Attribute("key", "<value>");
        assertEquals("key=\"&lt;value&gt;\"", attr.html());
    }

    // Test handling of supplementary characters in attribute key and value
    @Test
    public void testSupplementaryCharacterInKeyAndValue() {
        String supplementaryChar = new String(Character.toChars(135361));
        Attribute attr = new Attribute(supplementaryChar, "A" + supplementaryChar + "B");
        assertEquals(supplementaryChar + "=\"A" + supplementaryChar + "B\"", attr.html());
        assertEquals(attr.html(), attr.toString());
    }

    // Test validation of non-empty attribute keys
    @Test
    public void testKeyValidationNotEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute(" ", "Check"));
    }

    // Test validation of non-empty attribute keys via setKey method
    @Test
    public void testKeyValidationNotEmptyViaSet() {
        assertThrows(IllegalArgumentException.class, () -> {
            Attribute attr = new Attribute("One", "Check");
            attr.setKey(" ");
        });
    }

    // Test handling of boolean attributes with empty string values
    @Test
    public void testBooleanAttributesEmptyStringValues() {
        Document doc = Jsoup.parse("<div hidden>");
        Attributes attributes = doc.body().child(0).attributes();
        assertEquals("", attributes.get("hidden"));

        Attribute first = attributes.iterator().next();
        assertEquals("hidden", first.getKey());
        assertEquals("", first.getValue());
        assertFalse(first.hasDeclaredValue());
        assertTrue(Attribute.isBooleanAttribute(first.getKey()));
    }

    // Test setters on an orphan attribute
    @Test
    public void testSettersOnOrphanAttribute() {
        Attribute attr = new Attribute("one", "two");
        attr.setKey("three");
        String oldVal = attr.setValue("four");
        assertEquals("two", oldVal);
        assertEquals("three", attr.getKey());
        assertEquals("four", attr.getValue());
        assertNull(attr.parent);
    }

    // Test setters after removing parent attribute
    @Test
    public void testSettersAfterParentRemoval() {
        Attributes attrs = new Attributes();
        attrs.put("foo", "bar");
        Attribute attr = attrs.attribute("foo");
        assertNotNull(attr);
        attrs.remove("foo");
        assertEquals("foo", attr.getKey());
        assertEquals("bar", attr.getValue());
        attr.setKey("new");
        attr.setValue("newer");
        assertEquals("new", attr.getKey());
        assertEquals("newer", attr.getValue());
    }

    // Test if attributes have declared values
    @Test
    public void testHasDeclaredValue() {
        Attribute a1 = new Attribute("one", "");
        Attribute a2 = new Attribute("two", null);
        Attribute a3 = new Attribute("thr", "thr");

        assertTrue(a1.hasDeclaredValue());
        assertFalse(a2.hasDeclaredValue());
        assertTrue(a3.hasDeclaredValue());
    }

    // Test setting attribute value to null
    @Test
    public void testSetValueToNull() {
        Attribute attr = new Attribute("one", "val");
        String oldVal = attr.setValue(null);
        assertEquals("one", attr.html());
        assertEquals("val", oldVal);

        oldVal = attr.setValue("foo");
        assertEquals("", oldVal); // string, not null
    }

    // Test case insensitivity of boolean attributes
    @Test
    public void testBooleanAttributesCaseInsensitivity() {
        assertTrue(Attribute.isBooleanAttribute("required"));
        assertTrue(Attribute.isBooleanAttribute("REQUIRED"));
        assertTrue(Attribute.isBooleanAttribute("rEQUIREd"));
        assertFalse(Attribute.isBooleanAttribute("random string"));

        String html = "<a href=autofocus REQUIRED>One</a>";
        Document doc = Jsoup.parse(html);
        assertEquals("<a href=\"autofocus\" required>One</a>", doc.selectFirst("a").outerHtml());

        Document doc2 = Jsoup.parse(html, Parser.htmlParser().settings(ParseSettings.preserveCase));
        assertEquals("<a href=\"autofocus\" REQUIRED>One</a>", doc2.selectFirst("a").outerHtml());
    }

    // Test namespace handling for orphan attributes
    @Test
    public void testOrphanNamespace() {
        Attribute attr = new Attribute("one", "two");
        assertEquals("", attr.namespace());
    }
}