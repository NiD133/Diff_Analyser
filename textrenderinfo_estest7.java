package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Contains improved tests for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Tests that getRise() correctly calculates the text rise in user space.
     * <p>
     * The original auto-generated test asserted that a negative rise value in the
     * graphics state would result in a positive value from getRise(). This improved
     * test clarifies that apparent intent: to check if the absolute value is returned
     * when the coordinate transformation matrix (CTM) is an identity matrix (i.e., no scaling or rotation).
     * </p>
     */
    @Test
    public void getRiseShouldReturnAbsoluteValueForNegativeInputWithIdentityTransform() {
        // Arrange
        final float negativeRiseInTextSpace = -3505.32f;
        // The original test expected the absolute value of the input rise.
        final float expectedRiseInUserSpace = 3505.32f;
        final float delta = 0.01f;

        // Set up the graphics state with a specific rise and a default identity CTM.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.rise = negativeRiseInTextSpace;
        // A font is required by the TextRenderInfo constructor, so we provide a dummy one.
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        // The text matrix is also an identity matrix for this simple test case.
        Matrix identityTextMatrix = new Matrix();
        PdfString dummyString = new PdfString();

        // Act
        TextRenderInfo renderInfo = new TextRenderInfo(
                dummyString,
                graphicsState,
                identityTextMatrix,
                Collections.emptyList() // Marked content is not relevant for this test.
        );
        float actualRise = renderInfo.getRise();

        // Assert
        assertEquals(expectedRiseInUserSpace, actualRise, delta);
    }
}