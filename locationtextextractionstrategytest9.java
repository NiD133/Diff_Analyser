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

public class LocationTextExtractionStrategyTestTest9 extends SimpleTextExtractionStrategyTest {

    @Override
    @Before
    public void setUp() throws Exception {
    }

    @Override
    @After
    public void tearDown() throws Exception {
    }

    @Override
    public TextExtractionStrategy createRenderListenerForTest() {
        return new LocationTextExtractionStrategy();
    }

    private byte[] createPdfWithNegativeCharSpacing(String str1, float charSpacing, String str2) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        canvas.moveText(45, doc.getPageSize().getHeight() - 45);
        PdfTextArray ta = new PdfTextArray();
        ta.add(str1);
        ta.add(charSpacing);
        ta.add(str2);
        canvas.showText(ta);
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
        boolean rotate = true;
        PdfTemplate template = writer.getDirectContent().createTemplate(20, 100);
        template.setColorStroke(BaseColor.GREEN);
        template.rectangle(0, 0, template.getWidth(), template.getHeight());
        template.stroke();
        AffineTransform tx = new AffineTransform();
        if (rotate) {
            tx.translate(0, template.getHeight());
            tx.rotate(-90 / 180f * Math.PI);
        }
        template.transform(tx);
        template.beginText();
        template.setFontAndSize(BaseFont.createFont(), 12);
        if (rotate)
            template.moveText(0, template.getWidth() - 12);
        else
            template.moveText(0, template.getHeight() - 12);
        template.showText(xobjectText);
        template.endText();
        Image xobjectImage = Image.getInstance(template);
        if (rotate)
            xobjectImage.setRotationDegrees(90);
        doc.add(xobjectImage);
        doc.add(new Paragraph("C"));
        doc.close();
        return baos.toByteArray();
    }

    private byte[] createSimplePdf(Rectangle pageSize, final String... text) throws Exception {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Document document = new Document(pageSize);
        PdfWriter.getInstance(document, byteStream);
        document.open();
        for (String string : text) {
            document.add(new Paragraph(string));
            document.newPage();
        }
        document.close();
        final byte[] pdfBytes = byteStream.toByteArray();
        return pdfBytes;
    }

    protected byte[] createPdfWithOverlappingTextHorizontal(String[] text1, String[] text2) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();
        PdfContentByte canvas = writer.getDirectContent();
        float ystart = 500;
        float xstart = 50;
        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        float x = xstart;
        float y = ystart;
        for (String text : text1) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, y, 0);
            x += 70.0;
        }
        x = xstart + 12;
        y = ystart;
        for (String text : text2) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, y, 0);
            x += 70.0;
        }
        canvas.endText();
        doc.close();
        return baos.toByteArray();
    }

    private PdfReader createPdfWithOverlappingTextVertical(String[] text1, String[] text2) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();
        PdfContentByte canvas = writer.getDirectContent();
        float ystart = 500;
        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        float x = 50;
        float y = ystart;
        for (String text : text1) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, y, 0);
            y -= 25.0;
        }
        y = ystart - 13;
        for (String text : text2) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, y, 0);
            y -= 25.0;
        }
        canvas.endText();
        doc.close();
        return new PdfReader(baos.toByteArray());
    }

    private byte[] createPdfWithSupescript(String regularText, String superscriptText) throws Exception {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Document document = new Document();
        PdfWriter.getInstance(document, byteStream);
        document.open();
        document.add(new Chunk(regularText));
        Chunk c2 = new Chunk(superscriptText);
        c2.setTextRise(7.0f);
        document.add(c2);
        document.close();
        final byte[] pdfBytes = byteStream.toByteArray();
        return pdfBytes;
    }

    private byte[] createPdfWithFontSpacingEqualsCharSpacing() throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();
        BaseFont font = BaseFont.createFont();
        int fontSize = 12;
        float charSpace = font.getWidth(' ') / 1000.0f;
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(font, fontSize);
        canvas.moveText(45, doc.getPageSize().getHeight() - 45);
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
        doc.close();
        return baos.toByteArray();
    }

    private byte[] createPdfWithLittleFontSize() throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();
        BaseFont font = BaseFont.createFont();
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(font, 0.2f);
        canvas.moveText(45, doc.getPageSize().getHeight() - 45);
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
        doc.close();
        return baos.toByteArray();
    }

    @Test
    public void testSuperscript() throws Exception {
        byte[] content = createPdfWithSupescript("Hel", "lo");
        //TestResourceUtils.openBytesAsPdf(content);
        PdfReader r = new PdfReader(content);
        String text = PdfTextExtractor.getTextFromPage(r, 1, createRenderListenerForTest());
        Assert.assertEquals("Hello", text);
    }
}
