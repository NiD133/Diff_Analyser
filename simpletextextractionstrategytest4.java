package com.itextpdf.text.pdf.parser;

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
 * Tests the text extraction behavior of the {@link SimpleTextExtractionStrategy}.
 */
public class SimpleTextExtractionStrategyTest {

    private static final String FIRST_LINE_TEXT = "This is the first line.";
    private static final String SECOND_LINE_TEXT = "This is the second line.";

    /**
     * Tests that the strategy correctly inserts a newline character between chunks of text
     * that are rendered on separate lines in the PDF. This is determined by a change in the
     * vertical (Y) coordinate between text rendering operations.
     */
    @Test
    public void extractsTextWithNewlineForTextOnSeparateLines() throws Exception {
        // Arrange: Create a PDF with two lines of text, one directly below the other.
        // The SimpleTextExtractionStrategy should detect the vertical separation and
        // insert a newline.
        String expectedText = FIRST_LINE_TEXT + "\n" + SECOND_LINE_TEXT;
        byte[] pdfBytes = createPdfWithTwoLinesSeparatedVertically(FIRST_LINE_TEXT, SECOND_LINE_TEXT);
        TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        // Act: Extract text from the generated PDF.
        String actualText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, strategy);

        // Assert: Verify the extracted text contains a newline between the two lines.
        Assert.assertEquals(expectedText, actualText);
    }

    /**
     * Creates a simple PDF document containing two strings of text on separate lines.
     *
     * @param line1 The text for the first line.
     * @param line2 The text for the second line, to be placed directly below the first.
     * @return A byte array representing the generated PDF file.
     */
    private byte[] createPdfWithTwoLinesSeparatedVertically(String line1, String line2) throws Exception {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Document document = new Document(PageSize.LETTER);
        final PdfWriter writer = PdfWriter.getInstance(document, byteStream);
        document.open();

        PdfContentByte cb = writer.getDirectContent();
        BaseFont font = BaseFont.createFont();
        float x = 100;
        float y = 500;
        float verticalOffset = -20; // A negative offset moves the text cursor down for the next line.

        cb.beginText();
        cb.setFontAndSize(font, 12);
        cb.moveText(x, y); // Set initial position.

        cb.showText(line1);
        cb.moveText(0, verticalOffset); // Move to the next line, directly below.
        cb.showText(line2);

        cb.endText();
        document.close();

        return byteStream.toByteArray();
    }
}