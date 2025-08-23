package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

/**
 * This test verifies the spacing logic of the SimpleTextExtractionStrategy.
 * Specifically, it checks how the strategy handles a space between two separate
 * text chunks when the first chunk already ends with a space character.
 */
public class SimpleTextExtractionStrategySpacingTest {

    /**
     * Tests that the strategy does not add an extra space between two text chunks
     * if the first chunk already ends with a space. The strategy should correctly
     * identify that the accumulated text already ends with a space and not insert
     * a redundant one, even if there is a physical gap between the rendered chunks.
     */
    @Test
    public void shouldNotAddExtraSpaceWhenFirstChunkEndsWithSpace() throws Exception {
        // ARRANGE
        // Define two text chunks. The first one has a significant trailing space.
        String textChunk1WithTrailingSpace = "TEXT1 TEXT1 ";
        String textChunk2 = "TEXT2 TEXT2";

        // The expected result is the simple concatenation of the two chunks.
        String expectedText = "TEXT1 TEXT1 TEXT2 TEXT2";

        // Create a PDF where the two chunks are rendered with a physical gap between them.
        // A gap is necessary to trigger the strategy's space-insertion logic.
        final float gapBetweenChunks = 6.0f;
        byte[] pdfBytes = createPdfWithTwoSpacedTextChunks(textChunk1WithTrailingSpace, textChunk2, gapBetweenChunks);

        PdfReader reader = new PdfReader(pdfBytes);
        TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        // ACT
        // Extract text using the strategy under test.
        String actualText = PdfTextExtractor.getTextFromPage(reader, 1, strategy);
        reader.close();

        // ASSERT
        Assert.assertEquals("Strategy should not add a redundant space.", expectedText, actualText);
    }

    /**
     * Creates a simple PDF document containing two strings of text rendered on the same line
     * with an explicit horizontal gap between them.
     * <p>
     * This is done with two separate "show text" operations, which causes the text extraction
     * strategy to receive two distinct TextRenderInfo objects to process.
     *
     * @param chunk1 The first string to render.
     * @param chunk2 The second string to render.
     * @param gap    The horizontal space (in points) to insert between the two chunks.
     * @return A byte array containing the generated PDF.
     */
    private byte[] createPdfWithTwoSpacedTextChunks(String chunk1, String chunk2, float gap) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            PdfContentByte cb = writer.getDirectContent();
            BaseFont font = BaseFont.createFont();

            cb.beginText();
            cb.setFontAndSize(font, 12);
            cb.moveText(50, 750); // Set a starting position for the text.

            // Render the first chunk.
            cb.showText(chunk1);

            // Introduce an explicit horizontal gap before rendering the second chunk.
            cb.moveText(gap, 0);

            // Render the second chunk.
            cb.showText(chunk2);

            cb.endText();
            document.close();
            return baos.toByteArray();
        }
    }
}