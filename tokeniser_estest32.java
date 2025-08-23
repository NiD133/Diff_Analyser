package org.jsoup.parser;

import org.junit.Test;

/**
 * This test class verifies the behavior of the Tokeniser, particularly its internal state machine.
 */
public class Tokeniser_ESTestTest32 extends Tokeniser_ESTest_scaffolding {

    /**
     * Tests that calling emitCommentPending() on a Tokeniser that is not
     * in a comment-parsing state results in a NullPointerException.
     *
     * This test ensures the robustness of the internal state machine by verifying that
     * calling a state-specific method out of its intended sequence is handled by
     * failing fast, which prevents unpredictable behavior.
     */
    @Test(expected = NullPointerException.class)
    public void emitCommentPendingFailsWhenCalledOutOfSequence() {
        // Arrange: Create a Tokeniser.
        // The parse() call is a convenient way to initialize the underlying TreeBuilder,
        // which is a prerequisite for creating a Tokeniser. The parsed content itself
        // is not relevant to this test's objective.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("<!-- setup -->", "http://example.com");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);

        // Act: Directly invoke an internal state machine method out of its normal context.
        // We expect this to fail because the necessary state has not been established
        // by the state machine's regular flow (e.g., transitioning into a comment state).
        tokeniser.emitCommentPending();

        // Assert: A NullPointerException is expected, as specified by the @Test annotation.
    }
}