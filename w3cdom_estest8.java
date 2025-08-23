package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;

import javax.imageio.metadata.IIOMetadataNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// The original class structure is kept, as the focus is on improving the test case itself.
public class W3CDom_ESTestTest8 extends W3CDom_ESTest_scaffolding {

    /**
     * Verifies that sourceNodes() throws a DOMException when given a NodeList
     * containing nodes that do not support the getUserData() method. The method
     * relies on getUserData() to retrieve the original jsoup node.
     */
    @Test
    public void sourceNodesShouldThrowExceptionForUnsupportedNodeImplementation() {
        // Arrange
        W3CDom w3cDom = new W3CDom();

        // We use IIOMetadataNode because it's a standard org.w3c.dom.Node implementation
        // that is known to throw a DOMException on calls to getUserData(). This simulates
        // passing a W3C node to sourceNodes() that was not created by W3CDom.
        final String nodeName = "testNode";
        IIOMetadataNode unsupportedNode = new IIOMetadataNode(nodeName);
        NodeList nodeListWithUnsupportedNode = unsupportedNode.getElementsByTagName(nodeName);

        // Act & Assert
        try {
            w3cDom.sourceNodes(nodeListWithUnsupportedNode, Document.class);
            fail("Expected a DOMException to be thrown because IIOMetadataNode does not support getUserData().");
        } catch (DOMException e) {
            // Success: The expected exception was caught.
            // We can further verify that the exception is of the expected type.
            assertEquals("The exception code should indicate that the operation is not supported.",
                DOMException.NOT_SUPPORTED_ERR, e.code);
        }
    }
}