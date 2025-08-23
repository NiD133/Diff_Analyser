package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains tests for the DataFormatMatcher.
 * The original test class name suggests it may have been auto-generated,
 * so the contained tests have been refactored for clarity.
 */
public class DataFormatMatcher_ESTestTest13 extends DataFormatMatcher_ESTest_scaffolding {

    /**
     * Verifies the state of a DataFormatMatcher immediately after creation.
     *
     * This test ensures that when a DataFormatMatcher is created with a valid JsonFactory,
     * it correctly reports that a match has been found. It also confirms that if a null
     * MatchStrength is provided during creation, getMatchStrength() correctly defaults
     * to INCONCLUSIVE.
     */
    @Test(timeout = 4000)
    public void matcherShouldIndicateMatchWhenCreatedWithAFactory() {
        // Arrange: Set up the necessary objects for creating a DataFormatMatcher.
        // The actual content of the input data is not relevant for this specific test.
        byte[] inputData = new byte[9];
        JsonFactory jsonFactory = new JsonFactory();
        InputAccessor.Std inputAccessor = new InputAccessor.Std(inputData);

        // Act: Create a DataFormatMatcher. The createMatcher method is a simple factory
        // that directly calls the DataFormatMatcher constructor. We pass a null
        // MatchStrength to test the default handling.
        DataFormatMatcher matcher = inputAccessor.createMatcher(jsonFactory, null);

        // Assert: Verify the state of the newly created matcher.

        // A match should be reported because a non-null JsonFactory was provided.
        // The hasMatch() method simply checks if the internal factory is not null.
        assertTrue("A match should be reported when a JsonFactory is supplied.", matcher.hasMatch());

        // When a null MatchStrength is passed to the constructor via the factory method,
        // getMatchStrength() is expected to return the default value, INCONCLUSIVE.
        assertEquals("Match strength should default to INCONCLUSIVE when created with null.",
                     MatchStrength.INCONCLUSIVE, matcher.getMatchStrength());
    }
}