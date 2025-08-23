package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link W3CDom} helper class.
 */
public class W3CDomTest {

    /**
     * Verifies that a new W3CDom instance is configured to be namespace-aware by default.
     * This is the expected initial state for the object.
     */
    @Test
    public void newW3CDomIsNamespaceAwareByDefault() {
        // Arrange: Create a new instance of the class under test.
        W3CDom w3cDom = new W3CDom();

        // Act & Assert: Check the default value of the namespaceAware property.
        assertTrue("W3CDom should be namespace-aware by default upon instantiation.", w3cDom.namespaceAware());
    }
}