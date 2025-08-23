package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link W3CDom}.
 * This class focuses on specific behaviors of the W3CDom converter.
 */
public class W3CDomTest {

    /**
     * Verifies that executing an XPath query does not permanently alter the
     * namespace-aware configuration of the W3CDom instance.
     * The selectXpath method may temporarily modify this state for compatibility,
     * but it should be restored after the operation.
     */
    @Test
    public void selectXpathDoesNotAlterNamespaceAwareState() {
        // Arrange: Create a W3CDom converter and a sample document.
        // By default, a new W3CDom instance is namespace-aware.
        W3CDom w3cDom = new W3CDom();
        assertTrue("Initial state should be namespace-aware", w3cDom.namespaceAware());

        Document jsoupDoc = Document.createShell("http://example.com");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Act: Execute an XPath query.
        w3cDom.selectXpath("//body", w3cDoc);

        // Assert: Verify that the W3CDom instance's public state is unchanged.
        assertTrue("State should remain namespace-aware after selectXpath call", w3cDom.namespaceAware());
    }
}