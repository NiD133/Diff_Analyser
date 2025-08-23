package com.itextpdf.text.pdf.parser;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests the text extraction logic of {@link SimpleTextExtractionStrategy},
 * specifically how it handles spaces between collinear text chunks.
 */
public class SimpleTextExtractionStrategyCollinearTextTest {

    /**
     * This test verifies that SimpleTextExtractionStrategy does not insert an extra space
     * between two collinear text chunks when the first chunk already ends with a space.
     * The strategy should detect the existing space and merge the chunks without adding a redundant one.
     */
    @Test
    public void getTextFromPage_whenFirstOfTwoCollinearChunksEndsWithSpace_doesNotInsertExtraSpace() throws Exception {
        // ARRANGE: Define two text chunks to be placed on the same line. The first chunk
        // ends with a space, which is the key condition for this test.
        String textChunk1 = "Some text ending with a space ";
        String textChunk2 = "and the next chunk.";
        String expectedText = "Some text ending with a space and the next chunk.";

        // Create a PDF with two distinct, but horizontally adjacent, text rendering operations.
        byte[] pdfBytes = createPdfWithTwoCollinearTextChunks(textChunk1, textChunk2);

        // ACT: Extract text using the strategy under test.
        PdfReader reader = new PdfReader(pdfBytes);
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, new SimpleTextExtractionStrategy());
        reader.close();

        // ASSERT: Verify that no extra space was inserted between the chunks.
        Assert.assertEquals(expectedText, extractedText);
    }

    /**
     * Creates a simple PDF document with two text chunks rendered horizontally on the same line.
     * <p>
     * This helper method simulates two separate {@code showText} operations that are close enough
     * for the {@link SimpleTextExtractionStrategy} to consider them part of the same line of text.
     * A small gap is intentionally created between them to force the strategy to decide whether to
     * insert a space.
     *
     * @param text1 The first text chunk to render.
     * @param text2 The second text chunk to render.
     * @return A byte array representing the generated PDF.
     * @throws Exception if PDF creation fails.
     */
    private static byte[] createPdfWithTwoCollinearTextChunks(String text1, String text2) throws Exception {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.LETTER);
            PdfWriter writer = PdfWriter.getInstance(document, byteStream);
            document.open();

            PdfContentByte cb = writer.getDirectContent();
            BaseFont font = BaseFont.createFont();

            cb.beginText();
            cb.setFontAndSize(font, 12);
            cb.moveText(50, 700); // Set a starting position for the text.

            // Render the first text chunk. The text cursor automatically advances to the end of the string.
            cb.showText(text1);

            // Apply a small horizontal translation to create a second, distinct text rendering operation
            // on the same line. This forces the extraction strategy to evaluate the distance
            // between the end of the first chunk and the start of the second.
            cb.transform(AffineTransform.getTranslateInstance(2, 0)); // 2-point gap

            // Render the second text chunk.
            cb.showText(text2);

            cb.endText();
            document.close();

            return byteStream.toByteArray();
        }
    }
}