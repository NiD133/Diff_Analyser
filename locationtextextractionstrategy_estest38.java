package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunk;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the {@link LocationTextExtractionStrategy.TextChunk} inner class.
 */
// The original class name is kept to maintain context, though it is not ideal.
public class LocationTextExtractionStrategy_ESTestTest38 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Verifies that a TextChunk object correctly stores and returns the values
     * provided to its constructor.
     */
    @Test
    public void textChunkGettersShouldReturnConstructorValues() {
        // Arrange
        Vector expectedLocation = new Vector(0.0f, -2030.0f, 0.0f);
        float expectedCharSpaceWidth = 0.0f;

        // Act
        TextChunk textChunk = new TextChunk(
                "",               // text
                expectedLocation, // startLocation
                expectedLocation, // endLocation (same as start for this test)
                expectedCharSpaceWidth
        );

        // Assert
        // The original test only asserted the character space width after a confusing,
        // un-asserted call to getStartLocation(). This version properly asserts all
        // relevant properties to confirm the object's state is correct.
        assertEquals("Start location should match the value from the constructor.",
                expectedLocation, textChunk.getStartLocation());

        assertEquals("Character space width should match the value from the constructor.",
                expectedCharSpaceWidth, textChunk.getCharSpaceWidth(), 0.01f);
    }
}