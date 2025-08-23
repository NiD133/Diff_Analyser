package org.apache.ibatis.parsing;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode#toString()} method.
 */
public class XNodeToStringTest {

    /**
     * Verifies that {@link XNode#toString()} correctly formats a node with a single, empty child.
     * The expected output is a pretty-printed, XML-like string with proper indentation.
     */
    @Test
    public void toStringShouldCorrectlyFormatNodeWithSingleChild() {
        // Arrange
        // 1. Create a parent node with a single child. We use IIOMetadataNode as a concrete
        // implementation of org.w3c.dom.Node. Its default node name is null, which the
        // XNode renders as the string "null".
        Node parentNode = new IIOMetadataNode();
        Node childNode = new IIOMetadataNode();
        parentNode.appendChild(childNode);

        // 2. An XPathParser and Properties are required for XNode instantiation,
        // though they are not directly relevant to this toString() test.
        XPathParser xPathParser = new XPathParser((Document) null, false);
        Properties variables = new Properties();
        XNode xNode = new XNode(xPathParser, parentNode, variables);

        // 3. Define the expected string. It should represent the parent node containing
        // the indented child. The child is rendered as a self-closing tag as it is empty.
        String expectedXmlString =
                "<null>\n" +
                "  <null />\n" +
                "</null>\n";

        // Act
        String actualXmlString = xNode.toString();

        // Assert
        assertEquals(expectedXmlString, actualXmlString);
    }
}