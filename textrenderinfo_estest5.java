package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class TextRenderInfo_ESTestTest5 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that getSingleSpaceWidth() correctly incorporates the character spacing
     * defined in the GraphicsState.
     * <p>
     * The single space width is a combination of the font's default space width and
     * the character spacing. This test uses a mock font with a default space width of zero,
     * ensuring that the calculated width is determined solely by the character spacing.
     */
    @Test
    public void getSingleSpaceWidth_shouldReflectCharacterSpacing() {
        // Arrange
        final float characterSpacing = 1025.7177F;
        final float expectedSpaceWidth = 1025.7177F;
        final float delta = 0.01F;

        // Set up a graphics state with a specific character spacing.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.characterSpacing = characterSpacing;

        // Use a default CMapAwareDocumentFont which acts as a mock font.
        // Its width for a space character is effectively zero in this context.
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        // Create the minimal required parameters for the TextRenderInfo constructor.
        PdfString emptyText = new PdfString("");
        Matrix identityMatrix = new Matrix(); // An identity matrix ensures no scaling affects the width.
        Collection<MarkedContentInfo> noMarkedContent = Collections.emptyList();

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                emptyText,
                graphicsState,
                identityMatrix,
                noMarkedContent
        );

        // Act
        float actualSpaceWidth = textRenderInfo.getSingleSpaceWidth();

        // Assert
        // The resulting width should match the character spacing, given the font's
        // space width is zero. A delta is used for floating-point comparison.
        assertEquals(expectedSpaceWidth, actualSpaceWidth, delta);
    }
}