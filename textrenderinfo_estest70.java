package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link TextRenderInfo} class.
 * Note: The original test class name "TextRenderInfo_ESTestTest70" is kept as per the request.
 * In a real-world scenario, this would be renamed to "TextRenderInfoTest".
 */
public class TextRenderInfo_ESTestTest70 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Tests that the getPdfString() method returns the exact same PdfString instance
     * that was provided to the TextRenderInfo constructor.
     */
    @Test
    public void getPdfString_returnsSameInstancePassedToConstructor() {
        // Arrange: Create the necessary objects to instantiate TextRenderInfo.
        // A GraphicsState with a valid font is required for the constructor.
        GraphicsState graphicsState = new GraphicsState();
        PdfGState pdfGState = new PdfGState();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfGState);
        graphicsState.font = font;

        // The PdfString to be passed to the constructor. PdfDate is a valid subclass.
        PdfString originalPdfString = new PdfDate();

        // Other required constructor arguments.
        Matrix ctm = graphicsState.getCtm();
        Stack<MarkedContentInfo> markedContentStack = new Stack<>();

        TextRenderInfo textRenderInfo = new TextRenderInfo(originalPdfString, graphicsState, ctm, markedContentStack);

        // Act: Call the method under test.
        PdfString retrievedPdfString = textRenderInfo.getPdfString();

        // Assert: Verify that the returned object is the same instance as the original.
        assertSame("The getPdfString() method should return the same instance that was passed to the constructor.",
                originalPdfString, retrievedPdfString);
    }
}