package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

/**
 * Tests the text ordering capabilities of {@link LocationTextExtractionStrategy},
 * specifically its handling of text chunks based on their vertical (Y-axis) position.
 */
public class LocationTextExtractionStrategyYPositionTest extends SimpleTextExtractionStrategyTest {

    @Override
    @Before
    public void setUp() throws Exception {
        // Method from base class, no setup needed for this specific test.
    }

    @Override
    @After
    public void tearDown() throws Exception {
        // Method from base class, no teardown needed for this specific test.
    }

    @Override
    public TextExtractionStrategy createRenderListenerForTest() {
        return new LocationTextExtractionStrategy();
    }

    /**
     * Tests that the strategy correctly orders text that is vertically interleaved,
     * simulating two columns of text where one is slightly offset downwards.
     * The expected result is that text is sorted by its Y-position, resulting in
     * an interleaved output.
     */
    @Test
    public void getTextFromPage_whenTextIsVerticallyInterleaved_ordersTextCorrectly() throws Exception {
        // Arrange: Create a PDF with two columns of text that are vertically interleaved.
        String[] column1 = {"A", "B", "C", "D"};
        String[] column2 = {"AA", "BB", "CC", "DD"};
        PdfReader pdfReader = createPdfWithTwoInterleavedColumnsOfText(column1, column2);
        String expectedText = "A\nAA\nB\nBB\nC\nCC\nD\nDD";

        // Act: Extract text using the LocationTextExtractionStrategy.
        String actualText = PdfTextExtractor.getTextFromPage(pdfReader, 1, createRenderListenerForTest());

        // Assert: Verify that the extracted text is ordered by vertical position.
        Assert.assertEquals(expectedText, actualText);
    }

    /**
     * Creates a PDF document with two columns of text at the same X-coordinate.
     * The second column is shifted down vertically so that its lines appear between
     * the lines of the first column.
     *
     * @param text1 The strings for the first column.
     * @param text2 The strings for the second (interleaved) column.
     * @return A {@link PdfReader} for the generated PDF.
     */
    private PdfReader createPdfWithTwoInterleavedColumnsOfText(String[] text1, String[] text2) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        doc.open();

        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);

        final float xPosition = 50;
        final float initialYPosition = 500;
        final float lineHeight = 25.0f;

        // Draw the first column of text.
        float currentY = initialYPosition;
        for (String text : text1) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, xPosition, currentY, 0);
            currentY -= lineHeight;
        }

        // Draw the second column, starting it at a Y-position that falls between
        // the first and second lines of the first column. A -13 offset is slightly
        // more than half the line height, ensuring proper interleaving.
        currentY = initialYPosition - (lineHeight / 2.0f) - 1; // e.g., 500 - 12.5 - 1 = 486.5
        for (String text : text2) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, xPosition, currentY, 0);
            currentY -= lineHeight;
        }

        canvas.endText();
        doc.close();

        return new PdfReader(baos.toByteArray());
    }
}