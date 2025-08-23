package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;

public class TextRenderInfo_ESTestTest14 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Tests that hasMcid returns false when searching for a non-existent Marked Content ID (MCID)
     * at only the topmost level of the content stack.
     */
    @Test
    public void hasMcid_withTopmostOnlyCheck_returnsFalseWhenMcidIsAbsent() {
        // Arrange: Set up a TextRenderInfo with marked content that does not contain the target MCID.
        final int mcidToSearchFor = 1046;
        final int existingMcid = 99;

        // Create a marked content item with a different MCID to ensure the method checks for the correct value.
        PdfDictionary properties = new PdfDictionary();
        properties.put(PdfName.MCID, new PdfNumber(existingMcid));
        MarkedContentInfo existingMarkedContent = new MarkedContentInfo(PdfName.P, properties);

        List<MarkedContentInfo> markedContentInfos = new ArrayList<>();
        markedContentInfos.add(existingMarkedContent);

        // Create the necessary GraphicsState and other parameters for TextRenderInfo.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfDictionary()); // A non-null font is required.
        PdfString dummyText = new PdfString("test text");
        Matrix ctm = new Matrix();

        TextRenderInfo textRenderInfo = new TextRenderInfo(dummyText, graphicsState, ctm, markedContentInfos);

        // Act: Check for an MCID that is not present, searching only the topmost level.
        boolean result = textRenderInfo.hasMcid(mcidToSearchFor, true);

        // Assert: The method should return false as the specified MCID was not found.
        assertFalse("hasMcid should return false for a non-existent MCID.", result);
    }
}