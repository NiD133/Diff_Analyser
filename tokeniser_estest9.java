package org.jsoup.parser;

import org.junit.Test;

/**
 * Tests for the {@link Tokeniser} class, focusing on illegal state transitions.
 */
public class TokeniserTest {

    /**
     * Verifies that calling {@code emitTagPending()} is illegal when the tokeniser is not
     * in the process of parsing a tag. This is an important contract of the method,
     * as it should only be called by the state machine at the appropriate time.
     */
    @Test(expected = NullPointerException.class)
    public void emitTagPendingThrowsExceptionWhenNoTagIsBeingParsed() {
        // Arrange:
        // 1. Create an XmlTreeBuilder.
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // 2. Parse a minimal fragment. The main purpose of this call is to initialize
        // the internal state of the builder (e.g., its parser and settings), which is
        // a prerequisite for the Tokeniser's constructor. After this, the builder is idle.
        builder.parse("</", "");

        // 3. Create a new Tokeniser from the initialized but idle builder.
        // This tokeniser has a "pending" tag object, but it has not been populated
        // with a tag name or any other data from an input stream.
        Tokeniser tokeniser = new Tokeniser(builder);

        // Act & Assert:
        // Calling emitTagPending() in this invalid state should fail. The current
        // implementation throws a NullPointerException because the pending tag's internal
        // state is incomplete (e.g., its name is null).
        tokeniser.emitTagPending();
    }
}