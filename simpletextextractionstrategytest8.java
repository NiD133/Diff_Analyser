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
 * Tests the spacing detection logic of the {@link SimpleTextExtractionStrategy}.
 */
public class SimpleTextExtractionStrategySpacingTest {

    private static final String TEXT_CHUNK_1 = "TEXT1 TEXT1";
    private static final String TEXT_CHUNK_2 = "TEXT2 TEXT2";

    /**
     * This test verifies that SimpleTextExtractionStrategy correctly inserts a space
     * when two text chunks are separated by a large positioning adjustment,
     * which is a common way to create word spacing in a PDF.
     */
    @Test
    public void testWordSpacingCausedByExplicitGlyphPositioning() throws IOException, com.itextpdf.text.DocumentException {
        // Arrange
        // A positioning adjustment of -250 thousandths of a text unit is used to create a visual gap.
        final int spacingAdjustment = 250;
        byte[] pdfBytes = createPdfWithSpacedTextUsingTjOperator(TEXT_CHUNK_1, TEXT_CHUNK_2, spacingAdjustment);
        
        PdfReader reader = new PdfReader(pdfBytes);
        TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        String expectedText = TEXT_CHUNK_1 + " " + TEXT_CHUNK_2;

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, strategy);

        // Assert
        // The strategy should interpret the visual gap created by the TJ operator as a word-separating space.
        Assert.assertEquals(expectedText, extractedText);
    }

    /**
     * Creates a PDF where two text chunks are separated by a positioning adjustment
     * rather than a literal space character. This is achieved using the PDF 'TJ' operator.
     * <p>
     * The TJ operator takes an array of strings and numbers. The numbers represent
     * horizontal adjustments in thousandths of a text unit. A negative number moves the
     * subsequent text to the right, effectively creating a space.
     *
     * @param text1             The first text chunk.
     * @param text2             The second text chunk.
     * @param spacingAdjustment The spacing adjustment in thousandths of a text unit.
     * @return A byte array representing the generated PDF.
     */
    private byte[] createPdfWithSpacedTextUsingTjOperator(String text1, String text2, int spacingAdjustment)
            throws IOException, com.itextpdf.text.DocumentException {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.LETTER);
            PdfWriter writer = PdfWriter.getInstance(document, byteStream);
            document.open();

            PdfContentByte cb = writer.getDirectContent();
            cb.beginText();
            cb.setFontAndSize(BaseFont.createFont(), 12);
            
            // The format "[ (string1) adjustment (string2) ] TJ" positions string2 relative to string1.
            // A negative adjustment value moves the text to the right, creating a space.
            String tjOperator = String.format("[(%s) %d (%s)]TJ\n", text1, -spacingAdjustment, text2);
            cb.getInternalBuffer().append(tjOperator);
            
            cb.endText();
            document.close();
            return byteStream.toByteArray();
        }
    }
}