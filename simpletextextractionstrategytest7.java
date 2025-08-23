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

/**
 * Tests the text extraction behavior of {@link SimpleTextExtractionStrategy} for rotated text.
 */
public class SimpleTextExtractionStrategyRotationTest {

    private static final String TEXT_LINE_1 = "This is the first line.";
    private static final String TEXT_LINE_2 = "This is the second line.";

    @Test
    public void extractsTextWithNewline_whenTextIsRotatedAndMovedToNewLine() throws Exception {
        // ARRANGE
        final float rotationDegrees = 33;
        final float verticalMovePoints = -20; // A negative value moves down the page.
        final boolean useMoveTextOperator = true; // Use Td operator, which SimpleTextExtractionStrategy should interpret as a new line.

        byte[] pdfBytes = createPdfWithRotatedAndMovedText(
                TEXT_LINE_1, TEXT_LINE_2, rotationDegrees, useMoveTextOperator, verticalMovePoints);

        PdfReader reader = new PdfReader(pdfBytes);
        TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        String expectedText = TEXT_LINE_1 + "\n" + TEXT_LINE_2;

        // ACT
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, strategy);

        // ASSERT
        Assert.assertEquals("The strategy should detect a newline for rotated text followed by a Td operator.",
                expectedText, extractedText);
    }

    /**
     * Creates a PDF with two strings. The first is rotated, and the second is positioned relative to the first.
     * <p>
     * This helper is designed to test how text extraction handles different PDF text-positioning operators
     * in combination with text rotation.
     *
     * @param text1               The first string to render.
     * @param text2               The second string to render.
     * @param rotationDegrees     The rotation to apply to the text matrix.
     * @param useMoveTextOperator If true, uses the 'Td' operator (moveText) to position the second string.
     *                            If false, concatenates a new translation to the text matrix ('Tm' operator).
     * @param moveDelta           The distance to move for the second string.
     * @return A byte array representing the generated PDF.
     */
    private byte[] createPdfWithRotatedAndMovedText(String text1, String text2, float rotationDegrees,
                                                    boolean useMoveTextOperator, float moveDelta) throws Exception {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.LETTER);
            PdfWriter writer = PdfWriter.getInstance(document, byteStream);
            document.open();

            PdfContentByte cb = writer.getDirectContent();
            BaseFont font = BaseFont.createFont();

            // Set the origin to the center of the page for easier visual debugging.
            float x = document.getPageSize().getWidth() / 2;
            float y = document.getPageSize().getHeight() / 2;
            cb.transform(AffineTransform.getTranslateInstance(x, y));

            cb.beginText();
            cb.setFontAndSize(font, 12);

            // 1. Rotate the coordinate system and show the first line of text.
            cb.transform(AffineTransform.getRotateInstance(Math.toRadians(rotationDegrees)));
            cb.showText(text1);

            // 2. Move to a new position and show the second line of text.
            if (useMoveTextOperator) {
                // The Td operator moves the text cursor from the start of the current line.
                // SimpleTextExtractionStrategy should interpret this as a new line.
                cb.moveText(0, moveDelta);
            } else {
                // Concatenating a translation to the Tm just moves the cursor on the same line.
                cb.transform(AffineTransform.getTranslateInstance(moveDelta, 0));
            }
            cb.showText(text2);

            cb.endText();
            document.close();

            return byteStream.toByteArray();
        }
    }
}