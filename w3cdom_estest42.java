package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// The test class name is from the original. In a real-world scenario, 
// this would be renamed to something like W3CDomTest.
public class W3CDom_ESTestTest42 extends W3CDom_ESTest_scaffolding {

    /**
     * Verifies that the namespace-aware setting of a W3CDom instance can be configured.
     * The test confirms the default state is namespace-aware (true) and then checks
     * that calling `namespaceAware(false)` successfully changes the state.
     */
    @Test(timeout = 4000)
    public void namespaceAwareCanBeDisabled() {
        // Arrange: Create a new W3CDom instance.
        W3CDom w3cDom = new W3CDom();

        // Assert: Verify the default state is namespace-aware.
        assertTrue("W3CDom should be namespace-aware by default.", w3cDom.namespaceAware());

        // Act: Disable namespace awareness. The method returns 'this' for chaining.
        w3cDom.namespaceAware(false);

        // Assert: Verify the state has been updated.
        assertFalse("The namespace-aware setting should be updated to false.", w3cDom.namespaceAware());
    }
}