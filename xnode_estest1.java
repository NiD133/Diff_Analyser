package org.apache.ibatis.parsing;

import org.junit.Test;
import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that the toString() method correctly formats an XNode representing
     * an empty XML element.
     */
    @Test
    public void toStringShouldCorrectlyFormatAnEmptyNode() {
        // Arrange
        // 1. Define a simple name for our XML node.
        String nodeName = "root";

        // 2. Create a standard DOM Node. IIOMetadataNode is a concrete implementation.
        // This node is created without any children.
        Node domNode = new IIOMetadataNode(nodeName);

        // 3. The XNode constructor requires a Properties object for variables.
        // An empty one is sufficient for this test.
        Properties variables = new Properties();

        // 4. Instantiate the XNode. The XPathParser is not used by the toString()
        // method, so it can be null.
        XNode xNode = new XNode(null, domNode, variables);

        // Act
        // Call the method under test to get its string representation.
        String actualXmlString = xNode.toString();

        // Assert
        // The expected format for an empty node is <name>\n</name>\n
        String expectedXmlString = "<root>\n</root>\n";
        assertEquals(expectedXmlString, actualXmlString);
    }
}