package com.itextpdf.text.pdf.parser;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests the functionality of the MultiFilteredRenderListener to ensure it can
 * delegate to multiple listeners, each with its own region-based filter.
 */
public class MultiFilteredRenderListenerTest {

    /**
     * Verifies that text can be extracted from several distinct rectangular regions
     * of a PDF page in a single parsing operation.
     */
    @Test
    public void testExtractsTextFromMultipleDistinctRegions() throws IOException {
        // --- ARRANGE ---

        // 1. Define the test data: regions and their corresponding expected text.
        final String PDF_RESOURCE = "test.pdf";

        // Define the rectangular regions on the PDF page to extract text from.
        final Rectangle[] regions = {
                // Region 1: A title
                new Rectangle(90, 605, 220, 581),
                // Region 2: The first main paragraph
                new Rectangle(80, 578, 450, 486),
                // Region 3: A paragraph near the bottom
                new Rectangle(103, 196, 460, 143)
        };

        // Define the expected text content for each corresponding region.
        final String[] expectedTexts = {
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

        // 2. Set up the listeners and filters.
        final PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, PDF_RESOURCE);
        final MultiFilteredRenderListener multiListener = new MultiFilteredRenderListener();
        final List<LocationTextExtractionStrategy> strategies = new ArrayList<>();

        // For each region, create a filter and a strategy, then attach them to the multi-listener.
        // This consolidates the setup logic, making the relationship between a region,
        // its filter, and its extraction strategy clear.
        for (Rectangle region : regions) {
            RegionTextRenderFilter regionFilter = new RegionTextRenderFilter(region);
            LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();
            multiListener.attachRenderListener(strategy, regionFilter);
            strategies.add(strategy);
        }

        // --- ACT ---

        // Process the first page of the PDF. The multiListener will farm out events
        // to the appropriate strategies based on the region filters.
        PdfReaderContentParser parser = new PdfReaderContentParser(pdfReader);
        parser.processContent(1, multiListener);


        // --- ASSERT ---

        // Verify that each strategy extracted the correct text from its assigned region.
        Assert.assertEquals("Number of strategies should match number of regions.", regions.length, strategies.size());
        for (int i = 0; i < strategies.size(); i++) {
            String actualText = strategies.get(i).getResultantText();
            String expectedText = expectedTexts[i];
            String assertMessage = String.format("Text from region %d did not match.", i);
            Assert.assertEquals(assertMessage, expectedText, actualText);
        }
    }
}