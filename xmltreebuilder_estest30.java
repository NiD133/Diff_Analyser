package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link XmlTreeBuilder} class, focusing on handling invalid inputs.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that calling insertDoctypeFor with a Doctype token that has a null name
     * correctly throws a NullPointerException. The builder requires a valid name to
     * create a DocumentType node.
     */
    @Test
    public void insertDoctypeForWithNullNameTokenThrowsNullPointerException() {
        // Arrange: Create a builder and a Doctype token in an invalid state (null name).
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype invalidDoctypeToken = new Token.Doctype(); // A default token has a null name.

        // Act & Assert: Verify that a NullPointerException is thrown when processing the token.
        NullPointerException exception = assertThrows(
            "Should throw NullPointerException for a doctype token with a null name",
            NullPointerException.class,
            () -> xmlTreeBuilder.insertDoctypeFor(invalidDoctypeToken)
        );

        // Optional: Further assert that the exception message is null, matching the exact behavior.
        assertNull("The exception message was expected to be null.", exception.getMessage());
    }
}