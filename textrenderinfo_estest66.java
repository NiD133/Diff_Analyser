package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;

// The original test class name and inheritance are preserved as per the prompt.
public class TextRenderInfo_ESTestTest66 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Tests that the getRise() method correctly retrieves the text rise value
     * from the GraphicsState it was constructed with.
     *
     * The 'rise' in a PDF's graphics state determines how far, in user space units,
     * text should be drawn above or below the baseline.
     */
    @Test
    public void getRise_shouldReturnRiseValueFromGraphicsState() {
        // Arrange
        final float expectedRise = 12.0f;

        // 1. Create a GraphicsState and set a specific 'rise' value.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.rise = expectedRise;

        // 2. A font is a required dependency for the TextRenderInfo constructor,
        //    but its specific properties are not relevant for this test.
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        // 3. Create other required parameters for the TextRenderInfo constructor.
        PdfString emptyText = new PdfString();
        Matrix textMatrix = graphicsState.getCtm();
        Collection<MarkedContentInfo> markedContent = new LinkedHashSet<>();

        TextRenderInfo textRenderInfo = new TextRenderInfo(emptyText, graphicsState, textMatrix, markedContent);

        // Act
        float actualRise = textRenderInfo.getRise();

        // Assert
        assertEquals("The rise value should be correctly retrieved from the graphics state.",
                expectedRise, actualRise, 0.01f);
    }
}