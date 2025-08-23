package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the spacing logic of the {@link SimpleTextExtractionStrategy}.
 */
public class SimpleTextExtractionStrategySpacingTest {

    /**
     * This test verifies a specific behavior of the SimpleTextExtractionStrategy: how it handles
     * spaces between separate text chunks.
     *
     * The strategy is designed to insert a space when it detects a spatial gap between two
     * consecutive text chunks. However, to avoid inserting duplicate spaces, it should not add a
     * space if the second chunk already begins with one. This test validates that specific edge case.
     */
    @Test
    public void renderText_whenChunkStartsWithSpace_doesNotAddDuplicateSpace() throws Exception {
        // ARRANGE
        // We create a PDF with two text chunks that are spatially separated.
        // The second chunk intentionally starts with a space.
        final String firstChunk = "Hello";
        final String secondChunkWithLeadingSpace = " World";

        // The strategy should detect the gap but refrain from adding its own space
        // because the second chunk already provides one.
        final String expectedText = "Hello World";

        // The helper method ensures the two chunks are rendered as separate drawing operations
        // with a significant gap between them, forcing the strategy's spacing logic to be evaluated.
        byte[] pdfBytes = createPdfWithTwoSeparateTextChunks(firstChunk, secondChunkWithLeadingSpace);
        PdfReader reader = new PdfReader(pdfBytes);

        // ACT
        // Extract text using the strategy under test.
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, new SimpleTextExtractionStrategy());

        // ASSERT
        Assert.assertEquals("The strategy should not add a duplicate space.", expectedText, extractedText);
    }

    /**
     * Creates a simple PDF with two text strings rendered as separate text-showing operations
     * with a horizontal gap between them. This setup is designed to test how a text extraction
     * strategy handles spacing between distinct text chunks.
     *
     * @param text1 The first text string.
     * @param text2 The second text string.
     * @return A byte array representing the generated PDF.
     */
    private byte[] createPdfWithTwoSeparateTextChunks(String text1, String text2) throws Exception {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Document document = new Document(PageSize.A4);
        final PdfWriter writer = PdfWriter.getInstance(document, byteStream);

        // Disable compression to make the internal PDF content stream easier to debug if needed.
        writer.setCompressionLevel(0);
        document.open();

        try {
            PdfContentByte cb = writer.getDirectContent();
            BaseFont font = BaseFont.createFont();
            final float fontSize = 12;
            final float startX = 50;
            final float startY = 700;
            // A horizontal gap to ensure the strategy's space-insertion logic is triggered.
            final float horizontalGap = 10f;

            // Render the first text chunk as a complete text object.
            cb.beginText();
            cb.setFontAndSize(font, fontSize);
            cb.moveText(startX, startY);
            cb.showText(text1);
            cb.endText();

            // Render the second chunk as another text object, shifted horizontally from the first.
            float text1Width = font.getWidthPoint(text1, fontSize);
            cb.beginText();
            cb.setFontAndSize(font, fontSize);
            cb.moveText(startX + text1Width + horizontalGap, startY);
            cb.showText(text2);
            cb.endText();

        } finally {
            document.close();
        }
        return byteStream.toByteArray();
    }
}