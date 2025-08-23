package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Test suite for the {@link Tokeniser} class.
 * This class focuses on testing specific error-handling behaviors.
 */
public class TokeniserTest {

    /**
     * Verifies that calling the tokeniser.error(String) method correctly
     * adds a new ParseError to the associated error list.
     */
    @Test
    public void errorMethodAddsParseErrorToList() {
        // Arrange
        // A Tokeniser is constructed with a TreeBuilder, which holds the Parser
        // and its list of errors. We must initialize the builder to get a valid state.
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.parse("<a>", "https://example.com"); // Initialize internal state.

        // The Tokeniser instance under test.
        Tokeniser tokeniser = new Tokeniser(treeBuilder);
        
        // The error list is retrieved from the parser associated with the tree builder.
        List<ParseError> errors = treeBuilder.getParser().getErrors();
        assertTrue("Error list should be empty before the test action", errors.isEmpty());
        
        String expectedErrorMessage = "This is a custom error message.";

        // Act
        tokeniser.error(expectedErrorMessage);

        // Assert
        assertEquals("Error list should contain exactly one error after the call", 1, errors.size());
        
        ParseError actualError = errors.get(0);
        assertEquals("The error message should match the one provided",
            expectedErrorMessage, actualError.getErrorMessage());
    }
}