package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collection;
import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class TextRenderInfoImprovedTest {

    /**
     * Tests that the single space width calculation correctly incorporates the
     * wordSpacing property from the current GraphicsState.
     */
    @Test
    public void getSingleSpaceWidth_shouldIncorporateWordSpacingFromGraphicsState() {
        // Arrange
        final float wordSpacing = 87.0F;
        final float delta = 0.01F;

        // Create a GraphicsState with a specific word spacing.
        // A default CMapAwareDocumentFont is used, which is assumed to have a space-character
        // width of 0. This setup isolates the effect of the wordSpacing property.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.wordSpacing = wordSpacing;
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        // Create other required, but for this test case, neutral parameters for the TextRenderInfo constructor.
        PdfString dummyText = new PdfString("");
        Matrix identityMatrix = new Matrix();
        Collection<MarkedContentInfo> markedContentInfo = new Stack<>();

        TextRenderInfo renderInfo = new TextRenderInfo(dummyText, graphicsState, identityMatrix, markedContentInfo);

        // Act
        float actualSingleSpaceWidth = renderInfo.getSingleSpaceWidth();

        // Assert
        // The getSingleSpaceWidth() result is influenced by the graphics state's wordSpacing.
        // With a font space width of 0 and an identity transformation matrix, the result should
        // directly reflect the configured wordSpacing.
        assertEquals(
                "The single space width should be determined by the wordSpacing in the graphics state.",
                wordSpacing,
                actualSingleSpaceWidth,
                delta
        );
    }
}