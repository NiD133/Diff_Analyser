/*
 *    Copyright 2009-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.parsing;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * Tests for the XNode class.
 * Refactored for clarity and maintainability from an auto-generated test suite.
 */
public class XNodeTest {

  /**
   * Helper method to create an XNode from a given XML string.
   * This simplifies test setup significantly.
   *
   * @param xml The XML content.
   * @return An XNode representing the root element of the parsed XML.
   * @throws Exception if parsing fails.
   */
  private XNode createXNodeFromXml(String xml) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(new InputSource(new StringReader(xml)));
    // A null XPathParser is acceptable for most XNode functionality.
    return new XNode(null, doc.getDocumentElement(), null);
  }

  // --- Basic Getters ---

  @Test
  public void shouldReturnNodeName() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root/>");

    // Act
    String name = xNode.getName();

    // Assert
    assertEquals("root", name);
  }

  @Test
  public void shouldReturnTheUnderlyingDomNode() throws Exception {
    // Arrange
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(new InputSource(new StringReader("<root/>")));
    Node node = doc.getDocumentElement();
    XNode xNode = new XNode(null, node, null);

    // Act & Assert
    assertSame(node, xNode.getNode());
  }

  @Test
  public void shouldReturnParentNode() throws Exception {
    // Arrange
    XNode root = createXNodeFromXml("<root><child/></root>");
    XNode child = root.evalNode("child");

    // Act
    XNode parent = child.getParent();

    // Assert
    assertNotNull(parent);
    assertEquals("root", parent.getName());
  }

  @Test
  public void shouldReturnNullForParentOfRoot() throws Exception {
    // Arrange
    XNode root = createXNodeFromXml("<root/>");

    // Act & Assert
    assertNull(root.getParent());
  }

  @Test
  public void shouldGeneratePathForNode() throws Exception {
    // Arrange
    XNode root = createXNodeFromXml("<root><child><grandchild/></child></root>");
    XNode grandchild = root.evalNode("child/grandchild");

    // Act
    String path = grandchild.getPath();

    // Assert
    assertEquals("/root/child/grandchild", path);
  }

  @Test
  public void shouldGenerateValueBasedIdentifier() throws Exception {
    // Arrange
    XNode node = createXNodeFromXml("<user id='1' name='test'/>");

    // Act
    String identifier = node.getValueBasedIdentifier();

    // Assert
    assertEquals("/user[@id=1,@name=test]", identifier);
  }

  // --- Body Getters ---

  @Test
  public void shouldReturnStringBody() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root>Test Body</root>");

    // Act & Assert
    assertEquals("Test Body", xNode.getStringBody());
  }

  @Test
  public void shouldReturnNullStringBodyWhenEmpty() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root/>");

    // Act & Assert
    assertNull(xNode.getStringBody());
  }

  @Test
  public void shouldReturnDefaultStringBodyWhenEmpty() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root/>");

    // Act & Assert
    assertEquals("default", xNode.getStringBody("default"));
  }

  @Test
  public void shouldReturnBooleanBody() throws Exception {
    // Arrange
    XNode trueNode = createXNodeFromXml("<root>true</root>");
    XNode falseNode = createXNodeFromXml("<root>false</root>");

    // Act & Assert
    assertEquals(Boolean.TRUE, trueNode.getBooleanBody());
    assertEquals(Boolean.FALSE, falseNode.getBooleanBody());
  }

  @Test
  public void shouldReturnDefaultBooleanBodyWhenInvalid() throws Exception {
    // Arrange
    XNode emptyNode = createXNodeFromXml("<root/>");
    XNode invalidNode = createXNodeFromXml("<root>not-a-boolean</root>");

    // Act & Assert
    assertEquals(Boolean.TRUE, emptyNode.getBooleanBody(true));
    assertEquals(Boolean.FALSE, invalidNode.getBooleanBody(false));
  }

  @Test
  public void shouldReturnNumericBodies() throws Exception {
    // Arrange
    XNode intNode = createXNodeFromXml("<root>10</root>");
    XNode longNode = createXNodeFromXml("<root>20</root>");
    XNode doubleNode = createXNodeFromXml("<root>30.5</root>");
    XNode floatNode = createXNodeFromXml("<root>40.5</root>");

    // Act & Assert
    assertEquals(Integer.valueOf(10), intNode.getIntBody());
    assertEquals(Long.valueOf(20L), longNode.getLongBody());
    assertEquals(Double.valueOf(30.5), doubleNode.getDoubleBody());
    assertEquals(Float.valueOf(40.5f), floatNode.getFloatBody());
  }

  @Test
  public void shouldReturnDefaultNumericBodyWhenInvalid() throws Exception {
    // Arrange
    XNode invalidNode = createXNodeFromXml("<root>invalid</root>");

    // Act & Assert
    assertEquals(Integer.valueOf(99), invalidNode.getIntBody(99));
    assertEquals(Long.valueOf(99L), invalidNode.getLongBody(99L));
    assertEquals(Double.valueOf(99.0), invalidNode.getDoubleBody(99.0));
    assertEquals(Float.valueOf(99.0f), invalidNode.getFloatBody(99.0f));
  }

  // --- Attribute Getters ---

  @Test
  public void shouldReturnStringAttribute() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root name='value'/>");

    // Act & Assert
    assertEquals("value", xNode.getStringAttribute("name"));
  }

  @Test
  public void shouldReturnNullWhenStringAttributeIsMissing() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root/>");

    // Act & Assert
    assertNull(xNode.getStringAttribute("missing"));
  }

  @Test
  public void shouldReturnDefaultWhenStringAttributeIsMissing() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root/>");

    // Act & Assert
    assertEquals("default", xNode.getStringAttribute("missing", "default"));
  }

  @Test
  public void shouldReturnSuppliedDefaultWhenStringAttributeIsMissing() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root/>");

    // Act
    String value = xNode.getStringAttribute("missing", () -> "supplied_default");

    // Assert
    assertEquals("supplied_default", value);
  }

  @Test
  public void shouldReturnBooleanAttribute() throws Exception {
    // Arrange
    XNode trueNode = createXNodeFromXml("<root enabled='true'/>");
    XNode falseNode = createXNodeFromXml("<root enabled='false'/>");

    // Act & Assert
    assertEquals(Boolean.TRUE, trueNode.getBooleanAttribute("enabled", false));
    assertEquals(Boolean.FALSE, falseNode.getBooleanAttribute("enabled", true));
  }

  @Test
  public void shouldReturnDefaultWhenBooleanAttributeIsMissing() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root/>");

    // Act & Assert
    assertTrue(xNode.getBooleanAttribute("missing", true));
    assertFalse(xNode.getBooleanAttribute("missing", false));
  }

  @Test
  public void shouldReturnNumericAttributes() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root int='10' long='20' double='30.5' float='40.5'/>");

    // Act & Assert
    assertEquals(Integer.valueOf(10), xNode.getIntAttribute("int", 0));
    assertEquals(Long.valueOf(20L), xNode.getLongAttribute("long", 0L));
    assertEquals(Double.valueOf(30.5), xNode.getDoubleAttribute("double", 0.0));
    assertEquals(Float.valueOf(40.5f), xNode.getFloatAttribute("float", 0f));
  }

  @Test(expected = NumberFormatException.class)
  public void getIntAttributeShouldThrowExceptionForInvalidValue() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root value='invalid'/>");

    // Act
    xNode.getIntAttribute("value");
  }

  @Test
  public void shouldReturnEnumAttribute() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root type='DISPLAY'/>");

    // Act
    Locale.Category category = xNode.getEnumAttribute(Locale.Category.class, "type");

    // Assert
    assertEquals(Locale.Category.DISPLAY, category);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getEnumAttributeShouldThrowExceptionForInvalidValue() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root type='INVALID_ENUM'/>");

    // Act
    xNode.getEnumAttribute(Locale.Category.class, "type");
  }

  // --- Children and Structure ---

  @Test
  public void shouldReturnChildNodes() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root><child/><child/></root>");

    // Act
    List<XNode> children = xNode.getChildren();

    // Assert
    assertEquals(2, children.size());
    assertEquals("child", children.get(0).getName());
  }

  @Test
  public void shouldReturnEmptyListForNodeWithNoChildren() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root/>");

    // Act
    List<XNode> children = xNode.getChildren();

    // Assert
    assertTrue(children.isEmpty());
  }

  @Test
  public void shouldReturnChildrenAsProperties() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<properties><property name='key1' value='val1'/><property name='key2' value='val2'/></properties>");

    // Act
    Properties props = xNode.getChildrenAsProperties();

    // Assert
    assertEquals(2, props.size());
    assertEquals("val1", props.getProperty("key1"));
    assertEquals("val2", props.getProperty("key2"));
  }

  // --- toString() ---

  @Test
  public void shouldGenerateStringRepresentationForNodeWithAttributes() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root id='1' name='test'/>");
    String expected = "<root id=\"1\" name=\"test\"/>\n";

    // Act
    String result = xNode.toString();

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void shouldGenerateStringRepresentationForNodeWithChildren() throws Exception {
    // Arrange
    XNode xNode = createXNodeFromXml("<root><child/></root>");
    String expected = "<root>\n  <child/>\n</root>\n";

    // Act
    String result = xNode.toString();

    // Assert
    assertEquals(expected, result);
  }
}