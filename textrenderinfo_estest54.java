package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Stack;

/**
 * Test suite for {@link TextRenderInfo}.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that getAscentLine() throws a NoSuchMethodError when TextRenderInfo
     * is initialized with a font that has not been properly constructed.
     *
     * <p>This edge case simulates a scenario where a CMapAwareDocumentFont is created
     * from a default PdfGState, leaving it in an incomplete state. The subsequent
     * call to getAscentLine() fails deep within the font encoding logic, leading to
     * a linkage error. This test ensures that such a failure is caught.</p>
     */
    @Test(expected = NoSuchMethodError.class, timeout = 4000)
    public void getAscentLine_whenFontIsIncompletelyInitialized_throwsNoSuchMethodError() {
        // Arrange: Create a TextRenderInfo with a partially initialized font.
        // A CMapAwareDocumentFont created from a default PdfGState lacks the
        // necessary underlying font dictionary to function correctly.
        GraphicsState graphicsState = new GraphicsState();
        CMapAwareDocumentFont partiallyInitializedFont = new CMapAwareDocumentFont(new PdfGState());
        graphicsState.font = partiallyInitializedFont;

        PdfString sampleString = new PdfString("Helvetica-BoldOblique", "Cp1257");
        Matrix textMatrix = graphicsState.getCtm();
        Stack<MarkedContentInfo> markedContentStack = new Stack<>();

        TextRenderInfo renderInfo = new TextRenderInfo(sampleString, graphicsState, textMatrix, markedContentStack);

        // Act: Attempt to get the ascent line. This triggers a call chain that fails
        // due to the font's incomplete setup.
        renderInfo.getAscentLine();

        // Assert: The test expects a NoSuchMethodError, which is handled by the
        // @Test(expected=...) annotation.
    }
}