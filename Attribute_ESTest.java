package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A comprehensive test suite for the {@link Attribute} class.
 * This suite focuses on verifying the correctness of attribute creation, manipulation,
 * HTML rendering, and compliance with HTML/XML standards.
 */
public class AttributeTest {

    // --- Constructor and Factory Tests ---

    @Test
    void constructorTrimsAndValidatesKey() {
        Attribute attr = new Attribute("  key  ", "value");
        assertEquals("key", attr.getKey());
    }

    @Test
    void constructorAllowsNullValue() {
        Attribute attr = new Attribute("key", null);
        assertNotNull(attr);
        assertEquals("key", attr.getKey());
        assertEquals("", attr.getValue()); // getValue() returns empty string for null
    }

    @Test
    void constructorThrowsExceptionForNullOrEmptyKey() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute(null, "value"), "Null key should throw");
        assertThrows(IllegalArgumentException.class, () -> new Attribute("", "value"), "Empty key should throw");
        assertThrows(IllegalArgumentException.class, () -> new Attribute("  ", "value"), "Blank key should throw");
    }

    @Test
    void createFromEncodedCreatesAttribute() {
        Attribute attr = Attribute.createFromEncoded("key", "value&amp;");
        assertEquals("key", attr.getKey());
        assertEquals("value&", attr.getValue());
    }

    @Test
    void createFromEncodedThrowsExceptionForEmptyKey() {
        assertThrows(IllegalArgumentException.class, () -> Attribute.createFromEncoded("", "value"));
    }


    // --- Key and Value Management ---

    @Test
    void setKeyUpdatesKeyAndUpdatesParentAttributes() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("oldKey", "value");
        parent.put(attr);

        assertTrue(parent.hasKey("oldKey"));
        assertFalse(parent.hasKey("newKey"));

        // Act
        attr.setKey("newKey");

        // Assert
        assertFalse(parent.hasKey("oldKey"), "Old key should be removed from parent");
        assertTrue(parent.hasKey("newKey"), "New key should be added to parent");
        assertEquals("value", parent.get("newKey"));
        assertEquals("newKey", attr.getKey());
    }

    @Test
    void setKeyThrowsExceptionForNullOrEmptyKey() {
        Attribute attr = new Attribute("key", "value");
        assertThrows(IllegalArgumentException.class, () -> attr.setKey(null));
        assertThrows(IllegalArgumentException.class, () -> attr.setKey(""));
        assertThrows(IllegalArgumentException.class, () -> attr.setKey("  "));
    }

    @Test
    void setValueReturnsOldValue() {
        Attribute attr = new Attribute("key", "value1");
        assertEquals("value1", attr.setValue("value2"));
        assertEquals("value2", attr.getValue());
    }

    @Test
    void hasDeclaredValueIsTrueForNonNullValues() {
        assertTrue(new Attribute("key", "value").hasDeclaredValue());
        assertTrue(new Attribute("key", "").hasDeclaredValue());
        assertFalse(new Attribute("key", null).hasDeclaredValue());
    }


    // --- HTML and String Representation ---

    @Test
    void htmlRendersStandardAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.html());
    }

    @Test
    void toStringIsSameAsHtml() {
        Attribute attr = new Attribute("key", "value");
        assertEquals(attr.html(), attr.toString());
    }

    @Test
    void htmlRendersBooleanAttributeWithoutValueInHtmlMode() {
        Attribute attr = new Attribute("disabled", null);
        OutputSettings settings = new OutputSettings().syntax(OutputSettings.Syntax.html);
        StringBuilder sb = new StringBuilder();
        attr.html(sb, settings);
        assertEquals("disabled", sb.toString());
    }

    @Test
    void htmlRendersBooleanAttributeWithEmptyValueInXmlMode() {
        Attribute attr = new Attribute("disabled", null);
        OutputSettings settings = new OutputSettings().syntax(OutputSettings.Syntax.xml);
        StringBuilder sb = new StringBuilder();
        attr.html(sb, settings);
        assertEquals("disabled=\"\"", sb.toString());
    }

    @Test
    void htmlEscapesValueContainingQuotes() {
        Attribute attr = new Attribute("key", "it's a \"value\"");
        OutputSettings settings = new OutputSettings().escapeMode(EscapeMode.base);
        StringBuilder sb = new StringBuilder();
        attr.html(sb, settings);
        assertEquals("key=\"it's a &quot;value&quot;\"", sb.toString());
    }


    // --- Attribute Type Checks (Boolean, Data) ---

    @ParameterizedTest
    @ValueSource(strings = {"allowfullscreen", "async", "checked", "disabled", "readonly", "required"})
    void isBooleanAttributeReturnsTrueForBooleanAttributes(String key) {
        assertTrue(Attribute.isBooleanAttribute(key));
    }

    @ParameterizedTest
    @ValueSource(strings = {"allowfullscree", "data-async", "other", ""})
    void isBooleanAttributeReturnsFalseForNonBooleanAttributes(String key) {
        assertFalse(Attribute.isBooleanAttribute(key));
    }

    @ParameterizedTest
    @ValueSource(strings = {"data-foo", "data-a", "data-foo-bar"})
    void isDataAttributeReturnsTrueForDataAttributes(String key) {
        assertTrue(Attribute.isDataAttribute(key));
    }

    @ParameterizedTest
    @ValueSource(strings = {"data", "data-", "dat-foo", "foo"})
    void isDataAttributeReturnsFalseForNonDataAttributes(String key) {
        assertFalse(Attribute.isDataAttribute(key));
    }


    // --- Attribute Key Validation and Sanitization ---

    @ParameterizedTest(name = "[{index}] Input: ''{0}'', Expected: ''{1}'', Syntax: {2}")
    @CsvSource({
            // HTML Syntax
            "mykey, mykey, HTML",
            "my key, my_key, HTML",
            "my'key, my_key, HTML",
            "my\"key, my_key, HTML",
            "my=key, my_key, HTML",
            "my/key, my_key, HTML",
            // XML Syntax
            "mykey, mykey, XML",
            "my:key, my:key, XML",
            "my-key, my-key, XML",
            "my.key, my.key, XML",
            "my key, my_key, XML",
            "my>key, my_key, XML",
    })
    void getValidKeySanitizesKeyBasedOnSyntax(String input, String expected, OutputSettings.Syntax syntax) {
        assertEquals(expected, Attribute.getValidKey(input, syntax));
    }

    @Test
    void getValidKeyReturnsNullForEmptyOrWhitespaceKey() {
        assertNull(Attribute.getValidKey("", OutputSettings.Syntax.html));
        assertNull(Attribute.getValidKey("  ", OutputSettings.Syntax.html));
        assertNull(Attribute.getValidKey("", OutputSettings.Syntax.xml));
        assertNull(Attribute.getValidKey("  ", OutputSettings.Syntax.xml));
    }


    // --- Namespace Handling ---

    @Test
    void shouldCorrectlyParseNamespacedAttributes() {
        Attribute attr = new Attribute("xmlns:prefix", "uri");
        assertEquals("xmlns", attr.prefix());
        assertEquals("prefix", attr.localName());

        Attribute attr2 = new Attribute("prefix:name", "value");
        assertEquals("prefix", attr2.prefix());
        assertEquals("name", attr2.localName());
    }

    @Test
    void shouldHandleNonNamespacedAttributes() {
        Attribute attr = new Attribute("name", "value");
        assertEquals("", attr.prefix());
        assertEquals("name", attr.localName());
    }


    // --- Object Methods (equals, hashCode, clone) ---

    @Test
    void equalsAndHashCodeAreConsistent() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        Attribute attr3 = new Attribute("key", "differentValue");
        Attribute attr4 = new Attribute("differentKey", "value");
        Attribute attr5 = new Attribute("key", null);
        Attribute attr6 = new Attribute("key", null);

        assertEquals(attr1, attr2, "Attributes with same key and value should be equal");
        assertEquals(attr1.hashCode(), attr2.hashCode(), "Hash codes should be equal for equal objects");

        assertNotEquals(attr1, attr3, "Attributes with different values should not be equal");
        assertNotEquals(attr1, attr4, "Attributes with different keys should not be equal");
        assertNotEquals(attr1, attr5, "Attributes with different nullness of value should not be equal");

        assertEquals(attr5, attr6, "Attributes with same key and null value should be equal");
        assertEquals(attr5.hashCode(), attr6.hashCode());

        assertNotEquals(attr1, new Object());
        assertNotEquals(null, attr1);
    }

    @Test
    void cloneIsIndependentOfOriginal() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();

        assertEquals(attr, clonedAttr, "Cloned attribute should be equal to the original");
        assertNotSame(attr, clonedAttr, "Cloned attribute should not be the same instance");

        // Modify clone and verify original is unchanged
        clonedAttr.setValue("newValue");
        assertEquals("value", attr.getValue());
        assertEquals("newValue", clonedAttr.getValue());
    }
}