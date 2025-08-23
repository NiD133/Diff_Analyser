package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Stack;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link TextRenderInfo}.
 */
public class TextRenderInfoTest {

    /**
     * Tests that getCharacterRenderInfos propagates a NoSuchMethodError when a
     * binary incompatibility is encountered during font decoding.
     *
     * This unusual scenario can occur if the application is compiled against one
     * version of a library (e.g., a newer JDK) but run in an environment with an
     * older, incompatible version. The test ensures that such a critical linkage
     * error, specifically from the PdfEncodings class, is not silently swallowed.
     */
    @Test
    public void getCharacterRenderInfos_whenFontDecodingFailsDueToLinkageError_throwsNoSuchMethodError() {
        // Arrange: Set up a TextRenderInfo instance with a font that requires
        // complex decoding, which is the path where the linkage error is triggered.
        GraphicsState graphicsState = new GraphicsState();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(new PdfGState());
        graphicsState.font = font;

        Matrix textMatrix = graphicsState.getCtm();
        PdfString pdfString = new PdfString("Symbol", "MacRoman");
        Collection<MarkedContentInfo> markedContentStack = new Stack<>();

        TextRenderInfo renderInfo = new TextRenderInfo(pdfString, graphicsState, textMatrix, markedContentStack);

        // Act & Assert: Expect a NoSuchMethodError originating from the PdfEncodings class.
        try {
            renderInfo.getCharacterRenderInfos();
            fail("A NoSuchMethodError was expected but not thrown.");
        } catch (NoSuchMethodError e) {
            // Verify that the error originates from the expected class. This confirms
            // that the test is validating the intended failure scenario within the
            // font encoding mechanism.
            boolean errorOriginatedInPdfEncodings = Arrays.stream(e.getStackTrace())
                    .anyMatch(element -> element.getClassName().equals("com.itextpdf.text.pdf.PdfEncodings"));

            assertTrue(
                "The NoSuchMethodError should originate from the PdfEncodings class.",
                errorOriginatedInPdfEncodings
            );
        }
    }
}