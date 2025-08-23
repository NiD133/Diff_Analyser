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
 * Tests the {@link LocationTextExtractionStrategy} for its ability to handle
 * complex text spacing, such as negative character spacing and kerning.
 */
public class LocationTextExtractionStrategyComplexSpacingTest extends SimpleTextExtractionStrategyTest {

    @Override
    public TextExtractionStrategy createRenderListenerForTest() {
        return new LocationTextExtractionStrategy();
    }

    /**
     * Tests that the strategy correctly extracts a single word when its characters are positioned
     * using a combination of negative character spacing and individual kerning adjustments.
     */
    @Test
    public void shouldExtractWordCorrectly_whenTextHasComplexKerning() throws Exception {
        // ARRANGE: Create a PDF with the word "Preface" where characters have complex spacing.
        byte[] pdfBytes = createPdfWithKerningAdjustments();
        PdfReader reader = new PdfReader(pdfBytes);
        TextExtractionStrategy strategy = createRenderListenerForTest();
        String expectedText = "Preface";

        // ACT: Extract text from the PDF page.
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, strategy);

        // ASSERT: The strategy should correctly assemble the characters into a single word.
        Assert.assertEquals(expectedText, extractedText);
    }

    /**
     * Creates a PDF containing the word "Preface" with complex character positioning.
     * <p>
     * This method uses low-level PDF operators to simulate text with fine-grained spacing control:
     * 1. A negative character spacing (`Tc` operator) is applied globally.
     * 2. A `PdfTextArray` is used with the `TJ` operator to apply individual kerning
     *    adjustments between each character.
     * </p>
     * This setup creates a challenging scenario for a text extraction algorithm, ensuring it can
     * correctly reassemble a word from characters with non-standard spacing.
     *
     * @return A byte array representing the generated PDF.
     */
    private byte[] createPdfWithKerningAdjustments() throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0); // Disable compression for easier inspection
        doc.open();

        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();

        BaseFont font = BaseFont.createFont();
        int fontSize = 12;
        canvas.setFontAndSize(font, fontSize);
        canvas.moveText(45, doc.getPageSize().getHeight() - 45);

        // Set a negative character spacing (`Tc` operator) based on the font's space width.
        float spaceWidthInGlyphSpace = font.getWidth(' '); // e.g., 250
        float spaceWidthInTextSpace = spaceWidthInGlyphSpace / 1000.0f; // e.g., 0.250
        canvas.setCharacterSpacing(-spaceWidthInTextSpace * fontSize);

        // Use a `PdfTextArray` (`TJ` operator) to write "Preface" with individual kerning.
        // The negative numbers are adjustments in thousandths of text space units,
        // moving the subsequent character to the left.
        PdfTextArray textArray = new PdfTextArray();
        textArray.add("P");
        textArray.add(-226.2f); // Kerning adjustment between 'P' and 'r'
        textArray.add("r");
        textArray.add(-231.8f);
        textArray.add("e");
        textArray.add(-230.8f);
        textArray.add("f");
        textArray.add(-238);
        textArray.add("a");
        textArray.add(-238.9f);
        textArray.add("c");
        textArray.add(-228.9f);
        textArray.add("e");
        canvas.showText(textArray);

        canvas.endText();
        doc.close();
        return baos.toByteArray();
    }
}