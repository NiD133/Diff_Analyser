package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link LocationTextExtractionStrategy.TextChunk} inner class.
 */
public class LocationTextExtractionStrategyTextChunkTest {

    /**
     * Verifies that the getCharSpaceWidth method of a TextChunk
     * correctly returns the character space width value provided during its construction.
     */
    @Test
    public void getCharSpaceWidth_shouldReturnWidthSetInConstructor() {
        // Arrange
        // The specific values for text and locations are not relevant for this test,
        // as we are only verifying the charSpaceWidth.
        Vector startLocation = new Vector(0, 0, 0);
        Vector endLocation = new Vector(50, 0, 0);
        float expectedCharSpaceWidth = 2.0f;

        // Act
        LocationTextExtractionStrategy.TextChunk textChunk = new LocationTextExtractionStrategy.TextChunk(
                "any text",
                startLocation,
                endLocation,
                expectedCharSpaceWidth
        );
        float actualCharSpaceWidth = textChunk.getCharSpaceWidth();

        // Assert
        assertEquals(expectedCharSpaceWidth, actualCharSpaceWidth, 0.01f);
    }
}