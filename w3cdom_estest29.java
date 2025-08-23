package org.jsoup.helper;

import org.junit.Test;

/**
 * Test suite for the {@link W3CDom} helper class, focusing on edge cases and invalid inputs.
 */
public class W3CDomTest {

    /**
     * Verifies that the {@link W3CDom#contextNode(org.w3c.dom.Document)} method
     * throws a {@link NullPointerException} when passed a null document.
     * This is the expected behavior as the method cannot operate on a null input.
     */
    @Test(expected = NullPointerException.class)
    public void contextNodeShouldThrowNullPointerExceptionWhenDocumentIsNull() {
        // Arrange: Create an instance of the class under test.
        W3CDom w3cDom = new W3CDom();

        // Act & Assert: Call the method with a null argument.
        // The @Test(expected) annotation handles the assertion,
        // ensuring a NullPointerException is thrown.
        w3cDom.contextNode(null);
    }
}