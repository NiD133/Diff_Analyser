/*
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, Kevin Day, et al.
 *
 * [License text remains the same for brevity]
 */
package com.itextpdf.text.pdf.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.itextpdf.testutils.TestResourceUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

/**
 * Tests for SimpleTextExtractionStrategy to verify text extraction behavior
 * under various PDF text positioning and formatting scenarios.
 * 
 * @author kevin
 */
public class SimpleTextExtractionStrategyTest {

    // Test data constants
    private static final String FIRST_TEXT_SEGMENT = "TEXT1 TEXT1";
    private static final String SECOND_TEXT_SEGMENT = "TEXT2 TEXT2";
    
    // Test configuration
    private String text1 = FIRST_TEXT_SEGMENT;
    private String text2 = SECOND_TEXT_SEGMENT;
    
    @Before
    public void setUp() throws Exception {
        // Reset test data before each test
        text1 = FIRST_TEXT_SEGMENT;
        text2 = SECOND_TEXT_SEGMENT;
    }

    @After
    public void tearDown() throws Exception {
        // No cleanup needed
    }
    
    /**
     * Creates the text extraction strategy instance to be tested.
     * This method allows subclasses to override the strategy implementation.
     */
    private TextExtractionStrategy createTextExtractionStrategy() {
        return new SimpleTextExtractionStrategy();
    }

    // ========== Tests for Co-linear Text (same line) ==========
    
    @Test
    public void testCoLinearTextWithoutSpacing() throws Exception {
        // Given: Two text segments positioned on the same line with no gap
        byte[] pdfBytes = createPdfWithTextAtPositions(text1, text2, 0, false, 0);
        
        // When: Extracting text from the PDF
        String extractedText = extractTextFromPdf(pdfBytes);
        
        // Then: Text should be concatenated without spaces
        Assert.assertEquals("Co-linear text without spacing should be concatenated", 
                           text1 + text2, extractedText);
    }
    
    @Test
    public void testCoLinearTextWithSpacing() throws Exception {
        // Given: Two text segments positioned on the same line with a gap
        byte[] pdfBytes = createPdfWithTextAtPositions(text1, text2, 0, false, 2);
        
        // When: Extracting text from the PDF
        String extractedText = extractTextFromPdf(pdfBytes);
        
        // Then: Text should be separated by a space
        Assert.assertEquals("Co-linear text with spacing should have space inserted", 
                           text1 + " " + text2, extractedText);
    }
    
    @Test
    public void testCoLinearTextEndingWithSpace() throws Exception {
        // Given: First text segment already ends with a space, followed by second segment
        text1 = text1 + " ";
        byte[] pdfBytes = createPdfWithTextAtPositions(text1, text2, 0, false, 2);
        
        // When: Extracting text from the PDF
        String extractedText = extractTextFromPdf(pdfBytes);
        
        // Then: No extra space should be inserted (avoid double spacing)
        Assert.assertEquals("Text ending with space should not get extra space", 
                           text1 + text2, extractedText);
    }
    
    @Test
    public void testTrailingSpaceHandling() throws Exception {
        // Given: First text has trailing space, positioned with gap from second text
        byte[] pdfBytes = createPdfWithTextAtPositions(text1 + " ", text2, 0, false, 6);
        
        // When: Extracting text from the PDF
        String extractedText = extractTextFromPdf(pdfBytes);
        
        // Then: Should preserve natural spacing
        Assert.assertEquals("Trailing space should be handled correctly", 
                           text1 + " " + text2, extractedText);
    }

    @Test
    public void testLeadingSpaceHandling() throws Exception {
        // Given: Second text has leading space, positioned with gap from first text
        byte[] pdfBytes = createPdfWithTextAtPositions(text1, " " + text2, 0, false, 6);
        
        // When: Extracting text from the PDF
        String extractedText = extractTextFromPdf(pdfBytes);
        
        // Then: Should preserve natural spacing
        Assert.assertEquals("Leading space should be handled correctly", 
                           text1 + " " + text2, extractedText);
    }

    // ========== Tests for Multi-line Text ==========
    
    @Test
    public void testUnrotatedMultiLineText() throws Exception {
        // Given: Two text segments positioned on different lines (unrotated)
        byte[] pdfBytes = createPdfWithTextAtPositions(text1, text2, 0, true, -20);
        
        // When: Extracting text from the PDF
        String extractedText = extractTextFromPdf(pdfBytes);
        
        // Then: Text should be separated by newline
        Assert.assertEquals("Multi-line text should be separated by newline", 
                           text1 + "\n" + text2, extractedText);
    }

    @Test
    public void testRotatedMultiLineText_Minus90Degrees() throws Exception {
        // Given: Two text segments rotated -90 degrees on different lines
        byte[] pdfBytes = createPdfWithTextAtPositions(text1, text2, -90, true, -20);
        
        // When: Extracting text from the PDF
        String extractedText = extractTextFromPdf(pdfBytes);
        
        // Then: Text should still be separated by newline despite rotation
        Assert.assertEquals("Rotated text (-90°) should maintain line separation", 
                           text1 + "\n" + text2, extractedText);
    }
    
    @Test
    public void testRotatedMultiLineText_Plus90Degrees() throws Exception {
        // Given: Two text segments rotated +90 degrees on different lines
        byte[] pdfBytes = createPdfWithTextAtPositions(text1, text2, 90, true, -20);
        
        // When: Extracting text from the PDF
        String extractedText = extractTextFromPdf(pdfBytes);
        
        // Then: Text should still be separated by newline despite rotation
        Assert.assertEquals("Rotated text (+90°) should maintain line separation", 
                           text1 + "\n" + text2, extractedText);
    }

    @Test
    public void testPartiallyRotatedMultiLineText() throws Exception {
        // Given: Two text segments rotated 33 degrees on different lines
        byte[] pdfBytes = createPdfWithTextAtPositions(text1, text2, 33, true, -20);
        
        // When: Extracting text from the PDF
        String extractedText = extractTextFromPdf(pdfBytes);
        
        // Then: Text should still be separated by newline despite partial rotation
        Assert.assertEquals("Partially rotated text should maintain line separation", 
                           text1 + "\n" + text2, extractedText);
    }

    // ========== Tests for Glyph Positioning and Word Spacing ==========
    
    @Test
    public void testWordSpacingFromGlyphPositioning() throws Exception {
        // Given: Text with explicit glyph positioning that creates word spacing
        byte[] pdfBytes = createPdfWithArrayText(text1, text2, 250);
        
        // When: Extracting text from the PDF
        String extractedText = extractTextFromPdf(pdfBytes);
        
        // Then: Should insert space based on glyph positioning
        Assert.assertEquals("Glyph positioning should create word spacing", 
                           text1 + " " + text2, extractedText);
    }
    
    @Test
    public void testComplexGlyphPositioning() throws Exception {
        // Given: Complex text with multiple glyph positioning adjustments
        String complexTextArray = "[(S)3.2(an)-255.0(D)13.0(i)8.3(e)-10.1(g)1.6(o)-247.5(C)2.4(h)5.8(ap)3.0(t)10.7(er)]TJ";
        byte[] pdfBytes = createPdfWithArrayText(complexTextArray);
        
        // When: Extracting text from the PDF
        String extractedText = extractTextFromPdf(pdfBytes);
        
        // Then: Should correctly interpret glyph positioning as "San Diego Chapter"
        Assert.assertEquals("Complex glyph positioning should be interpreted correctly", 
                           "San Diego Chapter", extractedText);
    }

    // ========== Tests for XObject Text Extraction ==========
    
    @Test
    public void testExtractTextFromXObject() throws Exception {
        // Given: PDF containing text within an XObject (template/form)
        String xObjectText = "X";
        byte[] pdfBytes = createPdfWithXObject(xObjectText);
        
        // When: Extracting text from the PDF
        String extractedText = extractTextFromPdf(pdfBytes);
        
        // Then: Should extract text from XObject content
        Assert.assertTrue("XObject text should be extracted: " + extractedText, 
                         extractedText.contains(xObjectText));
    }

    // ========== Integration Tests with Real PDF Files ==========
    
    @Test
    public void testExtractionConsistencyWithPage229() throws IOException {
        // Skip test if running from subclass to avoid duplicate execution
        if (this.getClass() != SimpleTextExtractionStrategyTest.class) return;
        
        // Given: Real PDF file (page229.pdf)
        try (InputStream inputStream = TestResourceUtils.getResourceAsStream(this, "page229.pdf")) {
            PdfReader reader = new PdfReader(inputStream);
            
            // When: Extracting text using both normal and character-by-character strategies
            String normalExtraction = PdfTextExtractor.getTextFromPage(reader, 1, 
                    new SimpleTextExtractionStrategy());
            String characterExtraction = PdfTextExtractor.getTextFromPage(reader, 1, 
                    new SingleCharacterTextExtractionStrategy());
            
            // Then: Both extraction methods should produce identical results
            Assert.assertEquals("Normal and character-by-character extraction should match", 
                               normalExtraction, characterExtraction);
            reader.close();
        }
    }

    @Test
    public void testExtractionConsistencyWithIsoDocument() throws IOException {
        // Skip test if running from subclass to avoid duplicate execution
        if (this.getClass() != SimpleTextExtractionStrategyTest.class) return;
        
        // Given: Real PDF file (ISO document)
        String filename = "ISO-TC171-SC2_N0896_SC2WG5_Edinburgh_Agenda.pdf";
        try (InputStream inputStream = TestResourceUtils.getResourceAsStream(this, filename)) {
            PdfReader reader = new PdfReader(inputStream);
            
            // When: Extracting text from first two pages using both strategies
            String normalExtraction = extractMultiplePages(reader, new SimpleTextExtractionStrategy());
            String characterExtraction = extractMultiplePages(reader, new SingleCharacterTextExtractionStrategy());
            
            // Then: Both extraction methods should produce identical results
            Assert.assertEquals("Extraction strategies should produce consistent results", 
                               normalExtraction, characterExtraction);
            reader.close();
        }
    }

    // ========== Helper Methods ==========
    
    /**
     * Extracts text from a PDF byte array using the configured strategy.
     */
    private String extractTextFromPdf(byte[] pdfBytes) throws IOException {
        PdfReader reader = new PdfReader(pdfBytes);
        try {
            return PdfTextExtractor.getTextFromPage(reader, 1, createTextExtractionStrategy());
        } finally {
            reader.close();
        }
    }
    
    /**
     * Extracts text from multiple pages and concatenates with newlines.
     */
    private String extractMultiplePages(PdfReader reader, TextExtractionStrategy strategy) throws IOException {
        return PdfTextExtractor.getTextFromPage(reader, 1, strategy) +
               "\n" +
               PdfTextExtractor.getTextFromPage(reader, 2, strategy);
    }

    /**
     * Creates a PDF with an XObject containing the specified text.
     */
    private byte[] createPdfWithXObject(String xObjectText) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setCompressionLevel(0);
        document.open();

        // Add regular content
        document.add(new Paragraph("A"));
        document.add(new Paragraph("B"));
        
        // Create XObject template with text
        PdfTemplate template = writer.getDirectContent().createTemplate(100, 100);
        template.beginText();
        template.setFontAndSize(BaseFont.createFont(), 12);
        template.moveText(5, template.getHeight() - 5);
        template.showText(xObjectText);
        template.endText();
        
        // Add XObject as image to document
        Image xObjectImage = Image.getInstance(template);
        document.add(xObjectImage);
        
        document.add(new Paragraph("C"));
        document.close();
        
        return outputStream.toByteArray();
    }    
    
    /**
     * Creates a PDF with text using array notation for glyph positioning.
     */
    private byte[] createPdfWithArrayText(String textArrayNotation) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.LETTER);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfContentByte contentByte = writer.getDirectContent();
        BaseFont font = BaseFont.createFont();
        
        contentByte.transform(AffineTransform.getTranslateInstance(100, 500));
        contentByte.beginText();
        contentByte.setFontAndSize(font, 12);
        contentByte.getInternalBuffer().append(textArrayNotation + "\n");
        contentByte.endText();
        
        document.close();
        return outputStream.toByteArray();
    }    
    
    /**
     * Creates a PDF with two text segments using array notation with specified spacing.
     */
    private byte[] createPdfWithArrayText(String text1, String text2, int spacingInPoints) throws Exception {
        String textArray = "[(" + text1 + ")" + (-spacingInPoints) + "(" + text2 + ")]TJ";
        return createPdfWithArrayText(textArray);
    }
    
    /**
     * Creates a PDF with two text segments at specified positions and orientations.
     * 
     * @param text1 First text segment
     * @param text2 Second text segment  
     * @param rotationDegrees Rotation angle in degrees
     * @param moveToNextLine Whether to move to next line between text segments
     * @param movementDelta Distance to move (horizontally if same line, vertically if next line)
     */
    private byte[] createPdfWithTextAtPositions(String text1, String text2, float rotationDegrees, 
                                               boolean moveToNextLine, float movementDelta) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.LETTER);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfContentByte contentByte = writer.getDirectContent();
        BaseFont font = BaseFont.createFont();

        // Position at center of page
        float centerX = document.getPageSize().getWidth() / 2;
        float centerY = document.getPageSize().getHeight() / 2;
        contentByte.transform(AffineTransform.getTranslateInstance(centerX, centerY));

        // Draw crosshairs for reference (helpful for debugging)
        drawCrosshairs(contentByte);
        
        // Add text with specified positioning
        contentByte.beginText();
        contentByte.setFontAndSize(font, 12);
        contentByte.transform(AffineTransform.getRotateInstance(Math.toRadians(rotationDegrees)));
        contentByte.showText(text1);
        
        if (moveToNextLine) {
            contentByte.moveText(0, movementDelta);
        } else {
            contentByte.transform(AffineTransform.getTranslateInstance(movementDelta, 0));
        }
        
        contentByte.showText(text2);
        contentByte.endText();

        document.close();
        return outputStream.toByteArray();
    }
    
    /**
     * Draws crosshairs at the current position for visual reference.
     */
    private void drawCrosshairs(PdfContentByte contentByte) {
        contentByte.moveTo(-10, 0);
        contentByte.lineTo(10, 0);
        contentByte.moveTo(0, -10);
        contentByte.lineTo(0, 10);
        contentByte.stroke();
    }

    /**
     * Alternative text extraction strategy that processes text character by character.
     * Used for comparison testing to ensure consistent behavior.
     */
    private static class SingleCharacterTextExtractionStrategy extends SimpleTextExtractionStrategy {
        @Override
        public void renderText(TextRenderInfo renderInfo) {
            // Process each character individually instead of the entire text chunk
            for (TextRenderInfo characterInfo : renderInfo.getCharacterRenderInfos()) {
                super.renderText(characterInfo);
            }
        }
    }
}