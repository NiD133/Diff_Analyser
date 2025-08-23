package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.nio.charset.IllegalCharsetNameException;
import java.util.LinkedHashSet;

/**
 * Test suite for {@link SimpleTextExtractionStrategy}.
 */
public class SimpleTextExtractionStrategyTest {

    /**
     * Verifies that renderText throws an IllegalCharsetNameException when the
     * text to be rendered has an invalid encoding name.
     *
     * The underlying java.nio.charset.Charset.forName() method is expected to
     * throw this exception for malformed charset names.
     */
    @Test(expected = IllegalCharsetNameException.class)
    public void renderTextWithInvalidEncodingThrowsException() {
        // Arrange: Create a TextRenderInfo object containing a PdfString with an invalid encoding.
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        // The encoding ">|" is not a valid charset name and should cause an exception.
        String invalidEncoding = ">|";
        PdfString textWithInvalidEncoding = new PdfString("Identity-H", invalidEncoding);

        // Create the necessary dependencies for TextRenderInfo.
        // A CMapAwareDocumentFont is needed, which requires a PdfDictionary.
        // PdfAction is a convenient subclass of PdfDictionary for this purpose.
        PdfDictionary dummyPdfObjectForFont = PdfAction.createLaunch("", "", "PDF", "");
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(dummyPdfObjectForFont);

        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = font;

        TextRenderInfo renderInfo = new TextRenderInfo(
                textWithInvalidEncoding,
                graphicsState,
                new Matrix(),
                new LinkedHashSet<>()
        );

        // Act: Attempt to render the text. An IllegalCharsetNameException is expected
        // because the encoding name is invalid.
        strategy.renderText(renderInfo);

        // Assert: The test automatically passes if the expected exception is thrown.
        // It fails if no exception or a different one is thrown.
    }
}