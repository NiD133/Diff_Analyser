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
     * Verifies that a DataFormatMatcher, when created with a specific factory and match strength,
     * correctly reports that it has a match and returns the given strength.
     */
    @Test
    public void shouldCorrectlyReportMatchAndStrengthWhenCreated() {
        // Arrange: Set up the necessary objects for the test.
        byte[] emptyInput = new byte[0];
        InputAccessor.Std accessor = new InputAccessor.Std(emptyInput);
        JsonFactory jsonFactory = new JsonFactory();
        MatchStrength expectedStrength = MatchStrength.WEAK_MATCH;

        // Act: Create the DataFormatMatcher instance. This is the action under test.
        DataFormatMatcher matcher = accessor.createMatcher(jsonFactory, expectedStrength);

        // Assert: Verify the state of the newly created matcher.
        assertTrue("A matcher created with a factory should indicate a match.", matcher.hasMatch());
        assertEquals("The match strength should be the one provided during creation.",
                expectedStrength, matcher.getMatchStrength());
    }
}