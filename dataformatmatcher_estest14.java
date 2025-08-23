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
     * Tests that {@code DataFormatMatcher.hasMatch()} returns true if a {@link JsonFactory}
     * is present, even when the {@link MatchStrength} is {@code NO_MATCH}.
     * This clarifies that the presence of a match is determined by the factory, not the strength.
     */
    @Test
    public void hasMatchShouldReturnTrueWhenFactoryIsPresentRegardlessOfStrength() {
        // Arrange
        byte[] inputData = new byte[16];
        InputAccessor.Std inputAccessor = new InputAccessor.Std(inputData);
        JsonFactory jsonFactory = new JsonFactory();
        MatchStrength noMatchStrength = MatchStrength.NO_MATCH;

        // Act
        // Create a matcher with a valid factory but the lowest possible match strength.
        DataFormatMatcher matcher = inputAccessor.createMatcher(jsonFactory, noMatchStrength);

        // Assert
        // The matcher should correctly report the strength it was created with.
        assertEquals(noMatchStrength, matcher.getMatchStrength());

        // Crucially, hasMatch() should return true because a JsonFactory was provided.
        // The presence of a factory, not the strength, determines the result of hasMatch().
        assertTrue("Expected hasMatch() to be true because a JsonFactory was set.", matcher.hasMatch());
    }
}