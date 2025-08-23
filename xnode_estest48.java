package org.apache.ibatis.parsing;

import org.junit.Test;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that getPath() throws a StackOverflowError when the underlying DOM
     * node has a circular reference (i.e., it is its own ancestor). This is the
     * expected behavior, as getPath() recursively traverses up the parent hierarchy.
     */
    @Test(expected = StackOverflowError.class)
    public void getPathShouldThrowStackOverflowErrorForCircularNodeHierarchy() {
        // Arrange: Create a DOM node and make it its own child, forming a circular reference.
        IIOMetadataNode selfReferencingNode = new IIOMetadataNode();
        selfReferencingNode.appendChild(selfReferencingNode);

        // The XPathParser is not used by getPath(), so it can be null for this test.
        Properties variables = new Properties();
        XNode xNode = new XNode(null, selfReferencingNode, variables);

        // Act: Call getPath(), which should trigger infinite recursion.
        // The @Test(expected) annotation asserts that a StackOverflowError is thrown.
        xNode.getPath();
    }
}