package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that newXNode throws a NullPointerException when passed a null Node.
     * This is expected because the XNode constructor requires a non-null Node to operate on.
     */
    @Test(expected = NullPointerException.class)
    public void newXNodeShouldThrowNullPointerExceptionForNullNode() {
        // Arrange: Create a valid XNode instance to work with.
        // The contents of the parser and node are not relevant to this specific test.
        XPathParser xPathParser = new XPathParser((Document) null, false);
        Node dummyNode = new IIOMetadataNode();
        Properties variables = new Properties();
        XNode xNode = new XNode(xPathParser, dummyNode, variables);

        // Act: Call the method under test with a null argument.
        xNode.newXNode(null);

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected) annotation.
    }
}