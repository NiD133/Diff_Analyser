package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the configuration of the {@link W3CDom} helper class.
 */
public class W3CDomTest {

    /**
     * Verifies that the namespace-aware setting is enabled by default and can be
     * successfully disabled.
     */
    @Test
    public void namespaceAwareIsOnByDefaultAndCanBeDisabled() {
        // Arrange: Create a new W3CDom instance.
        W3CDom w3cDom = new W3CDom();

        // Assert: Verify the default state is namespace-aware.
        assertTrue("W3CDom should be namespace-aware by default.", w3cDom.namespaceAware());

        // Act: Disable namespace awareness.
        w3cDom.namespaceAware(false);

        // Assert: Verify the setting has been updated.
        assertFalse("After calling namespaceAware(false), the setting should be disabled.", w3cDom.namespaceAware());
    }
}