package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunk;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link LocationTextExtractionStrategy.TextChunk} inner class.
 */
public class LocationTextExtractionStrategyTextChunkTest {

    /**
     * Verifies that the getCharSpaceWidth method correctly returns the value
     * provided to the TextChunk constructor.
     */
    @Test
    public void getCharSpaceWidth_shouldReturnValueFromConstructor() {
        // Arrange: Set up the test data and the object under test.
        Vector startLocation = new Vector(0, 0, 0);
        Vector endLocation = new Vector(0, 0, 0);
        float expectedCharSpaceWidth = 1114.74F;

        TextChunk textChunk = new TextChunk("", startLocation, endLocation, expectedCharSpaceWidth);

        // Act: Call the method being tested.
        float actualCharSpaceWidth = textChunk.getCharSpaceWidth();

        // Assert: Verify the result is as expected.
        assertEquals(expectedCharSpaceWidth, actualCharSpaceWidth, 0.01F);
    }
}