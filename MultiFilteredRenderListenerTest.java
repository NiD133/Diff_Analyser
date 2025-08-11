package com.itextpdf.text.pdf.parser;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;

public class MultiFilteredRenderListenerTest {

    @Test
    public void testSingleRegionTextExtraction() throws IOException {
        // Load the PDF document
        PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, "test.pdf");

        // Define expected text for each region
        String[] expectedTexts = {
            "PostScript Compatibility",
            "Because the PostScript language does not support the transparent imaging \n" +
            "model, PDF 1.4 consumer applications must have some means for converting the \n" +
            "appearance of a document that uses transparency to a purely opaque description \n" +
            "for printing on PostScript output devices. Similar techniques can also be used to \n" +
            "convert such documents to a form that can be correctly viewed by PDF 1.3 and \n" +
            "earlier consumers. ",
            "Otherwise, flatten the colors to some assumed device color space with pre-\n" +
            "determined calibration. In the generated PostScript output, paint the flattened \n" +
            "colors in a CIE-based color space having that calibration."
        };

        // Define regions for text extraction
        Rectangle[] textRegions = {
            new Rectangle(90, 605, 220, 581),
            new Rectangle(80, 578, 450, 486),
            new Rectangle(103, 196, 460, 143)
        };

        // Create filters for each region
        RegionTextRenderFilter[] regionFilters = createRegionFilters(textRegions);

        // Initialize the listener and attach extraction strategies
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy[] extractionStrategies = attachExtractionStrategies(listener, regionFilters);

        // Process the first page of the PDF
        new PdfReaderContentParser(pdfReader).processContent(1, listener);

        // Verify the extracted text matches the expected text
        for (int i = 0; i < textRegions.length; i++) {
            String actualText = extractionStrategies[i].getResultantText();
            Assert.assertEquals(expectedTexts[i], actualText);
        }
    }

    @Test
    public void testMultipleFiltersForOneRegion() throws IOException {
        // Load the PDF document
        PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, "test.pdf");

        // Define regions for text extraction
        Rectangle[] textRegions = {
            new Rectangle(0, 0, 500, 650),
            new Rectangle(0, 0, 400, 400),
            new Rectangle(200, 200, 500, 600),
            new Rectangle(100, 100, 450, 400)
        };

        // Create filters for each region
        RegionTextRenderFilter[] regionFilters = createRegionFilters(textRegions);

        // Initialize the listener and attach a single extraction strategy
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy extractionStrategy = (LocationTextExtractionStrategy) listener.attachRenderListener(
            new LocationTextExtractionStrategy(), regionFilters);

        // Process the first page of the PDF
        new PdfReaderContentParser(pdfReader).processContent(1, listener);

        // Extract text using the filters and compare with expected text
        String actualText = extractionStrategy.getResultantText();
        String expectedText = PdfTextExtractor.getTextFromPage(pdfReader, 1, new FilteredTextRenderListener(new LocationTextExtractionStrategy(), regionFilters));

        Assert.assertEquals(expectedText, actualText);
    }

    /**
     * Creates an array of RegionTextRenderFilter based on the given regions.
     *
     * @param regions Array of Rectangle defining the regions.
     * @return Array of RegionTextRenderFilter for the given regions.
     */
    private RegionTextRenderFilter[] createRegionFilters(Rectangle[] regions) {
        RegionTextRenderFilter[] regionFilters = new RegionTextRenderFilter[regions.length];
        for (int i = 0; i < regions.length; i++) {
            regionFilters[i] = new RegionTextRenderFilter(regions[i]);
        }
        return regionFilters;
    }

    /**
     * Attaches LocationTextExtractionStrategy to the listener for each filter.
     *
     * @param listener MultiFilteredRenderListener to attach strategies to.
     * @param filters Array of RegionTextRenderFilter.
     * @return Array of LocationTextExtractionStrategy attached to the listener.
     */
    private LocationTextExtractionStrategy[] attachExtractionStrategies(MultiFilteredRenderListener listener, RegionTextRenderFilter[] filters) {
        LocationTextExtractionStrategy[] strategies = new LocationTextExtractionStrategy[filters.length];
        for (int i = 0; i < filters.length; i++) {
            strategies[i] = (LocationTextExtractionStrategy) listener.attachRenderListener(new LocationTextExtractionStrategy(), filters[i]);
        }
        return strategies;
    }
}