package com.itextpdf.text.pdf.parser;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test suite for LocationTextExtractionStrategy.
 * Ensures text extraction is consistent with physical layout.
 */
public class LocationTextExtractionStrategyTest extends SimpleTextExtractionStrategyTest {

    @Override
    @Before
    public void setUp() throws Exception {
        // Setup code before each test
    }

    @Override
    @After
    public void tearDown() throws Exception {
        // Cleanup code after each test
    }

    @Override
    public TextExtractionStrategy createRenderListenerForTest() {
        return new LocationTextExtractionStrategy();
    }

    @Test
    public void testVerticalTextExtraction() throws Exception {
        PdfReader reader = createPdfWithOverlappingTextVertical(
                new String[]{"A", "B", "C", "D"},
                new String[]{"AA", "BB", "CC", "DD"}
        );

        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("A\nAA\nB\nBB\nC\nCC\nD\nDD", extractedText);
    }

    @Test
    public void testHorizontalTextExtraction() throws Exception {
        byte[] pdfContent = createPdfWithOverlappingTextHorizontal(
                new String[]{"A", "B", "C", "D"},
                new String[]{"AA", "BB", "CC", "DD"}
        );
        PdfReader reader = new PdfReader(pdfContent);

        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("A AA B BB C CC D DD", extractedText);
    }

    @Test
    public void testRotatedPageExtraction() throws Exception {
        byte[] pdfContent = createSimplePdf(PageSize.LETTER.rotate(), "A\nB\nC\nD");
        PdfReader reader = new PdfReader(pdfContent);

        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("A\nB\nC\nD", extractedText);
    }

    @Test
    public void testDoubleRotatedPageExtraction() throws Exception {
        byte[] pdfContent = createSimplePdf(PageSize.LETTER.rotate().rotate(), "A\nB\nC\nD");
        PdfReader reader = new PdfReader(pdfContent);

        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("A\nB\nC\nD", extractedText);
    }

    @Test
    public void testTripleRotatedPageExtraction() throws Exception {
        byte[] pdfContent = createSimplePdf(PageSize.LETTER.rotate().rotate().rotate(), "A\nB\nC\nD");
        PdfReader reader = new PdfReader(pdfContent);

        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("A\nB\nC\nD", extractedText);
    }

    @Test
    public void testXObjectTextWithRotation() throws Exception {
        String xObjectText = "X";
        byte[] pdfContent = createPdfWithRotatedXObject(xObjectText);
        PdfReader reader = new PdfReader(pdfContent);

        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("A\nB\nX\nC", extractedText);
    }

    @Test
    public void testNegativeCharacterSpacing() throws Exception {
        byte[] pdfContent = createPdfWithNegativeCharSpacing("W", 200, "A");
        PdfReader reader = new PdfReader(pdfContent);

        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("WA", extractedText);
    }

    @Test
    public void testVectorMathSanityCheck() {
        Vector start = new Vector(0, 0, 1);
        Vector end = new Vector(1, 0, 1);
        Vector antiparallelStart = new Vector(0.9f, 0, 1);
        Vector parallelStart = new Vector(1.1f, 0, 1);

        float resultAntiParallel = antiparallelStart.subtract(end).dot(end.subtract(start).normalize());
        Assert.assertEquals(-0.1f, resultAntiParallel, 0.0001);

        float resultParallel = parallelStart.subtract(end).dot(end.subtract(start).normalize());
        Assert.assertEquals(0.1f, resultParallel, 0.0001);
    }

    @Test
    public void testSuperscriptTextExtraction() throws Exception {
        byte[] pdfContent = createPdfWithSuperscript("Hel", "lo");
        PdfReader reader = new PdfReader(pdfContent);

        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("Hello", extractedText);
    }

    @Test
    public void testFontSpacingEqualsCharSpacing() throws Exception {
        byte[] pdfContent = createPdfWithFontSpacingEqualsCharSpacing();
        PdfReader reader = new PdfReader(pdfContent);

        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("Preface", extractedText);
    }

    @Test
    public void testLittleFontSize() throws Exception {
        byte[] pdfContent = createPdfWithLittleFontSize();
        PdfReader reader = new PdfReader(pdfContent);

        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, createRenderListenerForTest());
        Assert.assertEquals("Preface Preface ", extractedText);
    }

    private byte[] createPdfWithNegativeCharSpacing(String str1, float charSpacing, String str2) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setCompressionLevel(0);
        document.open();

        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        canvas.moveText(45, document.getPageSize().getHeight() - 45);
        PdfTextArray textArray = new PdfTextArray();
        textArray.add(str1);
        textArray.add(charSpacing);
        textArray.add(str2);
        canvas.showText(textArray);
        canvas.endText();

        document.close();
        return outputStream.toByteArray();
    }

    private byte[] createPdfWithRotatedXObject(String xObjectText) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setCompressionLevel(0);
        document.open();

        document.add(new Paragraph("A"));
        document.add(new Paragraph("B"));

        PdfTemplate template = writer.getDirectContent().createTemplate(20, 100);
        template.setColorStroke(BaseColor.GREEN);
        template.rectangle(0, 0, template.getWidth(), template.getHeight());
        template.stroke();
        AffineTransform transform = new AffineTransform();
        transform.translate(0, template.getHeight());
        transform.rotate(-90 / 180f * Math.PI);
        template.transform(transform);
        template.beginText();
        template.setFontAndSize(BaseFont.createFont(), 12);
        template.moveText(0, template.getWidth() - 12);
        template.showText(xObjectText);
        template.endText();

        Image xObjectImage = Image.getInstance(template);
        xObjectImage.setRotationDegrees(90);
        document.add(xObjectImage);

        document.add(new Paragraph("C"));
        document.close();

        return outputStream.toByteArray();
    }

    private byte[] createSimplePdf(Rectangle pageSize, String... text) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(pageSize);
        PdfWriter.getInstance(document, outputStream);
        document.open();
        for (String line : text) {
            document.add(new Paragraph(line));
            document.newPage();
        }
        document.close();
        return outputStream.toByteArray();
    }

    private byte[] createPdfWithOverlappingTextHorizontal(String[] text1, String[] text2) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setCompressionLevel(0);
        document.open();

        PdfContentByte canvas = writer.getDirectContent();
        float yStart = 500;
        float xStart = 50;

        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        float x = xStart;
        for (String text : text1) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, yStart, 0);
            x += 70.0;
        }

        x = xStart + 12;
        for (String text : text2) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, yStart, 0);
            x += 70.0;
        }
        canvas.endText();

        document.close();
        return outputStream.toByteArray();
    }

    private PdfReader createPdfWithOverlappingTextVertical(String[] text1, String[] text2) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setCompressionLevel(0);
        document.open();

        PdfContentByte canvas = writer.getDirectContent();
        float yStart = 500;

        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        float y = yStart;
        for (String text : text1) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, 50, y, 0);
            y -= 25.0;
        }

        y = yStart - 13;
        for (String text : text2) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, 50, y, 0);
            y -= 25.0;
        }
        canvas.endText();

        document.close();
        return new PdfReader(outputStream.toByteArray());
    }

    private byte[] createPdfWithSuperscript(String regularText, String superscriptText) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        document.add(new Chunk(regularText));
        Chunk superscriptChunk = new Chunk(superscriptText);
        superscriptChunk.setTextRise(7.0f);
        document.add(superscriptChunk);

        document.close();
        return outputStream.toByteArray();
    }

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

        PdfTextArray textArray = new PdfTextArray();
        textArray.add("P");
        textArray.add(-226.2f);
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

        document.close();
        return outputStream.toByteArray();
    }

    private byte[] createPdfWithLittleFontSize() throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setCompressionLevel(0);
        document.open();

        BaseFont font = BaseFont.createFont();
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(font, 0.2f);
        canvas.moveText(45, document.getPageSize().getHeight() - 45);

        PdfTextArray textArray = new PdfTextArray();
        textArray.add("P");
        textArray.add("r");
        textArray.add("e");
        textArray.add("f");
        textArray.add("a");
        textArray.add("c");
        textArray.add("e");
        textArray.add(" ");

        canvas.showText(textArray);
        canvas.setFontAndSize(font, 10);
        canvas.showText(textArray);

        canvas.endText();

        document.close();
        return outputStream.toByteArray();
    }
}