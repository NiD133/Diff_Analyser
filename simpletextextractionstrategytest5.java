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
 * Tests the text extraction behavior of {@link SimpleTextExtractionStrategy} for rotated text.
 */
public class SimpleTextExtractionStrategyRotationTest {

    private static final String TEXT_LINE_1 = "This is the first line.";
    private static final String TEXT_LINE_2 = "This is the second line.";

    /**
     * Verifies that the strategy correctly identifies a newline between two text chunks
     * even when the entire text block is rotated. The strategy should detect the
     * line break based on the relative movement of text in the rotated coordinate space.
     */
    @Test
    public void extractsTextWithNewline_whenTextIsRotatedAndMovedToNextLine() throws Exception {
        // Arrange
        // Create a PDF with two lines of text, rotated by -90 degrees.
        // The second line is explicitly moved down relative to the first in the rotated text space.
        float rotationDegrees = -90;
        float verticalMoveDelta = -20; // A negative value moves "down" in the text's coordinate system.
        byte[] pdfBytes = createPdfWithRotatedText(TEXT_LINE_1, TEXT_LINE_2, rotationDegrees, verticalMoveDelta);

        PdfReader reader = new PdfReader(pdfBytes);
        TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, strategy);

        // Assert
        String expectedText = TEXT_LINE_1 + "\n" + TEXT_LINE_2;
        Assert.assertEquals("The extracted text should contain a newline for rotated text.", expectedText, extractedText);
    }

    /**
     * Creates a PDF document with two lines of text where the entire text block is rotated.
     *
     * @param text1           The first line of text.
     * @param text2           The second line of text.
     * @param rotationDegrees The rotation to apply to the text block (in degrees).
     * @param moveTextDeltaY  The vertical distance to move the text cursor before drawing the second line.
     *                        This simulates a newline operation in the rotated text space.
     * @return A byte array representing the generated PDF.
     * @throws IOException if a low-level I/O problem occurs.
     * @throws com.itextpdf.text.DocumentException if a document-related problem occurs.
     */
    private byte[] createPdfWithRotatedText(String text1, String text2, float rotationDegrees, float moveTextDeltaY) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.LETTER);
        PdfWriter writer = PdfWriter.getInstance(document, byteStream);
        document.open();

        PdfContentByte cb = writer.getDirectContent();
        BaseFont font = BaseFont.createFont();
        float x = document.getPageSize().getWidth() / 2;
        float y = document.getPageSize().getHeight() / 2;

        cb.beginText();
        cb.setFontAndSize(font, 12);

        // Create a transformation that first translates to the center of the page, then rotates.
        // This matrix is applied to the text, positioning and rotating it in one operation.
        AffineTransform textMatrix = AffineTransform.getTranslateInstance(x, y);
        textMatrix.rotate(Math.toRadians(rotationDegrees));
        cb.setTextMatrix(textMatrix);

        // Show the first line of text.
        cb.showText(text1);

        // Move to the next line in the rotated coordinate system.
        // The SimpleTextExtractionStrategy should interpret this as a line break.
        cb.moveText(0, moveTextDeltaY);
        cb.showText(text2);

        cb.endText();
        document.close();
        return byteStream.toByteArray();
    }
}