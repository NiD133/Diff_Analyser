package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that getURL() returns null when the requested series has been added
     * but is empty. This tests the boundary condition where a valid series index
     * is provided, but the item index is out of bounds because the series
     * contains no URLs.
     */
    @Test
    public void getURL_forItemInEmptySeries_shouldReturnNull() {
        // Arrange: Create a generator and add an empty list of URLs for a series.
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        List<String> emptyUrlSeries = new ArrayList<>();
        generator.addURLSeries(emptyUrlSeries);

        // Act: Attempt to retrieve the first URL from the first (empty) series.
        String url = generator.getURL(0, 0);

        // Assert: The returned URL should be null, as the series is empty.
        assertNull("Expected a null URL when accessing an item in an empty series.", url);
    }
}