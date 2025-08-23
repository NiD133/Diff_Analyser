package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that the getUnscaledBaseline() method correctly incorporates the
     * 'rise' property from the GraphicsState. The 'rise' dictates a vertical
     * shift of the text from its normal baseline.
     */
    @Test
    public void getUnscaledBaselineShouldIncorporateTextRise() {
        // Arrange
        final float expectedTextRise = 2.0f;

        // Set up a graphics state with a specific "rise" value.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.rise = expectedTextRise;

        // A font is required by the TextRenderInfo constructor. We can use a
        // simple dummy font, as its specific properties are not relevant to this test.
        graphicsState.font = new CMapAwareDocumentFont(new PdfDictionary());

        // Create the TextRenderInfo instance with the configured graphics state.
        // The other parameters can be default or empty values for this test case.
        TextRenderInfo textRenderInfo = new TextRenderInfo(
                new PdfString(""),
                graphicsState,
                new Matrix(), // Use a default identity matrix
                Collections.<MarkedContentInfo>emptyList()
        );

        // Act
        // Retrieve the unscaled baseline, which should be vertically shifted by the text rise.
        LineSegment unscaledBaseline = textRenderInfo.getUnscaledBaseline();

        // Assert
        // 1. The baseline segment should be successfully created.
        assertNotNull("The unscaled baseline should not be null.", unscaledBaseline);

        // 2. The vertical position (Y-coordinate) of the baseline is determined by the 'rise' property.
        //    Verify that both the start and end points of the baseline segment have a Y-coordinate
        //    equal to the specified text rise. A small delta is used for float comparison.
        float delta = 0.001f;
        assertEquals("The Y-coordinate of the baseline's start point should equal the text rise.",
                expectedTextRise, unscaledBaseline.getStartPoint().get(Vector.I2), delta);
        assertEquals("The Y-coordinate of the baseline's end point should equal the text rise.",
                expectedTextRise, unscaledBaseline.getEndPoint().get(Vector.I2), delta);
    }
}