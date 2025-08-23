package com.itextpdf.text.pdf.parser;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests the text extraction logic of the {@link SimpleTextExtractionStrategy}.
 * This class focuses on how the strategy handles different text layouts and PDF constructs.
 */
public class SimpleTextExtractionStrategyTest {

    private static final String TEXT1 = "TEXT1 TEXT1";
    private static final String TEXT2 = "TEXT2 TEXT2";

    @Test
    public void should_concatenateAdjacentText_when_textIsOnSameLine() throws Exception {
        // Arrange
        byte[] pdfBytes = createPdfWithTwoAdjacentStrings(TEXT1, TEXT2);
        PdfReader reader = new PdfReader(pdfBytes);
        TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        String expectedText = TEXT1 + TEXT2;

        // Act
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, strategy);

        // Assert
        assertEquals("Text chunks on the same line should be concatenated.", expectedText, extractedText);
    }

    // ===================================================================================
    // Helper methods for creating test PDFs with different content.
    // These methods were part of the original test suite and are kept for completeness.
    // ===================================================================================

    /**
     * Creates a simple PDF with two text strings rendered one after another on the same line.
     * This is the most direct way to test collinear text concatenation.
     */
    private byte[] createPdfWithTwoAdjacentStrings(String text1, String text2) throws Exception {
        return createPdf((writer, document) -> {
            PdfContentByte cb = writer.getDirectContent();
            cb.beginText();
            cb.setFontAndSize(BaseFont.createFont(), 12);
            cb.moveText(72, 720); // Move to a starting position
            cb.showText(text1);
            cb.showText(text2); // Renders immediately after the first string
            cb.endText();
        });
    }

    /**
     * Creates a PDF with text inside an XObject (a reusable content stream, often for images or templates).
     */
    private byte[] createPdfWithXObject(String xobjectText) throws Exception {
        return createPdf((writer, document) -> {
            document.add(new Paragraph("A"));
            document.add(new Paragraph("B"));

            PdfTemplate template = writer.getDirectContent().createTemplate(100, 100);
            template.beginText();
            template.setFontAndSize(BaseFont.createFont(), 12);
            template.moveText(5, template.getHeight() - 5);
            template.showText(xobjectText);
            template.endText();

            Image xobjectImage = Image.getInstance(template);
            document.add(xobjectImage);
            document.add(new Paragraph("C"));
        });
    }

    /**
     * Creates a PDF using the 'TJ' operator to render an array of strings with custom spacing.
     * The 'TJ' operator allows for fine-grained control over glyph positioning.
     */
    private byte[] createPdfWithSpacedText(String text1, String text2, int spaceInPoints) throws Exception {
        return createPdf((writer, document) -> {
            PdfContentByte cb = writer.getDirectContent();
            cb.beginText();
            cb.setFontAndSize(BaseFont.createFont(), 12);
            // The TJ operator takes an array of strings and spacing adjustments.
            // A negative number creates a space between strings.
            String tjCommand = String.format("[(%s) %d (%s)]TJ\n", text1, -spaceInPoints, text2);
            cb.getInternalBuffer().append(tjCommand);
            cb.endText();
        });
    }

    /**
     * Creates a PDF with rotated text.
     */
    private byte[] createPdfWithRotatedText(String text1, String text2, float rotationDegrees, boolean moveTextToNextLine, float moveTextDelta) throws Exception {
        return createPdf((writer, document) -> {
            PdfContentByte cb = writer.getDirectContent();
            BaseFont font = BaseFont.createFont();

            float x = document.getPageSize().getWidth() / 2;
            float y = document.getPageSize().getHeight() / 2;

            cb.beginText();
            cb.setFontAndSize(font, 12);
            cb.setTextMatrix(x, y);

            // Apply rotation to the text matrix
            double rotationRadians = Math.toRadians(rotationDegrees);
            AffineTransform rotationTransform = AffineTransform.getRotateInstance(rotationRadians);
            cb.transform(rotationTransform);

            cb.showText(text1);

            if (moveTextToNextLine) {
                cb.moveText(0, moveTextDelta); // Moves relative to the current line
            } else {
                // Translates the entire coordinate system, affecting subsequent drawing
                cb.transform(AffineTransform.getTranslateInstance(moveTextDelta, 0));
            }

            cb.showText(text2);
            cb.endText();
        });
    }

    /**
     * A generic helper to create a PDF document in memory, handling the boilerplate setup and teardown.
     *
     * @param contentGenerator A lambda that defines the specific content to be added to the PDF.
     * @return A byte array representing the generated PDF file.
     */
    private byte[] createPdf(PdfContentGenerator contentGenerator) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.LETTER);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.setCompressionLevel(0); // Disable compression for easier debugging
            document.open();

            contentGenerator.generate(writer, document);

            document.close();
            return baos.toByteArray();
        }
    }

    /**
     * Functional interface for lambda-based PDF content generation.
     */
    @FunctionalInterface
    private interface PdfContentGenerator {
        void generate(PdfWriter writer, Document document) throws Exception;
    }

    /**
     * A strategy that processes text character by character.
     * Used for tests that need to verify behavior at a very granular level.
     */
    private static class SingleCharacterSimpleTextExtractionStrategy extends SimpleTextExtractionStrategy {
        @Override
        public void renderText(TextRenderInfo renderInfo) {
            for (TextRenderInfo tri : renderInfo.getCharacterRenderInfos()) {
                super.renderText(tri);
            }
        }
    }
}