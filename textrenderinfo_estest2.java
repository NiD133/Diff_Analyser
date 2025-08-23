package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.List;
import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that getCharacterRenderInfos() correctly splits the original text into a list
     * of TextRenderInfo objects, with one object for each character in the source string.
     */
    @Test
    public void getCharacterRenderInfos_returnsOneInfoPerCharacter() {
        // Arrange: Set up the necessary objects for a TextRenderInfo instance.
        String testString = "Test Text"; // A string with 9 characters.
        PdfString pdfString = new PdfString(testString);

        // TextRenderInfo requires a GraphicsState with a valid font.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());
        graphicsState.characterSpacing = 7.0f; // This value is used in internal calculations.

        Matrix textMatrix = graphicsState.getCtm();
        Stack<MarkedContentInfo> markedContentStack = new Stack<>();

        TextRenderInfo fullTextRenderInfo = new TextRenderInfo(pdfString, graphicsState, textMatrix, markedContentStack);

        // Act: Call the method under test.
        List<TextRenderInfo> characterRenderInfos = fullTextRenderInfo.getCharacterRenderInfos();

        // Assert: Verify the results.
        // 1. The number of returned info objects should match the number of characters.
        assertEquals("The number of render infos should match the number of characters.",
                testString.length(), characterRenderInfos.size());

        // 2. The list of character-specific infos should not contain the parent info object.
        assertFalse("The list of character infos should not contain the parent info object.",
                characterRenderInfos.contains(fullTextRenderInfo));
    }
}