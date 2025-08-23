package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfTextArray;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests the {@link LocationTextExtractionStrategy} for its handling of text with unusual character spacing,
 * specifically negative spacing (kerning) that can cause characters to visually overlap.
 */
public class LocationTextExtractionStrategyCharacterSpacingTest {

    /**
     * This test verifies that the text extraction strategy correctly interprets the logical order of characters
     * even when negative spacing (kerning) causes them to visually overlap or appear out of order in the PDF.
     * The strategy should return the text in the order it was defined, not its visual order.
     */
    @Test
    public void getText_whenCharactersOverlapDueToKerning_returnsTextInLogicalOrder() throws IOException, DocumentException {
        // Arrange
        // Create a PDF with "W" followed by "A", but with a large negative kerning value
        // that moves "A" back over "W".
        byte[] pdfBytes = createPdfWithKerningAdjustment("W", "A", 200);
        PdfReader reader = new PdfReader(pdfBytes);
        LocationTextExtractionStrategy strategy = createStrategy();

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, strategy);

        // Assert
        // The logical order of characters is "WA", which should be preserved.
        Assert.assertEquals("WA", extractedText);
    }

    /**
     * Creates a simple PDF document containing two strings with a specified kerning adjustment between them.
     *
     * @param text1        The first string.
     * @param text2        The second string.
     * @param kerningValue The kerning value to apply between the strings. In a PDF `TJ` array,
     *                     a positive number corresponds to a negative horizontal shift (i.e., moving the cursor to the left).
     *                     The value is measured in thousandths of a text space unit.
     * @return A byte array representing the generated PDF.
     */
    private byte[] createPdfWithKerningAdjustment(String text1, String text2, float kerningValue) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0); // Disable compression for easier debugging if needed
        doc.open();

        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        canvas.moveText(50, 800);

        PdfTextArray textArray = new PdfTextArray();
        textArray.add(text1);
        textArray.add(kerningValue); // This value adjusts the space before the next string.
        textArray.add(text2);
        canvas.showText(textArray);

        canvas.endText();
        doc.close();
        return baos.toByteArray();
    }

    private LocationTextExtractionStrategy createStrategy() {
        return new LocationTextExtractionStrategy();
    }
}