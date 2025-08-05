package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Attribute} class, focusing on its construction, validation,
 * HTML representation, and behavior as a boolean or valued attribute.
 */
class AttributeTest {

    @Nested
    @DisplayName("HTML Output")
    class HtmlOutput {

        @Test
        @DisplayName("should escape ampersands in value")
        void shouldEscapeAmpersandInValue() {
            // Given
            Attribute attr = new Attribute("key", "value &");

            // When
            String html = attr.html();

            // Then
            assertEquals("key=\"value &amp;\"", html);
            assertEquals(html, attr.toString());
        }



        @Test
        @DisplayName("should escape angle brackets in value")
        void shouldEscapeAngleBracketsInValue() {
            // Given
            Attribute attr = new Attribute("key", "<value>");

            // When
            String html = attr.html();

            // Then
            assertEquals("key=\"&lt;value&gt;\"", html);
        }

        @Test
        @DisplayName("should correctly render supplementary characters in key and value")
        void shouldCorrectlyRenderSupplementaryCharacters() {
            // Given a supplementary character (U+210C1)
            String supplementaryChar = new String(Character.toChars(135361));
            Attribute attr = new Attribute(supplementaryChar, "A" + supplementaryChar + "B");

            // When
            String html = attr.html();

            // Then
            assertEquals(supplementaryChar + "=\"A" + supplementaryChar + "B\"", html);
            assertEquals(html, attr.toString());
        }
    }

    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("constructor should throw an exception for a blank key")
        void constructorShouldThrowExceptionForBlankKey() {
            // Expect an IllegalArgumentException when creating an attribute with a key containing only whitespace.
            assertThrows(IllegalArgumentException.class, () -> new Attribute(" ", "Check"));
        }

        @Test
        @DisplayName("setKey should throw an exception for a blank key")
        void setKeyShouldThrowExceptionForBlankKey() {
            // Given
            Attribute attr = new Attribute("One", "Check");

            // Expect an IllegalArgumentException when setting the key to a value containing only whitespace.
            assertThrows(IllegalArgumentException.class, () -> attr.setKey(" "));
        }
    }

    @Nested
    @DisplayName("Value Handling")
    class ValueHandling {

        @Test
        @DisplayName("hasDeclaredValue should be correct for null, empty, and non-empty values")
        void hasDeclaredValueIsCorrectForVariousValueStates() {
            // Given attributes with different value states
            Attribute attrWithEmptyValue = new Attribute("one", "");
            Attribute attrWithNullValue = new Attribute("two", null); // Represents a boolean attribute
            Attribute attrWithRealValue = new Attribute("thr", "thr");

            // Then
            assertTrue(attrWithEmptyValue.hasDeclaredValue());
            assertFalse(attrWithNullValue.hasDeclaredValue());
            assertTrue(attrWithRealValue.hasDeclaredValue());
        }

        @Test
        @DisplayName("setting value to null should convert it to a boolean attribute")
        void settingValueToNullMakesAttributeBoolean() {
            // Given an attribute with a value
            Attribute attr = new Attribute("one", "val");

            // When setting the value to null
            String oldVal = attr.setValue(null);

            // Then it should behave like a boolean attribute
            assertEquals("one", attr.html());
            assertEquals("val", oldVal);
            assertFalse(attr.hasDeclaredValue());

            // When setting a value again
            String previousVal = attr.setValue("foo");

            // Then the previous value should be an empty string (as it was a boolean attribute)
            assertEquals("", previousVal);
            assertTrue(attr.hasDeclaredValue());
        }
    }

    @Nested
    @DisplayName("Lifecycle and Mutation")
    class LifecycleAndMutation {

        @Test
        @DisplayName("setters should work correctly on a detached attribute")
        void settersShouldWorkOnDetachedAttribute() {
            // Given an attribute that has no parent
            Attribute attr = new Attribute("one", "two");

            // When
            attr.setKey("three");
            String oldVal = attr.setValue("four");

            // Then
            assertEquals("two", oldVal);
            assertEquals("three", attr.getKey());
            assertEquals("four", attr.getValue());
            assertNull(attr.parent);
        }

        @Test
        @DisplayName("setters should work correctly after an attribute is removed from its parent")
        void settersShouldWorkAfterRemovingAttributeFromParent() {
            // Given an attribute that is part of an Attributes collection
            Attributes attrs = new Attributes();
            attrs.put("foo", "bar");
            Attribute attr = attrs.attribute("foo");
            assertNotNull(attr);

            // When the attribute is removed from its parent
            attrs.remove("foo");
            assertEquals("foo", attr.getKey()); // State is retained
            assertEquals("bar", attr.getValue());

            // And then mutated
            attr.setKey("new");
            attr.setValue("newer");

            // Then the mutations should be applied
            assertEquals("new", attr.getKey());
            assertEquals("newer", attr.getValue());
        }
    }

    @Nested
    @DisplayName("Boolean Attributes")
    class BooleanAttributes {

        @Test
        @DisplayName("should be parsed with an empty string value")
        void parsedBooleanAttributeShouldHaveEmptyValue() {
            // Given HTML with a boolean attribute
            Document doc = Jsoup.parse("<div hidden>");
            Attributes attributes = doc.body().child(0).attributes();
            Attribute hiddenAttr = attributes.iterator().next();

            // Then the attribute value should be an empty string
            assertEquals("", attributes.get("hidden"));
            assertEquals("hidden", hiddenAttr.getKey());
            assertEquals("", hiddenAttr.getValue());

            // And it should be identified as a boolean attribute without a declared value
            assertFalse(hiddenAttr.hasDeclaredValue());
            assertTrue(Attribute.isBooleanAttribute(hiddenAttr.getKey()));
        }

        @Test
        @DisplayName("isBooleanAttribute check should be case-insensitive")
        void isBooleanAttributeShouldBeCaseInsensitive() {
            assertTrue(Attribute.isBooleanAttribute("required"));
            assertTrue(Attribute.isBooleanAttribute("REQUIRED"));
            assertTrue(Attribute.isBooleanAttribute("rEQUIREd"));
            assertFalse(Attribute.isBooleanAttribute("random string"));
        }

        @Test
        @DisplayName("parser should normalize boolean attribute case by default")
        void parserShouldNormalizeBooleanAttributeCaseByDefault() {
            // Given HTML with a boolean attribute in uppercase
            String html = "<a href=autofocus REQUIRED>One</a>";

            // When parsed with default settings
            Document doc = Jsoup.parse(html);

            // Then the boolean attribute key should be lower-cased
            assertEquals("<a href=\"autofocus\" required>One</a>", doc.selectFirst("a").outerHtml());
        }

        @Test
        @DisplayName("parser should preserve boolean attribute case when configured")
        void parserShouldPreserveBooleanAttributeCaseWhenConfigured() {
            // Given HTML with a boolean attribute in uppercase
            String html = "<a href=autofocus REQUIRED>One</a>";
            Parser parser = Parser.htmlParser().settings(ParseSettings.preserveCase);

            // When parsed with case preservation enabled
            Document doc = Jsoup.parse(html, parser);

            // Then the boolean attribute key case should be preserved
            assertEquals("<a href=\"autofocus\" REQUIRED>One</a>", doc.selectFirst("a").outerHtml());
        }
    }

    @Nested
    @DisplayName("Namespace")
    class Namespace {
        @Test
        @DisplayName("a newly created attribute should have an empty namespace")
        void newAttributeShouldHaveEmptyNamespace() {
            // Given
            Attribute attr = new Attribute("one", "two");

            // Then
            assertEquals("", attr.namespace());
        }
    }
}