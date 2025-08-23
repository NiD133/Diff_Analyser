package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests the {@link LocationTextExtractionStrategy} for its ability to correctly order
 * text chunks that are spatially interleaved on the page.
 */
public class LocationTextExtractionStrategyOverlappingTextTest extends SimpleTextExtractionStrategyTest {

    @Override
    public TextExtractionStrategy createRenderListenerForTest() {
        return new LocationTextExtractionStrategy();
    }

    /**
     * This test verifies that the {@link LocationTextExtractionStrategy} can correctly
     * reconstruct the reading order of text that is horizontally interleaved.
     * It creates a PDF where two sets of text strings are placed on the same line
     * in an alternating pattern (e.g., "A", then "AA" slightly to the right, then "B", etc.).
     * The strategy should correctly sort these chunks by their x-coordinate to produce a coherent line of text.
     */
    @Test
    public void extractsHorizontallyInterleavedTextInReadingOrder() throws Exception {
        // Arrange: Create a PDF with two sets of text that are interleaved on the same horizontal line.
        // The layout on the page will look like: A AA B BB C CC D DD
        byte[] pdfBytes = createPdfWithHorizontallyInterleavedText(
                new String[]{"A", "B", "C", "D"},
                new String[]{"AA", "BB", "CC", "DD"}
        );
        PdfReader pdfReader = new PdfReader(pdfBytes);
        TextExtractionStrategy strategy = createRenderListenerForTest();
        String expectedText = "A AA B BB C CC D DD";

        // Act: Extract text from the first page using the location-aware strategy.
        String extractedText = PdfTextExtractor.getTextFromPage(pdfReader, 1, strategy);

        // Assert: The extracted text should match the visual reading order.
        Assert.assertEquals(expectedText, extractedText);
    }

    /**
     * Creates a PDF document with two series of text fragments placed on the same horizontal line.
     * The second series is slightly offset to the right of the first, creating an interleaved effect.
     * For example, text1={"A", "B"} and text2={"AA", "BB"} would be placed like:
     * A   B
     *   AA  BB
     *
     * @param textFragments1 The first set of text fragments.
     * @param textFragments2 The second set of text fragments, to be interleaved with the first.
     * @return A byte array representing the generated PDF file.
     */
    private byte[] createPdfWithHorizontallyInterleavedText(String[] textFragments1, String[] textFragments2)
            throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        doc.open();

        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);

        // Define starting coordinates for the text.
        float yPosition = 500;
        float xStart = 50;
        float xIncrement = 70.0f;

        // Draw the first set of text fragments.
        float currentX = xStart;
        for (String text : textFragments1) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, currentX, yPosition, 0);
            currentX += xIncrement;
        }

        // Draw the second set of text fragments, slightly offset to the right to interleave them.
        float xOffset = 12;
        currentX = xStart + xOffset;
        for (String text : textFragments2) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, currentX, yPosition, 0);
            currentX += xIncrement;
        }

        canvas.endText();
        doc.close();
        return baos.toByteArray();
    }
}