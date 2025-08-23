package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that getCharacterRenderInfos() returns a list containing one
     * TextRenderInfo object for each character in the input string.
     */
    @Test
    public void getCharacterRenderInfos_shouldReturnOneInfoObjectPerCharacter() {
        // ARRANGE
        // 1. Define the input text. We use a 9-character string to make the
        //    expected size clear and consistent with the original test's assertion.
        String testString = "Test text";
        PdfString pdfString = new PdfString(testString);

        // 2. Set up a minimal GraphicsState required to instantiate TextRenderInfo.
        GraphicsState graphicsState = new GraphicsState();
        // A font must be set, otherwise the TextRenderInfo constructor throws a NullPointerException.
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());
        // The original test set horizontalScaling to 0. While it doesn't affect this assertion,
        // we retain it to preserve the original test's context.
        graphicsState.horizontalScaling = 0.0F;

        // 3. Provide other required parameters for the TextRenderInfo constructor.
        Matrix initialTextMatrix = graphicsState.ctm;
        Collection<MarkedContentInfo> markedContentInfos = Collections.emptyList();

        TextRenderInfo textRenderInfo = new TextRenderInfo(pdfString, graphicsState, initialTextMatrix, markedContentInfos);

        // ACT
        List<TextRenderInfo> characterInfos = textRenderInfo.getCharacterRenderInfos();

        // ASSERT
        // The method should return a list where the size matches the number of characters
        // (or more accurately, bytes) in the original string.
        assertEquals("The number of character info objects should match the input string length.",
                testString.length(), characterInfos.size());
    }
}