package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertFalse;

/**
 * This class contains tests for the {@link TextRenderInfo} class.
 * This specific test focuses on the behavior of the hasMcid method.
 */
public class TextRenderInfo_ESTestTest69 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that hasMcid returns false when the TextRenderInfo object
     * is initialized with an empty collection of marked content.
     */
    @Test
    public void hasMcid_shouldReturnFalse_whenMarkedContentIsEmpty() {
        // ARRANGE
        // The TextRenderInfo constructor requires a GraphicsState with a non-null font.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        // Create an empty collection for marked content information.
        Collection<MarkedContentInfo> emptyMarkedContent = new ArrayList<>();

        // Create other required constructor arguments.
        PdfString text = new PdfString("test");
        Matrix textMatrix = new Matrix();

        TextRenderInfo textRenderInfo = new TextRenderInfo(text, graphicsState, textMatrix, emptyMarkedContent);

        int mcidToSearchFor = 32;

        // ACT
        boolean hasMcid = textRenderInfo.hasMcid(mcidToSearchFor);

        // ASSERT
        assertFalse("hasMcid should return false if the marked content list is empty.", hasMcid);
    }
}