package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link W3CDom} helper class.
 */
public class W3CDomTest {

    /**
     * Verifies that the namespaceAware property can be set to true and its state
     * is correctly retrieved by the corresponding getter.
     */
    @Test
    public void namespaceAwarePropertyIsSettableAndGettable() {
        // Arrange: Create a new W3CDom instance.
        W3CDom w3cDom = new W3CDom();

        // Act: Set the namespaceAware property to true.
        w3cDom.namespaceAware(true);

        // Assert: Verify that the getter returns true.
        assertTrue("The namespaceAware property should be true after being set.", w3cDom.namespaceAware());
    }
}