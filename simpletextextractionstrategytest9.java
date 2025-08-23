package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
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
 * Tests the text extraction capabilities of {@link SimpleTextExtractionStrategy},
 * specifically focusing on text positioned with the TJ operator for glyph positioning.
 */
public class SimpleTextExtractionStrategyGlyphPositioningTest {

    /**
     * Verifies that text created with the "TJ" operator (show text with glyph positioning)
     * is extracted with correct word spacing.
     *
     * <p>The TJ operator in PDF allows for fine-grained control over the spacing between
     * characters and words by interspersing strings with numeric adjustments. This test ensures
     * that the extraction strategy correctly identifies word breaks based on these numeric
     * adjustments, which create larger-than-normal gaps between characters.</p>
     *
     * @throws IOException if a PDF or font-related I/O error occurs.
     * @throws DocumentException if a PDF creation error occurs.
     */
    @Test
    public void extractsTextWithCorrectSpacing_whenUsingGlyphPositioningOperator() throws IOException, DocumentException {
        // ARRANGE
        final String expectedText = "San Diego Chapter";

        // This string is a PDF content stream command using the "TJ" operator.
        // It displays an array of strings and spacing adjustments.
        // e.g., [(S) -255 (D)] renders "S", then moves the cursor left (creating a space), then renders "D".
        // The strategy must correctly interpret the large negative adjustments as word spaces.
        final String tjOperatorContent = "[(S)3.2(an)-255.0(D)13.0(i)8.3(e)-10.1(g)1.6(o)-247.5(C)2.4(h)5.8(ap)3.0(t)10.7(er)]TJ";
        byte[] pdfBytes = createPdfWithCustomContentStream(tjOperatorContent);

        PdfReader reader = new PdfReader(pdfBytes);
        TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        // ACT
        String actualText = PdfTextExtractor.getTextFromPage(reader, 1, strategy);

        // ASSERT
        Assert.assertEquals("The extracted text should match the intended words.", expectedText, actualText);
    }

    /**
     * Creates a simple PDF document containing a custom content stream command.
     * This is a low-level helper method to generate a PDF with specific syntax for testing.
     *
     * @param contentStreamCommand The raw PDF content stream command to embed in the page.
     * @return A byte array representing the generated PDF file.
     * @throws DocumentException if a PDF creation error occurs.
     * @throws IOException if a font-related I/O error occurs.
     */
    private static byte[] createPdfWithCustomContentStream(String contentStreamCommand) throws DocumentException, IOException {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.LETTER);
            PdfWriter writer = PdfWriter.getInstance(document, byteStream);
            document.open();

            PdfContentByte cb = writer.getDirectContent();
            BaseFont font = BaseFont.createFont();

            cb.beginText();
            cb.setFontAndSize(font, 12);
            cb.moveText(100, 500); // Position the text on the page for visibility.
            // Directly append the raw PDF operator to the content stream.
            cb.getInternalBuffer().append(contentStreamCommand).append("\n");
            cb.endText();

            document.close();
            return byteStream.toByteArray();
        }
    }
}