package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Stack;

/**
 * This test class contains tests for the {@link TextRenderInfo} class.
 * This specific test case was improved for clarity.
 */
public class TextRenderInfo_ESTestTest26 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that the {@link TextRenderInfo#hasMcid(int, boolean)} method throws a
     * NullPointerException if the internal collection of marked content information
     * contains a null element. This is a regression test to prevent crashes when
     * processing malformed or unexpected PDF content.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void hasMcid_whenMarkedContentInfosContainsNull_throwsNullPointerException() {
        // ARRANGE: Set up a TextRenderInfo instance where the collection of
        // marked content information contains a null element.
        GraphicsState graphicsState = new GraphicsState();
        // A font is required by the TextRenderInfo constructor to avoid a separate NPE.
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        // Create a collection for marked content and add a null element. This is the
        // specific condition that should trigger the NullPointerException.
        Stack<MarkedContentInfo> markedContentInfosWithNull = new Stack<>();
        markedContentInfosWithNull.add(null);

        PdfString dummyText = new PdfString("");
        Matrix ctm = graphicsState.getCtm();
        TextRenderInfo textRenderInfo = new TextRenderInfo(dummyText, graphicsState, ctm, markedContentInfosWithNull);

        int anyMcid = 2;
        boolean checkTopmostOnly = false;

        // ACT: Call the method under test. This is expected to throw a NullPointerException
        // because the method likely iterates over the markedContentInfos collection without a null check.
        textRenderInfo.hasMcid(anyMcid, checkTopmostOnly);

        // ASSERT: The 'expected' attribute of the @Test annotation automatically
        // verifies that a NullPointerException was thrown.
    }
}