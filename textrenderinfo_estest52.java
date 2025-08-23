package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.nio.charset.IllegalCharsetNameException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// Note: The original class name "TextRenderInfo_ESTestTest52" suggests it was
// auto-generated. A more conventional name would be "TextRenderInfoTest".
public class TextRenderInfo_ESTestTest52 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that getAscentLine() throws an IllegalCharsetNameException
     * when the TextRenderInfo is created with a PdfString that has an
     * invalid encoding name.
     *
     * The TextRenderInfo class must decode its internal PdfString to calculate
     * text metrics. This test ensures that it correctly handles and propagates
     * exceptions from Java's charset mechanism when an invalid charset name
     * (e.g., ".notdef") is provided in the PdfString.
     */
    @Test(timeout = 4000)
    public void getAscentLine_whenUsingInvalidEncoding_throwsIllegalCharsetNameException() {
        // ARRANGE: Set up a TextRenderInfo object with a PdfString that has an invalid encoding.
        final String invalidEncoding = ".notdef";
        PdfString pdfStringWithInvalidEncoding = new PdfString("any-text-value", invalidEncoding);

        // A minimal GraphicsState is required for the TextRenderInfo constructor.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                pdfStringWithInvalidEncoding,
                graphicsState,
                graphicsState.getCtm(),
                Collections.emptyList() // MarkedContentInfo is not relevant for this test.
        );

        // ACT & ASSERT: Expect an IllegalCharsetNameException when a method requiring
        // string decoding is called.
        try {
            textRenderInfo.getAscentLine();
            fail("Expected an IllegalCharsetNameException to be thrown due to the invalid encoding.");
        } catch (IllegalCharsetNameException e) {
            // Verify that the exception correctly reports the invalid charset name.
            assertEquals("The exception should contain the invalid charset name that caused the failure.",
                    invalidEncoding, e.getCharsetName());
        }
    }
}