package com.itextpdf.text.pdf.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.itextpdf.text.DocumentException;
import org.junit.Assert;
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

public class LocationTextExtractionStrategyTest extends SimpleTextExtractionStrategyTest {

    // Constants for test configuration
    private static final int DEFAULT_FONT_SIZE = 12;
    private static final float PAGE_MARGIN = 45;
    private static final float START_Y = 500;
    private static final float START_X = 50;
    private static final float LINE_OFFSET = 25;
    private static final float COLUMN_OFFSET = 70;
    private static final float SUPERSCRIPT_RISE = 7.0f;
    private static final float NEGATIVE_CHAR_SPACING = 200;
    private static final float SMALL_FONT_SIZE = 0.2f;

    @Override
    public TextExtractionStrategy createRenderListenerForTest() {
        return new LocationTextExtractionStrategy();
    }

    // Group 1: Text positioning tests
    // ===============================

    @Test
    public void textExtraction_WithVerticalOverlappingText_OrdersByYPosition() throws Exception {
        String[] firstColumn = {"A", "B", "C", "D"};
        String[] secondColumn = {"AA", "BB", "CC", "DD"};
        PdfReader reader = createPdfWithVerticalOverlap(firstColumn, secondColumn);

        String extracted = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("A\nAA\nB\nBB\nC\nCC\nD\nDD", extracted);
    }

    @Test
    public void textExtraction_WithHorizontalOverlappingText_OrdersByXPosition() throws Exception {
        String[] firstRow = {"A", "B", "C", "D"};
        String[] secondRow = {"AA", "BB", "CC", "DD"};
        byte[] pdf = createPdfWithHorizontalOverlap(firstRow, secondRow);
        PdfReader reader = new PdfReader(pdf);

        String extracted = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("A AA B BB C CC D DD", extracted);
    }

    // Group 2: Page rotation tests
    // ============================

    @Test
    public void textExtraction_On90DegreeRotatedPage_PreservesTextOrder() throws Exception {
        byte[] pdf = createSimplePdf(PageSize.LETTER.rotate(), "A\nB\nC\nD");
        PdfReader reader = new PdfReader(pdf);
        String extracted = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("A\nB\nC\nD", extracted);
    }

    @Test
    public void textExtraction_On180DegreeRotatedPage_PreservesTextOrder() throws Exception {
        byte[] pdf = createSimplePdf(PageSize.LETTER.rotate().rotate(), "A\nB\nC\nD");
        PdfReader reader = new PdfReader(pdf);
        String extracted = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("A\nB\nC\nD", extracted);
    }

    @Test
    public void textExtraction_On270DegreeRotatedPage_PreservesTextOrder() throws Exception {
        byte[] pdf = createSimplePdf(PageSize.LETTER.rotate().rotate().rotate(), "A\nB\nC\nD");
        PdfReader reader = new PdfReader(pdf);
        String extracted = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("A\nB\nC\nD", extracted);
    }

    // Group 3: Special rendering cases
    // ================================

    @Test
    public void textExtraction_FromRotatedXObject_IncludesInCorrectPosition() throws Exception {
        String xobjectText = "X";
        byte[] pdf = createPdfWithRotatedXObject(xobjectText);
        PdfReader reader = new PdfReader(pdf);
        String extracted = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("A\nB\nX\nC", extracted);
    }

    @Test
    public void textExtraction_WithNegativeCharacterSpacing_PreservesWordIntegrity() throws Exception {
        byte[] pdf = createPdfWithNegativeCharSpacing("W", NEGATIVE_CHAR_SPACING, "A");
        PdfReader reader = new PdfReader(pdf);
        String extracted = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("WA", extracted);
    }

    @Test
    public void textExtraction_WithSuperscript_RendersAsContinuousText() throws Exception {
        byte[] pdf = createPdfWithSuperscript("Hel", "lo");
        PdfReader reader = new PdfReader(pdf);
        String extracted = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("Hello", extracted);
    }

    @Test
    public void textExtraction_WhenFontSpacingEqualsCharSpacing_PreservesText() throws Exception {
        byte[] pdf = createPdfWithFontSpacingEqualsCharSpacing();
        PdfReader reader = new PdfReader(pdf);
        String extracted = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("Preface", extracted);
    }

    @Test
    public void textExtraction_WithVerySmallFontSize_PreservesText() throws Exception {
        byte[] pdf = createPdfWithVerySmallFontSize();
        PdfReader reader = new PdfReader(pdf);
        String extracted = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("Preface Preface ", extracted);
    }

    // Group 4: Core algorithm verification
    // ====================================

    @Test
    public void vectorMath_ForTextChunkLocation_CalculatesCorrectDistances() {
        Vector origin = new Vector(0, 0, 1);
        Vector rightDirection = new Vector(1, 0, 1);
        
        // Vector slightly before the end point
        Vector beforeEnd = new Vector(0.9f, 0, 1);
        float distanceBefore = beforeEnd.subtract(rightDirection)
                                      .dot(rightDirection.subtract(origin).normalize());
        Assert.assertEquals(-0.1f, distanceBefore, 0.0001);
        
        // Vector slightly after the end point
        Vector afterEnd = new Vector(1.1f, 0, 1);
        float distanceAfter = afterEnd.subtract(rightDirection)
                                     .dot(rightDirection.subtract(origin).normalize());
        Assert.assertEquals(0.1f, distanceAfter, 0.0001);
    }

    // Helper methods for PDF creation
    // ==============================

    private PdfReader createPdfWithVerticalOverlap(String[] column1, String[] column2) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), DEFAULT_FONT_SIZE);
        
        // First column
        float y = START_Y;
        for (String text : column1) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, START_X, y, 0);
            y -= LINE_OFFSET;
        }

        // Second column (offset vertically)
        y = START_Y - (LINE_OFFSET / 2);
        for (String text : column2) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, START_X, y, 0);
            y -= LINE_OFFSET;
        }
        
        canvas.endText();
        doc.close();
        return new PdfReader(baos.toByteArray());
    }

    private byte[] createPdfWithHorizontalOverlap(String[] row1, String[] row2) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), DEFAULT_FONT_SIZE);
        
        // First row
        float x = START_X;
        for (String text : row1) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, START_Y, 0);
            x += COLUMN_OFFSET;
        }

        // Second row (offset horizontally)
        x = START_X + (COLUMN_OFFSET / 6); // Small offset to create overlap
        for (String text : row2) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, START_Y, 0);
            x += COLUMN_OFFSET;
        }
        
        canvas.endText();
        doc.close();
        return baos.toByteArray();
    }

    private byte[] createPdfWithRotatedXObject(String xobjectText) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        doc.add(new Paragraph("A"));
        doc.add(new Paragraph("B"));
        
        // Create rotated template
        PdfTemplate template = writer.getDirectContent().createTemplate(20, 100);
        template.setColorStroke(BaseColor.GREEN);
        template.rectangle(0, 0, template.getWidth(), template.getHeight());
        template.stroke();
        
        // Apply 90-degree rotation
        AffineTransform tx = new AffineTransform();
        tx.translate(0, template.getHeight());
        tx.rotate(-Math.PI/2);
        template.transform(tx);
        
        // Add text to template
        template.beginText();
        template.setFontAndSize(BaseFont.createFont(), DEFAULT_FONT_SIZE);
        template.moveText(0, template.getWidth() - DEFAULT_FONT_SIZE);
        template.showText(xobjectText);
        template.endText();
        
        // Add template to document
        Image xobjectImage = Image.getInstance(template);
        xobjectImage.setRotationDegrees(90);
        doc.add(xobjectImage);
        
        doc.add(new Paragraph("C"));
        doc.close();
        return baos.toByteArray();
    }    

    private byte[] createPdfWithNegativeCharSpacing(String prefix, float spacing, String suffix) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();
        
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), DEFAULT_FONT_SIZE);
        canvas.moveText(PAGE_MARGIN, doc.getPageSize().getHeight() - PAGE_MARGIN);
        
        PdfTextArray textArray = new PdfTextArray();
        textArray.add(prefix);
        textArray.add(spacing);
        textArray.add(suffix);
        canvas.showText(textArray);
        canvas.endText();
        
        doc.close();
        return baos.toByteArray();
    }

    private byte[] createPdfWithSuperscript(String base, String superscript) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter.getInstance(doc, baos);
        doc.open();
        
        Chunk baseChunk = new Chunk(base);
        Chunk superscriptChunk = new Chunk(superscript);
        superscriptChunk.setTextRise(SUPERSCRIPT_RISE);
        
        doc.add(baseChunk);
        doc.add(superscriptChunk);
        doc.close();
        return baos.toByteArray();
    }

    private byte[] createPdfWithFontSpacingEqualsCharSpacing() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        BaseFont font = BaseFont.createFont();
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(font, DEFAULT_FONT_SIZE);
        canvas.moveText(PAGE_MARGIN, doc.getPageSize().getHeight() - PAGE_MARGIN);
        
        // Negative spacing equals font space width
        float charSpace = font.getWidth(' ') / 1000.0f;
        canvas.setCharacterSpacing(-charSpace * DEFAULT_FONT_SIZE);

        // Custom spacing between characters
        PdfTextArray textArray = new PdfTextArray();
        String word = "Preface";
        for (int i = 0; i < word.length(); i++) {
            textArray.add(String.valueOf(word.charAt(i)));
            if (i < word.length() - 1) {
                textArray.add(-230f); // Simulate tight spacing
            }
        }
        canvas.showText(textArray);
        canvas.endText();
        doc.close();
        return baos.toByteArray();
    }

    private byte[] createPdfWithVerySmallFontSize() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        BaseFont font = BaseFont.createFont();
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        
        // Very small font size
        canvas.setFontAndSize(font, SMALL_FONT_SIZE);
        canvas.moveText(PAGE_MARGIN, doc.getPageSize().getHeight() - PAGE_MARGIN);
        canvas.showText("Preface ");
        
        // Normal font size
        canvas.setFontAndSize(font, DEFAULT_FONT_SIZE);
        canvas.showText("Preface ");
        
        canvas.endText();
        doc.close();
        return baos.toByteArray();
    }

    private byte[] createSimplePdf(Rectangle pageSize, String... text) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(pageSize);
        PdfWriter.getInstance(doc, baos);
        doc.open();
        for (String line : text) {
            doc.add(new Paragraph(line));
            doc.newPage();
        }
        doc.close();
        return baos.toByteArray();
    }
}