package com.itextpdf.text.pdf.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.itextpdf.testutils.TestResourceUtils;
import org.junit.Assert;
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
 * Tests for {@link SimpleTextExtractionStrategy}
 */
public class SimpleTextExtractionStrategyTest {
    private static final String TEXT1 = "TEXT1 TEXT1";
    private static final String TEXT2 = "TEXT2 TEXT2";
    private static final String SAN_DIEGO_CHAPTER_TJ = 
        "[(S)3.2(an)-255.0(D)13.0(i)8.3(e)-10.1(g)1.6(o)-247.5(C)2.4(h)5.8(ap)3.0(t)10.7(er)]TJ";

    private TextExtractionStrategy createSUT() {
        return new SimpleTextExtractionStrategy();
    }

    // Horizontal text tests
    @Test
    public void horizontallyAdjacentTextFragments_shouldConcatenateWithoutSpace() throws Exception {
        byte[] pdf = createPdfWithRotatedText(TEXT1, TEXT2, 0, false, 0);
        String extracted = extractTextFromPage(pdf, 1);
        Assert.assertEquals(TEXT1 + TEXT2, extracted);
    }

    @Test
    public void horizontallySpacedTextFragments_shouldInsertSpace() throws Exception {
        byte[] pdf = createPdfWithRotatedText(TEXT1, TEXT2, 0, false, 2);
        String extracted = extractTextFromPage(pdf, 1);
        Assert.assertEquals(TEXT1 + " " + TEXT2, extracted);
    }

    @Test
    public void textEndingWithSpaceFollowedByText_shouldNotAddExtraSpace() throws Exception {
        String trailingSpaceText = TEXT1 + " ";
        byte[] pdf = createPdfWithRotatedText(trailingSpaceText, TEXT2, 0, false, 2);
        String extracted = extractTextFromPage(pdf, 1);
        Assert.assertEquals(trailingSpaceText + TEXT2, extracted);
    }

    // Vertical text tests
    @Test
    public void verticallyStackedText_shouldInsertNewline() throws Exception {
        byte[] pdf = createPdfWithRotatedText(TEXT1, TEXT2, 0, true, -20);
        String extracted = extractTextFromPage(pdf, 1);
        Assert.assertEquals(TEXT1 + "\n" + TEXT2, extracted);
    }

    @Test
    public void verticallyStackedTextWithMinus90Rotation_shouldInsertNewline() throws Exception {
        byte[] pdf = createPdfWithRotatedText(TEXT1, TEXT2, -90, true, -20);
        String extracted = extractTextFromPage(pdf, 1);
        Assert.assertEquals(TEXT1 + "\n" + TEXT2, extracted);
    }

    @Test
    public void verticallyStackedTextWith90Rotation_shouldInsertNewline() throws Exception {
        byte[] pdf = createPdfWithRotatedText(TEXT1, TEXT2, 90, true, -20);
        String extracted = extractTextFromPage(pdf, 1);
        Assert.assertEquals(TEXT1 + "\n" + TEXT2, extracted);
    }

    @Test
    public void verticallyStackedTextWith33DegreeRotation_shouldInsertNewline() throws Exception {
        byte[] pdf = createPdfWithRotatedText(TEXT1, TEXT2, 33, true, -20);
        String extracted = extractTextFromPage(pdf, 1);
        Assert.assertEquals(TEXT1 + "\n" + TEXT2, extracted);
    }

    // Special spacing cases
    @Test
    public void explicitGlyphPositioning_shouldInsertSpaces() throws Exception {
        byte[] pdf = createPdfWithArrayText(TEXT1, TEXT2, 250);
        String extracted = extractTextFromPage(pdf, 1);
        Assert.assertEquals(TEXT1 + " " + TEXT2, extracted);
    }

    @Test
    public void complexGlyphPositioning_shouldProduceCorrectText() throws Exception {
        byte[] pdf = createPdfWithArrayText(SAN_DIEGO_CHAPTER_TJ);
        String extracted = extractTextFromPage(pdf, 1);
        Assert.assertEquals("San Diego Chapter", extracted);
    }

    // Edge cases
    @Test
    public void trailingSpaceInFirstFragment_shouldPreserveSpace() throws Exception {
        String trailingSpaceText = TEXT1 + " ";
        byte[] pdf = createPdfWithRotatedText(trailingSpaceText, TEXT2, 0, false, 6);
        String extracted = extractTextFromPage(pdf, 1);
        Assert.assertEquals(trailingSpaceText + TEXT2, extracted);
    }

    @Test
    public void leadingSpaceInSecondFragment_shouldPreserveSpace() throws Exception {
        String leadingSpaceText = " " + TEXT2;
        byte[] pdf = createPdfWithRotatedText(TEXT1, leadingSpaceText, 0, false, 6);
        String extracted = extractTextFromPage(pdf, 1);
        Assert.assertEquals(TEXT1 + leadingSpaceText, extracted);
    }

    // XObject tests
    @Test
    public void textInXObject_shouldBeExtracted() throws Exception {
        String xobjectText = "X";
        byte[] pdf = createPdfWithXObject(xobjectText);
        String extracted = extractTextFromPage(pdf, 1);
        Assert.assertTrue("Extracted text should contain XObject content", 
                         extracted.contains(xobjectText));
    }

    // Comparison tests with real documents
    @Test
    public void whenExtractingPage229_shouldMatchSingleCharStrategy() throws Exception {
        if (skipRealDocumentTest()) return;
        
        try (InputStream is = TestResourceUtils.getResourceAsStream(this, "page229.pdf");
             PdfReader reader = new PdfReader(is)) {
            String defaultText = extractText(reader, new SimpleTextExtractionStrategy(), 1);
            String singleCharText = extractText(reader, new SingleCharacterSimpleTextExtractionStrategy(), 1);
            Assert.assertEquals(defaultText, singleCharText);
        }
    }

    @Test
    public void whenExtractingIsoTc171_shouldMatchSingleCharStrategy() throws Exception {
        if (skipRealDocumentTest()) return;
        
        try (InputStream is = TestResourceUtils.getResourceAsStream(this, "ISO-TC171-SC2_N0896_SC2WG5_Edinburgh_Agenda.pdf");
             PdfReader reader = new PdfReader(is)) {
            String defaultText = extractText(reader, new SimpleTextExtractionStrategy(), 1) + "\n" +
                                 extractText(reader, new SimpleTextExtractionStrategy(), 2);
            String singleCharText = extractText(reader, new SingleCharacterSimpleTextExtractionStrategy(), 1) + "\n" +
                                     extractText(reader, new SingleCharacterSimpleTextExtractionStrategy(), 2);
            Assert.assertEquals(defaultText, singleCharText);
        }
    }

    // Helper methods
    private String extractTextFromPage(byte[] pdfBytes, int pageNum) throws IOException {
        try (PdfReader reader = new PdfReader(pdfBytes)) {
            return PdfTextExtractor.getTextFromPage(reader, pageNum, createSUT());
        }
    }

    private String extractText(PdfReader reader, TextExtractionStrategy strategy, int page) throws IOException {
        return PdfTextExtractor.getTextFromPage(reader, page, strategy);
    }

    private boolean skipRealDocumentTest() {
        return this.getClass() != SimpleTextExtractionStrategyTest.class;
    }

    // PDF generation helpers (unchanged except minor formatting)
    private byte[] createPdfWithXObject(String xobjectText) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        doc.add(new Paragraph("A"));
        doc.add(new Paragraph("B"));
        
        PdfTemplate template = writer.getDirectContent().createTemplate(100, 100);
        template.beginText();
        template.setFontAndSize(BaseFont.createFont(), 12);
        template.moveText(5, template.getHeight()-5);
        template.showText(xobjectText);
        template.endText();
        
        doc.add(Image.getInstance(template));
        doc.add(new Paragraph("C"));
        doc.close();
        
        return baos.toByteArray();
    }

    private static byte[] createPdfWithArrayText(String directContentTj) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.LETTER);
        PdfWriter writer = PdfWriter.getInstance(document, byteStream);
        document.open();

        PdfContentByte cb = writer.getDirectContent();
        cb.transform(AffineTransform.getTranslateInstance(100, 500));
        cb.beginText();
        cb.setFontAndSize(BaseFont.createFont(), 12);
        cb.getInternalBuffer().append(directContentTj).append("\n");
        cb.endText();
        
        document.close();
        return byteStream.toByteArray();
    }

    private static byte[] createPdfWithArrayText(String text1, String text2, int spaceInPoints) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.LETTER);
        PdfWriter writer = PdfWriter.getInstance(document, byteStream);
        document.open();

        PdfContentByte cb = writer.getDirectContent();
        cb.beginText();
        cb.setFontAndSize(BaseFont.createFont(), 12);
        cb.getInternalBuffer().append("[(")
          .append(text1)
          .append(")")
          .append(-spaceInPoints)
          .append("(")
          .append(text2)
          .append(")]TJ\n");
        cb.endText();
        
        document.close();
        return byteStream.toByteArray();
    }

    private static byte[] createPdfWithRotatedText(String text1, String text2, float rotation, 
                                                  boolean moveToNextLine, float moveDelta) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.LETTER);
        PdfWriter writer = PdfWriter.getInstance(document, byteStream);
        document.open();

        PdfContentByte cb = writer.getDirectContent();
        float x = document.getPageSize().getWidth()/2;
        float y = document.getPageSize().getHeight()/2;
        
        // Draw crosshairs for reference
        cb.transform(AffineTransform.getTranslateInstance(x, y));
        cb.moveTo(-10, 0).lineTo(10, 0)
          .moveTo(0, -10).lineTo(0, 10).stroke();
        
        // Draw text
        cb.beginText();
        cb.setFontAndSize(BaseFont.createFont(), 12);
        cb.transform(AffineTransform.getRotateInstance(Math.toRadians(rotation)));
        cb.showText(text1);
        
        if (moveToNextLine) {
            cb.moveText(0, moveDelta);
        } else {
            cb.transform(AffineTransform.getTranslateInstance(moveDelta, 0));
        }
        cb.showText(text2);
        cb.endText();

        document.close();
        return byteStream.toByteArray();
    }

    private static class SingleCharacterSimpleTextExtractionStrategy extends SimpleTextExtractionStrategy {
        @Override
        public void renderText(TextRenderInfo renderInfo) {
            for (TextRenderInfo tri : renderInfo.getCharacterRenderInfos()) {
                super.renderText(tri);
            }
        }
    }
}