package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token.Comment;

// Note: The original test class name and scaffolding are kept to match the request context,
// but in a real-world scenario, they would be renamed to something like 'TokeniserTest'.
public class Tokeniser_ESTestTest35 extends Tokeniser_ESTest_scaffolding {

    /**
     * Verifies that createCommentPending() resets the internal comment token,
     * ensuring it's ready to build a new comment.
     */
    @Test(timeout = 4000)
    public void createCommentPendingShouldResetTheInternalCommentBuffer() {
        // Arrange: Create a tokeniser and simulate a state where its internal
        // comment buffer already contains data from a previous operation.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        // The parse method initializes the internal reader, which is required to instantiate
        // the Tokeniser. The input string itself is not relevant to this test's logic.
        xmlTreeBuilder.parse("", "https://example.com");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);

        // Directly access and "dirty" the internal comment token's data.
        // This ensures we are testing the method's reset behavior, not just its
        // behavior on a fresh token.
        Comment commentToken = tokeniser.commentPending;
        commentToken.getData().append("stale comment data");

        // Pre-condition check to ensure our setup is correct.
        assertFalse("Pre-condition failed: comment buffer should not be empty before test.",
                    commentToken.getData().toString().isEmpty());

        // Act: Call the method under test.
        tokeniser.createCommentPending();

        // Assert: The internal comment buffer should now be empty, ready for new data.
        String actualCommentData = commentToken.getData().toString();
        assertTrue("Expected the comment buffer to be empty after createCommentPending() was called.",
                   actualCommentData.isEmpty());
    }
}