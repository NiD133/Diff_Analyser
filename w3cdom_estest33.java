package org.jsoup.helper;

import org.junit.Test;

/**
 * Test suite for the {@link W3CDom} helper class.
 */
public class W3CDomTest {

    /**
     * Verifies that the asString() method throws a NullPointerException
     * when passed a null W3C Document. This is the expected behavior for
     * invalid input, preventing silent failures downstream.
     */
    @Test(expected = NullPointerException.class)
    public void asStringWithNullDocumentThrowsNullPointerException() {
        // Arrange: Create an instance of the class under test.
        W3CDom w3cDom = new W3CDom();

        // Act: Call the method with a null argument.
        // The @Test(expected) annotation handles the assertion.
        w3cDom.asString(null);
    }
}