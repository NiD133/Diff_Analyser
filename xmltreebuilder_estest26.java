package org.jsoup.parser;

import org.jsoup.nodes.LeafNode;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class contains tests for the {@link XmlTreeBuilder}.
 * This specific test was improved for clarity and maintainability.
 */
public class XmlTreeBuilder_ESTestTest26 extends XmlTreeBuilder_ESTest_scaffolding {

    /**
     * Verifies that calling {@link XmlTreeBuilder#insertLeafNode(LeafNode)} with a null argument
     * correctly throws an {@link IllegalArgumentException}. This ensures the method is robust
     * against invalid input.
     */
    @Test(timeout = 4000)
    public void insertLeafNode_withNullNode_throwsIllegalArgumentException() {
        // Arrange: Create a tree builder and initialize its internal state.
        // A minimal parse is required to set up the document and ensure the builder's
        // internal stack has a current element to which a node could be appended.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        parser.parseInput("", ""); // Initializes the builder with a document root.

        // Act & Assert: Attempt to insert a null node and verify the expected exception.
        try {
            xmlTreeBuilder.insertLeafNode(null);
            fail("Expected an IllegalArgumentException to be thrown for a null node, but no exception was raised.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct, as it's part of the method's contract.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}