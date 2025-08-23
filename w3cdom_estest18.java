package org.jsoup.helper;

import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * Test suite for the {@link W3CDom} class.
 */
public class W3CDomTest {

    /**
     * Verifies that the fromJsoup method throws a NullPointerException
     * if the internal DocumentBuilderFactory is null. This ensures the method
     * fails fast in case of an invalid internal state.
     */
    @Test(expected = NullPointerException.class)
    public void fromJsoupShouldThrowNPEWhenFactoryIsNull() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Element jsoupElement = new Element("p"); // A simple, standard element is sufficient.

        // Simulate an invalid internal state by nullifying the factory.
        // This is a white-box test to ensure robustness against internal errors.
        w3cDom.factory = null;

        // Act & Assert
        // This call is expected to throw a NullPointerException, which is
        // caught and verified by the @Test(expected=...) annotation.
        w3cDom.fromJsoup(jsoupElement);
    }
}