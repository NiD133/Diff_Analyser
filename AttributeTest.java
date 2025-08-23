package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Attribute with a focus on readability and intent.
 * Organized by behavior area with descriptive names and comments.
 */
@DisplayName("Attribute")
public class AttributeTest {

    // A representative supplementary Unicode code point (outside BMP)
    private static final int SUPPLEMENTARY_CODE_POINT = 135361;
    private static final String SUPPLEMENTARY_CHAR = new String(Character.toChars(SUPPLEMENTARY_CODE_POINT));

    @Nested
    @DisplayName("HTML rendering")
    class HtmlRendering {

        @Test
        @DisplayName("escapes ampersand in value")
        void escapesAmpersandInValue() {
            // Arrange
            Attribute attr = new Attribute("key", "value &");

            // Act
            String html = attr.html();

            // Assert
            assertEquals("key=\"value &amp;\"", html);
            assertEquals(html, attr.toString(), "toString() should delegate to html()");
        }

        @Test
        @DisplayName("escapes < and > in value")
        void escapesLtAndGtInValue() {
            Attribute attr = new Attribute("key", "<value>");
            assertEquals("key=\"&lt;value&gt;\"", attr.html());
        }

        @Test
        @DisplayName("supports supplementary characters in key and value")
        void supportsSupplementaryCharacterInKeyAndValue() {
            // Arrange
            String key = SUPPLEMENTARY_CHAR;
            String value = "A" + SUPPLEMENTARY_CHAR + "B";
            Attribute attr = new Attribute(key, value);

            // Assert
            assertEquals(key + "=\"A" + SUPPLEMENTARY_CHAR + "B\"", attr.html());
            assertEquals(attr.html(), attr.toString(), "toString() should delegate to html()");
        }
    }

    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("rejects blank keys at construction")
        void rejectsBlankKeysAtConstruction() {
            assertThrows(IllegalArgumentException.class, () -> new Attribute(" ", "Check"));
        }

        @Test
        @DisplayName("rejects setting blank keys after construction")
        void rejectsBlankKeysViaSetter() {
            assertThrows(IllegalArgumentException.class, () -> {
                Attribute attr = new Attribute("One", "Check");
                attr.setKey(" ");
            });
        }
    }

    @Nested
    @DisplayName("Boolean attributes")
    class BooleanAttributes {

        @Test
        @DisplayName("are represented as empty string values after parsing")
        void booleanAttributesAreEmptyStringValues() {
            // Arrange
            Document doc = Jsoup.parse("<div hidden>");
            Attributes attributes = doc.body().child(0).attributes();

            // Assert
            assertEquals("", attributes.get("hidden"), "Boolean attributes should read as empty string");

            Attribute first = attributes.iterator().next();
            assertEquals("hidden", first.getKey());
            assertEquals("", first.getValue());
            assertFalse(first.hasDeclaredValue(), "Boolean attribute has no declared value");
            assertTrue(Attribute.isBooleanAttribute(first.getKey()));
        }

        @Test
        @DisplayName("are case-insensitive and preserve original case with preserveCase setting")
        void booleanAttributesAreNotCaseSensitive() {
            // API-level checks
            assertTrue(Attribute.isBooleanAttribute("required"));
            assertTrue(Attribute.isBooleanAttribute("REQUIRED"));
            assertTrue(Attribute.isBooleanAttribute("rEQUIREd"));
            assertFalse(Attribute.isBooleanAttribute("random string"));

            // Rendering with default settings (normalized to lowercase)
            String html = "<a href=autofocus REQUIRED>One</a>";
            Document defaultDoc = Jsoup.parse(html);
            assertEquals("<a href=\"autofocus\" required>One</a>", defaultDoc.selectFirst("a").outerHtml());

            // Rendering with preserved case
            Document preservedDoc = Jsoup.parse(html, Parser.htmlParser().settings(ParseSettings.preserveCase));
            assertEquals("<a href=\"autofocus\" REQUIRED>One</a>", preservedDoc.selectFirst("a").outerHtml());
        }
    }

    @Nested
    @DisplayName("Mutation")
    class Mutation {

        @Test
        @DisplayName("can change key and value when not attached to a parent")
        void settersOnOrphanAttribute() {
            // Arrange
            Attribute attr = new Attribute("one", "two");

            // Act
            attr.setKey("three");
            String previous = attr.setValue("four");

            // Assert
            assertEquals("two", previous);
            assertEquals("three", attr.getKey());
            assertEquals("four", attr.getValue());
            assertNull(attr.parent, "orphan Attribute should not have a parent reference");
        }

        @Test
        @DisplayName("retain independent state after removal from parent")
        void settersAfterParentRemoval() {
            // Arrange
            Attributes attrs = new Attributes();
            attrs.put("foo", "bar");
            Attribute attr = attrs.attribute("foo");
            assertNotNull(attr, "Attribute should exist in parent before removal");

            // Act: remove from parent to orphan the attribute reference
            attrs.remove("foo");

            // Assert: still has original values
            assertEquals("foo", attr.getKey());
            assertEquals("bar", attr.getValue());

            // Act: mutate the now-orphaned attribute
            attr.setKey("new");
            attr.setValue("newer");

            // Assert
            assertEquals("new", attr.getKey());
            assertEquals("newer", attr.getValue());
        }
    }

    @Nested
    @DisplayName("Value presence and null semantics")
    class ValuePresence {

        @Test
        @DisplayName("hasDeclaredValue reflects presence of an explicit value")
        void hasDeclaredValue() {
            Attribute emptyString = new Attribute("one", "");
            Attribute nullValue = new Attribute("two", null);
            Attribute nonEmpty = new Attribute("thr", "thr");

            assertTrue(emptyString.hasDeclaredValue(), "Empty string is a declared value");
            assertFalse(nullValue.hasDeclaredValue(), "Null denotes a boolean (no declared value)");
            assertTrue(nonEmpty.hasDeclaredValue());
        }

        @Test
        @DisplayName("setValue accepts null (boolean attribute) and returns previous value")
        void canSetValueToNull() {
            // Arrange
            Attribute attr = new Attribute("one", "val");

            // Act & Assert: set to null (enabling boolean attribute)
            String oldVal = attr.setValue(null);
            assertEquals("one", attr.html(), "With null value, boolean attribute should collapse to just key");
            assertEquals("val", oldVal);

            // Act & Assert: set to non-null; prior value should be empty string (not null)
            oldVal = attr.setValue("foo");
            assertEquals("", oldVal, "Previous value is the empty string sentinel, not null");
        }
    }

    @Nested
    @DisplayName("Namespace/prefix/localName")
    class NamespaceInfo {

        @Test
        @DisplayName("namespace is empty for non-prefixed attributes")
        void orphanNamespace() {
            Attribute attr = new Attribute("one", "two");
            assertEquals("", attr.namespace());
        }
    }
}