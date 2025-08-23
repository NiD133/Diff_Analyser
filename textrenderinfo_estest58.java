package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertNull;

public class TextRenderInfo_ESTestTest58 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Tests that getMcid() returns null when the TextRenderInfo is created
     * with no marked content information.
     */
    @Test
    public void getMcid_whenNoMarkedContentInfoIsProvided_shouldReturnNull() {
        // Arrange: Set up the necessary objects for creating a TextRenderInfo instance.
        // A GraphicsState with a valid font is required by the constructor.
        GraphicsState graphicsState = new GraphicsState();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(new PdfGState());
        graphicsState.font = font;

        PdfString text = new PdfString("test text");
        Matrix textMatrix = new Matrix(); // An identity matrix is sufficient.

        // Use an empty collection to simulate the absence of marked content.
        Collection<MarkedContentInfo> emptyMarkedContentInfos = Collections.emptySet();

        TextRenderInfo textRenderInfo = new TextRenderInfo(text, graphicsState, textMatrix, emptyMarkedContentInfos);

        // Act: Call the method under test.
        Integer mcid = textRenderInfo.getMcid();

        // Assert: Verify that the Marked Content ID (MCID) is null, as expected.
        assertNull("The MCID should be null when the marked content info collection is empty.", mcid);
    }
}