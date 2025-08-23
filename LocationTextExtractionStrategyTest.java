package com.itextpdf.text.pdf.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

/**
 * Tests for LocationTextExtractionStrategy that focus on spatial ordering and spacing heuristics.
 */
public class LocationTextExtractionStrategyTest extends SimpleTextExtractionStrategyTest {

    private static final int FIRST_PAGE = 1;
    private static final String NL = "\n";

    @Override
    @Before
    public void setUp() throws Exception {
        // Intentionally empty; kept to honor parent lifecycle if needed.
    }

    @Override
    @After
    public void tearDown() throws Exception {
        // Intentionally empty; kept to honor parent lifecycle if needed.
    }

    @Override
    public TextExtractionStrategy createRenderListenerForTest() {
        return new LocationTextExtractionStrategy();
    }

    @Test
    public void ordersByYPosition_whenTextLinesOverlapVertically() throws Exception {
        byte[] pdf = createPdfWithOverlappingTextVertical(
                new String[]{"A", "B", "C", "D"},
                new String[]{"AA", "BB", "CC", "DD"}
        );

        String extracted = extractFirstPageText(pdf);
        Assert.assertEquals("Expected interleaved lines based on Y ordering",
                "A\nAA\nB\nBB\nC\nCC\nD\nDD", extracted);
    }

    @Test
    public void ordersByXPosition_whenTextLinesOverlapHorizontally() throws Exception {
        byte[] pdf = createPdfWithOverlappingTextHorizontal(
                new String[]{"A", "B", "C", "D"},
                new String[]{"AA", "BB", "CC", "DD"}
        );

        String extracted = extractFirstPageText(pdf);
        Assert.assertEquals("Expected interleaved tokens based on X ordering",
                "A AA B BB C CC D DD", extracted);
        // Tab-separated variant intentionally not asserted (was commented in original).
    }

    @Test
    public void preservesTextOrder_onRotatedPage_90Degrees() throws Exception {
        byte[] pdf = createSimplePdf(PageSize.LETTER.rotate(), "A\nB\nC\nD");

        String extracted = extractFirstPageText(pdf);
        Assert.assertEquals("Text order should be preserved on 90-degree rotation",
                "A\nB\nC\nD", extracted);
    }

    @Test
    public void preservesTextOrder_onRotatedPage_180Degrees() throws Exception {
        byte[] pdf = createSimplePdf(PageSize.LETTER.rotate().rotate(), "A\nB\nC\nD");

        String extracted = extractFirstPageText(pdf);
        Assert.assertEquals("Text order should be preserved on 180-degree rotation",
                "A\nB\nC\nD", extracted);
    }

    @Test
    public void preservesTextOrder_onRotatedPage_270Degrees() throws Exception {
        byte[] pdf = createSimplePdf(PageSize.LETTER.rotate().rotate().rotate(), "A\nB\nC\nD");

        String extracted = extractFirstPageText(pdf);
        Assert.assertEquals("Text order should be preserved on 270-degree rotation",
                "A\nB\nC\nD", extracted);
    }

    @Test
    public void extractsTextFromRotatedXObject() throws Exception {
        byte[] pdf = createPdfWithRotatedXObject("X");

        String extracted = extractFirstPageText(pdf);
        Assert.assertEquals("Expected XObject text to appear between paragraphs B and C",
                "A\nB\nX\nC", extracted);
    }

    @Test
    public void mergesCharacters_whenNegativeCharacterSpacingBringsThemTogether() throws Exception {
        byte[] pdf = createPdfWithNegativeCharSpacing("W", 200, "A");

        String extracted = extractFirstPageText(pdf);
        Assert.assertEquals("Characters brought together by negative char spacing should merge",
                "WA", extracted);
    }

    @Test
    public void vectorMath_dotProductSign_sanityCheck() {
        Vector start = new Vector(0, 0, 1);
        Vector end = new Vector(1, 0, 1);
        Vector antiparallelStart = new Vector(0.9f, 0, 1);
        Vector parallelStart = new Vector(1.1f, 0, 1);

        float antiParallel = antiparallelStart.subtract(end).dot(end.subtract(start).normalize());
        Assert.assertEquals("Antiparallel segment should produce negative dot product", -0.1f, antiParallel, 0.0001);

        float parallel = parallelStart.subtract(end).dot(end.subtract(start).normalize());
        Assert.assertEquals("Parallel segment should produce positive dot product", 0.1f, parallel, 0.0001);
    }

    @Test
    public void mergesSuperscriptWithBaselineText() throws Exception {
        byte[] pdf = createPdfWithSuperscript("Hel", "lo");

        String extracted = extractFirstPageText(pdf);
        Assert.assertEquals("Superscript should be merged inline with surrounding text",
                "Hello", extracted);
    }

    @Test
    public void handlesFontSpacingEqualToCharSpacing() throws Exception {
        byte[] pdf = createPdfWithFontSpacingEqualsCharSpacing();

        String extracted = extractFirstPageText(pdf);
        Assert.assertEquals("Preface", extracted);
    }

    @Test
    public void handlesVerySmallFontSizes() throws Exception {
        byte[] pdf = createPdfWithLittleFontSize();

        String extracted = extractFirstPageText(pdf);
        Assert.assertEquals("Preface Preface ", extracted);
    }

    // --------------------------------------------------------------------------------------------
    // Helpers - PDF generation
    // --------------------------------------------------------------------------------------------

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

        // Show "str1", then apply a positive spacing adjustment (PDF value), then "str2".
        // Positive PDF adjustment moves next glyph left, effectively negative visual spacing.
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

        // Regular paragraphs before and after XObject
        doc.add(new Paragraph("A"));
        doc.add(new Paragraph("B"));

        // Create a template (XObject), rotate its internal content, then rotate image placement to compensate
        PdfTemplate template = writer.getDirectContent().createTemplate(20, 100);
        template.setColorStroke(BaseColor.GREEN);
        template.rectangle(0, 0, template.getWidth(), template.getHeight());
        template.stroke();

        boolean rotate = true;
        AffineTransform tx = new AffineTransform();
        if (rotate) {
            tx.translate(0, template.getHeight());
            tx.rotate(Math.toRadians(-90));
        }
        template.transform(tx);

        template.beginText();
        template.setFontAndSize(BaseFont.createFont(), 12);
        if (rotate) {
            template.moveText(0, template.getWidth() - 12);
        } else {
            template.moveText(0, template.getHeight() - 12);
        }
        template.showText(xobjectText);
        template.endText();

        Image xobjectImage = Image.getInstance(template);
        if (rotate) {
            xobjectImage.setRotationDegrees(90);
        }
        doc.add(xobjectImage);

        doc.add(new Paragraph("C"));
        doc.close();
        return baos.toByteArray();
    }

    /**
     * Creates a PDF with a single page containing all text in a single Paragraph (newlines preserved).
     */
    private byte[] createSimplePdf(Rectangle pageSize, final String... text) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(pageSize);
        PdfWriter.getInstance(document, baos);
        document.open();
        for (String s : text) {
            document.add(new Paragraph(s));
            // New page after each paragraph; our use passes a single multi-line string.
            document.newPage();
        }
        document.close();
        return baos.toByteArray();
    }

    /**
     * Places two sequences of strings at nearly the same Y, slightly offset in X, to test horizontal ordering.
     */
    protected byte[] createPdfWithOverlappingTextHorizontal(String[] text1, String[] text2) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        PdfContentByte canvas = writer.getDirectContent();
        float y = 500;
        float xStart = 50;

        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);

        float x = xStart;
        for (String t : text1) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, t, x, y, 0);
            x += 70.0f;
        }

        x = xStart + 12; // offset slightly to the right
        for (String t : text2) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, t, x, y, 0);
            x += 70.0f;
        }
        canvas.endText();

        doc.close();
        return baos.toByteArray();
    }

    /**
     * Places two vertical sequences of strings at nearly the same X, slightly offset in Y, to test vertical ordering.
     */
    private byte[] createPdfWithOverlappingTextVertical(String[] text1, String[] text2) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        PdfContentByte canvas = writer.getDirectContent();
        float yStart = 500;
        float x = 50;

        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);

        float y = yStart;
        for (String t : text1) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, t, x, y, 0);
            y -= 25.0f;
        }

        y = yStart - 13; // slight vertical offset
        for (String t : text2) {
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, t, x, y, 0);
            y -= 25.0f;
        }
        canvas.endText();

        doc.close();
        return baos.toByteArray();
    }

    private byte[] createPdfWithSuperscript(String baselineText, String superscriptText) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();

        document.add(new Chunk(baselineText));
        Chunk sup = new Chunk(superscriptText);
        sup.setTextRise(7.0f);
        document.add(sup);

        document.close();
        return baos.toByteArray();
    }

    private byte[] createPdfWithFontSpacingEqualsCharSpacing() throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        BaseFont font = BaseFont.createFont();
        int fontSize = 12;
        float charSpace = font.getWidth(' ') / 1000.0f; // width in glyph space

        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(font, fontSize);
        canvas.moveText(45, doc.getPageSize().getHeight() - 45);
        canvas.setCharacterSpacing(-charSpace * fontSize);

        // Construct "Preface" with explicit kerning to emulate font spacing == char spacing condition
        PdfTextArray textArray = new PdfTextArray();
        textArray.add("P"); textArray.add(-226.2f);
        textArray.add("r"); textArray.add(-231.8f);
        textArray.add("e"); textArray.add(-230.8f);
        textArray.add("f"); textArray.add(-238f);
        textArray.add("a"); textArray.add(-238.9f);
        textArray.add("c"); textArray.add(-228.9f);
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

        // Write "Preface " at tiny size to ensure extractor still captures it
        canvas.setFontAndSize(font, 0.2f);
        canvas.moveText(45, doc.getPageSize().getHeight() - 45);
        PdfTextArray textArray = new PdfTextArray();
        textArray.add("P"); textArray.add("r"); textArray.add("e"); textArray.add("f");
        textArray.add("a"); textArray.add("c"); textArray.add("e"); textArray.add(" ");
        canvas.showText(textArray);

        // Write the same text again at normal size right after
        canvas.setFontAndSize(font, 10);
        canvas.showText(textArray);

        canvas.endText();
        doc.close();
        return baos.toByteArray();
    }

    // --------------------------------------------------------------------------------------------
    // Helpers - extraction
    // --------------------------------------------------------------------------------------------

    private String extractFirstPageText(byte[] pdfBytes) throws IOException {
        PdfReader reader = null;
        try {
            reader = new PdfReader(pdfBytes);
            return PdfTextExtractor.getTextFromPage(reader, FIRST_PAGE, createRenderListenerForTest());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}