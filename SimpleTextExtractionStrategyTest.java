package com.itextpdf.text.pdf.parser;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for SimpleTextExtractionStrategy ensuring correct spacing, line breaks,
 * and handling of rotations and XObjects.
 */
public class SimpleTextExtractionStrategyTest {

    private static final String TEXT_ONE = "TEXT1 TEXT1";
    private static final String TEXT_TWO = "TEXT2 TEXT2";
    private static final String SPACE = " ";
    private static final String NEWLINE = "\n";
    private static final float DEFAULT_FONT_SIZE = 12f;

    private TextExtractionStrategy createStrategy() {
        return new SimpleTextExtractionStrategy();
    }

    // Region: Co-linear (same baseline) scenarios

    @Test
    public void coLinearText_noExplicitSpace_noGap() throws Exception {
        byte[] pdf = createPdfWithRotatedText(TEXT_ONE, TEXT_TWO, 0, false, 0);
        Assert.assertEquals(TEXT_ONE + TEXT_TWO, extractText(pdf, 1, createStrategy()));
    }

    @Test
    public void coLinearText_gapBetweenChunks_insertsSingleSpace() throws Exception {
        byte[] pdf = createPdfWithRotatedText(TEXT_ONE, TEXT_TWO, 0, false, 2);
        Assert.assertEquals(TEXT_ONE + SPACE + TEXT_TWO, extractText(pdf, 1, createStrategy()));
    }

    @Test
    public void coLinearText_firstChunkEndsWithSpace_noExtraSpaceInserted() throws Exception {
        String textOneWithTrailingSpace = TEXT_ONE + SPACE;
        byte[] pdf = createPdfWithRotatedText(textOneWithTrailingSpace, TEXT_TWO, 0, false, 2);
        Assert.assertEquals(textOneWithTrailingSpace + TEXT_TWO, extractText(pdf, 1, createStrategy()));
    }

    // Region: Line breaks (moving text to next line)

    @Test
    public void unrotatedText_movedToNextLine_insertsNewline() throws Exception {
        byte[] pdf = createPdfWithRotatedText(TEXT_ONE, TEXT_TWO, 0, true, -20);
        Assert.assertEquals(TEXT_ONE + NEWLINE + TEXT_TWO, extractText(pdf, 1, createStrategy()));
    }

    @Test
    public void rotatedText_negative90_movedToNextLine_insertsNewline() throws Exception {
        byte[] pdf = createPdfWithRotatedText(TEXT_ONE, TEXT_TWO, -90, true, -20);
        Assert.assertEquals(TEXT_ONE + NEWLINE + TEXT_TWO, extractText(pdf, 1, createStrategy()));
    }

    @Test
    public void rotatedText_positive90_movedToNextLine_insertsNewline() throws Exception {
        byte[] pdf = createPdfWithRotatedText(TEXT_ONE, TEXT_TWO, 90, true, -20);
        Assert.assertEquals(TEXT_ONE + NEWLINE + TEXT_TWO, extractText(pdf, 1, createStrategy()));
    }

    @Test
    public void rotatedText_nonRightAngle_movedToNextLine_insertsNewline() throws Exception {
        byte[] pdf = createPdfWithRotatedText(TEXT_ONE, TEXT_TWO, 33, true, -20);
        Assert.assertEquals(TEXT_ONE + NEWLINE + TEXT_TWO, extractText(pdf, 1, createStrategy()));
    }

    // Region: Glyph-position-based spacing (TJ array)

    @Test
    public void wordSpacing_explicitGlyphPositioning_insertsSpace() throws Exception {
        byte[] pdf = createPdfWithArrayText(TEXT_ONE, TEXT_TWO, 250);
        Assert.assertEquals(TEXT_ONE + SPACE + TEXT_TWO, extractText(pdf, 1, createStrategy()));
    }

    @Test
    public void wordSpacing_explicitGlyphPositioning_complex() throws Exception {
        // A crafted TJ array that should result in "San Diego Chapter"
        byte[] pdf = createPdfWithArrayText("[(S)3.2(an)-255.0(D)13.0(i)8.3(e)-10.1(g)1.6(o)-247.5(C)2.4(h)5.8(ap)3.0(t)10.7(er)]TJ");
        Assert.assertEquals("San Diego Chapter", extractText(pdf, 1, createStrategy()));
    }

    // Region: Leading/trailing space behavior

    @Test
    public void trailingSpace_preserved_and_noExtraSpaceAdded() throws Exception {
        byte[] pdf = createPdfWithRotatedText(TEXT_ONE + SPACE, TEXT_TWO, 0, false, 6);
        Assert.assertEquals(TEXT_ONE + SPACE + TEXT_TWO, extractText(pdf, 1, createStrategy()));
    }

    @Test
    public void leadingSpace_preserved_and_noExtraSpaceAdded() throws Exception {
        byte[] pdf = createPdfWithRotatedText(TEXT_ONE, SPACE + TEXT_TWO, 0, false, 6);
        Assert.assertEquals(TEXT_ONE + SPACE + TEXT_TWO, extractText(pdf, 1, createStrategy()));
    }

    // Region: XObject extraction

    @Test
    public void extractTextFromXObject_contentIsFound() throws Exception {
        String expected = "X";
        byte[] pdf = createPdfWithXObject(expected);
        String text = extractText(pdf, 1, createStrategy());
        Assert.assertTrue("Extracted text must contain '" + expected + "'", text.contains(expected));
    }

    // Region: Regression-style comparisons with per-character strategy

    @Test
    public void extractFromPage229_sameAsCharacterByCharacter() throws IOException {
        Assume.assumeTrue(getClass() == SimpleTextExtractionStrategyTest.class);
        try (InputStream is = TestResourceUtils.getResourceAsStream(this, "page229.pdf")) {
            PdfReader reader = new PdfReader(is);
            try {
                String simple = PdfTextExtractor.getTextFromPage(reader, 1, new SimpleTextExtractionStrategy());
                String perChar = PdfTextExtractor.getTextFromPage(reader, 1, new CharacterByCharacterStrategy());
                Assert.assertEquals(simple, perChar);
            } finally {
                reader.close();
            }
        }
    }

    @Test
    public void extractFromIsoTc171_firstTwoPages_sameAsCharacterByCharacter() throws IOException {
        Assume.assumeTrue(getClass() == SimpleTextExtractionStrategyTest.class);
        try (InputStream is = TestResourceUtils.getResourceAsStream(this,
                "ISO-TC171-SC2_N0896_SC2WG5_Edinburgh_Agenda.pdf")) {
            PdfReader reader = new PdfReader(is);
            try {
                String simple = PdfTextExtractor.getTextFromPage(reader, 1, new SimpleTextExtractionStrategy())
                        + NEWLINE
                        + PdfTextExtractor.getTextFromPage(reader, 2, new SimpleTextExtractionStrategy());
                String perChar = PdfTextExtractor.getTextFromPage(reader, 1, new CharacterByCharacterStrategy())
                        + NEWLINE
                        + PdfTextExtractor.getTextFromPage(reader, 2, new CharacterByCharacterStrategy());
                Assert.assertEquals(simple, perChar);
            } finally {
                reader.close();
            }
        }
    }

    // Region: Helpers to create PDFs

    /**
     * Creates a one-page PDF with two text chunks. The second chunk can be placed on the same line
     * with a horizontal offset or moved to a new line by a vertical delta. A rotation can be applied.
     */
    private static byte[] createPdfWithRotatedText(String text1,
                                                   String text2,
                                                   float rotationDegrees,
                                                   boolean moveTextToNextLine,
                                                   float moveTextDelta) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.LETTER);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        PdfContentByte cb = writer.getDirectContent();
        BaseFont font = BaseFont.createFont();

        // Center baseline crosshair for reference (not visible in extraction)
        float x = document.getPageSize().getWidth() / 2;
        float y = document.getPageSize().getHeight() / 2;
        cb.transform(AffineTransform.getTranslateInstance(x, y));
        cb.moveTo(-10, 0);
        cb.lineTo(10, 0);
        cb.moveTo(0, -10);
        cb.lineTo(0, 10);
        cb.stroke();

        // Render text with optional rotation and movement
        cb.beginText();
        cb.setFontAndSize(font, DEFAULT_FONT_SIZE);
        cb.transform(AffineTransform.getRotateInstance(rotationDegrees / 180f * Math.PI));
        cb.showText(text1);
        if (moveTextToNextLine) {
            cb.moveText(0, moveTextDelta);
        } else {
            cb.transform(AffineTransform.getTranslateInstance(moveTextDelta, 0));
        }
        cb.showText(text2);
        cb.endText();

        document.close();
        return baos.toByteArray();
    }

    /**
     * Creates a one-page PDF injecting a raw TJ operation string into the content stream.
     * This intentionally uses the internal buffer to simulate precise glyph-positioning scenarios.
     */
    private static byte[] createPdfWithArrayText(String directContentTj) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.LETTER);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        PdfContentByte cb = writer.getDirectContent();
        BaseFont font = BaseFont.createFont();

        cb.transform(AffineTransform.getTranslateInstance(100, 500));
        cb.beginText();
        cb.setFontAndSize(font, DEFAULT_FONT_SIZE);
        // Inject TJ array directly to simulate spacing via glyph positioning
        cb.getInternalBuffer().append(directContentTj).append('\n');
        cb.endText();

        document.close();
        return baos.toByteArray();
    }

    /**
     * Creates a one-page PDF with two strings separated in a TJ array by a negative kerning value,
     * which should result in a space being inserted by the extraction strategy.
     */
    private static byte[] createPdfWithArrayText(String text1, String text2, int spaceInPoints) throws Exception {
        String tj = "[(" + text1 + ")" + (-spaceInPoints) + "(" + text2 + ")]TJ";
        return createPdfWithArrayText(tj);
    }

    /**
     * Creates a PDF with an XObject containing text, placed among other content.
     */
    private static byte[] createPdfWithXObject(String xobjectText) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setCompressionLevel(0); // Make content easier to inspect
        document.open();

        document.add(new Paragraph("A"));
        document.add(new Paragraph("B"));

        PdfTemplate template = writer.getDirectContent().createTemplate(100, 100);
        template.beginText();
        template.setFontAndSize(BaseFont.createFont(), DEFAULT_FONT_SIZE);
        template.moveText(5, template.getHeight() - 5);
        template.showText(xobjectText);
        template.endText();

        Image xobjectImage = Image.getInstance(template);
        document.add(xobjectImage);

        document.add(new Paragraph("C"));

        document.close();
        return baos.toByteArray();
    }

    // Region: Small utilities

    private static String extractText(byte[] pdfBytes, int page, TextExtractionStrategy strategy) throws IOException {
        PdfReader reader = new PdfReader(pdfBytes);
        try {
            return PdfTextExtractor.getTextFromPage(reader, page, strategy);
        } finally {
            reader.close();
        }
    }

    /**
     * Strategy that forwards each character to SimpleTextExtractionStrategy individually.
     * Useful for baseline comparisons with more granular extraction.
     */
    private static class CharacterByCharacterStrategy extends SimpleTextExtractionStrategy {
        @Override
        public void renderText(TextRenderInfo renderInfo) {
            for (TextRenderInfo tri : renderInfo.getCharacterRenderInfos()) {
                super.renderText(tri);
            }
        }
    }
}