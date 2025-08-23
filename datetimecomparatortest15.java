package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.Assert.assertNull;

/**
 * Contains tests for the {@link DataFormatMatcher} class.
 */
public class DataFormatMatcherTest {

    /**
     * Tests that {@link DataFormatMatcher#getMatchedFormatName()} returns null
     * when no data format has been matched.
     *
     * This scenario is simulated by creating a DataFormatMatcher instance
     * with a null JsonFactory, which represents the absence of a successful match.
     */
    @Test
    public void getMatchedFormatNameShouldReturnNullWhenNoMatchIsFound() {
        // Arrange: Set up a DataFormatMatcher representing a state where no format was matched.
        // An empty input stream and a null JsonFactory are used to simulate this case.
        byte[] inputData = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(inputData);
        JsonFactory noMatchingFactory = null; // A null factory indicates no format was matched.
        MatchStrength strength = MatchStrength.INCONCLUSIVE;

        DataFormatMatcher matcher = new DataFormatMatcher(
                inputStream,
                inputData,
                0, // bufferStart
                0, // bufferLength
                noMatchingFactory,
                strength
        );

        // Act: Call the method to get the name of the matched format.
        String formatName = matcher.getMatchedFormatName();

        // Assert: Verify that the returned format name is null, as expected.
        assertNull("The format name should be null when no JsonFactory is matched.", formatName);
    }
}