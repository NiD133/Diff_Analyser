package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link DataFormatMatcher} class.
 */
public class DataFormatMatcherTest {

    /**
     * Verifies that a DataFormatMatcher created with a non-null factory but a null
     * MatchStrength correctly reports having a match and defaults to an INCONCLUSIVE strength.
     *
     * This scenario is important for cases where a format is identified, but its
     * confidence level isn't specified.
     */
    @Test
    public void shouldReportMatchAndInconclusiveStrengthWhenCreatedWithNullStrength() {
        // Arrange: Set up the necessary objects for creating a DataFormatMatcher.
        byte[] inputData = new byte[10];
        InputAccessor.Std inputAccessor = new InputAccessor.Std(inputData);
        JsonFactory matchedFactory = new JsonFactory();
        MatchStrength unspecifiedStrength = null; // Explicitly use null for clarity

        // Act: Create a DataFormatMatcher instance with a null match strength.
        DataFormatMatcher matcher = inputAccessor.createMatcher(matchedFactory, unspecifiedStrength);

        // Assert: Verify the state of the newly created matcher.
        // 1. It should report a match because a factory was provided.
        assertTrue("A matcher created with a factory should report hasMatch() as true.", matcher.hasMatch());

        // 2. The match strength should default to INCONCLUSIVE as per the class contract.
        assertEquals("Match strength should default to INCONCLUSIVE when created with null.",
                MatchStrength.INCONCLUSIVE, matcher.getMatchStrength());
    }
}