package org.jsoup.parser;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

/**
 * This test class contains tests for the Tokeniser.
 * The original class name, Tokeniser_ESTestTest23, is from an auto-generated suite.
 * In a real-world scenario, it would be renamed to something more descriptive,
 * like TokeniserErrorHandlingTest.
 */
public class Tokeniser_ESTestTest23 extends Tokeniser_ESTest_scaffolding {

    /**
     * Verifies that calling the error(TokeniserState) method correctly logs a
     * ParseError with an appropriate message and the current character position.
     */
    @Test
    public void errorLogsParseErrorForGivenStateAtCurrentPosition() {
        // Arrange: Set up the Tokeniser in a specific state.
        // The Tokeniser's internal state (like the reader's position) is initialized
        // through a TreeBuilder that has parsed some input.
        String inputHtml = "<body>";
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();

        // Parsing the input advances the reader to the end of the string.
        // The error position should reflect this final position.
        xmlBuilder.parse(inputHtml, "http://example.com/");

        Tokeniser tokeniser = new Tokeniser(xmlBuilder);
        TokeniserState errorState = TokeniserState.BeforeDoctypeSystemIdentifier;

        // Act: Trigger the error logging.
        tokeniser.error(errorState);

        // Assert: Verify that the error was logged correctly.
        List<ParseError> errors = xmlBuilder.parser.getErrors();

        // 1. Check that exactly one error was created.
        assertEquals("Should have logged exactly one parse error.", 1, errors.size());

        // 2. Check the details of the logged error.
        ParseError loggedError = errors.get(0);
        String expectedMessage = String.format("Unexpected character in state %s", errorState);
        int expectedPosition = inputHtml.length();

        assertEquals("Error message should match the expected format.", expectedMessage, loggedError.getErrorMessage());
        assertEquals("Error position should be at the end of the parsed input.", expectedPosition, loggedError.getPosition());
    }
}