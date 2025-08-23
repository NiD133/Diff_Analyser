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

public class SimpleTextExtractionStrategyTestTest7 {

    String TEXT1 = "TEXT1 TEXT1";

    String TEXT2 = "TEXT2 TEXT2";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    public TextExtractionStrategy createRenderListenerForTest() {
        return new SimpleTextExtractionStrategy();
    }

    byte[] createPdfWithXObject(String xobjectText) throws Exception {
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
        template.moveText(5, template.getHeight() - 5);
        template.showText(xobjectText);
        template.endText();
        Image xobjectImage = Image.getInstance(template);
        doc.add(xobjectImage);
        doc.add(new Paragraph("C"));
        doc.close();
        return baos.toByteArray();
    }

    private static byte[] createPdfWithArrayText(String directContentTj) throws Exception {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, byteStream);
        document.setPageSize(PageSize.LETTER);
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        BaseFont font = BaseFont.createFont();
        cb.transform(AffineTransform.getTranslateInstance(100, 500));
        cb.beginText();
        cb.setFontAndSize(font, 12);
        cb.getInternalBuffer().append(directContentTj + "\n");
        cb.endText();
        document.close();
        final byte[] pdfBytes = byteStream.toByteArray();
        return pdfBytes;
    }

    private static byte[] createPdfWithArrayText(String text1, String text2, int spaceInPoints) throws Exception {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, byteStream);
        document.setPageSize(PageSize.LETTER);
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        BaseFont font = BaseFont.createFont();
        cb.beginText();
        cb.setFontAndSize(font, 12);
        cb.getInternalBuffer().append("[(" + text1 + ")" + (-spaceInPoints) + "(" + text2 + ")]TJ\n");
        cb.endText();
        document.close();
        final byte[] pdfBytes = byteStream.toByteArray();
        return pdfBytes;
    }

    private static byte[] createPdfWithRotatedText(String text1, String text2, float rotation, boolean moveTextToNextLine, float moveTextDelta) throws Exception {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, byteStream);
        document.setPageSize(PageSize.LETTER);
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        BaseFont font = BaseFont.createFont();
        float x = document.getPageSize().getWidth() / 2;
        float y = document.getPageSize().getHeight() / 2;
        cb.transform(AffineTransform.getTranslateInstance(x, y));
        cb.moveTo(-10, 0);
        cb.lineTo(10, 0);
        cb.moveTo(0, -10);
        cb.lineTo(0, 10);
        cb.stroke();
        cb.beginText();
        cb.setFontAndSize(font, 12);
        cb.transform(AffineTransform.getRotateInstance(rotation / 180f * Math.PI));
        cb.showText(text1);
        if (moveTextToNextLine)
            cb.moveText(0, moveTextDelta);
        else
            cb.transform(AffineTransform.getTranslateInstance(moveTextDelta, 0));
        cb.showText(text2);
        cb.endText();
        document.close();
        final byte[] pdfBytes = byteStream.toByteArray();
        return pdfBytes;
    }

    private static class SingleCharacterSimpleTextExtractionStrategy extends SimpleTextExtractionStrategy {

        @Override
        public void renderText(TextRenderInfo renderInfo) {
            for (TextRenderInfo tri : renderInfo.getCharacterRenderInfos()) super.renderText(tri);
        }
    }

    @Test
    public void testPartiallyRotatedText() throws Exception {
        byte[] bytes = createPdfWithRotatedText(TEXT1, TEXT2, 33, true, -20);
        Assert.assertEquals(TEXT1 + "\n" + TEXT2, PdfTextExtractor.getTextFromPage(new PdfReader(bytes), 1, createRenderListenerForTest()));
    }
}
