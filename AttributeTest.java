package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the Attribute class which represents HTML/XML attributes.
 * Tests cover attribute creation, HTML rendering, validation, and special cases
 * like boolean attributes and supplementary characters.
 */
@DisplayName("Attribute Tests")
public class AttributeTest {

    @Nested
    @DisplayName("HTML Rendering Tests")
    class HtmlRenderingTests {
        
        @Test
        @DisplayName("Should escape ampersand in attribute value when rendering to HTML")
        void shouldEscapeAmpersandInAttributeValue() {
            // Given an attribute with an ampersand in its value
            Attribute attribute = new Attribute("key", "value &");
            
            // When rendered to HTML
            String htmlOutput = attribute.html();
            
            // Then the ampersand should be escaped
            assertEquals("key=\"value &amp;\"", htmlOutput);
            // And toString() should produce the same output as html()
            assertEquals(htmlOutput, attribute.toString());
        }

        @Test
        @DisplayName("Should escape less-than and greater-than symbols in attribute value")
        void shouldEscapeLessThanAndGreaterThanSymbols() {
            // Given an attribute with < and > in its value
            Attribute attribute = new Attribute("key", "<value>");
            
            // When rendered to HTML
            String htmlOutput = attribute.html();
            
            // Then the symbols should be escaped
            assertEquals("key=\"&lt;value&gt;\"", htmlOutput);
        }

        @Test
        @DisplayName("Should handle supplementary Unicode characters in both key and value")
        void shouldHandleSupplementaryUnicodeCharacters() {
            // Given a supplementary character (Unicode code point 135361)
            String supplementaryChar = new String(Character.toChars(135361));
            
            // When creating an attribute with supplementary characters
            Attribute attribute = new Attribute(supplementaryChar, "A" + supplementaryChar + "B");
            
            // Then the HTML output should preserve these characters
            String expectedHtml = supplementaryChar + "=\"A" + supplementaryChar + "B\"";
            assertEquals(expectedHtml, attribute.html());
            assertEquals(expectedHtml, attribute.toString());
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {
        
        @Test
        @DisplayName("Should throw exception when creating attribute with empty key")
        void shouldThrowExceptionForEmptyKeyInConstructor() {
            // Attempting to create an attribute with a blank key should fail
            assertThrows(IllegalArgumentException.class, 
                () -> new Attribute(" ", "Check"),
                "Attribute key cannot be empty or whitespace only");
        }

        @Test
        @DisplayName("Should throw exception when setting empty key on existing attribute")
        void shouldThrowExceptionWhenSettingEmptyKey() {
            // Given a valid attribute
            Attribute attribute = new Attribute("One", "Check");
            
            // When trying to set an empty key
            // Then it should throw an exception
            assertThrows(IllegalArgumentException.class, 
                () -> attribute.setKey(" "),
                "Cannot set attribute key to empty or whitespace only");
        }
    }

    @Nested
    @DisplayName("Boolean Attribute Tests")
    class BooleanAttributeTests {
        
        @Test
        @DisplayName("Should parse boolean attributes as empty string values")
        void shouldParseBooleanAttributesAsEmptyStringValues() {
            // Given HTML with a boolean attribute
            Document document = Jsoup.parse("<div hidden>");
            
            // When accessing the boolean attribute
            Attributes attributes = document.body().child(0).attributes();
            Attribute hiddenAttribute = attributes.iterator().next();
            
            // Then it should have an empty string value
            assertEquals("", attributes.get("hidden"));
            assertEquals("hidden", hiddenAttribute.getKey());
            assertEquals("", hiddenAttribute.getValue());
            assertFalse(hiddenAttribute.hasDeclaredValue());
            assertTrue(Attribute.isBooleanAttribute(hiddenAttribute.getKey()));
        }

        @Test
        @DisplayName("Should recognize boolean attributes regardless of case")
        void shouldRecognizeBooleanAttributesRegardlessOfCase() {
            // Boolean attribute recognition should be case-insensitive
            assertTrue(Attribute.isBooleanAttribute("required"));
            assertTrue(Attribute.isBooleanAttribute("REQUIRED"));
            assertTrue(Attribute.isBooleanAttribute("rEQUIREd"));
            assertFalse(Attribute.isBooleanAttribute("random string"));
            
            // Test in actual HTML parsing
            String html = "<a href=autofocus REQUIRED>One</a>";
            
            // Default parser normalizes to lowercase
            Document defaultDoc = Jsoup.parse(html);
            assertEquals("<a href=\"autofocus\" required>One</a>", 
                defaultDoc.selectFirst("a").outerHtml());
            
            // Case-preserving parser maintains original case
            Document casePreservingDoc = Jsoup.parse(html, 
                Parser.htmlParser().settings(ParseSettings.preserveCase));
            assertEquals("<a href=\"autofocus\" REQUIRED>One</a>", 
                casePreservingDoc.selectFirst("a").outerHtml());
        }
    }

    @Nested
    @DisplayName("Attribute Modification Tests")
    class AttributeModificationTests {
        
        @Test
        @DisplayName("Should allow key and value changes on orphan attribute")
        void shouldAllowModificationOfOrphanAttribute() {
            // Given an attribute not attached to any parent
            Attribute attribute = new Attribute("one", "two");
            
            // When modifying key and value
            attribute.setKey("three");
            String previousValue = attribute.setValue("four");
            
            // Then changes should be applied and previous value returned
            assertEquals("two", previousValue);
            assertEquals("three", attribute.getKey());
            assertEquals("four", attribute.getValue());
            assertNull(attribute.parent, "Orphan attribute should have no parent");
        }

        @Test
        @DisplayName("Should allow modifications after removal from parent attributes")
        void shouldAllowModificationsAfterParentRemoval() {
            // Given an attribute in a parent Attributes collection
            Attributes parentAttributes = new Attributes();
            parentAttributes.put("foo", "bar");
            Attribute attribute = parentAttributes.attribute("foo");
            assertNotNull(attribute);
            
            // When removed from parent
            parentAttributes.remove("foo");
            
            // Then the attribute should retain its values
            assertEquals("foo", attribute.getKey());
            assertEquals("bar", attribute.getValue());
            
            // And modifications should still work
            attribute.setKey("new");
            attribute.setValue("newer");
            assertEquals("new", attribute.getKey());
            assertEquals("newer", attribute.getValue());
        }
    }

    @Nested
    @DisplayName("Attribute Value Tests")
    class AttributeValueTests {
        
        @Test
        @DisplayName("Should correctly identify attributes with declared values")
        void shouldIdentifyAttributesWithDeclaredValues() {
            // Test various value states
            Attribute emptyStringValue = new Attribute("one", "");
            Attribute nullValue = new Attribute("two", null);
            Attribute normalValue = new Attribute("thr", "thr");
            
            // Empty string is considered a declared value
            assertTrue(emptyStringValue.hasDeclaredValue());
            // Null is not a declared value (boolean attribute)
            assertFalse(nullValue.hasDeclaredValue());
            // Normal values are declared
            assertTrue(normalValue.hasDeclaredValue());
        }

        @Test
        @DisplayName("Should handle setting value to null correctly")
        void shouldHandleNullValueSetting() {
            // Given an attribute with a value
            Attribute attribute = new Attribute("one", "val");
            
            // When setting value to null
            String previousValue = attribute.setValue(null);
            
            // Then it should render as boolean attribute
            assertEquals("one", attribute.html());
            assertEquals("val", previousValue);
            
            // When setting a new value after null
            previousValue = attribute.setValue("foo");
            
            // Then previous value should be empty string (not null)
            assertEquals("", previousValue, "getValue() returns empty string for null values");
        }
    }

    @Test
    @DisplayName("Should return empty namespace for orphan attribute")
    void shouldReturnEmptyNamespaceForOrphanAttribute() {
        // Given an attribute without namespace information
        Attribute attribute = new Attribute("one", "two");
        
        // When querying namespace
        String namespace = attribute.namespace();
        
        // Then it should return empty string
        assertEquals("", namespace);
    }
}