package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the static method {@link TokenQueue#escapeCssIdentifier(String)}.
 */
public class TokenQueueEscapeCssIdentifierTest {

    /**
     * Verifies that calling escapeCssIdentifier with a null input string
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void escapeCssIdentifierWithNullInputShouldThrowException() {
        TokenQueue.escapeCssIdentifier(null);
    }
}