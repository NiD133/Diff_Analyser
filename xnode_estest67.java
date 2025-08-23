package org.apache.ibatis.parsing;

import java.util.Properties;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class.
 * This specific test focuses on constructor argument validation.
 */
// The original test class name and inheritance are kept for context.
// In a real-world scenario, the class might be renamed to XNodeTest.
public class XNode_ESTestTest67 extends XNode_ESTest_scaffolding {

    /**
     * Verifies that the XNode constructor throws a NullPointerException
     * when the provided Node object is null. This is expected because the
     * constructor immediately tries to access the node's properties.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNPEWhenNodeIsNull() {
        // Arrange: Prepare the arguments for the constructor.
        // The Node is intentionally set to null to trigger the exception.
        Properties variables = new Properties();
        Node nullNode = null;
        XPathParser nullParser = null;

        // Act & Assert:
        // Attempt to construct an XNode with a null Node.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        new XNode(nullParser, nullNode, variables);
    }
}