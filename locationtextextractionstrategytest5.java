package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

/**
 * Tests the text extraction capabilities of {@link LocationTextExtractionStrategy}
 * specifically concerning pages with rotation metadata.
 */
public class LocationTextExtractionStrategyPageRotationTest extends SimpleTextExtractionStrategyTest {

    @Override
    public TextExtractionStrategy createRenderListenerForTest() {
        return new LocationTextExtractionStrategy();
    }

    /**
     * Tests that text is extracted correctly and in the proper order from a page
     * that has been rotated 270 degrees. This ensures the strategy correctly
     * interprets the page's transformation matrix.
     */
    @Test
    public void testTextExtractionFromPageRotated270Degrees() throws Exception {
        // Arrange
        final String content = "A\nB\nC\nD";
        // A standard letter page is rotated 90 degrees three times (270 degrees clockwise).
        final Rectangle rotatedPageSize = PageSize.LETTER.rotate().rotate().rotate();

        byte[] pdfBytes = createPdfWithContentOnNewPages(rotatedPageSize, content);
        PdfReader reader = new PdfReader(pdfBytes);

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());

        // Assert
        Assert.assertEquals("The extracted text should match the original content despite page rotation.",
                content, extractedText);
    }

    /**
     * Creates a simple PDF document with the given page size.
     * Each string provided in the text array is added as a new paragraph on a new page.
     *
     * @param pageSize The page size for the document.
     * @param text     The content to add to the document, with each string on a new page.
     * @return A byte array representing the generated PDF.
     */
    private byte[] createPdfWithContentOnNewPages(Rectangle pageSize, final String... text) throws Exception {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Document document = new Document(pageSize);
        PdfWriter.getInstance(document, byteStream);

        document.open();
        for (String line : text) {
            document.add(new Paragraph(line));
            document.newPage();
        }
        document.close();

        return byteStream.toByteArray();
    }
}