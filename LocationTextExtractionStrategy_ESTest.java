package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfOCProperties;
import com.itextpdf.text.pdf.parser.GraphicsState;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;

import java.util.LinkedList;

/**
 * Test suite for LocationTextExtractionStrategy - a PDF text extraction strategy
 * that preserves the spatial positioning of text elements.
 */
public class LocationTextExtractionStrategyTest {

    // Test constants for better readability
    private static final float ZERO = 0.0f;
    private static final float SMALL_DISTANCE = 1.0E-4f;
    private static final String EMPTY_TEXT = "";
    private static final String SAMPLE_TEXT = "Sample";
    
    // ========== TextChunk Distance Calculation Tests ==========
    
    @Test
    public void testTextChunkDistanceCalculation_SamePosition_ReturnsZeroDistance() {
        // Given: Two text chunks at the same position
        Vector position = new Vector(ZERO, -2030.0f, ZERO);
        LocationTextExtractionStrategy.TextChunk firstChunk = 
            createTextChunk(EMPTY_TEXT, position, position, ZERO);
        LocationTextExtractionStrategy.TextChunk secondChunk = 
            createTextChunk("Lm", position, position, 2.0f);
        
        // When: Calculating distance between chunks
        float distance = firstChunk.distanceFromEndOf(secondChunk);
        
        // Then: Distance should be zero since they're at the same position
        assertEquals("Distance between chunks at same position should be zero", 
                     ZERO, distance, 0.01f);
        assertEquals("Second chunk should have correct character space width", 
                     2.0f, secondChunk.getCharSpaceWidth(), 0.01f);
    }
    
    // ========== TextChunkLocation Comparison Tests ==========
    
    @Test
    public void testTextChunkLocationComparison_DifferentPositions_ReturnsCorrectOrder() {
        // Given: Two text chunk locations at different positions
        Vector firstPosition = new Vector(-555.0505f, -555.0505f, -555.0505f);
        Vector secondPosition = new Vector(0, -3318.408f, 1);
        
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp firstLocation = 
            createTextChunkLocation(firstPosition, firstPosition, -3318.408f);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp secondLocation = 
            createTextChunkLocation(firstPosition, secondPosition, ZERO);
        
        // When: Comparing locations
        int comparisonResult = firstLocation.compareTo(secondLocation);
        
        // Then: First location should come after second location
        assertEquals("First location should be ordered after second location", 
                     1, comparisonResult);
        assertLocationProperties(secondLocation, ZERO, -1372, 3192.0986f, 641, 319.22217f);
        assertEquals("First location should have correct perpendicular distance", 
                     555, firstLocation.distPerpendicular());
    }
    
    // ========== Word Boundary Detection Tests ==========
    
    @Test
    public void testWordBoundaryDetection_SameStartAndEndPosition_NotAtWordBoundary() {
        // Given: Two text chunks with same start position but different end positions
        Vector startPosition = createZeroVector();
        Vector extendedEndPosition = startPosition.multiply(1163.3555f);
        
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp firstLocation = 
            createTextChunkLocation(startPosition, extendedEndPosition, 7.0f);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp secondLocation = 
            createTextChunkLocation(startPosition, startPosition, 4.0f);
        
        // When: Checking if locations are at word boundary
        boolean isAtWordBoundary = secondLocation.isAtWordBoundary(firstLocation);
        
        // Then: Should not be at word boundary
        assertFalse("Chunks with overlapping positions should not be at word boundary", 
                    isAtWordBoundary);
        assertEquals("First location should have correct parallel end distance", 
                     1163.3555f, firstLocation.distParallelEnd(), 0.01f);
        assertLocationProperties(secondLocation, ZERO, 0, ZERO, 0, ZERO);
    }
    
    @Test
    public void testWordBoundaryDetection_SignificantGap_IsAtWordBoundary() {
        // Given: Two text chunks with significant gap between them
        Vector firstPosition = new Vector(2.0f, 2.0f, 175.0f);
        Vector secondEndPosition = new Vector(175.0f, 2.0f, 175.0f);
        
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp firstLocation = 
            createTextChunkLocation(firstPosition, firstPosition, -5.186149E-6f);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp secondLocation = 
            createTextChunkLocation(firstPosition, secondEndPosition, ZERO);
        
        // When: Checking if locations are at word boundary
        boolean isAtWordBoundary = firstLocation.isAtWordBoundary(secondLocation);
        
        // Then: Should be at word boundary due to significant gap
        assertTrue("Chunks with significant gap should be at word boundary", 
                   isAtWordBoundary);
        assertEquals("Second location should have correct parallel end distance", 
                     175.0f, secondLocation.distParallelEnd(), 0.01f);
    }
    
    // ========== Same Line Detection Tests ==========
    
    @Test
    public void testSameLineDetection_DifferentPerpendicularDistances_NotSameLine() {
        // Given: Two text chunks at different perpendicular distances
        Vector firstPosition = new Vector(-53.7697f, 1000.0f, 1000.0f);
        Vector secondPosition = new Vector(1.089f, -53.7697f, 1.089f);
        
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp firstLocation = 
            createTextChunkLocation(firstPosition, firstPosition, ZERO);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp secondLocation = 
            createTextChunkLocation(secondPosition, secondPosition, ZERO);
        
        // When: Checking if chunks are on same line
        boolean sameLine = firstLocation.sameLine(secondLocation);
        
        // Then: Should not be on same line
        assertFalse("Chunks at different perpendicular distances should not be on same line", 
                    sameLine);
        assertLocationProperties(secondLocation, ZERO, 0, 1.089f, 53, 1.089f);
    }
    
    // ========== Text Extraction Strategy Tests ==========
    
    @Test
    public void testTextExtraction_EmptyDocument_ReturnsEmptyString() {
        // Given: Empty text extraction strategy
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();
        
        // When: Getting resultant text
        String result = strategy.getResultantText();
        
        // Then: Should return empty string
        assertEquals("Empty document should return empty string", EMPTY_TEXT, result);
    }
    
    @Test
    public void testTextExtraction_WithFilter_ReturnsFilteredText() {
        // Given: Strategy with text content and a filter
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();
        TextRenderInfo textInfo = createTextRenderInfo();
        strategy.renderText(textInfo);
        
        LocationTextExtractionStrategy.TextChunkFilter acceptAllFilter = chunk -> true;
        
        // When: Getting filtered text
        String result = strategy.getResultantText(acceptAllFilter);
        
        // Then: Should return filtered content
        assertEquals("Filtered text extraction should work correctly", EMPTY_TEXT, result);
    }
    
    @Test
    public void testTextExtraction_MultipleTextChunks_InsertsNewlineForDifferentLines() {
        // Given: Strategy with text chunks on different lines
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();
        TextRenderInfo firstTextInfo = createTextRenderInfo();
        TextRenderInfo secondTextInfo = createTextRenderInfoWithDifferentMatrix();
        
        // When: Processing multiple text chunks
        strategy.renderText(firstTextInfo);
        strategy.renderText(secondTextInfo);
        String result = strategy.getResultantText();
        
        // Then: Should insert newline between different lines
        assertEquals("Different lines should be separated by newline", "\n", result);
    }
    
    // ========== Error Handling Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void testRenderText_NullInput_ThrowsException() {
        // Given: Text extraction strategy
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();
        
        // When: Rendering null text info
        // Then: Should throw NullPointerException
        strategy.renderText(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testWordBoundaryCheck_NullInputs_ThrowsException() {
        // Given: Text extraction strategy
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();
        
        // When: Checking word boundary with null inputs
        // Then: Should throw NullPointerException
        strategy.isChunkAtWordBoundary(null, null);
    }
    
    // ========== Utility Methods for Test Setup ==========
    
    private LocationTextExtractionStrategy.TextChunk createTextChunk(
            String text, Vector startPos, Vector endPos, float charSpaceWidth) {
        return new LocationTextExtractionStrategy.TextChunk(text, startPos, endPos, charSpaceWidth);
    }
    
    private LocationTextExtractionStrategy.TextChunkLocationDefaultImp createTextChunkLocation(
            Vector startPos, Vector endPos, float charSpaceWidth) {
        return new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(
                startPos, endPos, charSpaceWidth);
    }
    
    private Vector createZeroVector() {
        return new Vector(ZERO, ZERO, ZERO);
    }
    
    private TextRenderInfo createTextRenderInfo() {
        GraphicsState graphicsState = new GraphicsState();
        Matrix matrix = new Matrix(8, 2);
        PdfOCProperties pdfProperties = new PdfOCProperties();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfProperties);
        graphicsState.font = font;
        
        return new TextRenderInfo(new PdfDate(), graphicsState, matrix, new LinkedList<>());
    }
    
    private TextRenderInfo createTextRenderInfoWithDifferentMatrix() {
        GraphicsState graphicsState = new GraphicsState();
        Matrix matrix = new Matrix(341.6828f, -9.18953f, 5, 23, 6, 3);
        PdfOCProperties pdfProperties = new PdfOCProperties();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfProperties);
        graphicsState.font = font;
        
        return new TextRenderInfo(new PdfDate(), graphicsState, matrix, new LinkedList<>());
    }
    
    private void assertLocationProperties(
            LocationTextExtractionStrategy.TextChunkLocationDefaultImp location,
            float expectedCharSpaceWidth, int expectedOrientationMagnitude,
            float expectedParallelEnd, int expectedPerpendicular, 
            float expectedParallelStart) {
        
        assertEquals("Character space width should match", 
                     expectedCharSpaceWidth, location.getCharSpaceWidth(), 0.01f);
        assertEquals("Orientation magnitude should match", 
                     expectedOrientationMagnitude, location.orientationMagnitude());
        assertEquals("Parallel end distance should match", 
                     expectedParallelEnd, location.distParallelEnd(), 0.01f);
        assertEquals("Perpendicular distance should match", 
                     expectedPerpendicular, location.distPerpendicular());
        assertEquals("Parallel start distance should match", 
                     expectedParallelStart, location.distParallelStart(), 0.01f);
    }
}