package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * Test suite for the {@link Elements} class, focusing on exception handling.
 */
public class ElementsExceptionTest {

    /**
     * Verifies that calling the attr(key, value) method with a null attribute key
     * throws a NullPointerException, as attribute keys are not permitted to be null.
     */
    @Test(expected = NullPointerException.class)
    public void attrWithNullKeyThrowsNullPointerException() {
        // Arrange: Create a non-empty Elements object. The specific content is not
        // important for this test, only that the collection contains at least one element.
        Document doc = Document.createShell("");
        Elements elements = doc.select("body");

        // Act & Assert: Attempting to set an attribute with a null key should
        // immediately throw a NullPointerException. The expected exception is declared
        // in the @Test annotation.
        elements.attr(null, "any-value");
    }
}