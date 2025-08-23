package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that getCharacterRenderInfos() returns an empty list
     * when the TextRenderInfo is created with an empty PdfString.
     */
    @Test
    public void getCharacterRenderInfos_withEmptyString_returnsEmptyList() {
        // Arrange: Set up the necessary objects for creating a TextRenderInfo instance.
        // A GraphicsState with a font is required by the constructor.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        PdfString emptyPdfString = new PdfString();
        Matrix initialTextMatrix = graphicsState.ctm;
        Collection<MarkedContentInfo> markedContent = new Stack<>();

        TextRenderInfo textRenderInfo = new TextRenderInfo(
            emptyPdfString,
            graphicsState,
            initialTextMatrix,
            markedContent
        );

        // Act: Call the method under test.
        List<TextRenderInfo> characterRenderInfos = textRenderInfo.getCharacterRenderInfos();

        // Assert: Verify that the returned list is empty.
        assertTrue(
            "The list of character render infos should be empty for an empty input string.",
            characterRenderInfos.isEmpty()
        );
    }
}