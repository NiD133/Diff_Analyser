package org.apache.ibatis.parsing;

import org.junit.Test;
import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class.
 * This specific test focuses on the behavior of attribute-retrieval methods.
 */
// The class name is kept to match the original context, though in a real-world
// scenario, it would likely be renamed to something like XNodeTest.
public class XNode_ESTestTest49 extends XNode_ESTest_scaffolding {

    /**
     * Verifies that calling getLongAttribute with a null attribute name throws a NullPointerException.
     * This is the expected behavior because the underlying Properties object, which stores
     * the attributes, does not permit null keys.
     */
    @Test(expected = NullPointerException.class)
    public void getLongAttributeShouldThrowNullPointerExceptionWhenNameIsNull() {
        // Arrange: Create an XNode with a dummy node and no variables.
        Node dummyNode = new IIOMetadataNode();
        Properties emptyVariables = new Properties();
        XNode xNode = new XNode(null, dummyNode, emptyVariables);
        Long defaultValue = 94L;

        // Act: Attempt to get an attribute using a null name.
        // The test expects this line to throw a NullPointerException.
        xNode.getLongAttribute(null, defaultValue);

        // Assert: The test framework verifies that a NullPointerException was thrown,
        // as specified by the @Test(expected = ...) annotation.
    }
}