package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link W3CDom} helper class.
 */
public class W3CDomTest {

    /**
     * Verifies that calling selectXpath with a null context node throws an IllegalArgumentException.
     */
    @Test
    public void selectXpathWithNullContextNodeThrowsIllegalArgumentException() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        String anyXpathQuery = "/"; // The query itself is irrelevant for this test.
        Node nullContextNode = null;

        // Act & Assert
        try {
            w3cDom.selectXpath(anyXpathQuery, nullContextNode);
            fail("Expected an IllegalArgumentException to be thrown for a null context node.");
        } catch (IllegalArgumentException e) {
            // Verify the exception message is clear and helpful.
            String expectedMessage = "The parameter 'contextNode' must not be null.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}