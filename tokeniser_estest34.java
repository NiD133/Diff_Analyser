package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the {@link Tokeniser} class.
 */
public class TokeniserTest {

    /**
     * Verifies that calling createBogusCommentPending() on an initialized Tokeniser
     * does not throw an exception.
     *
     * This is a basic "smoke test" to ensure the method can be invoked safely
     * without causing a crash, which is a common goal for tool-generated tests.
     */
    @Test
    public void createBogusCommentPendingDoesNotThrowException() {
        // Arrange: Set up a Tokeniser. The specific input used to initialize the
        // parser is not relevant to this test, as we are only checking the
        // behavior of creating a pending token.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("", "http://example.com"); // Initialize with empty input
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);

        // Act: Call the method under test.
        tokeniser.createBogusCommentPending();

        // Assert: The test is successful if no exception is thrown.
        // No explicit assertion is needed.
    }
}