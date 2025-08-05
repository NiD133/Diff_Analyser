package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.nodes.Document.OutputSettings.Syntax;

/**
 * Test suite for the Attribute class functionality including:
 * - Attribute creation and validation
 * - HTML/XML output formatting
 * - Boolean attribute handling
 * - Data attribute detection
 * - Key validation and sanitization
 */
public class AttributeTest {

    // ========== Boolean Attribute Tests ==========
    
    @Test
    public void shouldRecognizeAllowFullScreenAsBooleanAttribute() {
        boolean isBooleanAttr = Attribute.isBooleanAttribute("allowfullscreen");
        assertTrue("allowfullscreen should be recognized as boolean attribute", isBooleanAttr);
    }

    @Test
    public void shouldNotRecognizeEmptyStringAsBooleanAttribute() {
        boolean isBooleanAttr = Attribute.isBooleanAttribute("");
        assertFalse("Empty string should not be boolean attribute", isBooleanAttr);
    }

    @Test
    public void shouldCollapseKnownBooleanAttributeWithSameValue() {
        Attribute ismapAttr = Attribute.createFromEncoded("ismap", "test");
        ismapAttr.setValue("ismap"); // Set value same as key
        
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean shouldCollapse = ismapAttr.shouldCollapseAttribute(outputSettings);
        
        assertTrue("Boolean attribute with same key/value should collapse", shouldCollapse);
    }

    @Test
    public void shouldCollapseAttributeWithNullValue() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean shouldCollapse = Attribute.shouldCollapseAttribute("test", null, outputSettings);
        
        assertTrue("Attribute with null value should collapse", shouldCollapse);
    }

    // ========== Data Attribute Tests ==========
    
    @Test
    public void shouldRecognizeValidDataAttribute() {
        boolean isDataAttr = Attribute.isDataAttribute("data-test-value");
        assertTrue("data-test-value should be recognized as data attribute", isDataAttr);
    }

    @Test
    public void shouldNotRecognizeDataPrefixOnlyAsDataAttribute() {
        boolean isDataAttr = Attribute.isDataAttribute("data-");
        assertFalse("'data-' without suffix should not be data attribute", isDataAttr);
    }

    @Test
    public void shouldDetectDataAttributeOnInstance() {
        Attributes attributes = new Attributes();
        Attribute dataAttr = new Attribute("data-user-id", "123", attributes);
        
        boolean isDataAttr = dataAttr.isDataAttribute();
        assertTrue("Instance should detect itself as data attribute", isDataAttr);
    }

    // ========== Key Validation Tests ==========
    
    @Test
    public void shouldReturnValidXmlKeyUnchanged() {
        String validKey = Attribute.getValidKey("validKey", Syntax.xml);
        assertEquals("Valid XML key should remain unchanged", "validKey", validKey);
    }

    @Test
    public void shouldSanitizeInvalidXmlKeyCharacters() {
        String sanitizedKey = Attribute.getValidKey("invalid>key ", Syntax.xml);
        assertEquals("Invalid XML characters should be replaced with underscore", "invalid_key_", sanitizedKey);
    }

    @Test
    public void shouldReturnValidHtmlKeyUnchanged() {
        String validKey = Attribute.getValidKey("validKey123", Syntax.html);
        assertEquals("Valid HTML key should remain unchanged", "validKey123", validKey);
    }

    @Test
    public void shouldSanitizeInvalidHtmlKeyCharacters() {
        String sanitizedKey = Attribute.getValidKey("key=with'quotes", Syntax.html);
        assertEquals("Invalid HTML characters should be replaced", "key_with_quotes", sanitizedKey);
    }

    @Test
    public void shouldReturnNullForEmptyKeyInXml() {
        String result = Attribute.getValidKey("", Syntax.xml);
        assertNull("Empty key should return null in XML", result);
    }

    @Test
    public void shouldReturnNullForEmptyKeyInHtml() {
        String result = Attribute.getValidKey("", Syntax.html);
        assertNull("Empty key should return null in HTML", result);
    }

    // ========== HTML Output Tests ==========
    
    @Test
    public void shouldGenerateCorrectHtmlOutput() {
        Attribute testAttr = Attribute.createFromEncoded("class", "test-value");
        StringBuilder output = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        
        QuietAppendable appendable = QuietAppendable.wrap(output);
        testAttr.html(appendable, settings);
        
        assertEquals("Should generate proper HTML attribute format", "class=\"test-value\"", output.toString());
    }

    @Test
    public void shouldEscapeQuotesInAttributeValue() {
        Document.OutputSettings settings = new Document.OutputSettings();
        StringBuilder output = new StringBuilder();
        
        Attribute.html("title", "value with \"quotes\"", QuietAppendable.wrap(output), settings);
        
        assertTrue("Quotes should be escaped in output", output.toString().contains("&quot;"));
    }

    @Test
    public void shouldHandleNullValueInHtmlOutput() {
        Document.OutputSettings settings = new Document.OutputSettings();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, Charset.defaultCharset());
        QuietAppendable appendable = QuietAppendable.wrap(writer);
        
        // Should not throw exception with null value
        Attribute.htmlNoValidate(null, null, appendable, settings);
        assertTrue("Should handle null values gracefully", settings.prettyPrint());
    }

    // ========== Attribute Creation and Basic Operations ==========
    
    @Test
    public void shouldCreateAttributeFromEncodedValues() {
        Attribute attr = Attribute.createFromEncoded("data-test", "encoded&value");
        
        assertEquals("Key should be set correctly", "data-test", attr.getKey());
        assertEquals("Value should be decoded", "encoded&value", attr.getValue());
    }

    @Test
    public void shouldHandleNullValueAsEmptyString() {
        Attributes attributes = new Attributes();
        Attribute attr = new Attribute("test-key", null, attributes);
        
        String value = attr.getValue();
        assertEquals("Null value should return empty string", "", value);
    }

    @Test
    public void shouldAllowValueUpdate() {
        Attribute attr = new Attribute("test", "initial");
        attr.setValue("updated");
        
        assertEquals("Value should be updated", "updated", attr.getValue());
    }

    @Test
    public void shouldDetectDeclaredValue() {
        Attribute attrWithValue = new Attribute("test", "value");
        assertTrue("Should detect declared value", attrWithValue.hasDeclaredValue());
        
        Attribute attrWithNull = Attribute.createFromEncoded("test", "value");
        attrWithNull.setValue(null);
        assertFalse("Should detect no declared value for null", attrWithNull.hasDeclaredValue());
    }

    // ========== Namespace and Prefix Tests ==========
    
    @Test
    public void shouldReturnEmptyNamespaceForSimpleAttribute() {
        Attributes attributes = new Attributes();
        Attribute attr = new Attribute("simple", "value", attributes);
        
        String namespace = attr.namespace();
        assertEquals("Simple attribute should have empty namespace", "", namespace);
    }

    @Test
    public void shouldReturnEmptyPrefixForSimpleAttribute() {
        Attribute attr = new Attribute("simple", "value");
        
        String prefix = attr.prefix();
        assertEquals("Simple attribute should have empty prefix", "", prefix);
    }

    @Test
    public void shouldExtractPrefixFromNamespacedAttribute() {
        Attribute attr = new Attribute("xml:lang", "en");
        
        String prefix = attr.prefix();
        assertEquals("Should extract prefix from namespaced attribute", "xml", prefix);
    }

    @Test
    public void shouldReturnLocalNameWithoutPrefix() {
        Attribute attr = new Attribute("xml:lang", "en");
        
        String localName = attr.localName();
        assertEquals("Should return local name without prefix", "lang", localName);
    }

    @Test
    public void shouldReturnEmptyLocalNameForInvalidKey() {
        Attribute attr = new Attribute("invalid\u0000key:", "value");
        
        String localName = attr.localName();
        assertEquals("Invalid key should result in empty local name", "", localName);
    }

    // ========== Equality and Cloning Tests ==========
    
    @Test
    public void shouldBeEqualToItself() {
        Attribute attr = Attribute.createFromEncoded("test", "value");
        
        boolean isEqual = attr.equals(attr);
        assertTrue("Attribute should equal itself", isEqual);
    }

    @Test
    public void shouldNotEqualNull() {
        Attribute attr = Attribute.createFromEncoded("test", "value");
        
        boolean isEqual = attr.equals(null);
        assertFalse("Attribute should not equal null", isEqual);
    }

    @Test
    public void shouldNotEqualDifferentType() {
        Attribute attr = new Attribute("test", "value");
        
        boolean isEqual = attr.equals("test");
        assertFalse("Attribute should not equal string", isEqual);
    }

    @Test
    public void shouldNotEqualDifferentKey() {
        Attribute attr1 = Attribute.createFromEncoded("key1", "value");
        Attribute attr2 = new Attribute("key2", "value");
        
        boolean isEqual = attr2.equals(attr1);
        assertFalse("Attributes with different keys should not be equal", isEqual);
    }

    @Test
    public void shouldNotEqualDifferentValue() {
        Attribute attr1 = Attribute.createFromEncoded("key", "value1");
        Attribute attr2 = attr1.clone();
        attr1.setValue("value2");
        
        boolean isEqual = attr1.equals(attr2);
        assertFalse("Attributes with different values should not be equal", isEqual);
    }

    @Test
    public void shouldEqualClonedAttribute() {
        Attribute original = Attribute.createFromEncoded("test", "value");
        Attribute cloned = original.clone();
        
        boolean isEqual = cloned.equals(original);
        assertTrue("Cloned attribute should equal original", isEqual);
    }

    // ========== String Representation Tests ==========
    
    @Test
    public void shouldGenerateCorrectStringRepresentation() {
        Attributes attributes = new Attributes();
        Attribute attr = new Attribute("class", "", attributes);
        
        String stringRep = attr.toString();
        assertEquals("Should generate correct string representation", "class=\"\"", stringRep);
    }

    @Test
    public void shouldGenerateCorrectHtmlString() {
        Attribute attr = Attribute.createFromEncoded("title", "test");
        
        String html = attr.html();
        assertEquals("Should generate correct HTML string", "title=\"test\"", html);
    }

    @Test
    public void shouldEscapeSpecialCharactersInHtml() {
        Attribute attr = new Attribute("title", "test\"value");
        StringWriter writer = new StringWriter();
        Document.OutputSettings settings = new Document.OutputSettings();
        
        attr.html(writer, settings);
        assertEquals("Should escape quotes in HTML output", "title=\"test&quot;value\"", writer.toString());
    }

    // ========== Error Handling Tests ==========
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectEmptyKey() {
        new Attribute("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNullKey() {
        Attributes attributes = new Attributes();
        new Attribute(null, "value", attributes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectEmptyKeyInCreateFromEncoded() {
        Attribute.createFromEncoded("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectEmptyKeyInSetKey() {
        Attribute attr = new Attribute("initial", "value");
        attr.setKey("");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnNullKeyInCreateFromEncoded() {
        Attribute.createFromEncoded(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnNullKeyInIsDataAttribute() {
        Attribute.isDataAttribute(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnNullKeyInGetValidKey() {
        Attribute.getValidKey(null, Syntax.html);
    }

    // ========== Edge Cases and Complex Scenarios ==========
    
    @Test
    public void shouldHandleComplexKeyValidation() {
        // Test various edge cases in key validation
        assertEquals("Should handle mixed invalid chars", "test_key_", 
                    Attribute.getValidKey("test>key ", Syntax.xml));
        assertEquals("Should handle quotes and equals", "test_key_", 
                    Attribute.getValidKey("test'key=", Syntax.html));
        assertEquals("Should handle newlines", "_", 
                    Attribute.getValidKey("\r\n\r\n", Syntax.html));
    }

    @Test
    public void shouldHandleXmlVsHtmlSyntaxDifferences() {
        Document.OutputSettings xmlSettings = new Document.OutputSettings().syntax(Syntax.xml);
        boolean shouldCollapse = Attribute.shouldCollapseAttribute("", "value", xmlSettings);
        
        assertFalse("XML syntax should not collapse non-empty values", shouldCollapse);
    }

    @Test
    public void shouldMaintainAttributeParentRelationship() {
        Attributes parentAttributes = new Attributes();
        parentAttributes.add("existing", "value");
        Attribute attr = new Attribute("test", "value", parentAttributes);
        
        // Test key change updates parent
        attr.setKey("newKey");
        assertEquals("Key should be updated", "newKey", attr.getKey());
        
        // Test value change
        String oldValue = attr.setValue("newValue");
        assertEquals("Should return old value", "value", oldValue);
        assertEquals("Value should be updated", "newValue", attr.getValue());
    }
}