package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.DocumentFont;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TextRenderInfo_ESTestTest42 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that getDescentLine propagates a low-level error when the font
     * is incompletely initialized.
     *
     * This test simulates a scenario with a malformed PDF where a font dictionary
     * is present but lacks the necessary data for metric calculations. Attempting to
     * get text metrics should trigger an error in the underlying font encoding
     * mechanism, which is expected to be propagated. The test specifically checks
     * for a NoSuchMethodError, which was a known failure mode in certain environments.
     */
    @Test(timeout = 4000)
    public void getDescentLineWithIncompletelyInitializedFontShouldPropagateError() {
        // Arrange: Create a GraphicsState with a font that is missing essential data.
        // A CMapAwareDocumentFont created from an empty dictionary simulates a
        // malformed font definition.
        GraphicsState graphicsState = new GraphicsState();
        PdfDictionary emptyFontDictionary = new PdfGState(); // PdfGState is a subclass of PdfDictionary
        DocumentFont incompleteFont = new CMapAwareDocumentFont(emptyFontDictionary);
        graphicsState.font = incompleteFont;

        Matrix currentTransformationMatrix = graphicsState.getCtm();
        List<MarkedContentInfo> markedContentStack = Collections.emptyList();
        PdfString sampleText = new PdfString("test text", "Cp1250");

        TextRenderInfo renderInfo = new TextRenderInfo(
                sampleText,
                graphicsState,
                currentTransformationMatrix,
                markedContentStack
        );

        // Act & Assert: Expect a NoSuchMethodError when accessing font metrics.
        // The call to getDescentLine requires decoding the text, which fails due to
        // the incomplete font data, leading to an error deep in the encoding logic.
        try {
            renderInfo.getDescentLine();
            fail("Expected a NoSuchMethodError due to the incomplete font setup, but no exception was thrown.");
        } catch (NoSuchMethodError e) {
            // The exception is expected. We can further assert that the error originates
            // from the expected part of the codebase to make the test more precise.
            assertStackTraceContains(e, "com.itextpdf.text.pdf.PdfEncodings");
        }
    }

    /**
     * Helper method to assert that an exception's stack trace contains a specific class name.
     *
     * @param throwable The exception to inspect.
     * @param className The class name expected to be in the stack trace.
     */
    private void assertStackTraceContains(Throwable throwable, String className) {
        for (StackTraceElement element : throwable.getStackTrace()) {
            if (element.getClassName().equals(className)) {
                return; // Found it
            }
        }
        fail("The exception's stack trace was expected to contain the class '" + className + "', but it did not.");
    }
}