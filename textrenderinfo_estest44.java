package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.nio.charset.IllegalCharsetNameException;
import java.util.Collections;

public class TextRenderInfo_ESTestTest44 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Tests that getCharacterRenderInfos() throws an IllegalCharsetNameException
     * when the underlying PdfString was created with an invalid encoding name.
     */
    @Test(timeout = 4000, expected = IllegalCharsetNameException.class)
    public void getCharacterRenderInfosShouldThrowExceptionForInvalidEncoding() {
        // Arrange: Create a TextRenderInfo with a PdfString that has an invalid encoding.
        // The string "com/itextpdf/text/pdf/fonts/" is not a valid Java charset name,
        // which should cause a failure during string decoding.
        String invalidEncoding = "com/itextpdf/text/pdf/fonts/";
        PdfString stringWithInvalidEncoding = new PdfString(".notdef", invalidEncoding);

        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());
        Matrix textMatrix = new Matrix();

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                stringWithInvalidEncoding,
                graphicsState,
                textMatrix,
                Collections.emptyList()
        );

        // Act: Attempt to get character information, which triggers the decoding.
        // The @Test(expected=...) annotation asserts that the expected exception is thrown.
        textRenderInfo.getCharacterRenderInfos();
    }
}