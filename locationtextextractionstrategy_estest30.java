package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

public class LocationTextExtractionStrategy_ESTestTest30 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Tests that the renderText method correctly adjusts the text baseline
     * when the GraphicsState has a non-zero "rise" value. A positive rise
     * should result in a downward vertical translation of the baseline.
     */
    @Test(timeout = 4000)
    public void renderText_whenTextHasRise_usesTransformedBaselineForLocation() {
        // Arrange
        final float TEXT_RISE = 3.0f;

        // 1. Define the original baseline of the text.
        Vector startPoint = new Vector(10, 20, 1);
        Vector endPoint = new Vector(50, 20, 1);
        LineSegment originalBaseline = new LineSegment(startPoint, endPoint);

        // 2. Mock TextRenderInfo to simulate text with a non-zero "rise".
        // This isolates the test from the complexity of building a full GraphicsState.
        TextRenderInfo mockRenderInfo = mock(TextRenderInfo.class);
        when(mockRenderInfo.getRise()).thenReturn(TEXT_RISE);
        when(mockRenderInfo.getBaseline()).thenReturn(originalBaseline);
        // The getText() method is called internally, so we must stub it.
        when(mockRenderInfo.getText()).thenReturn("any text");
        // The getFont() method is also called internally.
        when(mockRenderInfo.getFont()).thenReturn(mock(CMapAwareDocumentFont.class));


        // 3. Mock the strategy used for creating text chunk locations.
        LocationTextExtractionStrategy.TextChunkLocationStrategy mockLocationStrategy =
                mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class);

        // 4. Create the strategy instance to be tested, injecting our mock dependency.
        LocationTextExtractionStrategy extractionStrategy = new LocationTextExtractionStrategy(mockLocationStrategy);
        ArgumentCaptor<LineSegment> baselineCaptor = ArgumentCaptor.forClass(LineSegment.class);

        // Act
        // Call the method under test.
        extractionStrategy.renderText(mockRenderInfo);

        // Assert
        // Verify that our location strategy was called with a correctly transformed baseline.
        // The transformation for "rise" is a vertical translation by its negative value.
        verify(mockLocationStrategy).createLocation(eq(mockRenderInfo), baselineCaptor.capture());

        LineSegment capturedBaseline = baselineCaptor.getValue();
        Vector expectedStartPoint = new Vector(startPoint.get(Vector.I1), startPoint.get(Vector.I2) - TEXT_RISE, 1);
        Vector expectedEndPoint = new Vector(endPoint.get(Vector.I1), endPoint.get(Vector.I2) - TEXT_RISE, 1);

        assertEquals("The baseline's start point should be translated vertically by the negative rise.",
                expectedStartPoint, capturedBaseline.getStartPoint());
        assertEquals("The baseline's end point should be translated vertically by the negative rise.",
                expectedEndPoint, capturedBaseline.getEndPoint());
    }
}