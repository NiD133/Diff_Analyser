package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunk;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite focuses on the {@link LocationTextExtractionStrategy.TextChunk} inner class.
 */
public class LocationTextExtractionStrategy_ESTestTest35 {

    /**
     * Verifies that the getCharSpaceWidth() method of a TextChunk instance
     * correctly returns the value that was provided to its constructor.
     */
    @Test
    public void getCharSpaceWidth_shouldReturnCharSpaceWidth_givenInConstructor() {
        // Arrange: Define test data and create a TextChunk instance.
        final float expectedCharSpaceWidth = 1114.74f;
        Vector startLocation = new Vector(0, 0, 1);
        Vector endLocation = new Vector(50, 0, 1);
        String text = "sample text";

        TextChunk textChunk = new TextChunk(text, startLocation, endLocation, expectedCharSpaceWidth);

        // Act: Call the method under test.
        float actualCharSpaceWidth = textChunk.getCharSpaceWidth();

        // Assert: Verify that the returned value matches the expected value within a small tolerance.
        final float delta = 0.01f;
        assertEquals(expectedCharSpaceWidth, actualCharSpaceWidth, delta);
    }
}