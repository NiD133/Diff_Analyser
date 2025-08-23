package com.itextpdf.text.pdf.parser;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Test suite for {@link MultiFilteredRenderListener}.
 */
// Renamed class for clarity and correctness.
public class MultiFilteredRenderListenerTest {

    private static final String TEST_PDF = "test.pdf";
    private static final int PAGE_NUMBER = 1;

    /**
     * Verifies that attaching a single listener with multiple region filters to a
     * {@link MultiFilteredRenderListener} behaves identically to using a standard
     * {@link FilteredTextRenderListener} with the same set of filters.
     * <p>
     * This effectively tests that the MultiFilteredRenderListener correctly applies an 'AND'
     * condition to all provided filters, extracting text only from the geometric intersection
     * of all specified regions.
     */
    @Test
    public void shouldExtractTextFromIntersectionOfMultipleRegions() throws IOException {
        // --- ARRANGE ---
        PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, TEST_PDF);

        // Define a set of overlapping regions. Text should only be extracted if it falls
        // within the intersection of ALL these regions.
        List<Rectangle> overlappingRegions = Arrays.asList(
                new Rectangle(0, 0, 500, 650),
                new Rectangle(0, 0, 400, 400),
                new Rectangle(200, 200, 500, 600),
                new Rectangle(100, 100, 450, 400)
        );

        // Create a corresponding filter for each region using a modern, declarative style.
        RenderFilter[] regionFilters = overlappingRegions.stream()
                .map(RegionTextRenderFilter::new)
                .toArray(RenderFilter[]::new);

        // The "expected" result is obtained using a standard FilteredTextRenderListener,
        // which serves as the behavioral baseline for this test.
        String expectedText = getExpectedTextWithStandardFilter(pdfReader, PAGE_NUMBER, regionFilters);

        // --- ACT ---
        // Configure the MultiFilteredRenderListener (the class under test) with the same filters.
        MultiFilteredRenderListener multiFilteredRenderListener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy actualStrategy = multiFilteredRenderListener.attachRenderListener(
                new LocationTextExtractionStrategy(),
                regionFilters
        );

        // Process the PDF content to trigger the listeners.
        new PdfReaderContentParser(pdfReader).processContent(PAGE_NUMBER, multiFilteredRenderListener);
        String actualText = actualStrategy.getResultantText();

        // --- ASSERT ---
        // Verify that the behavior of the class under test matches the baseline.
        Assert.assertEquals("Text extracted using MultiFilteredRenderListener should match the baseline from FilteredTextRenderListener.",
                expectedText, actualText);
    }

    /**
     * Extracts text from a specific page and set of regions using the standard
     * {@link FilteredTextRenderListener}. This provides the "golden" or expected
     * result for the test, acting as a baseline for correct behavior.
     *
     * @param reader    The PdfReader for the document.
     * @param pageNum   The page number to extract text from.
     * @param filters   The filters to apply, which define the extraction area.
     * @return The extracted text.
     * @throws IOException if an I/O error occurs during text extraction.
     */
    private String getExpectedTextWithStandardFilter(PdfReader reader, int pageNum, RenderFilter... filters) throws IOException {
        // A FilteredTextRenderListener can be used as a TextExtractionStrategy directly.
        TextExtractionStrategy standardFilteredStrategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filters);
        return PdfTextExtractor.getTextFromPage(reader, pageNum, standardFilteredStrategy);
    }
}