package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

/**
 * Test suite for {@link SimpleTextExtractionStrategy}.
 * This class focuses on specific rendering scenarios.
 */
public class SimpleTextExtractionStrategyTest {

    /**
     * Verifies that renderText throws a NoSuchMethodError when processing a PdfString
     * that triggers a specific, problematic encoding conversion path.
     *
     * This test case simulates a low-level binary incompatibility issue that can occur
     * in certain runtime environments when iText's PdfEncodings class is invoked.
     */
    @Test(expected = NoSuchMethodError.class, timeout = 4000)
    public void renderText_whenPdfStringRequiresEncodingConversion_throwsErrorOnCompatibilityIssue() {
        // Arrange
        // 1. Create the strategy instance to be tested.
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        // 2. Set up the necessary rendering context (GraphicsState).
        // A font must be present for the TextRenderInfo to be valid.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfAction(""));

        // 3. Create a PdfString with a value and a different encoding. This specific
        // constructor forces a call into the PdfEncodings utility class, which is
        // the source of the expected error in an incompatible environment.
        PdfString textWithMismatchedEncoding = new PdfString("Cp1252", "Cp1250");

        // 4. Assemble the TextRenderInfo object with the prepared data.
        TextRenderInfo textRenderInfo = new TextRenderInfo(
                textWithMismatchedEncoding,
                graphicsState,
                graphicsState.getCtm(),
                Collections.emptySet()
        );

        // Act
        // Call the method under test. This will trigger the encoding conversion
        // that leads to the expected NoSuchMethodError.
        strategy.renderText(textRenderInfo);

        // Assert
        // The test passes if a NoSuchMethodError is thrown, as declared by the
        // @Test(expected = ...) annotation. No further assertions are needed.
    }
}