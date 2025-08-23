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
 * Test suite for SimpleTextExtractionStrategy.
 * This suite tests various scenarios of text extraction from PDFs.
 */
public class SimpleTextExtractionStrategyTest {

    private static final String SAMPLE_TEXT1 = "TEXT1 TEXT1";
    private static final String SAMPLE_TEXT2 = "TEXT2 TEXT2";

    @Before
    public void setUp() throws Exception {
        // Setup code if needed
    }

    @After
    public void tearDown() throws Exception {
        // Teardown code if needed
    }

    /**
     * Creates a text extraction strategy for testing.
     * @return a new instance of SimpleTextExtractionStrategy
     */
    private TextExtractionStrategy createTextExtractionStrategy() {
        return new SimpleTextExtractionStrategy();
    }

    @Test
    public void testCollinearText() throws Exception {
        byte[] pdfBytes = createPdfWithRotatedText(SAMPLE_TEXT1, SAMPLE_TEXT2, 0, false, 0);
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createTextExtractionStrategy());
        Assert.assertEquals(SAMPLE_TEXT1 + SAMPLE_TEXT2, extractedText);
    }

    @Test
    public void testCollinearTextWithSpace() throws Exception {
        byte[] pdfBytes = createPdfWithRotatedText(SAMPLE_TEXT1, SAMPLE_TEXT2, 0, false, 2);
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createTextExtractionStrategy());
        Assert.assertEquals(SAMPLE_TEXT1 + " " + SAMPLE_TEXT2, extractedText);
    }

    @Test
    public void testCollinearTextEndingWithSpace() throws Exception {
        String textWithTrailingSpace = SAMPLE_TEXT1 + " ";
        byte[] pdfBytes = createPdfWithRotatedText(textWithTrailingSpace, SAMPLE_TEXT2, 0, false, 2);
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createTextExtractionStrategy());
        Assert.assertEquals(textWithTrailingSpace + SAMPLE_TEXT2, extractedText);
    }

    @Test
    public void testUnrotatedText() throws Exception {
        byte[] pdfBytes = createPdfWithRotatedText(SAMPLE_TEXT1, SAMPLE_TEXT2, 0, true, -20);
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createTextExtractionStrategy());
        Assert.assertEquals(SAMPLE_TEXT1 + "\n" + SAMPLE_TEXT2, extractedText);
    }

    @Test
    public void testRotatedText() throws Exception {
        byte[] pdfBytes = createPdfWithRotatedText(SAMPLE_TEXT1, SAMPLE_TEXT2, -90, true, -20);
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createTextExtractionStrategy());
        Assert.assertEquals(SAMPLE_TEXT1 + "\n" + SAMPLE_TEXT2, extractedText);
    }

    @Test
    public void testRotatedText90Degrees() throws Exception {
        byte[] pdfBytes = createPdfWithRotatedText(SAMPLE_TEXT1, SAMPLE_TEXT2, 90, true, -20);
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createTextExtractionStrategy());
        Assert.assertEquals(SAMPLE_TEXT1 + "\n" + SAMPLE_TEXT2, extractedText);
    }

    @Test
    public void testPartiallyRotatedText() throws Exception {
        byte[] pdfBytes = createPdfWithRotatedText(SAMPLE_TEXT1, SAMPLE_TEXT2, 33, true, -20);
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createTextExtractionStrategy());
        Assert.assertEquals(SAMPLE_TEXT1 + "\n" + SAMPLE_TEXT2, extractedText);
    }

    @Test
    public void testWordSpacingWithExplicitGlyphPositioning() throws Exception {
        byte[] pdfBytes = createPdfWithArrayText(SAMPLE_TEXT1, SAMPLE_TEXT2, 250);
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createTextExtractionStrategy());
        Assert.assertEquals(SAMPLE_TEXT1 + " " + SAMPLE_TEXT2, extractedText);
    }

    @Test
    public void testWordSpacingWithExplicitGlyphPositioning2() throws Exception {
        byte[] pdfBytes = createPdfWithArrayText("[(S)3.2(an)-255.0(D)13.0(i)8.3(e)-10.1(g)1.6(o)-247.5(C)2.4(h)5.8(ap)3.0(t)10.7(er)]TJ");
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createTextExtractionStrategy());
        Assert.assertEquals("San Diego Chapter", extractedText);
    }

    @Test
    public void testTrailingSpace() throws Exception {
        byte[] pdfBytes = createPdfWithRotatedText(SAMPLE_TEXT1 + " ", SAMPLE_TEXT2, 0, false, 6);
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createTextExtractionStrategy());
        Assert.assertEquals(SAMPLE_TEXT1 + " " + SAMPLE_TEXT2, extractedText);
    }

    @Test
    public void testLeadingSpace() throws Exception {
        byte[] pdfBytes = createPdfWithRotatedText(SAMPLE_TEXT1, " " + SAMPLE_TEXT2, 0, false, 6);
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createTextExtractionStrategy());
        Assert.assertEquals(SAMPLE_TEXT1 + " " + SAMPLE_TEXT2, extractedText);
    }

    @Test
    public void testExtractXObjectText() throws Exception {
        String xObjectText = "X";
        byte[] pdfBytes = createPdfWithXObject(xObjectText);
        String extractedText = PdfTextExtractor.getTextFromPage(new PdfReader(pdfBytes), 1, createTextExtractionStrategy());
        Assert.assertTrue("Extracted text must contain '" + xObjectText + "'", extractedText.contains(xObjectText));
    }

    @Test
    public void testExtractFromPage229() throws IOException {
        if (this.getClass() != SimpleTextExtractionStrategyTest.class) return;
        InputStream inputStream = TestResourceUtils.getResourceAsStream(this, "page229.pdf");
        PdfReader reader = new PdfReader(inputStream);
        String extractedText1 = PdfTextExtractor.getTextFromPage(reader, 1, new SimpleTextExtractionStrategy());
        String extractedText2 = PdfTextExtractor.getTextFromPage(reader, 1, new SingleCharacterSimpleTextExtractionStrategy());
        Assert.assertEquals(extractedText1, extractedText2);
        reader.close();
    }

    @Test
    public void testExtractFromIsoTc171() throws IOException {
        if (this.getClass() != SimpleTextExtractionStrategyTest.class) return;
        InputStream inputStream = TestResourceUtils.getResourceAsStream(this, "ISO-TC171-SC2_N0896_SC2WG5_Edinburgh_Agenda.pdf");
        PdfReader reader = new PdfReader(inputStream);
        String extractedText1 = PdfTextExtractor.getTextFromPage(reader, 1, new SimpleTextExtractionStrategy()) +
                "\n" +
                PdfTextExtractor.getTextFromPage(reader, 2, new SimpleTextExtractionStrategy());
        String extractedText2 = PdfTextExtractor.getTextFromPage(reader, 1, new SingleCharacterSimpleTextExtractionStrategy()) +
                "\n" +
                PdfTextExtractor.getTextFromPage(reader, 2, new SingleCharacterSimpleTextExtractionStrategy());
        Assert.assertEquals(extractedText1, extractedText2);
        reader.close();
    }

    /**
     * Creates a PDF with an XObject containing the specified text.
     * @param xObjectText the text to include in the XObject
     * @return the PDF as a byte array
     * @throws Exception if an error occurs during PDF creation
     */
    private byte[] createPdfWithXObject(String xObjectText) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setCompressionLevel(0);
        document.open();

        document.add(new Paragraph("A"));
        document.add(new Paragraph("B"));

        PdfTemplate template = writer.getDirectContent().createTemplate(100, 100);
        template.beginText();
        template.setFontAndSize(BaseFont.createFont(), 12);
        template.moveText(5, template.getHeight() - 5);
        template.showText(xObjectText);
        template.endText();

        Image xObjectImage = Image.getInstance(template);
        document.add(xObjectImage);
        document.add(new Paragraph("C"));

        document.close();
        return outputStream.toByteArray();
    }

    /**
     * Creates a PDF with text using an array TJ operator.
     * @param directContentTj the TJ operator content
     * @return the PDF as a byte array
     * @throws Exception if an error occurs during PDF creation
     */
    private static byte[] createPdfWithArrayText(String directContentTj) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.setPageSize(PageSize.LETTER);
        document.open();

        PdfContentByte contentByte = writer.getDirectContent();
        BaseFont font = BaseFont.createFont();
        contentByte.transform(AffineTransform.getTranslateInstance(100, 500));
        contentByte.beginText();
        contentByte.setFontAndSize(font, 12);
        contentByte.getInternalBuffer().append(directContentTj + "\n");
        contentByte.endText();

        document.close();
        return outputStream.toByteArray();
    }

    /**
     * Creates a PDF with two pieces of text, optionally rotated and moved.
     * @param text1 the first piece of text
     * @param text2 the second piece of text
     * @param rotation the rotation angle in degrees
     * @param moveTextToNextLine whether to move the second text to the next line
     * @param moveTextDelta the amount to move the second text
     * @return the PDF as a byte array
     * @throws Exception if an error occurs during PDF creation
     */
    private static byte[] createPdfWithRotatedText(String text1, String text2, float rotation, boolean moveTextToNextLine, float moveTextDelta) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.setPageSize(PageSize.LETTER);
        document.open();

        PdfContentByte contentByte = writer.getDirectContent();
        BaseFont font = BaseFont.createFont();

        float x = document.getPageSize().getWidth() / 2;
        float y = document.getPageSize().getHeight() / 2;
        contentByte.transform(AffineTransform.getTranslateInstance(x, y));

        contentByte.moveTo(-10, 0);
        contentByte.lineTo(10, 0);
        contentByte.moveTo(0, -10);
        contentByte.lineTo(0, 10);
        contentByte.stroke();

        contentByte.beginText();
        contentByte.setFontAndSize(font, 12);
        contentByte.transform(AffineTransform.getRotateInstance(rotation / 180f * Math.PI));
        contentByte.showText(text1);
        if (moveTextToNextLine) {
            contentByte.moveText(0, moveTextDelta);
        } else {
            contentByte.transform(AffineTransform.getTranslateInstance(moveTextDelta, 0));
        }
        contentByte.showText(text2);
        contentByte.endText();

        document.close();
        return outputStream.toByteArray();
    }

    /**
     * A custom text extraction strategy that processes each character individually.
     */
    private static class SingleCharacterSimpleTextExtractionStrategy extends SimpleTextExtractionStrategy {
        @Override
        public void renderText(TextRenderInfo renderInfo) {
            for (TextRenderInfo charRenderInfo : renderInfo.getCharacterRenderInfos()) {
                super.renderText(charRenderInfo);
            }
        }
    }
}