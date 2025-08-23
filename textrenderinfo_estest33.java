package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import org.junit.Test;

import java.util.ArrayList;

public class TextRenderInfo_ESTestTest33 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that calling getUnscaledBaseline() on a TextRenderInfo instance
     * created with a null PdfString throws a NullPointerException.
     * This is expected because calculating the baseline requires measuring the
     * string's width, which is not possible if the string is null.
     */
    @Test(expected = NullPointerException.class)
    public void getUnscaledBaseline_whenConstructedWithNullPdfString_throwsNullPointerException() {
        // Arrange: Create a TextRenderInfo with a null PdfString, which is the condition under test.
        // A GraphicsState with a valid font is required for the object's construction.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        Matrix textMatrix = new Matrix();
        ArrayList<MarkedContentInfo> markedContent = new ArrayList<>();

        TextRenderInfo textRenderInfo = new TextRenderInfo(null, graphicsState, textMatrix, markedContent);

        // Act: Attempt to get the unscaled baseline.
        textRenderInfo.getUnscaledBaseline();

        // Assert: The @Test(expected) annotation handles the verification that an NPE was thrown.
    }
}