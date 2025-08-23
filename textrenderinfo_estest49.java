package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import org.junit.Test;

import java.util.Collections;

/**
 * Test suite for the {@link TextRenderInfo} class, focusing on edge cases and invalid inputs.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that getBaseline() throws a NullPointerException when TextRenderInfo
     * is constructed with a null PdfString.
     *
     * This is the expected behavior because internal calculations for the baseline,
     * such as determining the string's width, cannot proceed without a valid string object.
     */
    @Test(expected = NullPointerException.class)
    public void getBaseline_whenPdfStringIsNull_throwsNullPointerException() {
        // Arrange: Create a TextRenderInfo instance with a null PdfString.
        // This requires setting up a valid GraphicsState and Matrix as dependencies.
        GraphicsState graphicsState = new GraphicsState();
        
        // A font must be set in the graphics state for the TextRenderInfo constructor to succeed.
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());
        Matrix initialMatrix = graphicsState.getCtm();

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                null, // The null PdfString is the specific condition under test.
                graphicsState,
                initialMatrix,
                Collections.emptyList()
        );

        // Act: Attempt to get the baseline. This action is expected to trigger the exception.
        textRenderInfo.getBaseline();

        // Assert: The @Test(expected) annotation automatically verifies that a
        // NullPointerException was thrown. No further assertion is needed.
    }
}