package org.apache.ibatis.parsing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Locale;
import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class, focusing on attribute parsing.
 */
public class XNodeTest {

  /**
   * Verifies that getEnumAttribute() throws an IllegalArgumentException
   * when the attribute's value does not match any constant in the specified enum.
   */
  @Test
  public void getEnumAttributeShouldThrowExceptionForInvalidEnumValue() {
    // Arrange
    final String attributeName = "countryCode";
    final String invalidAttributeValue = "INVALID_CODE";

    // Create a DOM Node with an attribute that has an invalid enum value.
    // IIOMetadataNode is a concrete implementation of org.w3c.dom.Node suitable for testing.
    Node node = new IIOMetadataNode();
    ((IIOMetadataNode) node).setAttribute(attributeName, invalidAttributeValue);

    // The XPathParser is not used for attribute parsing, so it can be null.
    // The variables map is also not needed for this test.
    XNode xNode = new XNode(null, node, new Properties());

    // A default value that should not be used, as an exception is expected.
    Locale.IsoCountryCode defaultValue = Locale.IsoCountryCode.PART1_ALPHA2;

    // Act & Assert
    try {
      xNode.getEnumAttribute(Locale.IsoCountryCode.class, attributeName, defaultValue);
      fail("Expected an IllegalArgumentException to be thrown for an invalid enum value.");
    } catch (IllegalArgumentException e) {
      // Verify that the exception message clearly indicates the cause of the error.
      String expectedMessage = "No enum constant " + Locale.IsoCountryCode.class.getName() + "." + invalidAttributeValue;
      assertEquals(expectedMessage, e.getMessage());
    }
  }
}