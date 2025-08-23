package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link DataFormatMatcher} class, focusing on its state
 * and stream handling capabilities.
 */
public class DataFormatMatcherTest {

    /**
     * Verifies that calling {@link DataFormatMatcher#getDataStream()} returns a valid stream
     * and does not alter the internal state of the matcher, such as its match status
     * and strength.
     */
    @Test
    public void getDataStreamShouldNotAlterMatchState() throws IOException {
        // Arrange: Set up the necessary objects for creating a DataFormatMatcher.
        byte[] bufferedData = new byte[10];
        InputStream originalInputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        InputAccessor.Std inputAccessor = new InputAccessor.Std(originalInputStream, bufferedData);

        JsonFactory matchedFactory = new JsonFactory();
        MatchStrength expectedStrength = MatchStrength.WEAK_MATCH;

        // Create the DataFormatMatcher instance to be tested.
        DataFormatMatcher matcher = inputAccessor.createMatcher(matchedFactory, expectedStrength);

        // Act: Call the method under test.
        // The goal is to retrieve the data stream, which should combine buffered data
        // with the original stream's remaining content.
        InputStream resultStream = matcher.getDataStream();

        // Assert: Verify that the matcher's state remains consistent after the call.
        assertNotNull("getDataStream() should not return a null stream.", resultStream);
        
        assertTrue("Matcher must still indicate a match after stream retrieval.", matcher.hasMatch());
        
        assertEquals("Match strength should be preserved after stream retrieval.",
                expectedStrength, matcher.getMatchStrength());
    }
}