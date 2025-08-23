package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link DataFormatMatcher} class.
 * This test focuses on the state of a matcher instance representing a non-match.
 */
public class DataFormatMatcher_ESTestTest8 extends DataFormatMatcher_ESTest_scaffolding {

    /**
     * Verifies that a DataFormatMatcher created with a 'NO_MATCH' strength
     * correctly reports its state via hasMatch() and getMatchStrength().
     */
    @Test
    public void shouldCorrectlyReportNoMatchWhenCreatedWithNoMatchStrength() {
        // Arrange: Set up the input and the expected match result.
        // In this case, we simulate a scenario where no data format was matched.
        byte[] emptyInput = new byte[0];
        InputAccessor.Std inputAccessor = new InputAccessor.Std(emptyInput);
        MatchStrength noMatchStrength = MatchStrength.NO_MATCH;
        JsonFactory nonMatchingFactory = null; // A null factory is appropriate for a non-match.

        // Act: Create the DataFormatMatcher with the non-matching result.
        DataFormatMatcher matcher = inputAccessor.createMatcher(nonMatchingFactory, noMatchStrength);

        // Assert: Verify the matcher's state reflects the lack of a match.
        assertFalse("A matcher with NO_MATCH strength should return false for hasMatch().", matcher.hasMatch());
        assertEquals("The match strength should be correctly reported as NO_MATCH.",
                MatchStrength.NO_MATCH, matcher.getMatchStrength());
    }
}