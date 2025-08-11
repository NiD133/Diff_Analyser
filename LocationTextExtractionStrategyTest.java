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

import com.itextpdf.text.DocumentException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfTextArray;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Tests for LocationTextExtractionStrategy which extracts text while preserving
 * spatial positioning and layout information from PDF documents.
 * 
 * This strategy maintains text order based on position rather than the order
 * in which text appears in the PDF content stream.
 */
public class LocationTextExtractionStrategyTest extends SimpleTextExtractionStrategyTest {

    @Override
    @Before
    public void setUp() throws Exception {
        // No setup required for this test suite
    }

    @Override
    @After
    public void tearDown() throws Exception {
        // No cleanup required for this test suite
    }

    @Override
    public TextExtractionStrategy createRenderListenerForTest() {
        return new LocationTextExtractionStrategy();
    }
    
    /**
     * Tests that text positioned at different Y coordinates (vertical positions)
     * is extracted in the correct top-to-bottom order, even when the text
     * appears in a different order in the PDF content stream.
     */
    @Test
    public void testVerticalTextPositioning() throws Exception {
        String[] firstColumn = {"A", "B", "C", "D"};
        String[] secondColumn = {"AA", "BB", "CC", "DD"};
        
        PdfReader reader = createPdfWithVerticallyOverlappingText(firstColumn, secondColumn);
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        
        // Text should be ordered by vertical position (top to bottom)
        Assert.assertEquals("A\nAA\nB\nBB\nC\nCC\nD\nDD", extractedText);
    }
    
    /**
     * Tests that text positioned at different X coordinates (horizontal positions)
     * is extracted in the correct left-to-right order.
     */
    @Test
    public void testHorizontalTextPositioning() throws Exception {
        String[] firstRow = {"A", "B", "C", "D"};
        String[] secondRow = {"AA", "BB", "CC", "DD"};
        
        byte[] pdfContent = createPdfWithHorizontallyOverlappingText(firstRow, secondRow);
        PdfReader reader = new PdfReader(pdfContent);
        
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        
        // Text should be ordered by horizontal position (left to right)
        Assert.assertEquals("A AA B BB C CC D DD", extractedText);
    }

    /**
     * Tests text extraction from a page rotated 90 degrees clockwise.
     * The extracted text should maintain logical reading order regardless of rotation.
     */
    @Test
    public void testRotatedPage90Degrees() throws Exception {
        String expectedText = "A\nB\nC\nD";
        byte[] pdfBytes = createSimplePdf(PageSize.LETTER.rotate(), expectedText);
        PdfReader reader = new PdfReader(pdfBytes);
        
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        
        Assert.assertEquals(expectedText, extractedText);
    }
    
    /**
     * Tests text extraction from a page rotated 180 degrees.
     */
    @Test
    public void testRotatedPage180Degrees() throws Exception {
        String expectedText = "A\nB\nC\nD";
        byte[] pdfBytes = createSimplePdf(PageSize.LETTER.rotate().rotate(), expectedText);
        PdfReader reader = new PdfReader(pdfBytes);
        
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        
        Assert.assertEquals(expectedText, extractedText);
    }

    /**
     * Tests text extraction from a page rotated 270 degrees.
     */
    @Test
    public void testRotatedPage270Degrees() throws Exception {
        String expectedText = "A\nB\nC\nD";
        byte[] pdfBytes = createSimplePdf(PageSize.LETTER.rotate().rotate().rotate(), expectedText);
        PdfReader reader = new PdfReader(pdfBytes);
        
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        
        Assert.assertEquals(expectedText, extractedText);
    }

    /**
     * Tests extraction of text from a rotated XObject (form object).
     * XObjects can contain text that is rotated independently of the page rotation.
     */
    @Test
    public void testTextExtractionFromRotatedXObject() throws Exception {
        String xObjectText = "X";
        byte[] pdfContent = createPdfWithRotatedXObject(xObjectText);
        PdfReader reader = new PdfReader(pdfContent);
        
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        
        // The X from the rotated XObject should appear in correct position
        Assert.assertEquals("A\nB\nX\nC", extractedText);
    }

    /**
     * Tests handling of negative character spacing, which can cause characters
     * to overlap or appear closer together than normal.
     */
    @Test
    public void testNegativeCharacterSpacing() throws Exception {
        String firstChar = "W";
        String secondChar = "A";
        float negativeSpacing = 200; // Large negative value to test extreme case
        
        byte[] pdfContent = createPdfWithNegativeCharSpacing(firstChar, negativeSpacing, secondChar);
        PdfReader reader = new PdfReader(pdfContent);
        
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        
        // Characters should still be extracted in correct order despite negative spacing
        Assert.assertEquals("WA", extractedText);
    }
    
    /**
     * Tests the mathematical vector operations used internally by the strategy.
     * This ensures the geometric calculations for text positioning are correct.
     */
    @Test
    public void testVectorMathematicsForTextPositioning() {
        // Create test vectors representing text baseline positions
        Vector lineStart = new Vector(0, 0, 1);
        Vector lineEnd = new Vector(1, 0, 1);
        Vector antiparallelPosition = new Vector(0.9f, 0, 1); // Before line end
        Vector parallelPosition = new Vector(1.1f, 0, 1);     // After line end
        
        // Test antiparallel (backward) direction calculation
        float antiparallelDistance = antiparallelPosition.subtract(lineEnd)
                                                         .dot(lineEnd.subtract(lineStart).normalize());
        Assert.assertEquals("Antiparallel distance should be negative", -0.1f, antiparallelDistance, 0.0001);
        
        // Test parallel (forward) direction calculation
        float parallelDistance = parallelPosition.subtract(lineEnd)
                                                .dot(lineEnd.subtract(lineStart).normalize());
        Assert.assertEquals("Parallel distance should be positive", 0.1f, parallelDistance, 0.0001);
    }
    
    /**
     * Tests extraction of superscript text, which is positioned above the baseline.
     * The strategy should handle text at different vertical offsets correctly.
     */
    @Test
    public void testSuperscriptTextExtraction() throws Exception {
        String baseText = "Hel";
        String superscriptText = "lo";
        
        byte[] pdfContent = createPdfWithSuperscript(baseText, superscriptText);
        PdfReader reader = new PdfReader(pdfContent);
        
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        
        // Superscript text should be combined with base text in reading order
        Assert.assertEquals("Hello", extractedText);
    }

    /**
     * Tests a specific edge case where font spacing equals character spacing.
     * This can affect how the strategy determines word boundaries.
     */
    @Test
    public void testFontSpacingEqualsCharacterSpacing() throws Exception {
        byte[] pdfContent = createPdfWithFontSpacingEqualsCharSpacing();
        PdfReader reader = new PdfReader(pdfContent);
        
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        
        Assert.assertEquals("Preface", extractedText);
    }

    /**
     * Tests extraction from text rendered with very small font sizes.
     * This ensures the strategy handles extreme scaling correctly.
     */
    @Test
    public void testVerySmallFontSize() throws Exception {
        byte[] pdfContent = createPdfWithVerySmallFontSize();
        PdfReader reader = new PdfReader(pdfContent);
        
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        
        Assert.assertEquals("Preface Preface ", extractedText);
    }

    // ========== PDF Creation Helper Methods ==========

    /**
     * Creates a PDF with text that has negative character spacing between characters.
     */
    private byte[] createPdfWithNegativeCharSpacing(String firstText, float charSpacing, String secondText) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setCompressionLevel(0); // No compression for easier debugging
        document.open();
        
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        canvas.moveText(45, document.getPageSize().getHeight() - 45);
        
        // Create text array with negative spacing
        PdfTextArray textArray = new PdfTextArray();
        textArray.add(firstText);
        textArray.add(charSpacing); // Negative value creates overlap
        textArray.add(secondText);
        canvas.showText(textArray);
        
        canvas.endText();
        document.close();
        
        return outputStream.toByteArray();
    }
    
    /**
     * Creates a PDF containing a rotated XObject with text.
     */
    private byte[] createPdfWithRotatedXObject(String xObjectText) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setCompressionLevel(0);
        document.open();

        // Add regular paragraphs
        document.add(new Paragraph("A"));
        document.add(new Paragraph("B"));
        
        // Create rotated template (XObject)
        PdfTemplate template = writer.getDirectContent().createTemplate(20, 100);
        template.setColorStroke(BaseColor.GREEN);
        template.rectangle(0, 0, template.getWidth(), template.getHeight());
        template.stroke();
        
        // Apply rotation transformation
        AffineTransform transform = new AffineTransform();
        transform.translate(0, template.getHeight());
        transform.rotate(-90 / 180f * Math.PI);
        template.transform(transform);
        
        // Add text to template
        template.beginText();
        template.setFontAndSize(BaseFont.createFont(), 12);
        template.moveText(0, template.getWidth() - 12);
        template.showText(xObjectText);
        template.endText();
        
        // Add template as image to document
        Image xObjectImage = Image.getInstance(template);
        xObjectImage.setRotationDegrees(90);
        document.add(xObjectImage);
        
        document.add(new Paragraph("C"));
        document.close();
        
        return outputStream.toByteArray();
    }    
    
    /**
     * Creates a simple PDF with the given page size and text content.
     */
    private byte[] createSimplePdf(Rectangle pageSize, String... textLines) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(pageSize);
        PdfWriter.getInstance(document, outputStream);
        document.open();
        
        for (String text : textLines) {
            document.add(new Paragraph(text));
            document.newPage();
        }
        
        document.close();
        return outputStream.toByteArray();
    }
    
    /**
     * Creates a PDF with two sets of text positioned horizontally with some overlap.
     */
    private byte[] createPdfWithHorizontallyOverlappingText(String[] firstRowTexts, String[] secondRowTexts) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setCompressionLevel(0);
        document.open();

        PdfContentByte canvas = writer.getDirectContent();
        float startY = 500;
        float startX = 50;
        float horizontalSpacing = 70.0f;
        
        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        
        // Position first row of text
        float x = startX;
        for (String text : firstRowTexts) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, startY, 0);
            x += horizontalSpacing;
        }

        // Position second row of text with slight horizontal offset
        x = startX + 12;
        for (String text : secondRowTexts) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, startY, 0);
            x += horizontalSpacing;
        }
        
        canvas.endText();
        document.close();
        
        return outputStream.toByteArray();
    }    
    
    /**
     * Creates a PDF with two sets of text positioned vertically with some overlap.
     */
    private PdfReader createPdfWithVerticallyOverlappingText(String[] firstColumnTexts, String[] secondColumnTexts) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setCompressionLevel(0);
        document.open();

        PdfContentByte canvas = writer.getDirectContent();
        float startY = 500;
        float startX = 50;
        float verticalSpacing = 25.0f;
        
        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        
        // Position first column of text
        float y = startY;
        for (String text : firstColumnTexts) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, startX, y, 0);
            y -= verticalSpacing;
        }

        // Position second column with slight vertical offset
        y = startY - 13; // Offset to create interleaving
        for (String text : secondColumnTexts) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, startX, y, 0);
            y -= verticalSpacing;
        }
        
        canvas.endText();
        document.close();
        
        return new PdfReader(outputStream.toByteArray());
    }    
    
    /**
     * Creates a PDF with superscript text (text raised above the baseline).
     */
    private byte[] createPdfWithSuperscript(String regularText, String superscriptText) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        
        // Add regular text
        document.add(new Chunk(regularText));
        
        // Add superscript text with vertical rise
        Chunk superscriptChunk = new Chunk(superscriptText);
        superscriptChunk.setTextRise(7.0f); // Raise text above baseline
        document.add(superscriptChunk);

        document.close();
        return outputStream.toByteArray();
    }

    /**
     * Creates a PDF where font spacing equals character spacing.
     * This tests edge cases in word boundary detection.
     */
    private byte[] createPdfWithFontSpacingEqualsCharSpacing() throws DocumentException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setCompressionLevel(0);
        document.open();

        BaseFont font = BaseFont.createFont();
        int fontSize = 12;
        float charSpace = font.getWidth(' ') / 1000.0f;

        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(font, fontSize);
        canvas.moveText(45, document.getPageSize().getHeight() - 45);
        canvas.setCharacterSpacing(-charSpace * fontSize);

        // Create text with specific spacing adjustments
        PdfTextArray textArray = new PdfTextArray();
        textArray.add("P"); textArray.add(-226.2f);
        textArray.add("r"); textArray.add(-231.8f);
        textArray.add("e"); textArray.add(-230.8f);
        textArray.add("f"); textArray.add(-238);
        textArray.add("a"); textArray.add(-238.9f);
        textArray.add("c"); textArray.add(-228.9f);
        textArray.add("e");

        canvas.showText(textArray);
        canvas.endText();
        document.close();

        return outputStream.toByteArray();
    }

    /**
     * Creates a PDF with text rendered at very small font sizes.
     */
    private byte[] createPdfWithVerySmallFontSize() throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setCompressionLevel(0);
        document.open();

        BaseFont font = BaseFont.createFont();
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        
        // First text with extremely small font (0.2pt)
        canvas.setFontAndSize(font, 0.2f);
        canvas.moveText(45, document.getPageSize().getHeight() - 45);

        PdfTextArray textArray = new PdfTextArray();
        textArray.add("P"); textArray.add("r"); textArray.add("e");
        textArray.add("f"); textArray.add("a"); textArray.add("c");
        textArray.add("e"); textArray.add(" ");
        canvas.showText(textArray);
        
        // Second text with normal font size (10pt)
        canvas.setFontAndSize(font, 10);
        canvas.showText(textArray);

        canvas.endText();
        document.close();

        return outputStream.toByteArray();
    }
}