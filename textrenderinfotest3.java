package com.itextpdf.text.pdf.parser;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the calculation of text positioning, specifically for Type 3 fonts.
 */
public class TextRenderInfoType3FontTest {

    private static final int FIRST_PAGE = 1;
    private static final String TYPE3_FONT_TEST_PDF = "type3font_text.pdf";

    // Expected coordinates for the baseline of the text in the test PDF.
    private static final float EXPECTED_START_X = 20.3246f;
    private static final float EXPECTED_END_X = 151.22923f;
    private static final float EXPECTED_Y = 769.4974f;
    private static final float COORDINATE_TOLERANCE = 0.5f;

    /**
     * A simple RenderListener that captures the baseline of each text chunk.
     */
    private static class TextBaselineRenderListener implements RenderListener {

        private final List<LineSegment> baselines = new ArrayList<>();

        public List<LineSegment> getBaselines() {
            return baselines;
        }

        @Override
        public void renderText(TextRenderInfo renderInfo) {
            baselines.add(renderInfo.getBaseline());
        }

        @Override
        public void beginTextBlock() {
        }

        @Override
        public void endTextBlock() {
        }

        @Override
        public void renderImage(ImageRenderInfo renderInfo) {
        }
    }

    @Test
    public void getBaseline_ForType3Font_ShouldReturnCorrectCoordinates() throws Exception {
        // Arrange
        PdfReader reader = TestResourceUtils.getResourceAsPdfReader(this, TYPE3_FONT_TEST_PDF);
        TextBaselineRenderListener listener = new TextBaselineRenderListener();
        PdfContentStreamProcessor processor = new PdfContentStreamProcessor(listener);

        PdfDictionary pageDictionary = reader.getPageN(FIRST_PAGE);
        PdfDictionary resourceDictionary = pageDictionary.getAsDict(PdfName.RESOURCES);

        // Act
        processor.processContent(ContentByteUtils.getContentBytesForPage(reader, FIRST_PAGE), resourceDictionary);
        List<LineSegment> capturedBaselines = listener.getBaselines();

        // Assert
        Assert.assertFalse("No text baselines were captured from the PDF.", capturedBaselines.isEmpty());

        LineSegment firstBaseline = capturedBaselines.get(0);
        Vector startPoint = firstBaseline.getStartPoint();
        Vector endPoint = firstBaseline.getEndPoint();

        Assert.assertEquals("Baseline start X-coordinate is incorrect.",
                EXPECTED_START_X, startPoint.get(Vector.I1), COORDINATE_TOLERANCE);
        Assert.assertEquals("Baseline start Y-coordinate is incorrect.",
                EXPECTED_Y, startPoint.get(Vector.I2), COORDINATE_TOLERANCE);

        Assert.assertEquals("Baseline end X-coordinate is incorrect.",
                EXPECTED_END_X, endPoint.get(Vector.I1), COORDINATE_TOLERANCE);
        Assert.assertEquals("Baseline end Y-coordinate is incorrect.",
                EXPECTED_Y, endPoint.get(Vector.I2), COORDINATE_TOLERANCE);
    }
}