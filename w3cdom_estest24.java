package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.nodes.Element;
import org.w3c.dom.Document;

/**
 * Test suite for the {@link W3CDom} helper class.
 */
public class W3CDomTest {

    /**
     * Verifies that calling {@link W3CDom#convert(Element, Document)} with null arguments
     * throws a NullPointerException. This is the expected behavior, as the method requires
     * valid Jsoup and W3C document objects to perform the conversion.
     */
    @Test(expected = NullPointerException.class)
    public void convertShouldThrowNullPointerExceptionForNullInputs() {
        // Arrange: Create an instance of the class under test.
        W3CDom w3cDom = new W3CDom();

        // Act & Assert: Call the method with null inputs and expect an exception.
        // The method should fail fast when provided with invalid arguments.
        w3cDom.convert((Element) null, (Document) null);
    }
}