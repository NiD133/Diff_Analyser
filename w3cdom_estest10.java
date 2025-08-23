package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.nodes.XmlDeclaration;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.NodeList;

import static org.junit.Assert.fail;

/**
 * This test class contains tests for the W3CDom helper class.
 * This specific test case focuses on the robustness of the sourceNodes method.
 */
public class W3CDomTest {

    /**
     * Tests that the sourceNodes method throws a NullPointerException when given a malformed NodeList
     * that contains null elements. This ensures the method is not robust against buggy DOM implementations
     * but documents its current behavior.
     */
    @Test
    public void sourceNodesThrowsNPEWhenIteratingMalformedNodeList() {
        // Arrange
        W3CDom w3cDom = new W3CDom();

        // Create a malformed NodeList using a quirk in IIOMetadataNode.
        // The following call puts the node into an inconsistent state where its
        // NodeList implementation reports a positive length, but item(0) returns null.
        // This simulates a buggy or corrupt DOM structure.
        IIOMetadataNode malformedNode = new IIOMetadataNode();
        malformedNode.insertBefore(malformedNode, malformedNode);

        NodeList malformedNodeList = malformedNode;

        // Act & Assert
        try {
            // The method is expected to fail when it encounters the null item in the list.
            w3cDom.sourceNodes(malformedNodeList, XmlDeclaration.class);
            fail("A NullPointerException was expected, but no exception was thrown.");
        } catch (NullPointerException e) {
            // Success: The expected exception was caught, confirming the method's behavior.
        }
    }
}