package com.itextpdf.text.pdf.parser;

import com.itextpdf.testutils.TestResourceUtils;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for TextRenderInfo that focus on character positioning and text extraction edge cases.
 */
public class TextRenderInfoTest {

    // Common test constants
    private static final int FIRST_PAGE = 1;
    private static final int FIRST_ELEMENT_INDEX = 0;

    // Numeric tolerances
    private static final float ONE_POINT_TOLERANCE = 1f / 72f; // ~1 point in user space
    private static final float HALF_POINT_TOLERANCE = 0.5f;

    // Using a double rotation to exercise text transforms while ending up with the original orientation.
    private static final Rectangle TEST_PAGE_SIZE = PageSize.LETTER.rotate().rotate();

    /**
     * Verifies that getCharacterRenderInfos returns character segments whose baselines are contiguous:
     * the end of the previous character equals the start of the next character (within tolerance).
     */
    @Test
    public void testCharacterRenderInfos_baselineContinuity() throws Exception {
        final byte[] bytes = createSimplePdf(TEST_PAGE_SIZE, "ABCD");

        PdfReader reader = new PdfReader(bytes);
        try {
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            parser.processContent(FIRST_PAGE, new BaselineContinuityStrategy());
        } finally {
            reader.close();
        }
    }

    /**
     * Regression test for an issue where certain Japanese characters could cause an AIOOBE
     * during text extraction. We ensure the first line of extracted text matches the expected string.
     */
    @Test
    public void testUnicodeEmptyString_noAIOOBE() throws Exception {
        final String inFile = "japanese_text.pdf";

        PdfReader reader = TestResourceUtils.getResourceAsPdfReader(this, inFile);
        try {
            TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
            String extracted = PdfTextExtractor.getTextFromPage(reader, FIRST_PAGE, strategy);

            // Compare only the first line for stability
            String actualFirstLine = extracted.substring(0, extracted.indexOf('\n'));
            String expectedFirstLine =
                    "\u76f4\u8fd1\u306e\u0053\uff06\u0050\u0035\u0030\u0030"
                    + "\u914d\u5f53\u8cb4\u65cf\u6307\u6570\u306e\u30d1\u30d5"
                    + "\u30a9\u30fc\u30de\u30f3\u30b9\u306f\u0053\uff06\u0050"
                    + "\u0035\u0030\u0030\u6307\u6570\u3092\u4e0a\u56de\u308b";

            Assert.assertEquals("Unexpected first line of extracted Japanese text",
                    expectedFirstLine, actualFirstLine);
        } finally {
            reader.close();
        }
    }

    /**
     * Ensures type3 font text baseline positions are computed correctly.
     * This validates X coordinates of the start and end points of the first baseline.
     */
    @Test
    public void testType3FontWidth_baselineCoordinates() throws Exception {
        final String inFile = "type3font_text.pdf";
        final LineSegment expectedBaseline = new LineSegment(
                new Vector(20.3246f, 769.4974f, 1.0f),
                new Vector(151.22923f, 769.4974f, 1.0f)
        );

        PdfReader reader = TestResourceUtils.getResourceAsPdfReader(this, inFile);
        try {
            TextPositionRenderListener renderListener = new TextPositionRenderListener();
            PdfContentStreamProcessor processor = new PdfContentStreamProcessor(renderListener);

            PdfDictionary pageDic = reader.getPageN(FIRST_PAGE);
            PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
            processor.processContent(ContentByteUtils.getContentBytesForPage(reader, FIRST_PAGE), resourcesDic);

            List<LineSegment> segments = renderListener.getLineSegments();
            Assert.assertFalse("No baselines collected from content stream", segments.isEmpty());

            LineSegment actualBaseline = segments.get(FIRST_ELEMENT_INDEX);

            Assert.assertEquals("Unexpected X coordinate of baseline start",
                    expectedBaseline.getStartPoint().get(FIRST_ELEMENT_INDEX),
                    actualBaseline.getStartPoint().get(FIRST_ELEMENT_INDEX),
                    HALF_POINT_TOLERANCE);

            Assert.assertEquals("Unexpected X coordinate of baseline end",
                    expectedBaseline.getEndPoint().get(FIRST_ELEMENT_INDEX),
                    actualBaseline.getEndPoint().get(FIRST_ELEMENT_INDEX),
                    HALF_POINT_TOLERANCE);
        } finally {
            reader.close();
        }
    }

    /**
     * Listener that collects baseline positions of all rendered text.
     */
    private static class TextPositionRenderListener implements RenderListener {
        private final List<LineSegment> lineSegments = new ArrayList<LineSegment>();

        List<LineSegment> getLineSegments() {
            return lineSegments;
        }

        public void beginTextBlock() {
            // no-op
        }

        public void renderText(TextRenderInfo renderInfo) {
            lineSegments.add(renderInfo.getBaseline());
        }

        public void endTextBlock() {
            // no-op
        }

        public void renderImage(ImageRenderInfo renderInfo) {
            // no-op
        }
    }

    /**
     * TextExtractionStrategy that asserts baseline continuity at a character level:
     * for each pair of consecutive characters, the previous baseline end equals the next baseline start.
     */
    private static class BaselineContinuityStrategy implements TextExtractionStrategy {

        public void beginTextBlock() {
            // no-op
        }

        public void renderText(TextRenderInfo renderInfo) {
            List<TextRenderInfo> chars = renderInfo.getCharacterRenderInfos();
            if (chars == null || chars.isEmpty()) {
                return; // nothing to validate
            }

            TextRenderInfo previous = chars.get(0);
            for (int i = 1; i < chars.size(); i++) {
                TextRenderInfo current = chars.get(i);

                Vector previousEnd = previous.getBaseline().getEndPoint();
                Vector currentStart = current.getBaseline().getStartPoint();

                assertSamePosition(
                        "Character '" + current.getText() + "' does not start at the end of the previous character",
                        previousEnd, currentStart, ONE_POINT_TOLERANCE);

                previous = current;
            }
        }

        public void endTextBlock() {
            // no-op
        }

        public void renderImage(ImageRenderInfo renderInfo) {
            // no-op
        }

        public String getResultantText() {
            // Not used by these tests; returning empty string avoids nulls.
            return "";
        }

        private static void assertSamePosition(String message, Vector expected, Vector actual, float tolerance) {
            Assert.assertEquals(message + " (X mismatch)", expected.get(0), actual.get(0), tolerance);
            Assert.assertEquals(message + " (Y mismatch)", expected.get(1), actual.get(1), tolerance);
        }
    }

    /**
     * Creates a simple PDF where each string is placed on a separate page.
     */
    private static byte[] createSimplePdf(Rectangle pageSize, String... pagesText) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        Document document = new Document(pageSize);
        PdfWriter.getInstance(document, byteStream);

        document.open();
        for (String text : pagesText) {
            document.add(new Paragraph(text));
            document.newPage();
        }
        document.close();

        return byteStream.toByteArray();
    }
}