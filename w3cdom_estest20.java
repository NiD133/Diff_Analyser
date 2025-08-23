package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link W3CDom} helper class.
 */
public class W3CDomTest {

    /**
     * Verifies that calling fromJsoup() with a null Document throws an IllegalArgumentException.
     * This is crucial for ensuring the method's input validation is working correctly.
     */
    @Test
    public void fromJsoupShouldThrowExceptionForNullDocument() {
        // Arrange: Create an instance of the class under test.
        W3CDom w3cDom = new W3CDom();
        Document nullDocument = null;

        // Act & Assert: Attempt the invalid operation and verify the expected exception.
        try {
            w3cDom.fromJsoup(nullDocument);
            fail("Expected an IllegalArgumentException to be thrown for a null input document.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct, confirming the right validation failed.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}