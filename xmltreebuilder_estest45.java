package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the {@link XmlTreeBuilder} class.
 * This test case verifies the behavior of inserting a comment before parsing has started.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that calling insertCommentFor() on an uninitialized XmlTreeBuilder
     * throws a NullPointerException.
     * <p>
     * An XmlTreeBuilder is only initialized with a root document when a parse
     * operation begins. Calling insertion methods before that point is an invalid
     * state, as there is no document to append nodes to.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void insertCommentOnUninitializedBuilderThrowsNullPointerException() {
        // Arrange: Create a builder that has not been initialized via a parse method.
        // At this stage, its internal document object is null.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment();

        // Act: Attempt to insert a comment node. This should fail because there is no
        // document or parent element to attach the comment to.
        xmlTreeBuilder.insertCommentFor(commentToken);

        // Assert: The test expects a NullPointerException, which is handled by the
        // `expected` parameter in the @Test annotation.
    }
}