package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that getTextRenderMode() correctly retrieves the default render mode
     * from the associated GraphicsState. A newly initialized GraphicsState should have
     * a default render mode of 0.
     */
    @Test
    public void getTextRenderMode_shouldReturnDefaultValueFromGraphicsState() {
        // Arrange
        // A default GraphicsState has a renderMode of 0. The getTextRenderMode() method
        // is expected to return this value.
        GraphicsState graphicsState = new GraphicsState();

        // The TextRenderInfo constructor requires a non-null font in the GraphicsState
        // to avoid a NullPointerException. We create a dummy font for this purpose.
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        // Create other dummy objects required by the TextRenderInfo constructor.
        PdfString dummyText = new PdfString("any text");
        Matrix dummyMatrix = new Matrix();

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                dummyText,
                graphicsState,
                dummyMatrix,
                Collections.emptyList()
        );

        // Act
        int actualRenderMode = textRenderInfo.getTextRenderMode();

        // Assert
        // According to the PDF specification, a render mode of 0 means "Fill text".
        int expectedRenderMode = 0;
        assertEquals("The default text render mode should be 0.", expectedRenderMode, actualRenderMode);
    }
}