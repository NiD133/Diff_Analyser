package org.apache.ibatis.parsing;

import org.junit.Test;

import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.Properties;

import static org.junit.Assert.assertNull;

// Note: The class name and inheritance from a scaffolding class are preserved
// from the original auto-generated test. In a typical, manually written test suite,
// the class would be named 'XNodeTest'.
public class XNode_ESTestTest123 extends XNode_ESTest_scaffolding {

    /**
     * Verifies that getBooleanAttribute() returns null when the specified attribute does not exist on the node.
     */
    @Test
    public void getBooleanAttributeShouldReturnNullWhenAttributeIsMissing() {
        // Arrange
        // 1. Create a DOM node that has no attributes.
        Node nodeWithoutAttributes = new IIOMetadataNode();

        // 2. The XPathParser is a required dependency for XNode, but it is not used by the
        //    method under test. We can pass a dummy instance.
        XPathParser dummyParser = new XPathParser((Document) null, true);

        // 3. The 'variables' Properties object is also a required dependency, but not used here.
        Properties variables = new Properties();

        XNode xNode = new XNode(dummyParser, nodeWithoutAttributes, variables);

        // Act
        // Attempt to retrieve a boolean value for an attribute that is not present.
        Boolean result = xNode.getBooleanAttribute("nonexistentAttribute");

        // Assert
        // The method should return null to indicate the attribute was not found.
        assertNull("Expected null for a missing boolean attribute", result);
    }
}