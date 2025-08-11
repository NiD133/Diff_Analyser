package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunk;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunkLocation;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunkLocationDefaultImp;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link LocationTextExtractionStrategy}.
 *
 * These tests verify the logic for text chunk positioning, sorting, and final text assembly.
 * Helper methods are used to create test objects, making the tests clearer and more focused
 * on the behavior being tested.
 */
public class LocationTextExtractionStrategyTest {

    private LocationTextExtractionStrategy strategy;

    @Before
    public void setUp() {
        strategy = new LocationTextExtractionStrategy();
    }

    //region Helper Methods

    /**
     * Creates a TextChunk with a default character space width.
     */
    private TextChunk createTextChunk(String text, float startX, float startY, float endX, float endY) {
        // A typical non-zero width for spacing checks.
        float charSpaceWidth = 10f;
        return createTextChunk(text, startX, startY, endX, endY, charSpaceWidth);
    }

    /**
     * Creates a TextChunk with specific coordinates and character space width.
     */
    private TextChunk createTextChunk(String text, float startX, float startY, float endX, float endY, float charSpaceWidth) {
        Vector start = new Vector(startX, startY, 1);
        Vector end = new Vector(endX, endY, 1);
        TextChunkLocation location = new TextChunkLocationDefaultImp(start, end, charSpaceWidth);
        return new TextChunk(text, location);
    }

    /**
     * Mocks a TextRenderInfo object to simulate rendering a piece of text.
     */
    private TextRenderInfo mockRenderInfo(String text, float startX, float startY, float endX, float endY) {
        TextRenderInfo renderInfo = mock(TextRenderInfo.class);
        when(renderInfo.getText()).thenReturn(text);

        Vector start = new Vector(startX, startY, 1);
        Vector end = new Vector(endX, endY, 1);
        LineSegment baseline = new LineSegment(start, end);
        when(renderInfo.getBaseline()).thenReturn(baseline);

        // The default strategy uses half of the single space width as the chunk's charSpaceWidth.
        // We provide a value of 10f, so the chunk's charSpaceWidth will be 5f.
        when(renderInfo.getSingleSpaceWidth()).thenReturn(10f);
        return renderInfo;
    }

    //endregion

    //region LocationTextExtractionStrategy.TextChunkLocationDefaultImp Tests

    @Test
    public void locationProperties_areCalculatedCorrectly() {
        // Arrange: A horizontal text chunk
        Vector start = new Vector(10, 50, 1);
        Vector end = new Vector(100, 50, 1);

        // Act
        TextChunkLocation location = new TextChunkLocationDefaultImp(start, end, 5f);

        // Assert
        assertEquals("Orientation should be 0 for horizontal text", 0, location.orientationMagnitude());
        assertEquals("Perpendicular distance is based on Y-coordinate", 50, location.distPerpendicular());
        assertEquals("Parallel start distance is based on X-coordinate", 10f, location.distParallelStart(), 0.01);
        assertEquals("Parallel end distance is based on X-coordinate", 100f, location.distParallelEnd(), 0.01);
        assertEquals(5f, location.getCharSpaceWidth(), 0.01);
    }

    @Test
    public void compareTo_withDifferentOrientations_sortsByOrientation() {
        // Arrange
        TextChunkLocation horizontal = createTextChunk("H", 0, 0, 10, 0).getLocation(); // orientation ~0
        TextChunkLocation vertical = createTextChunk("V", 0, 0, 0, 10).getLocation();   // orientation ~1570 (PI/2 * 1000)

        // Act & Assert
        assertTrue("Vertical should come after horizontal", vertical.compareTo(horizontal) > 0);
        assertTrue("Horizontal should come before vertical", horizontal.compareTo(vertical) < 0);
    }

    @Test
    public void compareTo_withSameOrientation_sortsByPerpendicularDistance() {
        // Arrange
        TextChunkLocation top = createTextChunk("Top", 0, 100, 10, 100).getLocation(); // y = 100
        TextChunkLocation bottom = createTextChunk("Bottom", 0, 50, 10, 50).getLocation(); // y = 50

        // Act & Assert
        assertTrue("Top line should come after bottom line", top.compareTo(bottom) > 0);
        assertTrue("Bottom line should come before top line", bottom.compareTo(top) < 0);
    }

    @Test
    public void compareTo_withSameLine_sortsByParallelDistance() {
        // Arrange
        TextChunkLocation left = createTextChunk("Left", 10, 50, 20, 50).getLocation();
        TextChunkLocation right = createTextChunk("Right", 30, 50, 40, 50).getLocation();

        // Act & Assert
        assertTrue("Right chunk should come after left chunk", right.compareTo(left) > 0);
        assertTrue("Left chunk should come before right chunk", left.compareTo(right) < 0);
    }

    @Test
    public void sameLine_whenOrientationAndPerpendicularMatch_returnsTrue() {
        // Arrange
        TextChunkLocation loc1 = createTextChunk("A", 10, 50, 20, 50).getLocation();
        TextChunkLocation loc2 = createTextChunk("B", 30, 50, 40, 50).getLocation();

        // Act & Assert
        assertTrue("Chunks with same orientation and Y-coordinate should be on the same line", loc1.sameLine(loc2));
    }

    @Test
    public void sameLine_whenPerpendicularDiffers_returnsFalse() {
        // Arrange
        TextChunkLocation loc1 = createTextChunk("A", 10, 50, 20, 50).getLocation();
        TextChunkLocation loc2 = createTextChunk("B", 10, 60, 20, 60).getLocation(); // Different Y

        // Act & Assert
        assertFalse("Chunks on different lines should not be considered the same line", loc1.sameLine(loc2));
    }

    @Test
    public void isAtWordBoundary_whenGapIsSufficient_returnsTrue() {
        // Arrange: Gap (10) is larger than half the char space width (10f / 2 = 5f)
        TextChunkLocation prev = createTextChunk("end", 0, 0, 50, 0, 10f).getLocation();
        TextChunkLocation current = createTextChunk("start", 60, 0, 100, 0, 10f).getLocation();

        // Act & Assert
        assertTrue("A sufficient gap should indicate a word boundary", current.isAtWordBoundary(prev));
    }

    @Test
    public void isAtWordBoundary_whenChunksOverlap_returnsTrue() {
        // Arrange: Current chunk starts before the previous one ends
        TextChunkLocation prev = createTextChunk("end", 0, 0, 50, 0).getLocation();
        TextChunkLocation current = createTextChunk("start", 40, 0, 80, 0).getLocation();

        // Act & Assert
        assertTrue("Overlapping text should indicate a word boundary", current.isAtWordBoundary(prev));
    }

    @Test
    public void isAtWordBoundary_whenGapIsSmall_returnsFalse() {
        // Arrange: Gap (2) is smaller than half the char space width (10f / 2 = 5f)
        TextChunkLocation prev = createTextChunk("end", 0, 0, 50, 0, 10f).getLocation();
        TextChunkLocation current = createTextChunk("start", 52, 0, 100, 0, 10f).getLocation();

        // Act & Assert
        assertFalse("A small gap should not indicate a word boundary", current.isAtWordBoundary(prev));
    }

    //endregion

    //region LocationTextExtractionStrategy Main Class Tests

    @Test
    public void getResultantText_onEmptyStrategy_returnsEmptyString() {
        // Act
        String result = strategy.getResultantText();

        // Assert
        assertEquals("", result);
    }

    @Test
    public void getResultantText_whenChunksAreOnSameLineAndClose_joinsWithoutSpace() {
        // Arrange
        strategy.renderText(mockRenderInfo("he", 10, 50, 30, 50));
        strategy.renderText(mockRenderInfo("llo", 31, 50, 50, 50)); // 1 unit gap, less than space width

        // Act
        String result = strategy.getResultantText();

        // Assert
        assertEquals("hello", result);
    }

    @Test
    public void getResultantText_whenChunksAreOnSameLineAndFar_joinsWithSpace() {
        // Arrange
        strategy.renderText(mockRenderInfo("Hello", 10, 50, 50, 50));
        strategy.renderText(mockRenderInfo("World", 60, 50, 100, 50)); // 10 unit gap

        // Act
        String result = strategy.getResultantText();

        // Assert
        assertEquals("Hello World", result);
    }

    @Test
    public void getResultantText_whenTextEndsWithSpace_joinsWithSpace() {
        // Arrange
        strategy.renderText(mockRenderInfo("Hello ", 10, 50, 50, 50)); // Ends with space
        strategy.renderText(mockRenderInfo("World", 51, 50, 100, 50)); // Adjoins closely

        // Act
        String result = strategy.getResultantText();

        // Assert
        assertEquals("Hello World", result);
    }

    @Test
    public void getResultantText_whenChunksAreOnDifferentLines_addsNewline() {
        // Arrange
        strategy.renderText(mockRenderInfo("Line 1", 10, 100, 80, 100)); // Top line
        strategy.renderText(mockRenderInfo("Line 2", 10, 50, 80, 50));  // Bottom line

        // Act
        String result = strategy.getResultantText();

        // Assert
        assertEquals("Line 1\nLine 2", result);
    }

    @Test
    public void getResultantText_withFilter_returnsOnlyAcceptedChunks() {
        // Arrange
        strategy.renderText(mockRenderInfo("Keep", 10, 50, 50, 50));
        strategy.renderText(mockRenderInfo("Discard", 60, 50, 120, 50));
        LocationTextExtractionStrategy.TextChunkFilter filter = chunk -> chunk.getText().equals("Keep");

        // Act
        String result = strategy.getResultantText(filter);

        // Assert
        assertEquals("Keep", result);
    }

    @Test(expected = NullPointerException.class)
    public void renderText_withNullRenderInfo_throwsNPE() {
        // Act
        strategy.renderText(null);
    }

    @Test
    public void noOpMethods_doNotThrowExceptions() {
        // Act & Assert: These methods are no-ops and should not throw.
        strategy.beginTextBlock();
        strategy.endTextBlock();
        strategy.renderImage(null); // Should ignore image info
    }

    //endregion
}