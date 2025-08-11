package com.itextpdf.text.pdf.parser;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for MultiFilteredRenderListener that aim to be explicit about:
 * - what is being extracted (regions),
 * - how listeners/filters are wired,
 * - and what the expected results are.
 */
public class MultiFilteredRenderListenerTest {

    private static final String TEST_PDF = "test.pdf";
    private static final int PAGE_NUMBER = 1;

    private PdfReader pdfReader;

    @Before
    public void setUp() throws IOException {
        pdfReader = TestResourceUtils.getResourceAsPdfReader(this, TEST_PDF);
    }

    @After
    public void tearDown() {
        if (pdfReader != null) {
            pdfReader.close();
        }
    }

    /**
     * Verifies that:
     * - Each region gets its own LocationTextExtractionStrategy instance,
     * - Each strategy is invoked only for text passing its corresponding filter,
     * - The extracted text matches the known content for those regions.
     */
    @Test
    public void extractsTextFromMultipleIndependentRegions() throws IOException {
        // Expected text per region (taken from the known content of test.pdf on page 1).
        final String[] expectedTexts = new String[] {
                "PostScript Compatibility",
                "Because the PostScript language does not support the transparent imaging \n" +
                        "model, PDF 1.4 consumer applications must have some means for converting the \n" +
                        "appearance of a document that uses transparency to a purely opaque description \n" +
                        "for printing on PostScript output devices. Similar techniques can also be used to \n" +
                        "convert such documents to a form that can be correctly viewed by PDF 1.3 and \n" +
                        "earlier consumers. ",
                "Otherwise, flatten the colors to some assumed device color space with pre-\n" +
                        "determined calibration. In the generated PostScript output, paint the flattened \n" +
                        "colors in a CIE-based color space having that calibration. "
        };

        // Regions correspond to: title, first paragraph, and a later paragraph.
        final Rectangle[] regions = new Rectangle[] {
                new Rectangle(90, 605, 220, 581),
                new Rectangle(80, 578, 450, 486),
                new Rectangle(103, 196, 460, 143)
        };

        final RegionTextRenderFilter[] filters = createRegionFilters(regions);

        // Wire one extraction strategy per region/filter.
        final MultiFilteredRenderListener multiListener = new MultiFilteredRenderListener();
        final LocationTextExtractionStrategy[] strategies =
                attachOneStrategyPerFilter(multiListener, filters);

        // Execute parsing.
        new PdfReaderContentParser(pdfReader).processContent(PAGE_NUMBER, multiListener);

        // Validate extracted text per region.
        for (int i = 0; i < regions.length; i++) {
            String actual = strategies[i].getResultantText();
            assertEquals("Text mismatch for region index " + i, expectedTexts[i], actual);
        }
    }

    /**
     * Verifies that:
     * - Attaching a single strategy with multiple filters to MultiFilteredRenderListener
     *   yields the same result as using a FilteredTextRenderListener with the same filters.
     */
    @Test
    public void singleStrategyWithMultipleFilters_matchesFilteredTextRenderListener() throws IOException {
        final Rectangle[] regions = new Rectangle[] {
                new Rectangle(0, 0, 500, 650),
                new Rectangle(0, 0, 400, 400),
                new Rectangle(200, 200, 500, 600),
                new Rectangle(100, 100, 450, 400)
        };
        final RegionTextRenderFilter[] filters = createRegionFilters(regions);

        // MultiFilteredRenderListener: attach a single strategy with all filters.
        final MultiFilteredRenderListener multiListener = new MultiFilteredRenderListener();
        final LocationTextExtractionStrategy strategyUnderTest =
                attachSingleStrategy(multiListener, filters);

        new PdfReaderContentParser(pdfReader).processContent(PAGE_NUMBER, multiListener);
        final String actual = strategyUnderTest.getResultantText();

        // Reference: FilteredTextRenderListener with the same filters.
        final String expected = PdfTextExtractor.getTextFromPage(
                pdfReader,
                PAGE_NUMBER,
                new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filters)
        );

        assertEquals("Extracted text should match FilteredTextRenderListener output", expected, actual);
    }

    // Helpers

    private static RegionTextRenderFilter[] createRegionFilters(Rectangle[] regions) {
        RegionTextRenderFilter[] filters = new RegionTextRenderFilter[regions.length];
        for (int i = 0; i < regions.length; i++) {
            filters[i] = new RegionTextRenderFilter(regions[i]);
        }
        return filters;
    }

    private static LocationTextExtractionStrategy[] attachOneStrategyPerFilter(
            MultiFilteredRenderListener listener,
            RegionTextRenderFilter[] filters
    ) {
        LocationTextExtractionStrategy[] strategies = new LocationTextExtractionStrategy[filters.length];
        for (int i = 0; i < filters.length; i++) {
            strategies[i] = (LocationTextExtractionStrategy)
                    listener.attachRenderListener(new LocationTextExtractionStrategy(), filters[i]);
        }
        return strategies;
    }

    private static LocationTextExtractionStrategy attachSingleStrategy(
            MultiFilteredRenderListener listener,
            RenderFilter... filters
    ) {
        return (LocationTextExtractionStrategy)
                listener.attachRenderListener(new LocationTextExtractionStrategy(), filters);
    }
}