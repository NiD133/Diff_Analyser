package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

/**
 * This test was part of an auto-generated suite.
 * The original class name (TextRenderInfo_ESTestTest45) and scaffolding are preserved
 * for context, but a more conventional name would be TextRenderInfoTest.
 */
public class TextRenderInfo_ESTestTest45 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that getCharacterRenderInfos() throws a NullPointerException
     * when the TextRenderInfo object was initialized with a null PdfString.
     * The method requires a non-null string to split into individual characters.
     */
    @Test(expected = NullPointerException.class)
    public void getCharacterRenderInfos_whenPdfStringIsNull_throwsNullPointerException() {
        // Arrange: Create a TextRenderInfo instance with a null PdfString.
        // The constructor requires a valid GraphicsState with a font.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());
        Matrix textMatrix = new Matrix();

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                null, // The null PdfString that is expected to cause the exception
                graphicsState,
                textMatrix,
                Collections.emptyList()
        );

        // Act: Call the method under test.
        textRenderInfo.getCharacterRenderInfos();

        // Assert: A NullPointerException is expected, as specified by the @Test annotation.
    }
}