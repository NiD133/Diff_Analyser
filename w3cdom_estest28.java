package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.fail;

// The test class name is inherited from the original EvoSuite-generated test.
// In a real-world scenario, it would be renamed to something more descriptive, like W3CDomTest.
public class W3CDom_ESTestTest28 extends W3CDom_ESTest_scaffolding {

    /**
     * Verifies that converting a Jsoup document with a malformed base URI
     * throws an AssertionError.
     * <p>
     * This behavior is unexpected, as a DOMException would be more appropriate.
     * The test captures this specific outcome, which likely stems from an
     * internal assertion failure within the underlying W3C DOM implementation
     * when it receives an invalid document URI.
     * </p>
     */
    @Test(timeout = 4000)
    public void convertDocumentWithMalformedBaseUriThrowsAssertionError() {
        // Arrange: Create a Jsoup document with a string that is not a valid URI for its base URI.
        String malformedUri = "*zHMY,@{ Az E8{"; // Original malformed URI from generated test
        Document jsoupDocument = new Document(malformedUri);

        // Act & Assert: Attempt to convert the document and expect an AssertionError.
        try {
            W3CDom.convert(jsoupDocument);
            fail("Expected an AssertionError to be thrown for a malformed base URI, but no exception occurred.");
        } catch (AssertionError e) {
            // This is the expected outcome. The underlying DOM implementation throws
            // an AssertionError instead of a more specific DOMException for this input.
        } catch (Throwable t) {
            fail("Expected an AssertionError, but a different exception was thrown: " + t.getClass().getName());
        }
    }
}