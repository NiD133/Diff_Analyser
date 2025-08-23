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
 * Tests the {@link LocationTextExtractionStrategy} for its ability to extract text
 * when rendered at different font sizes, including very small ones.
 */
public class LocationTextExtractionStrategyFontSizeTest extends SimpleTextExtractionStrategyTest {

    @Override
    public TextExtractionStrategy createRenderListenerForTest() {
        return new LocationTextExtractionStrategy();
    }

    /**
     * Tests that text is extracted correctly regardless of its font size,
     * including very small and normal sizes within the same document.
     */
    @Test
    public void extractsTextCorrectly_whenPdfContainsVaryingFontSizes() throws Exception {
        // Arrange
        byte[] pdfBytes = createPdfWithVaryingFontSizes();
        PdfReader pdfReader = new PdfReader(pdfBytes);
        TextExtractionStrategy strategy = createRenderListenerForTest();
        String expectedText = "Preface Preface ";

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(pdfReader, 1, strategy);

        // Assert
        Assert.assertEquals("The extracted text should match the content written to the PDF.",
                expectedText, extractedText);
    }

    /**
     * Creates a PDF document containing the same text written twice: once with a very small
     * font size and once with a normal font size.
     * <p>
     * This setup is designed to test the text extraction strategy's robustness
     * against extreme font scaling.
     *
     * @return A byte array representing the generated PDF file.
     * @throws IOException       if the font cannot be created.
     * @throws DocumentException if an error occurs during document creation.
     */
    private byte[] createPdfWithVaryingFontSizes() throws IOException, DocumentException {
        final float verySmallFontSize = 0.2f;
        final float normalFontSize = 10f;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0); // Disable compression for easier debugging
        doc.open();

        PdfContentByte canvas = writer.getDirectContent();
        BaseFont font = BaseFont.createFont();

        // The text "Preface " is added as a sequence of characters to a PdfTextArray.
        // This is a low-level way to add text, which this test needs to handle.
        PdfTextArray textArray = new PdfTextArray();
        textArray.add("P");
        textArray.add("r");
        textArray.add("e");
        textArray.add("f");
        textArray.add("a");
        textArray.add("c");
        textArray.add("e");
        textArray.add(" ");

        canvas.beginText();
        canvas.moveText(45, doc.getPageSize().getHeight() - 45);

        // First, show the text with a very small font size.
        canvas.setFontAndSize(font, verySmallFontSize);
        canvas.showText(textArray);

        // Second, show the same text with a normal font size.
        canvas.setFontAndSize(font, normalFontSize);
        canvas.showText(textArray);

        canvas.endText();
        doc.close();
        return baos.toByteArray();
    }
}