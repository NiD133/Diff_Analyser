package org.apache.ibatis.parsing;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Test suite for the {@link XNode} class.
 * This class contains the improved version of the original test case.
 */
public class XNodeTest {

    /**
     * Verifies that getDoubleAttribute throws a NumberFormatException
     * when the attribute's value is not a valid double.
     */
    @Test(expected = NumberFormatException.class)
    public void getDoubleAttributeShouldThrowNumberFormatExceptionForNonNumericValue() {
        // Arrange: Create an XNode with an attribute that cannot be parsed as a double.
        IIOMetadataNode node = new IIOMetadataNode();
        node.setAttribute("value", "not-a-double");

        // The XPathParser and variables are required for the XNode constructor,
        // but their specific state is not relevant to this test.
        XPathParser dummyParser = new XPathParser((Document) null, false);
        Properties variables = new Properties();
        XNode xNode = new XNode(dummyParser, node, variables);

        // Act & Assert: Attempting to get the attribute as a double should throw an exception.
        xNode.getDoubleAttribute("value");
    }
}