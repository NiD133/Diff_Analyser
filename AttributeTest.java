package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AttributeTest {

    // Tests for HTML representation
    @Test
    public void html_shouldEscapeSpecialCharacters() {
        Attribute attr = new Attribute("key", "value &");
        assertEquals("key=\"value &amp;\"", attr.html());
    }

    @Test
    public void html_shouldMatchToString() {
        Attribute attr = new Attribute("key", "value");
        assertEquals(attr.html(), attr.toString());
    }

    @Test
    public void html_shouldEscapeLtAndGtInValue() {
        Attribute attr = new Attribute("key", "<value>");
        assertEquals("key=\"&lt;value&gt;\"", attr.html());
    }

    @Test
    public void html_shouldHandleSupplementaryCharacters() {
        String supplementary = new String(Character.toChars(135361));
        Attribute attr = new Attribute(supplementary, "A" + supplementary + "B");
        String expected = supplementary + "=\"A" + supplementary + "B\"";
        assertEquals(expected, attr.html());
    }

    // Tests for attribute validation
    @Test
    public void shouldThrowException_whenKeyIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute(" ", "Check"));
    }

    @Test
    public void shouldThrowException_whenSettingKeyToEmpty() {
        Attribute attr = new Attribute("valid", "value");
        assertThrows(IllegalArgumentException.class, () -> attr.setKey(" "));
    }

    // Tests for boolean attributes
    @Test
    public void booleanAttribute_shouldHaveEmptyValue() {
        Document doc = Jsoup.parse("<div hidden>");
        Attributes attributes = doc.body().child(0).attributes();
        
        assertEquals("", attributes.get("hidden"));
        
        Attribute first = attributes.iterator().next();
        assertEquals("hidden", first.getKey());
        assertEquals("", first.getValue());
        assertFalse(first.hasDeclaredValue());
    }

    @Test
    public void shouldRecognizeBooleanAttributes() {
        assertTrue(Attribute.isBooleanAttribute("hidden"));
    }

    @Test
    public void booleanAttribute_checkShouldBeCaseInsensitive() {
        assertTrue(Attribute.isBooleanAttribute("REQUIRED"));
        assertTrue(Attribute.isBooleanAttribute("rEQUIREd"));
        assertFalse(Attribute.isBooleanAttribute("invalid"));
    }

    @Test
    public void booleanAttribute_shouldNormalizeCaseInOutput() {
        String html = "<a href=autofocus REQUIRED>One</a>";
        Document doc = Jsoup.parse(html);
        assertEquals("<a href=\"autofocus\" required>One</a>", doc.selectFirst("a").outerHtml());
    }

    @Test
    public void booleanAttribute_shouldPreserveCase_whenConfigured() {
        String html = "<a href=autofocus REQUIRED>One</a>";
        Document doc = Jsoup.parse(html, Parser.htmlParser().settings(ParseSettings.preserveCase));
        assertEquals("<a href=\"autofocus\" REQUIRED>One</a>", doc.selectFirst("a").outerHtml());
    }

    // Tests for attribute setters
    @Test
    public void setters_shouldWorkOnOrphanAttribute() {
        Attribute attr = new Attribute("one", "two");
        
        attr.setKey("three");
        String oldValue = attr.setValue("four");
        
        assertEquals("two", oldValue);
        assertEquals("three", attr.getKey());
        assertEquals("four", attr.getValue());
        assertNull(attr.parent);
    }

    @Test
    public void setters_shouldWorkAfterParentRemoval() {
        Attributes attrs = new Attributes();
        attrs.put("foo", "bar");
        Attribute attr = attrs.attribute("foo");
        
        attrs.remove("foo");
        attr.setKey("new");
        attr.setValue("newer");
        
        assertEquals("new", attr.getKey());
        assertEquals("newer", attr.getValue());
    }

    @Test
    public void hasDeclaredValue_shouldReturnCorrectState() {
        Attribute withEmptyValue = new Attribute("one", "");
        Attribute noValue = new Attribute("two", null);
        Attribute withValue = new Attribute("three", "value");
        
        assertTrue(withEmptyValue.hasDeclaredValue());
        assertFalse(noValue.hasDeclaredValue());
        assertTrue(withValue.hasDeclaredValue());
    }

    @Test
    public void setValue_shouldAllowNullForBooleanAttributes() {
        Attribute attr = new Attribute("one", "val");
        
        String oldVal = attr.setValue(null);
        assertEquals("one", attr.html());  // Should become boolean attribute
        assertEquals("val", oldVal);
        
        oldVal = attr.setValue("foo");
        assertEquals("", oldVal);  // Returns empty string when changing from boolean state
    }

    // Tests for namespace behavior
    @Test
    public void namespace_shouldBeEmptyForOrphanAttributes() {
        Attribute attr = new Attribute("one", "two");
        assertEquals("", attr.namespace());
    }
}