package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.Document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link W3CDom} helper class.
 */
public class W3CDomTest {

    /**
     * Verifies that the selectXpath method throws an IllegalArgumentException
     * when the provided W3C Document context node is null.
     */
    @Test
    public void selectXpathShouldThrowExceptionForNullContextNode() {
        // Arrange: Create a W3CDom instance and define a null context.
        W3CDom w3cDom = new W3CDom();
        String anyXpathQuery = "//div"; // The query itself is not relevant for this test.
        Document nullDocumentContext = null;

        // Act & Assert: Attempt to call the method and expect an exception.
        try {
            w3cDom.selectXpath(anyXpathQuery, nullDocumentContext);
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct.
            String expectedMessage = "The parameter 'contextNode' must not be null.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}