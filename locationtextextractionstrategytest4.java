package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests text extraction using {@link LocationTextExtractionStrategy} on PDFs with rotated pages.
 * This class inherits from {@link SimpleTextExtractionStrategyTest} to reuse its framework.
 */
public class LocationTextExtractionStrategyPageRotationTest extends SimpleTextExtractionStrategyTest {

    @Override
    public TextExtractionStrategy createRenderListenerForTest() {
        return new LocationTextExtractionStrategy();
    }

    /**
     * Verifies that text is extracted correctly from a page that has been rotated twice (180 degrees).
     * The {@link LocationTextExtractionStrategy} must correctly interpret the page's transformation
     * matrix to extract text in the proper reading order, even when the page orientation is not standard.
     */
    @Test
    public void extractsTextCorrectlyFromDoublyRotatedPage() throws Exception {
        // Arrange
        final String expectedText = "A\nB\nC\nD";
        // A page rotated twice (90 + 90 = 180 degrees) is effectively upside down.
        Rectangle pageSize = PageSize.LETTER.rotate().rotate();
        byte[] pdfBytes = createSinglePagePdfWithText(pageSize, expectedText);
        PdfReader reader = new PdfReader(pdfBytes);

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());

        // Assert
        Assert.assertEquals("Extracted text should match the original text on the rotated page.",
                expectedText, extractedText);
    }

    /**
     * Creates a simple, single-page PDF containing the given text on a page of a specific size.
     *
     * @param pageSize The page dimensions.
     * @param text     The text to add to the document's first page.
     * @return A byte array representing the generated PDF.
     * @throws Exception if an error occurs during PDF creation.
     */
    private byte[] createSinglePagePdfWithText(Rectangle pageSize, final String text) throws Exception {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Document document = new Document(pageSize);
        PdfWriter.getInstance(document, byteStream);

        document.open();
        document.add(new Paragraph(text));
        document.close();

        return byteStream.toByteArray();
    }
}