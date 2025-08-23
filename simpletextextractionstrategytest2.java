package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests the text extraction logic of {@link SimpleTextExtractionStrategy}.
 */
public class SimpleTextExtractionStrategyTest {

    private static final String TEXT_CHUNK_1 = "Hello";
    private static final String TEXT_CHUNK_2 = "World";

    /**
     * Verifies that the strategy inserts a space between two text chunks that are on the same line
     * but separated by a small horizontal gap. This simulates how words are typically spaced in a sentence.
     */
    @Test
    public void shouldInsertSpaceBetweenSlightlySeparatedTextOnSameLine() throws IOException, DocumentException {
        // Arrange: Create a PDF with two text chunks on the same line with a small gap between them.
        final float spacing = 2f; // A small horizontal gap in points.
        byte[] pdfBytes = createPdfWithTwoHorizontallySpacedStrings(TEXT_CHUNK_1, TEXT_CHUNK_2, spacing);
        PdfReader reader = new PdfReader(pdfBytes);
        TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        String expectedText = TEXT_CHUNK_1 + " " + TEXT_CHUNK_2;

        // Act: Extract text from the PDF page.
        String actualText = PdfTextExtractor.getTextFromPage(reader, 1, strategy);

        // Assert: The extracted text should contain a space between the two original chunks.
        Assert.assertEquals(expectedText, actualText);
    }

    /**
     * Creates a simple PDF document containing two strings of text written on the same
     * horizontal line, separated by a specified spacing.
     *
     * @param text1   The first string to write.
     * @param text2   The second string to write.
     * @param spacing The horizontal space (in points) to leave between the two strings.
     * @return A byte array representing the generated PDF file.
     */
    private byte[] createPdfWithTwoHorizontallySpacedStrings(String text1, String text2, float spacing)
            throws DocumentException, IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            PdfContentByte cb = writer.getDirectContent();
            cb.beginText();
            cb.setFontAndSize(BaseFont.createFont(), 12);
            cb.moveText(100, 500); // Set starting position

            // Write the first part of the text
            cb.showText(text1);

            // Move the text cursor horizontally to create a gap before the next word
            cb.moveText(spacing, 0);

            // Write the second part of the text
            cb.showText(text2);

            cb.endText();
            document.close();
            return baos.toByteArray();
        }
    }
}