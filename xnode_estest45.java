package org.apache.ibatis.parsing;

import org.junit.Test;
import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;
import java.util.function.Supplier;
import org.w3c.dom.Node;

/**
 * This test suite contains tests for the {@link XNode} class.
 * The original test was auto-generated and has been improved for clarity.
 */
public class XNode_ESTestTest45 extends XNode_ESTest_scaffolding {

    /**
     * Verifies that `getStringAttribute` throws a NullPointerException when called
     * with a null default value supplier.
     *
     * This scenario is triggered when an attribute is requested that does not exist on the node.
     * The method then attempts to use the provided supplier to get a default value. If that
     * supplier is null, a NullPointerException is the expected behavior.
     */
    @Test(expected = NullPointerException.class)
    public void getStringAttributeWithNullDefaultSupplierShouldThrowNPE() {
        // Arrange: Create an XNode based on a node with no attributes.
        // The XPathParser is not used when retrieving attributes directly, so it can be null for this test.
        Node emptyNode = new IIOMetadataNode();
        Properties variables = new Properties();
        XNode xNode = new XNode(null, emptyNode, variables);

        // Act: Attempt to get a non-existent attribute with a null default value supplier.
        // The `Supplier<String>` is explicitly cast to null to match the method signature.
        xNode.getStringAttribute("anyAttributeName", (Supplier<String>) null);

        // Assert: The @Test(expected) annotation handles the exception assertion.
        // If no NullPointerException is thrown, the test will fail.
    }
}