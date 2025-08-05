package org.apache.ibatis.parsing;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Supplier;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ext.DefaultHandler2;

public class XNodeTest {

    // Test for XNode.toString() method
    @Test(timeout = 4000)
    public void testToStringMethod() throws Throwable {
        IIOMetadataNode metadataNode = new IIOMetadataNode("testNode");
        metadataNode.removeChild(metadataNode);
        Properties properties = new Properties();
        XNode xNode = new XNode(null, metadataNode, properties);
        String result = xNode.toString();
        assertEquals("<testNode>\n</testNode>\n", result);
    }

    // Test for XNode.newXNode() method
    @Test(timeout = 4000)
    public void testNewXNodeMethod() throws Throwable {
        Properties properties = new Properties();
        IIOMetadataNode metadataNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, metadataNode, properties);
        XNode newNode = xNode.newXNode(metadataNode);
        assertNull(newNode.getFloatBody());
    }

    // Test for XNode.getValueBasedIdentifier() method
    @Test(timeout = 4000)
    public void testGetValueBasedIdentifierMethod() throws Throwable {
        Properties properties = new Properties();
        IIOMetadataNode metadataNode = new IIOMetadataNode("");
        XPathParser xPathParser = new XPathParser((Document) null, false);
        XNode xNode = new XNode(xPathParser, metadataNode, properties);
        String identifier = xNode.getValueBasedIdentifier();
        assertEquals("", identifier);
    }

    // Test for XNode.getStringBody() method with default value
    @Test(timeout = 4000)
    public void testGetStringBodyWithDefault() throws Throwable {
        Properties properties = new Properties();
        IIOMetadataNode metadataNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, metadataNode, properties);
        String result = xNode.getStringBody("defaultString");
        assertEquals("defaultString", result);
    }

    // Test for XNode.getStringAttribute() method with Supplier
    @Test(timeout = 4000)
    public void testGetStringAttributeWithSupplier() throws Throwable {
        Properties properties = new Properties();
        IIOMetadataNode metadataNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, metadataNode, properties);
        Supplier<String> defaultSupplier = mock(Supplier.class, new ViolatedAssumptionAnswer());
        doReturn(null).when(defaultSupplier).get();
        String result = xNode.getStringAttribute("", defaultSupplier);
        assertNull(result);
    }

    // Test for XNode.getStringAttribute() method with default value
    @Test(timeout = 4000)
    public void testGetStringAttributeWithDefault() throws Throwable {
        IIOMetadataNode metadataNode = new IIOMetadataNode();
        metadataNode.setAttribute("attr", "value");
        XPathParser xPathParser = new XPathParser((Document) null, false);
        Properties properties = new Properties();
        XNode xNode = new XNode(xPathParser, metadataNode, properties);
        String result = xNode.getStringAttribute("attr");
        assertEquals("value", result);
    }

    // Test for XNode.getName() method
    @Test(timeout = 4000)
    public void testGetNameMethod() throws Throwable {
        IIOMetadataNode metadataNode = new IIOMetadataNode("nodeName");
        Properties properties = new Properties();
        XNode xNode = new XNode(null, metadataNode, properties);
        String name = xNode.getName();
        assertEquals("nodeName", name);
    }

    // Test for XNode.getLongBody() method with default value
    @Test(timeout = 4000)
    public void testGetLongBodyWithDefault() throws Throwable {
        Properties properties = new Properties();
        IIOMetadataNode metadataNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, metadataNode, properties);
        Long defaultValue = 0L;
        Long result = xNode.getLongBody(defaultValue);
        assertEquals(defaultValue, result);
    }

    // Test for XNode.getIntBody() method with default value
    @Test(timeout = 4000)
    public void testGetIntBodyWithDefault() throws Throwable {
        Properties properties = new Properties();
        IIOMetadataNode metadataNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, metadataNode, properties);
        Integer defaultValue = 0;
        Integer result = xNode.getIntBody(defaultValue);
        assertEquals(defaultValue, result);
    }

    // Test for XNode.getFloatBody() method with default value
    @Test(timeout = 4000)
    public void testGetFloatBodyWithDefault() throws Throwable {
        Properties properties = new Properties();
        IIOMetadataNode metadataNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, metadataNode, properties);
        Float defaultValue = 0.0F;
        Float result = xNode.getFloatBody(defaultValue);
        assertEquals(defaultValue, result, 0.01F);
    }

    // Test for XNode.getDoubleBody() method with default value
    @Test(timeout = 4000)
    public void testGetDoubleBodyWithDefault() throws Throwable {
        Properties properties = new Properties();
        IIOMetadataNode metadataNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, metadataNode, properties);
        Double defaultValue = 0.0;
        Double result = xNode.getDoubleBody(defaultValue);
        assertEquals(defaultValue, result, 0.01);
    }

    // Test for XNode.getBooleanBody() method with default value
    @Test(timeout = 4000)
    public void testGetBooleanBodyWithDefault() throws Throwable {
        Properties properties = new Properties();
        IIOMetadataNode metadataNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, metadataNode, properties);
        Boolean defaultValue = Boolean.TRUE;
        Boolean result = xNode.getBooleanBody(defaultValue);
        assertEquals(defaultValue, result);
    }

    // Test for XNode.getChildren() method
    @Test(timeout = 4000)
    public void testGetChildrenMethod() throws Throwable {
        IIOMetadataNode metadataNode = new IIOMetadataNode();
        metadataNode.appendChild(new IIOMetadataNode());
        XPathParser xPathParser = new XPathParser((Document) null, false);
        Properties properties = new Properties();
        XNode xNode = new XNode(xPathParser, metadataNode, properties);
        List<XNode> children = xNode.getChildren();
        assertEquals(1, children.size());
    }

    // Test for XNode.getChildrenAsProperties() method
    @Test(timeout = 4000)
    public void testGetChildrenAsPropertiesMethod() throws Throwable {
        IIOMetadataNode metadataNode = new IIOMetadataNode();
        metadataNode.appendChild(new IIOMetadataNode());
        Properties properties = new Properties();
        XNode xNode = new XNode(null, metadataNode, properties);
        Properties childrenProperties = xNode.getChildrenAsProperties();
        assertEquals(properties, childrenProperties);
    }

    // Test for XNode.getNode() method
    @Test(timeout = 4000)
    public void testGetNodeMethod() throws Throwable {
        IIOMetadataNode metadataNode = new IIOMetadataNode();
        Properties properties = new Properties();
        XNode xNode = new XNode(null, metadataNode, properties);
        Node resultNode = xNode.getNode();
        assertSame(metadataNode, resultNode);
    }

    // Test for XNode.evalString() method
    @Test(timeout = 4000)
    public void testEvalStringMethod() throws Throwable {
        IIOMetadataNode metadataNode = new IIOMetadataNode();
        XPathParser xPathParser = new XPathParser((Document) null, false);
        Properties properties = new Properties();
        XNode xNode = new XNode(xPathParser, metadataNode, properties);
        String result = xNode.evalString("org.apache.ibatis.parsing.PropertyParser.enable-default-value");
        assertEquals("", result);
    }
}