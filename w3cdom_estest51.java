package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.Document;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.fail;

/**
 * Test suite for the {@link W3CDom} helper class, focusing on the asString method.
 */
public class W3CDomTest {

    /**
     * Verifies that W3CDom.asString() throws an IllegalArgumentException
     * when the provided W3C Document is null.
     */
    @Test
    public void asStringShouldThrowExceptionForNullDocument() {
        // Arrange: Define the invalid input for the method under test.
        Document nullDocument = null;
        Map<String, String> properties = Collections.emptyMap(); // Properties are not relevant for this test.

        // Act & Assert: Call the method and verify that it throws the expected exception.
        try {
            W3CDom.asString(nullDocument, properties);
            fail("Expected an IllegalArgumentException to be thrown for a null document input.");
        } catch (IllegalArgumentException expected) {
            // This is the correct behavior, so the test passes.
            // The exception is caught and the test completes successfully.
        }
    }
}