package org.apache.ibatis.parsing;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Supplier;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Node;

/**
 * Test suite for XNode class functionality.
 * Tests XML node parsing, attribute retrieval, and type conversions.
 */
public class XNodeTest {

    // Test Data Setup Helpers
    private XNode createXNodeWithName(String nodeName) {
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        Properties properties = new Properties();
        return new XNode(null, node, properties);
    }

    private XNode createXNodeWithAttribute(String attributeName, String attributeValue) {
        IIOMetadataNode node = new IIOMetadataNode();
        node.setAttribute(attributeName, attributeValue);
        Properties properties = new Properties();
        XPathParser parser = new XPathParser(null, false);
        return new XNode(parser, node, properties);
    }

    // Basic Node Operations Tests
    
    @Test
    public void testGetName_ReturnsCorrectNodeName() {
        String expectedName = "testNode";
        XNode xNode = createXNodeWithName(expectedName);
        
        String actualName = xNode.getName();
        
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetName_ReturnsNullForUnnamedNode() {
        IIOMetadataNode node = new IIOMetadataNode();
        XNode xNode = new XNode(new XPathParser(null, false), node, new Properties());
        
        String name = xNode.getName();
        
        assertNull(name);
    }

    @Test
    public void testToString_GeneratesCorrectXmlFormat() {
        String nodeName = "testElement";
        XNode xNode = createXNodeWithName(nodeName);
        
        String xmlString = xNode.toString();
        
        assertEquals("<testElement>\n</testElement>\n", xmlString);
    }

    @Test
    public void testToString_IncludesAttributesInOutput() {
        XNode xNode = createXNodeWithAttribute("id", "123");
        
        String xmlString = xNode.toString();
        
        assertTrue(xmlString.contains("id=\"123\""));
    }

    // String Attribute Tests

    @Test
    public void testGetStringAttribute_ReturnsAttributeValue() {
        String attributeName = "name";
        String expectedValue = "testValue";
        XNode xNode = createXNodeWithAttribute(attributeName, expectedValue);
        
        String actualValue = xNode.getStringAttribute(attributeName);
        
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetStringAttribute_ReturnsNullForMissingAttribute() {
        XNode xNode = createXNodeWithName("test");
        
        String value = xNode.getStringAttribute("nonexistent");
        
        assertNull(value);
    }

    @Test
    public void testGetStringAttribute_WithDefaultValue_ReturnsDefault() {
        XNode xNode = createXNodeWithName("test");
        String defaultValue = "defaultValue";
        
        String value = xNode.getStringAttribute("missing", defaultValue);
        
        assertEquals(defaultValue, value);
    }

    @Test
    public void testGetStringAttribute_WithSupplier_ReturnsSuppliedValue() {
        XNode xNode = createXNodeWithName("test");
        String suppliedValue = "suppliedValue";
        Supplier<String> supplier = () -> suppliedValue;
        
        String value = xNode.getStringAttribute("missing", supplier);
        
        assertEquals(suppliedValue, value);
    }

    @Test
    public void testGetStringAttribute_WithSupplier_ReturnsNullWhenSupplierReturnsNull() {
        XNode xNode = createXNodeWithName("test");
        Supplier<String> supplier = () -> null;
        
        String value = xNode.getStringAttribute("missing", supplier);
        
        assertNull(value);
    }

    // Numeric Attribute Tests

    @Test
    public void testGetIntAttribute_WithDefaultValue_ReturnsDefault() {
        XNode xNode = createXNodeWithName("test");
        Integer defaultValue = 42;
        
        Integer value = xNode.getIntAttribute("missing", defaultValue);
        
        assertEquals(defaultValue, value);
    }

    @Test
    public void testGetIntAttribute_ReturnsNullForMissingAttribute() {
        XNode xNode = createXNodeWithName("test");
        
        Integer value = xNode.getIntAttribute("missing");
        
        assertNull(value);
    }

    @Test
    public void testGetLongAttribute_WithDefaultValue_ReturnsDefault() {
        XNode xNode = createXNodeWithName("test");
        Long defaultValue = 100L;
        
        Long value = xNode.getLongAttribute("missing", defaultValue);
        
        assertEquals(defaultValue, value);
    }

    @Test
    public void testGetFloatAttribute_WithDefaultValue_ReturnsDefault() {
        XNode xNode = createXNodeWithName("test");
        Float defaultValue = 3.14f;
        
        Float value = xNode.getFloatAttribute("missing", defaultValue);
        
        assertEquals(defaultValue, value, 0.01f);
    }

    @Test
    public void testGetFloatAttribute_ReturnsNullForMissingAttribute() {
        XNode xNode = createXNodeWithName("test");
        
        Float value = xNode.getFloatAttribute("missing");
        
        assertNull(value);
    }

    @Test
    public void testGetDoubleAttribute_WithDefaultValue_ReturnsDefault() {
        XNode xNode = createXNodeWithName("test");
        Double defaultValue = 2.718;
        
        Double value = xNode.getDoubleAttribute("missing", defaultValue);
        
        assertEquals(defaultValue, value, 0.001);
    }

    @Test
    public void testGetDoubleAttribute_ReturnsNullForMissingAttribute() {
        XNode xNode = createXNodeWithName("test");
        
        Double value = xNode.getDoubleAttribute("missing");
        
        assertNull(value);
    }

    // Boolean Attribute Tests

    @Test
    public void testGetBooleanAttribute_ParsesFalseValue() {
        XNode xNode = createXNodeWithAttribute("enabled", "false");
        
        Boolean value = xNode.getBooleanAttribute("enabled");
        
        assertFalse(value);
    }

    @Test
    public void testGetBooleanAttribute_WithDefaultValue_ReturnsDefault() {
        XNode xNode = createXNodeWithName("test");
        Boolean defaultValue = true;
        
        Boolean value = xNode.getBooleanAttribute("missing", defaultValue);
        
        assertEquals(defaultValue, value);
    }

    @Test
    public void testGetBooleanAttribute_ReturnsNullForMissingAttribute() {
        XNode xNode = createXNodeWithName("test");
        
        Boolean value = xNode.getBooleanAttribute("missing");
        
        assertNull(value);
    }

    // Enum Attribute Tests

    @Test
    public void testGetEnumAttribute_WithDefaultValue_ReturnsDefault() {
        XNode xNode = createXNodeWithName("test");
        Locale.IsoCountryCode defaultValue = Locale.IsoCountryCode.PART1_ALPHA3;
        
        Locale.IsoCountryCode value = xNode.getEnumAttribute(
            Locale.IsoCountryCode.class, "missing", defaultValue);
        
        assertEquals(defaultValue, value);
    }

    // Body Content Tests

    @Test
    public void testGetStringBody_WithDefaultValue_ReturnsDefault() {
        XNode xNode = createXNodeWithName("test");
        String defaultValue = "defaultBody";
        
        String body = xNode.getStringBody(defaultValue);
        
        assertEquals(defaultValue, body);
    }

    @Test
    public void testGetStringBody_ReturnsNullForEmptyBody() {
        XNode xNode = createXNodeWithName("test");
        
        String body = xNode.getStringBody();
        
        assertNull(body);
    }

    @Test
    public void testGetIntBody_WithDefaultValue_ReturnsDefault() {
        XNode xNode = createXNodeWithName("test");
        Integer defaultValue = 123;
        
        Integer body = xNode.getIntBody(defaultValue);
        
        assertEquals(defaultValue, body);
    }

    @Test
    public void testGetIntBody_ReturnsNullForEmptyBody() {
        XNode xNode = createXNodeWithName("test");
        
        Integer body = xNode.getIntBody();
        
        assertNull(body);
    }

    @Test
    public void testGetLongBody_WithDefaultValue_ReturnsDefault() {
        XNode xNode = createXNodeWithName("test");
        Long defaultValue = 456L;
        
        Long body = xNode.getLongBody(defaultValue);
        
        assertEquals(defaultValue, body);
    }

    @Test
    public void testGetFloatBody_WithDefaultValue_ReturnsDefault() {
        XNode xNode = createXNodeWithName("test");
        Float defaultValue = 1.23f;
        
        Float body = xNode.getFloatBody(defaultValue);
        
        assertEquals(defaultValue, body, 0.01f);
    }

    @Test
    public void testGetFloatBody_ReturnsNullForEmptyBody() {
        XNode xNode = createXNodeWithName("test");
        
        Float body = xNode.getFloatBody();
        
        assertNull(body);
    }

    @Test
    public void testGetDoubleBody_WithDefaultValue_ReturnsDefault() {
        XNode xNode = createXNodeWithName("test");
        Double defaultValue = 4.56;
        
        Double body = xNode.getDoubleBody(defaultValue);
        
        assertEquals(defaultValue, body, 0.001);
    }

    @Test
    public void testGetBooleanBody_WithDefaultValue_ReturnsDefault() {
        XNode xNode = createXNodeWithName("test");
        Boolean defaultValue = true;
        
        Boolean body = xNode.getBooleanBody(defaultValue);
        
        assertEquals(defaultValue, body);
    }

    @Test
    public void testGetBooleanBody_ReturnsNullForEmptyBody() {
        XNode xNode = createXNodeWithName("test");
        
        Boolean body = xNode.getBooleanBody();
        
        assertNull(body);
    }

    // Child Node Tests

    @Test
    public void testGetChildren_ReturnsEmptyListForNodeWithoutChildren() {
        XNode xNode = createXNodeWithName("parent");
        
        List<XNode> children = xNode.getChildren();
        
        assertTrue(children.isEmpty());
    }

    @Test
    public void testGetChildren_ReturnsChildNodes() {
        IIOMetadataNode parent = new IIOMetadataNode("parent");
        IIOMetadataNode child = new IIOMetadataNode("child");
        parent.appendChild(child);
        
        XNode xNode = new XNode(new XPathParser(null, false), parent, new Properties());
        
        List<XNode> children = xNode.getChildren();
        
        assertEquals(1, children.size());
    }

    @Test
    public void testNewXNode_CreatesNewXNodeInstance() {
        IIOMetadataNode originalNode = new IIOMetadataNode("original");
        IIOMetadataNode newNode = new IIOMetadataNode("new");
        XNode originalXNode = new XNode(new XPathParser(null, false), originalNode, new Properties());
        
        XNode newXNode = originalXNode.newXNode(newNode);
        
        assertNotNull(newXNode);
        assertNull(newXNode.getFloatBody()); // Verify it's a proper XNode instance
    }

    // XPath Evaluation Tests (with XPathParser)

    @Test
    public void testEvalBoolean_WithValidXPath_ReturnsFalse() {
        IIOMetadataNode node = new IIOMetadataNode();
        XPathParser parser = new XPathParser(null, true);
        XNode xNode = new XNode(parser, node, new Properties());
        
        Boolean result = xNode.evalBoolean("name");
        
        assertFalse(result);
    }

    @Test
    public void testEvalString_WithValidXPath_ReturnsEmptyString() {
        IIOMetadataNode node = new IIOMetadataNode();
        XPathParser parser = new XPathParser(null, false);
        XNode xNode = new XNode(parser, node, new Properties());
        
        String result = xNode.evalString("name");
        
        assertEquals("", result);
    }

    @Test
    public void testEvalNodes_ReturnsEmptyList() {
        IIOMetadataNode node = new IIOMetadataNode();
        XPathParser parser = new XPathParser(null, true);
        XNode xNode = new XNode(parser, node, new Properties());
        
        List<XNode> nodes = xNode.evalNodes("nonexistent");
        
        assertNotNull(nodes);
        assertFalse(nodes.contains(xNode));
    }

    @Test
    public void testEvalNode_ReturnsNullForNonexistentPath() {
        IIOMetadataNode node = new IIOMetadataNode();
        XPathParser parser = new XPathParser(null, false);
        XNode xNode = new XNode(parser, node, new Properties());
        
        XNode result = xNode.evalNode("nonexistent");
        
        assertNull(result);
    }

    // Value-Based Identifier Tests

    @Test
    public void testGetValueBasedIdentifier_WithEmptyNodeName() {
        XNode xNode = createXNodeWithName("");
        
        String identifier = xNode.getValueBasedIdentifier();
        
        assertEquals("", identifier);
    }

    @Test
    public void testGetValueBasedIdentifier_WithParentNode() {
        IIOMetadataNode parent = new IIOMetadataNode();
        IIOMetadataNode child = new IIOMetadataNode();
        parent.appendChild(child);
        
        XNode childXNode = new XNode(new XPathParser(null, false), child, new Properties());
        
        String identifier = childXNode.getValueBasedIdentifier();
        
        assertEquals("null_null", identifier);
    }

    // Path Tests

    @Test
    public void testGetPath_WithParentChild() {
        IIOMetadataNode parent = new IIOMetadataNode();
        IIOMetadataNode child = new IIOMetadataNode();
        parent.appendChild(child);
        
        XNode childXNode = new XNode(new XPathParser(null, false), child, new Properties());
        
        String path = childXNode.getPath();
        
        assertEquals("null/null", path);
    }

    @Test
    public void testGetParent_ReturnsNullForRootNode() {
        XNode xNode = createXNodeWithName("root");
        
        XNode parent = xNode.getParent();
        
        assertNull(parent);
    }

    // Error Handling Tests

    @Test(expected = NullPointerException.class)
    public void testConstructor_ThrowsExceptionForNullNode() {
        new XNode(null, null, new Properties());
    }

    @Test(expected = NullPointerException.class)
    public void testGetStringAttribute_ThrowsExceptionForNullAttributeName() {
        XNode xNode = createXNodeWithName("test");
        xNode.getStringAttribute(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetStringAttribute_WithSupplier_ThrowsExceptionForNullSupplier() {
        XNode xNode = createXNodeWithName("test");
        xNode.getStringAttribute("attr", (Supplier<String>) null);
    }

    @Test(expected = NumberFormatException.class)
    public void testGetIntAttribute_ThrowsExceptionForInvalidNumber() {
        XNode xNode = createXNodeWithAttribute("number", "notANumber");
        xNode.getIntAttribute("number", 0);
    }

    @Test(expected = NumberFormatException.class)
    public void testGetFloatAttribute_ThrowsExceptionForInvalidNumber() {
        XNode xNode = createXNodeWithAttribute("number", "notANumber");
        xNode.getFloatAttribute("number");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumAttribute_ThrowsExceptionForInvalidEnumValue() {
        XNode xNode = createXNodeWithAttribute("country", "INVALID");
        xNode.getEnumAttribute(Locale.IsoCountryCode.class, "country", Locale.IsoCountryCode.PART1_ALPHA2);
    }

    // Integration Tests

    @Test
    public void testGetChildrenAsProperties_ReturnsEmptyPropertiesForNodeWithoutChildren() {
        XNode xNode = createXNodeWithName("test");
        
        Properties properties = xNode.getChildrenAsProperties();
        
        assertTrue(properties.isEmpty());
    }

    @Test
    public void testGetNode_ReturnsUnderlyingDomNode() {
        IIOMetadataNode domNode = new IIOMetadataNode("test");
        XNode xNode = new XNode(null, domNode, new Properties());
        
        Node retrievedNode = xNode.getNode();
        
        assertSame(domNode, retrievedNode);
    }
}