package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

// The original test class name and inheritance are preserved as per the prompt's context.
public class TextRenderInfo_ESTestTest59 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that getRise() returns 0.0F when the GraphicsState is initialized
     * with its default values, where the text rise is expected to be zero.
     */
    @Test
    public void getRise_withDefaultGraphicsState_returnsZero() {
        // Arrange: Set up a TextRenderInfo with a default GraphicsState.
        // A default GraphicsState has a textRise of 0.0F.
        GraphicsState graphicsState = new GraphicsState();
        // A font is a required property for the GraphicsState.
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        // Create other required parameters for the TextRenderInfo constructor.
        PdfString dummyText = new PdfString("");
        Matrix identityMatrix = new Matrix();

        TextRenderInfo renderInfo = new TextRenderInfo(
                dummyText,
                graphicsState,
                identityMatrix,
                Collections.emptyList()
        );

        // Act: Call the method under test.
        float actualRise = renderInfo.getRise();

        // Assert: Verify the result is the expected default value.
        float expectedRise = 0.0F;
        // The rise should be exactly zero for a default state, so a delta of 0.0 is appropriate.
        assertEquals("The text rise should be 0.0F for a default GraphicsState.",
                expectedRise, actualRise, 0.0F);
    }
}