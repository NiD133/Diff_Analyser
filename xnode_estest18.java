package org.apache.ibatis.parsing;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the default value behavior of XNode's body parsing methods.
 */
public class XNodeTest {

  /**
   * Verifies that getIntBody(defaultValue) returns the default value
   * when the underlying XML node has no text content, resulting in a null body.
   */
  @Test
  public void getIntBodyShouldReturnDefaultValueWhenNodeHasNoBody() {
    // Arrange
    // An IIOMetadataNode without any children or text content results in a null body for the XNode.
    Node emptyNode = new IIOMetadataNode();
    XNode xNode = new XNode(null, emptyNode, new Properties());
    Integer defaultValue = 0;

    // Act
    Integer result = xNode.getIntBody(defaultValue);

    // Assert
    assertEquals("Should return the default value for a node with no body", defaultValue, result);
  }
}