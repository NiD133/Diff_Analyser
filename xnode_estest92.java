package org.apache.ibatis.parsing;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Document;

/**
 * Test suite for the {@link XNode} class, focusing on the toString() method.
 */
public class XNodeToStringTest {

    /**
     * Verifies that the toString() method correctly formats an XNode
     * that represents a simple XML element with a single attribute.
     */
    @Test
    public void toStringShouldRenderNodeWithAttribute() {
        // Arrange: Set up the test objects.

        // 1. Create a DOM Node. IIOMetadataNode is a concrete implementation from the JDK.
        // The default constructor creates a node with a null name, which is part of this test's scenario.
        IIOMetadataNode node = new IIOMetadataNode();
        node.setAttribute("id", "test-node");

        // 2. Create a dummy XPathParser and an empty Properties object, as required by the XNode constructor.
        // The parser is initialized with null because it is not used by the toString() method.
        XPathParser dummyParser = new XPathParser((Document) null, false);
        Properties variables = new Properties();

        // 3. Create the XNode instance to be tested.
        XNode xNode = new XNode(dummyParser, node, variables);

        // Act: Call the method under test.
        String actualXmlString = xNode.toString();

        // Assert: Verify the result.
        // The expected format is "<[nodeName] [attrName]=\"[attrValue]\" />\n"
        String expectedXmlString = "<null id=\"test-node\" />\n";
        assertEquals(expectedXmlString, actualXmlString);
    }
}