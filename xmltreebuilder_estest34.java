package org.jsoup.parser;

import org.junit.Test;

/**
 * Tests for the {@link XmlTreeBuilder} focusing on its token processing logic.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that the process method throws a NullPointerException when given a
     * comment token that has not been properly initialized (i.e., its internal data is null).
     * This is an edge case that ensures the builder is robust against malformed token inputs.
     */
    @Test(expected = NullPointerException.class)
    public void processShouldThrowNullPointerExceptionForUninitializedCommentToken() {
        // Arrange: Create a new XmlTreeBuilder and a comment token in its default, uninitialized state.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment uninitializedCommentToken = new Token.Comment();

        // Act & Assert: Processing the uninitialized token is expected to throw a NullPointerException
        // because the underlying TreeBuilder will attempt to access its null data.
        xmlTreeBuilder.process(uninitializedCommentToken);
    }
}