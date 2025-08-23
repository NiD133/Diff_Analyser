package org.apache.ibatis.parsing;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that getValueBasedIdentifier() returns an empty string for a node
     * that has an empty name and no identifying attributes (e.g., 'id', 'value').
     * This represents a base case for identifier generation.
     */
    @Test
    public void identifierForNodeWithEmptyNameAndNoIdShouldBeEmpty() {
        // Arrange
        // An IIOMetadataNode is a concrete org.w3c.dom.Node implementation that can be
        // instantiated directly, which is convenient for testing without a full XML document.
        Node nodeWithEmptyName = new IIOMetadataNode("");
        Properties variables = new Properties();

        // The XPathParser dependency is not used by the getValueBasedIdentifier() method,
        // so we can pass null to simplify the test setup.
        XNode xNode = new XNode(null, nodeWithEmptyName, variables);

        // Act
        String identifier = xNode.getValueBasedIdentifier();

        // Assert
        assertEquals("Identifier should be empty for a root node with an empty name and no 'id' attributes.",
                "", identifier);
    }
}