package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link W3CDom}.
 * This class focuses on verifying the behavior of XPath selection.
 */
public class W3CDomTest {

    /**
     * Verifies that calling {@link W3CDom#selectXpath(String, Node)} with a syntactically
     * invalid XPath query throws an {@link IllegalStateException}.
     */
    @Test
    public void selectXpathWithInvalidQueryThrowsIllegalStateException() {
        // Arrange: Create a W3CDom instance and a context node.
        W3CDom w3cDom = new W3CDom();
        // A simple, concrete implementation of org.w3c.dom.Node to serve as a context.
        Node contextNode = new IIOMetadataNode();
        String invalidXPathQuery = "-"; // This is not a valid XPath expression.

        // Act & Assert: Attempt to select with the invalid query and verify the exception.
        try {
            w3cDom.selectXpath(invalidXPathQuery, contextNode);
            fail("Expected an IllegalStateException to be thrown for an invalid XPath query.");
        } catch (IllegalStateException e) {
            // The exception is expected. Now, verify its message is informative.
            String expectedMessagePrefix = "Could not evaluate XPath query [-]:";
            String actualMessage = e.getMessage();

            assertTrue(
                "Exception message should clearly indicate the failure and include the invalid query. " +
                "Expected prefix: '" + expectedMessagePrefix + "', but got: '" + actualMessage + "'",
                actualMessage.startsWith(expectedMessagePrefix)
            );
        }
    }
}