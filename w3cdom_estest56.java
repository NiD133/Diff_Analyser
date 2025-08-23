package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the W3CDom helper class.
 */
public class W3CDomTest {

    /**
     * Verifies that the namespace-aware flag can be configured.
     * It should be true by default and can be updated.
     */
    @Test
    public void namespaceAwareCanBeConfigured() {
        // Arrange: Create a new W3CDom instance.
        W3CDom w3cDom = new W3CDom();

        // Assert: The default setting for namespace awareness should be true.
        assertTrue("W3CDom should be namespace-aware by default.", w3cDom.namespaceAware());

        // Act: Disable namespace awareness.
        w3cDom.namespaceAware(false);

        // Assert: The setting should now be updated to false.
        assertFalse("The namespace-aware flag should be updated to false after setting.", w3cDom.namespaceAware());
    }
}