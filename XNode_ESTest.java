package org.apache.ibatis.parsing;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Properties;

import javax.imageio.metadata.IIOMetadataNode;

import org.junit.Test;

/**
 * Readable tests for XNode that focus on core behaviors:
 * - Basic metadata (name, toString)
 * - Body getters (with and without defaults)
 * - Attribute getters (String, numeric, enum) and their defaults
 * - Parent/path/value-based identifier
 * - Children collection
 *
 * Notes:
 * - IIOMetadataNode is used as a convenient DOM Element implementation.
 * - XPath-dependent behavior (eval*) is intentionally not covered to keep tests deterministic and simple.
 */
public class XNodeReadableTest {

  private static final String ATTR = "attr";
  private static final String VALUE = "val";

  private static XNode xNode(XPathParser parser, IIOMetadataNode node) {
    return new XNode(parser, node, new Properties());
  }

  private static XNode xNode(IIOMetadataNode node) {
    return xNode(null, node);
  }

  // Simple enum for getEnumAttribute tests
  private enum Color {
    RED, GREEN
  }

  // ---------------------------
  // Basic metadata and toString
  // ---------------------------

  @Test
  public void name_is_node_tag_name() {
    IIOMetadataNode node = new IIOMetadataNode("user");
    XNode x = xNode(node);

    assertEquals("user", x.getName());
  }

  @Test
  public void toString_renders_single_element_with_attributes() {
    IIOMetadataNode node = new IIOMetadataNode("user");
    node.setAttribute(ATTR, VALUE);
    XNode x = xNode(node);

    String xml = x.toString();
    assertTrue(xml.contains("<user"));
    assertTrue(xml.contains(ATTR + "=\"" + VALUE + "\""));
    assertTrue(xml.trim().endsWith("/>"));
  }

  @Test
  public void toString_renders_nested_children() {
    IIOMetadataNode parent = new IIOMetadataNode("parent");
    IIOMetadataNode child = new IIOMetadataNode("child");
    parent.appendChild(child);
    XNode x = xNode(parent);

    String xml = x.toString();
    // Example (spacing/indentation may vary):
    // <parent>
    //   <child />
    // </parent>
    assertTrue(xml.contains("<parent"));
    assertTrue(xml.contains("<child"));
    assertTrue(xml.contains("</parent>"));
  }

  // ---------------------------
  // Body getters
  // ---------------------------

  @Test
  public void string_body_returns_null_when_empty() {
    IIOMetadataNode node = new IIOMetadataNode("n");
    XNode x = xNode(node);
    assertNull(x.getStringBody());
  }

  @Test
  public void string_body_uses_default_when_empty() {
    IIOMetadataNode node = new IIOMetadataNode("n");
    XNode x = xNode(node);
    assertEquals("default", x.getStringBody("default"));
  }

  @Test
  public void typed_bodies_use_default_when_empty() {
    IIOMetadataNode node = new IIOMetadataNode("n");
    XNode x = xNode(node);

    assertEquals(Integer.valueOf(7), x.getIntBody(7));
    assertEquals(Long.valueOf(8L), x.getLongBody(8L));
    assertEquals(Double.valueOf(3.14), x.getDoubleBody(3.14));
    assertEquals(Float.valueOf(2.5F), x.getFloatBody(2.5F));
    assertEquals(Boolean.TRUE, x.getBooleanBody(Boolean.TRUE));
  }

  @Test
  public void typed_bodies_return_null_when_empty_and_no_default() {
    IIOMetadataNode node = new IIOMetadataNode("n");
    XNode x = xNode(node);

    assertNull(x.getIntBody());
    assertNull(x.getLongBody());
    assertNull(x.getDoubleBody());
    assertNull(x.getFloatBody());
    assertNull(x.getBooleanBody());
  }

  // ---------------------------
  // String attribute getters
  // ---------------------------

  @Test
  public void string_attribute_returns_value_if_present() {
    IIOMetadataNode node = new IIOMetadataNode("n");
    node.setAttribute(ATTR, VALUE);
    XNode x = xNode(node);

    assertEquals(VALUE, x.getStringAttribute(ATTR));
  }

  @Test
  public void string_attribute_uses_literal_default_when_absent() {
    IIOMetadataNode node = new IIOMetadataNode("n");
    XNode x = xNode(node);

    assertEquals("fallback", x.getStringAttribute("missing", "fallback"));
  }

  @Test
  public void string_attribute_uses_supplier_default_when_absent() {
    IIOMetadataNode node = new IIOMetadataNode("n");
    XNode x = xNode(node);

    assertEquals("computed", x.getStringAttribute("missing", () -> "computed"));
  }

  @Test
  public void string_attribute_throws_when_supplier_is_null() {
    IIOMetadataNode node = new IIOMetadataNode("n");
    XNode x = xNode(node);

    assertThrows(NullPointerException.class, () -> x.getStringAttribute("missing", (java.util.function.Supplier<String>) null));
  }

  // ---------------------------
  // Numeric attribute getters
  // ---------------------------

  @Test
  public void numeric_attributes_parse_numbers() {
    IIOMetadataNode node = new IIOMetadataNode("n");
    node.setAttribute("i", "10");
    node.setAttribute("l", "20");
    node.setAttribute("d", "2.5");
    node.setAttribute("f", "1.25");
    XNode x = xNode(node);

    assertEquals(Integer.valueOf(10), x.getIntAttribute("i"));
    assertEquals(Long.valueOf(20L), x.getLongAttribute("l"));
    assertEquals(Double.valueOf(2.5), x.getDoubleAttribute("d"));
    assertEquals(Float.valueOf(1.25F), x.getFloatAttribute("f"));
  }

  @Test
  public void numeric_attributes_return_null_when_absent() {
    IIOMetadataNode node = new IIOMetadataNode("n");
    XNode x = xNode(node);

    assertNull(x.getIntAttribute("missing"));
    assertNull(x.getLongAttribute("missing"));
    assertNull(x.getDoubleAttribute("missing"));
    assertNull(x.getFloatAttribute("missing"));
  }

  @Test
  public void numeric_attributes_use_default_when_absent() {
    IIOMetadataNode node = new IIOMetadataNode("n");
    XNode x = xNode(node);

    assertEquals(Integer.valueOf(1), x.getIntAttribute("i", 1));
    assertEquals(Long.valueOf(2L), x.getLongAttribute("l", 2L));
    assertEquals(Double.valueOf(3.0), x.getDoubleAttribute("d", 3.0));
    assertEquals(Float.valueOf(4.0F), x.getFloatAttribute("f", 4.0F));
  }

  @Test
  public void numeric_attributes_throw_when_present_but_not_parsable() {
    IIOMetadataNode node = new IIOMetadataNode("n");
    node.setAttribute("i", "NaN");
    node.setAttribute("l", "oops");
    node.setAttribute("d", "not-a-double");
    node.setAttribute("f", "not-a-float");
    XNode x = xNode(node);

    assertThrows(NumberFormatException.class, () -> x.getIntAttribute("i"));
    assertThrows(NumberFormatException.class, () -> x.getLongAttribute("l"));
    assertThrows(NumberFormatException.class, () -> x.getDoubleAttribute("d"));
    assertThrows(NumberFormatException.class, () -> x.getFloatAttribute("f"));
  }

  // ---------------------------
  // Enum attribute getters
  // ---------------------------

  @Test
  public void enum_attribute_parses_value() {
    IIOMetadataNode node = new IIOMetadataNode("n");
    node.setAttribute("color", "RED");
    XNode x = xNode(node);

    assertEquals(Color.RED, x.getEnumAttribute(Color.class, "color"));
  }

  @Test
  public void enum_attribute_returns_default_when_absent() {
    IIOMetadataNode node = new IIOMetadataNode("n");
    XNode x = xNode(node);

    assertEquals(Color.GREEN, x.getEnumAttribute(Color.class, "missing", Color.GREEN));
  }

  @Test
  public void enum_attribute_throws_on_invalid_value() {
    IIOMetadataNode node = new IIOMetadataNode("n");
    node.setAttribute("color", "BLUE"); // not defined in our enum
    XNode x = xNode(node);

    assertThrows(IllegalArgumentException.class, () -> x.getEnumAttribute(Color.class, "color"));
  }

  // ---------------------------
  // Parent, path, identifier
  // ---------------------------

  @Test
  public void parent_returns_wrapped_parent_node() {
    IIOMetadataNode parent = new IIOMetadataNode("root");
    IIOMetadataNode child = new IIOMetadataNode("child");
    parent.appendChild(child);

    XNode childX = xNode(child);
    XNode parentX = childX.getParent();

    assertNotNull(parentX);
    assertEquals("root", parentX.getName());
  }

  @Test
  public void path_is_built_from_hierarchy() {
    IIOMetadataNode root = new IIOMetadataNode("root");
    IIOMetadataNode child = new IIOMetadataNode("child");
    root.appendChild(child);

    XNode x = xNode(child);
    assertEquals("root/child", x.getPath());
  }

  @Test
  public void value_based_identifier_uses_names() {
    IIOMetadataNode root = new IIOMetadataNode("root");
    IIOMetadataNode child = new IIOMetadataNode("child");
    root.appendChild(child);

    XNode x = xNode(child);
    // In MyBatis, this is typically "<parent>_<child>" if no id/value/property attrs
    assertEquals("root_child", x.getValueBasedIdentifier());
  }

  // ---------------------------
  // Children
  // ---------------------------

  @Test
  public void children_returns_direct_element_children() {
    IIOMetadataNode parent = new IIOMetadataNode("parent");
    parent.appendChild(new IIOMetadataNode("c1"));
    parent.appendChild(new IIOMetadataNode("c2"));

    XNode x = xNode(parent);
    List<XNode> children = x.getChildren();

    assertEquals(2, children.size());
    assertEquals("c1", children.get(0).getName());
    assertEquals("c2", children.get(1).getName());
  }
}