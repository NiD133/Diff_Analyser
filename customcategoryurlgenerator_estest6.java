package org.jfree.chart.urls;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that getURL() returns null when the requested series index is
     * out of bounds (i.e., greater than or equal to the number of series added).
     */
    @Test
    public void getURL_whenSeriesIndexIsOutOfBounds_shouldReturnNull() {
        // Arrange: Create a generator and add a single series of URLs.
        // This establishes a known state where the only valid series index is 0.
        CustomCategoryURLGenerator urlGenerator = new CustomCategoryURLGenerator();
        urlGenerator.addURLSeries(List.of("http://example.com/item0"));

        // Define an index that is one greater than the highest valid index.
        int outOfBoundsSeriesIndex = 1;
        int anyItemIndex = 0; // The item index is irrelevant if the series index is invalid.

        // Act: Attempt to retrieve a URL using the out-of-bounds series index.
        String actualUrl = urlGenerator.getURL(outOfBoundsSeriesIndex, anyItemIndex);

        // Assert: The result should be null because the requested series does not exist.
        assertNull("getURL should return null for an out-of-bounds series index.", actualUrl);
    }
}