package com.itextpdf.text.pdf.parser;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import junit.framework.Assert;
import org.junit.Test;
import java.io.IOException;

public class MultiFilteredRenderListenerTestTest2 {

    @Test
    public void multipleFiltersForOneRegionTest() throws IOException {
        final PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, "test.pdf");
        final Rectangle[] regions = new Rectangle[] { new Rectangle(0, 0, 500, 650), new Rectangle(0, 0, 400, 400), new Rectangle(200, 200, 500, 600), new Rectangle(100, 100, 450, 400) };
        final RegionTextRenderFilter[] regionFilters = new RegionTextRenderFilter[regions.length];
        for (int i = 0; i < regions.length; i++) regionFilters[i] = new RegionTextRenderFilter(regions[i]);
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy extractionStrategy = (LocationTextExtractionStrategy) listener.attachRenderListener(new LocationTextExtractionStrategy(), regionFilters);
        new PdfReaderContentParser(pdfReader).processContent(1, listener);
        String actualText = extractionStrategy.getResultantText();
        String expectedText = PdfTextExtractor.getTextFromPage(pdfReader, 1, new FilteredTextRenderListener(new LocationTextExtractionStrategy(), regionFilters));
        Assert.assertEquals(expectedText, actualText);
    }
}
