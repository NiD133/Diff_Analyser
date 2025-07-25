package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttributeTest {

    /**
     * Tests HTML representation of an attribute with special characters in its value.
     */
    @Test
    public void testHtmlWithSpecialCharactersInValue() {
        Attribute attr = new Attribute("key", "value &");
        String expectedHtml = "key=\"value &amp;\"";
        assertEquals(expectedHtml, attr.html());
        assertEquals(attr.html(), attr.toString());
    }

    /**
     * Tests HTML representation of an attribute with special characters in its value.
     */
    @Test
    public void testHtmlWithLtAndGtInValue() {
        Attribute attr = new Attribute("key", "<value>");
        String expectedHtml = "key=\"&lt;value&gt;\"";
        assertEquals(expectedHtml, attr.html());
    }

    /**
     * Tests attribute creation with a supplementary character in the key and value.
     */
    @Test
    public void testAttributeWithSupplementaryCharacter() {
        String supplementaryChar = new String(Character.toChars(135361));
        Attribute attr = new Attribute(supplementaryChar, "A" + supplementaryChar + "B");
        String expectedHtml = supplementaryChar + "=\"A" + supplementaryChar + "B\"";
        assertEquals(expectedHtml, attr.html());
        assertEquals(attr.html(), attr.toString());
    }

    /**
     * Tests validation of attribute keys for emptiness.
     */
    @Test
    public void testValidationOfEmptyKeys() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute(" ", "Check"));
    }

    /**
     * Tests validation of attribute keys for emptiness via the setKey method.
     */
    @Test
    public void testValidationOfEmptyKeysViaSet() {
        assertThrows(IllegalArgumentException.class, () -> {
            Attribute attr = new Attribute("One", "Check");
            attr.setKey(" ");
        });
    }

    /**
     * Tests boolean attributes having empty string values.
     */
    @Test
    public void testBooleanAttributes() {
        Document doc = Jsoup.parse("<div hidden>");
        Attributes attributes = doc.body().child(0).attributes();
        assertEquals("", attributes.get("hidden"));

        Attribute first = attributes.iterator().next();
        assertEquals("hidden", first.getKey());
        assertEquals("", first.getValue());
        assertFalse(first.hasDeclaredValue());
        assertTrue(Attribute.isBooleanAttribute(first.getKey()));
    }

    /**
     * Tests setting attribute key and value on an orphan attribute.
     */
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

    /**
     * Tests setting attribute key and value after parent removal.
     */
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

    /**
     * Tests the hasValue method for attributes with and without declared values.
     */
    @Test
    public void testHasValue() {
        Attribute a1 = new Attribute("one", "");
        Attribute a2 = new Attribute("two", null);
        Attribute a3 = new Attribute("thr", "thr");

        assertTrue(a1.hasDeclaredValue());
        assertFalse(a2.hasDeclaredValue());
        assertTrue(a3.hasDeclaredValue());
    }

    /**
     * Tests setting attribute value to null.
     */
    @Test
    public void testSetValueToNull() {
        Attribute attr = new Attribute("one", "val");
        String oldVal = attr.setValue(null);
        assertEquals("one", attr.html());
        assertEquals("val", oldVal);

        oldVal = attr.setValue("foo");
        assertEquals("", oldVal); // string, not null
    }

    /**
     * Tests boolean attributes for case insensitivity.
     */
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

    /**
     * Tests the namespace of an orphan attribute.
     */
    @Test
    public void testOrphanNamespace() {
        Attribute attr = new Attribute("one", "two");
        assertEquals("", attr.namespace());
    }
}