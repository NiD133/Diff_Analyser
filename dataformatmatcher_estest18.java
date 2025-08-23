package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link DataFormatMatcher} class.
 */
public class DataFormatMatcherTest {

    /**
     * Tests that a DataFormatMatcher, when created with a specific factory and match strength,
     * correctly reports the format name and the match strength.
     */
    @Test
    public void shouldReturnCorrectFormatNameAndStrengthOnMatch() {
        // Arrange: Set up the test data and objects.
        // The actual content of the input data is not relevant for this test.
        byte[] inputData = new byte[4];
        JsonFactory jsonFactory = new JsonFactory(); // The factory that "matched" the data.
        InputAccessor.Std inputAccessor = new InputAccessor.Std(inputData);
        MatchStrength expectedStrength = MatchStrength.SOLID_MATCH;

        // Act: Create the DataFormatMatcher, which is the object under test.
        DataFormatMatcher matcher = inputAccessor.createMatcher(jsonFactory, expectedStrength);

        // Assert: Verify that the matcher returns the expected values.
        // Check if the strength is the one we provided.
        assertEquals(expectedStrength, matcher.getMatchStrength());

        // Check if the format name corresponds to the provided JsonFactory.
        // The default format name for JsonFactory is "JSON".
        assertEquals(jsonFactory.getFormatName(), matcher.getMatchedFormatName());
    }
}