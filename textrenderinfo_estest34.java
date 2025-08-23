package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Stack;

import static org.evosuite.runtime.EvoAssertions.verifyException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TextRenderInfo_ESTestTest34 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that getUnscaledBaseline throws a NoSuchMethodError when an underlying
     * font encoding operation encounters a binary incompatibility.
     *
     * This test simulates a specific, low-level runtime environment issue where the
     * PdfEncodings class, used for font character mapping, calls a method
     * (e.g., java.nio.ByteBuffer.rewind()) with a signature that is unavailable
     * in the runtime environment, leading to a NoSuchMethodError.
     */
    @Test(timeout = 4000)
    public void getUnscaledBaseline_whenFontEncodingCausesBinaryIncompatibility_throwsNoSuchMethodError() {
        // Arrange: Set up a graphics state and a TextRenderInfo instance with a font
        // that requires character map ("CMap") encoding.
        GraphicsState graphicsState = new GraphicsState();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(new PdfGState());
        graphicsState.font = font;

        Matrix textMatrix = graphicsState.getCtm();
        Stack<MarkedContentInfo> markedContentStack = new Stack<>();

        // Using an explicit encoding like "Cp1250" ensures the font encoding logic is triggered.
        PdfString pdfString = new PdfString("SomeText", "Cp1250");
        TextRenderInfo textRenderInfo = new TextRenderInfo(pdfString, graphicsState, textMatrix, markedContentStack);

        // Act & Assert: Attempt to get the baseline and verify the expected error.
        try {
            textRenderInfo.getUnscaledBaseline();
            fail("Expected a NoSuchMethodError to be thrown due to a simulated binary incompatibility.");
        } catch (NoSuchMethodError e) {
            // Assert that the error message matches the expected missing method signature.
            // This confirms the specific nature of the incompatibility being tested.
            assertEquals("java.nio.ByteBuffer.rewind()Ljava/nio/ByteBuffer;", e.getMessage());

            // The EvoSuite assertion verifies that the error originates from the expected class,
            // confirming the failure occurs within the font encoding mechanism.
            verifyException("com.itextpdf.text.pdf.PdfEncodings", e);
        }
    }
}