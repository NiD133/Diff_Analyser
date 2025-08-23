package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * This test class contains refactored tests for the {@link W3CDom} class.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class W3CDom_ESTestTest37 {

    /**
     * Verifies that converting a Jsoup document to a W3C DOM does not alter the
     * namespace-aware setting of the W3CDom converter instance.
     */
    @Test
    public void fromJsoupDoesNotAlterNamespaceAwareProperty() {
        // Arrange
        // A new W3CDom instance is namespace-aware by default.
        W3CDom w3cDom = new W3CDom();

        // The original auto-generated test used a document containing only a DOCTYPE,
        // likely to ensure that specific code path is exercised. We use a valid, simple DOCTYPE.
        Document jsoupDocument = Parser.parse("<!DOCTYPE html>", "");

        // Act
        // Convert the Jsoup document. The specific content is less important than
        // exercising the conversion process itself.
        w3cDom.fromJsoup(jsoupDocument);

        // Assert
        // The conversion process should not have any side effects on the converter's properties.
        assertTrue("The namespaceAware property should remain true after conversion", w3cDom.namespaceAware());
    }
}