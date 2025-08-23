package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link XNode} class, focusing on edge cases.
 */
public class XNodeTest {

    /**
     * Tests that XNode.toString() throws a NullPointerException when operating on a
     * DOM node involved in a circular reference.
     *
     * This specific failure is triggered by a combination of two conditions:
     * 1. A malformed DOM where a parent node is also a descendant of itself.
     * 2. An XPathParser initialized with a null Document.
     *
     * While a circular reference would typically cause a StackOverflowError during
     * recursion, the interaction with the parser's internal state leads to an NPE instead.
     */
    @Test
    public void toStringShouldThrowNPEForNodeInACircularDomReference() {
        // Arrange: Create a DOM structure with a circular reference.
        // The structure will be: parentNode -> childNode -> parentNode
        Node parentNode = new IIOMetadataNode();
        Node childNode = new IIOMetadataNode();

        // Establish the circular dependency.
        // Note: IIOMetadataNode is a lenient DOM implementation that allows creating
        // this invalid structure, which would throw a DOMException with a stricter DOM implementation.
        parentNode.appendChild(childNode);
        childNode.insertBefore(parentNode, childNode); // Makes parentNode a child of its own child.

        // Arrange: Create an XPathParser with a null document. This is a key part of the setup
        // required to trigger the NullPointerException instead of a StackOverflowError.
        XPathParser parserWithNullDocument = new XPathParser((Document) null);
        Properties emptyVariables = new Properties();

        // The XNode instance wraps the parentNode, which is part of the cycle.
        XNode xNode = new XNode(parserWithNullDocument, parentNode, emptyVariables);

        // Act & Assert: Expect a NullPointerException when calling toString().
        try {
            xNode.toString();
            fail("Expected a NullPointerException due to the circular DOM structure and null-document parser.");
        } catch (NullPointerException e) {
            // This is the expected exception. The test passes.
        }
    }
}