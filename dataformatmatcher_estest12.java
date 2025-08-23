package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/*
 * The original class name "DataFormatMatcher_ESTestTest12" and inheritance
 * from a scaffolding class suggest it was auto-generated.
 * The test content has been rewritten for clarity and correctness.
 */
public class DataFormatMatcher_ESTestTest12 extends DataFormatMatcher_ESTest_scaffolding {

    /**
     * Verifies that createParserWithMatch() returns null when the DataFormatMatcher
     * was created to represent a "no match" scenario (i.e., with a null JsonFactory).
     */
    @Test
    public void createParserWithMatch_ShouldReturnNull_WhenNoMatchExists() throws IOException {
        // Arrange: Create a DataFormatMatcher that represents a scenario where no data format was matched.
        // This is achieved by passing a null JsonFactory to the createMatcher method.
        byte[] emptyInput = new byte[0];
        InputAccessor.Std inputAccessor = new InputAccessor.Std(emptyInput);
        JsonFactory noMatchingFactory = null;
        MatchStrength strength = MatchStrength.NO_MATCH;

        DataFormatMatcher matcher = inputAccessor.createMatcher(noMatchingFactory, strength);

        // Pre-condition check: Ensure the matcher is in the expected "no match" state.
        assertFalse("A matcher with no factory should report hasMatch() as false", matcher.hasMatch());
        assertEquals(MatchStrength.NO_MATCH, matcher.getMatchStrength());

        // Act: Attempt to create a parser from the matcher.
        JsonParser resultParser = matcher.createParserWithMatch();

        // Assert: The method should return null, and the matcher's state should remain unchanged.
        assertNull("createParserWithMatch() must return null when no format has been matched", resultParser);
        assertEquals("The match strength should not be altered by the call",
                MatchStrength.NO_MATCH, matcher.getMatchStrength());
    }
}