package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test suite for {@link W3CDom}.
 */
public class W3CDomTest {

    /**
     * Verifies that fromJsoup throws a NullPointerException if the internal
     * DocumentBuilderFactory is null. This is a white-box test for a defensive
     * check, as the factory is initialized in the constructor and should not
     * normally be null.
     */
    @Test(expected = NullPointerException.class)
    public void fromJsoupThrowsExceptionWhenInternalFactoryIsNull() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Parser.parse("", "https://jsoup.org/");

        // Manually set the internal factory to null to simulate an invalid state.
        // The fromJsoup() method should fail fast in this scenario.
        w3cDom.factory = null;

        // Act & Assert
        // This call is expected to throw a NullPointerException.
        w3cDom.fromJsoup(jsoupDoc);
    }
}