package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Contains tests for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that hasMcid() throws a NullPointerException if the underlying
     * collection of marked content information contains a null element.
     * This tests the method's robustness against potentially corrupt or
     * unexpected PDF content structures.
     */
    @Test(expected = NullPointerException.class)
    public void hasMcid_whenMarkedContentInfoListContainsNull_throwsNullPointerException() {
        // ARRANGE: Set up a TextRenderInfo instance where the list of marked content
        // information contains a null value. This is the specific edge case under test.

        // A GraphicsState with a font is required by the TextRenderInfo constructor.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        // Create a collection for MarkedContentInfo and add a null element.
        Collection<MarkedContentInfo> markedContentWithNull = new ArrayList<>();
        markedContentWithNull.add(null);

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                new PdfString("test text"),
                graphicsState,
                graphicsState.getCtm(),
                markedContentWithNull
        );

        // ACT: Call the method that is expected to fail.
        // The value of the mcid does not matter for this test.
        textRenderInfo.hasMcid(42);

        // ASSERT: The @Test(expected) annotation handles the assertion,
        // failing the test if a NullPointerException is not thrown.
    }
}