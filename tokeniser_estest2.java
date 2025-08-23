package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.XmlTreeBuilder;

// Note: The original class name and inheritance are preserved.
public class Tokeniser_ESTestTest2 extends Tokeniser_ESTest_scaffolding {

    /**
     * Verifies that calling eofError() adds a "premature end of file" parse error
     * to the error list, corresponding to the provided tokeniser state.
     */
    @Test
    public void eofErrorAddsParseErrorForGivenState() {
        // Arrange: Set up a Tokeniser.
        // The XmlTreeBuilder is used to configure the Tokeniser with a parser and an error list.
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // The parse() method initializes the internal state (reader, parser) required by the Tokeniser.
        // The input content is empty because it's not relevant to testing the eofError method.
        builder.parse("", "https://example.com");

        Tokeniser tokeniser = new Tokeniser(builder);
        // The Tokeniser shares its error list with the builder's parser.
        ParseErrorList errors = builder.parser.getErrors();

        assertTrue("The error list should be empty before the error is triggered.", errors.isEmpty());

        // Act: Trigger an End-Of-File error in a specific state.
        TokeniserState errorState = TokeniserState.ScriptDataEscapedLessthanSign;
        tokeniser.eofError(errorState);

        // Assert: Verify that a specific parse error was added.
        assertEquals("There should be exactly one error after eofError is called.", 1, errors.size());

        ParseError error = errors.get(0);
        String expectedMessage = "Unexpectedly reached end of file (in " + errorState + " state)";
        assertEquals("The error message should match the expected format.", expectedMessage, error.getErrorMessage());
    }
}