package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Tests that getValueBasedIdentifier recursively constructs an identifier from parent to child.
     * When nodes lack 'id', 'value', or 'property' attributes and have null names, their
     * identifier part becomes the string "null". The final identifier is a concatenation
     * of these parts, separated by underscores.
     */
    @Test
    public void getValueBasedIdentifier_withNestedNodesAndNullNames_shouldReturnConcatenatedNullIdentifier() {
        // Arrange
        // Create a parent-child node hierarchy. IIOMetadataNode is used as a concrete
        // implementation of org.w3c.dom.Node. By default, its name is null and it has no attributes.
        Node parentNode = new IIOMetadataNode();
        Node childNode = new IIOMetadataNode();
        parentNode.appendChild(childNode);

        // The XPathParser and variables are required for the XNode constructor but are not
        // directly involved in the logic being tested here.
        XPathParser xPathParser = new XPathParser((Document) null, false);
        Properties variables = new Properties();
        XNode xNodeForChild = new XNode(xPathParser, childNode, variables);

        // Act
        // This method should generate an identifier by traversing up to the parent,
        // generating the parent's identifier first, and then appending the child's.
        String identifier = xNodeForChild.getValueBasedIdentifier();

        // Assert
        // Expected: "parent_identifier" + "_" + "child_identifier"
        // Since both nodes result in a "null" identifier part, the combined result is "null_null".
        assertEquals("Identifier should be a concatenation of parent and child identifiers.",
                "null_null", identifier);
    }
}