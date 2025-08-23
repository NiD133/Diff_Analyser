package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertNull;

/**
 * Contains tests for the {@link TextRenderInfo} class, focusing on Marked Content identification.
 */
// The original class name and extension are preserved to maintain compatibility
// with the existing test suite structure, which may rely on scaffolding.
public class TextRenderInfo_ESTestTest57 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that {@link TextRenderInfo#getMcid()} returns null when the associated
     * marked content exists but does not have a specific Marked Content ID (MCID) property.
     */
    @Test
    public void getMcid_shouldReturnNull_whenMarkedContentHasNoMcidProperty() {
        // Arrange: Set up the necessary objects to create a TextRenderInfo instance.
        
        // A GraphicsState with a font is required for TextRenderInfo instantiation.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfDictionary());

        // Create a MarkedContentInfo with a properties dictionary that intentionally
        // lacks the MCID (Marked Content ID) key. This is the specific condition under test.
        PdfDictionary propertiesWithoutMcid = new PdfDictionary();
        MarkedContentInfo markedContentInfo = new MarkedContentInfo(new PdfName("Tag"), propertiesWithoutMcid);
        Collection<MarkedContentInfo> markedContentInfos = Collections.singleton(markedContentInfo);

        // Create the TextRenderInfo instance to be tested.
        TextRenderInfo renderInfo = new TextRenderInfo(
                new PdfString("test text"),
                graphicsState,
                new Matrix(),
                markedContentInfos
        );

        // Act: Call the method under test.
        Integer mcid = renderInfo.getMcid();

        // Assert: Verify that the result is null, as expected.
        assertNull("The MCID should be null when the marked content properties do not define an MCID.", mcid);
    }
}