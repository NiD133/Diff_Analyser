package com.itextpdf.text.pdf.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class LocationTextExtractionStrategyReadableTest {

    // Helper to create a simple horizontal (X-axis) chunk location
    private LocationTextExtractionStrategy.TextChunkLocationDefaultImp horizontal(float startX, float endX, float y, float charSpace) {
        Vector start = new Vector(startX, y, 1);
        Vector end = new Vector(endX, y, 1);
        return new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(start, end, charSpace);
    }

    // Helper to wrap a location with text
    private LocationTextExtractionStrategy.TextChunk chunk(String text,
                                                          LocationTextExtractionStrategy.TextChunkLocation loc) {
        return new LocationTextExtractionStrategy.TextChunk(text, loc);
    }

    @Test
    public void textChunkLocation_horizontalProjectsAndDistancesAreConsistent() {
        // Given: a simple horizontal chunk from (0,0) to (10,0) with charSpaceWidth 2
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp loc =
                horizontal(0f, 10f, 0f, 2f);

        // Then: orientation is along X, perpendicular distance is ~0, and projections match endpoints
        assertEquals("orientationMagnitude should be 0 for a (1,0,0) orientation", 0, loc.orientationMagnitude());
        assertEquals("distPerpendicular should be 0 at y=0", 0, loc.distPerpendicular());
        assertEquals("distParallelStart should project to start.x", 0f, loc.distParallelStart(), 0.0001f);
        assertEquals("distParallelEnd should project to end.x", 10f, loc.distParallelEnd(), 0.0001f);
        assertEquals("char space width should match constructor arg", 2f, loc.getCharSpaceWidth(), 0.0001f);
    }

    @Test
    public void textChunkLocation_sameLineFalseForDifferentY() {
        // Given: two horizontal chunks at different y-values
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp line1 = horizontal(0, 10, 0, 2);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp line2 = horizontal(0, 10, 10, 2);

        // When/Then: they are not on the same line
        assertFalse(line1.sameLine(line2));
        assertNotEquals("Different y should change perpendicular distance",
                line1.distPerpendicular(), line2.distPerpendicular());
    }

    @Test
    public void textChunk_wrapsTextAndLocation() {
        // Given: a location and a text string
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp loc = horizontal(1, 3, 5, 0.5f);
        LocationTextExtractionStrategy.TextChunk chunk = chunk("Hi", loc);

        // Then: getters return what was provided
        assertEquals("Hi", chunk.getText());
        assertSame(loc, chunk.getLocation());
        assertEquals(0.5f, chunk.getCharSpaceWidth(), 0.0001f);
        assertEquals(loc.getStartLocation(), chunk.getStartLocation());
        assertEquals(loc.getEndLocation(), chunk.getEndLocation());
    }

    @Test
    public void textChunk_distanceFromEndOf_sameChunkIsZero() {
        // Given: a single chunk
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp loc = horizontal(0, 10, 0, 2);
        LocationTextExtractionStrategy.TextChunk chunk = chunk("A", loc);

        // When/Then: distance from its own end to its start is zero
        assertEquals(0f, chunk.distanceFromEndOf(chunk), 0.0001f);
    }

    @Test
    public void getResultantText_beforeRendering_isEmpty() {
        // Given: a fresh strategy
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();

        // When/Then: no text has been rendered yet
        assertEquals("", strategy.getResultantText());
    }

    @Test(expected = NullPointerException.class)
    public void renderText_null_throwsNullPointerException() {
        // Given
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();

        // When: renderText is invoked with null
        strategy.renderText(null);
    }
}