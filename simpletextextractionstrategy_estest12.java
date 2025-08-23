package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.nio.CharBuffer;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * This test class was improved to clarify the purpose and behavior being tested
 * for the SimpleTextExtractionStrategy.
 */
public class SimpleTextExtractionStrategyTest {

    /**
     * Tests that calling renderText() after appendTextChunk() correctly combines the text
     * without causing errors or unexpected side effects.
     *
     * The original test performed this sequence of calls but had a meaningless assertion.
     * This version verifies that the strategy's final output is correct.
     */
    @Test
    public void renderText_whenCalledAfterAppendingChunk_buildsResultCorrectly() {
        // ARRANGE
        // Create the object under test.
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        // Create a TextRenderInfo object representing an empty text block.
        // This setup is required by the TextRenderInfo constructor.
        GraphicsState graphicsState = new GraphicsState();
        PdfAction dummyActionForFont = PdfAction.createLaunch("", "", "PDF", "");
        graphicsState.font = new CMapAwareDocumentFont(dummyActionForFont);
        Matrix matrix = graphicsState.ctm;
        TextRenderInfo emptyTextRenderInfo = new TextRenderInfo(new PdfString(""), graphicsState, matrix, Collections.emptySet());

        // Define the text to be appended manually.
        int bufferSize = 1037;
        CharBuffer textToAppend = CharBuffer.allocate(bufferSize);
        // The buffer is filled with null characters ('\u0000') by default.
        String expectedText = new String(new char[bufferSize]);

        // ACT
        // 1. Render an empty text chunk. This initializes the strategy's internal state (e.g., last text position).
        strategy.renderText(emptyTextRenderInfo);

        // 2. Append a chunk of text directly using the protected method.
        strategy.appendTextChunk(textToAppend);

        // 3. Render another empty text chunk. This ensures the internal state is not corrupted by the
        // append operation and that subsequent renders behave as expected.
        strategy.renderText(emptyTextRenderInfo);

        // ASSERT
        // The final result from the strategy should be exactly the text that was appended,
        // as the rendered text blocks were empty.
        String actualText = strategy.getResultantText();
        assertEquals("The extracted text should match the appended chunk.", expectedText, actualText);
    }
}