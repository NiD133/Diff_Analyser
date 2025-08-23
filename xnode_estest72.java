package org.apache.ibatis.parsing;

import org.junit.Test;
import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;
import org.w3c.dom.Document;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that calling getDoubleAttribute with an attribute value that cannot be
     * parsed as a double correctly throws a NumberFormatException.
     */
    @Test(expected = NumberFormatException.class)
    public void getDoubleAttributeShouldThrowExceptionForNonNumericValue() {
        // Arrange: Create a node with an attribute that has a non-numeric string value.
        IIOMetadataNode node = new IIOMetadataNode();
        node.setAttribute("price", "invalid-number");

        // These dependencies are required for the XNode constructor but are not
        // central to the logic being tested.
        XPathParser parser = new XPathParser((Document) null, false);
        Properties variables = new Properties();
        XNode xNode = new XNode(parser, node, variables);

        // Act: Attempt to retrieve the attribute as a Double.
        // This action is expected to throw a NumberFormatException, which is
        // handled by the @Test(expected=...) annotation.
        xNode.getDoubleAttribute("price", 99.99);
    }
}