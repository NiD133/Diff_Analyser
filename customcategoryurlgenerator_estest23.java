package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that getURL returns null for a series that was added as a null list.
     * This tests the generator's ability to handle cases where a specific series
     * has no associated URLs.
     */
    @Test
    public void getURL_whenSeriesUrlListIsNull_shouldReturnNull() {
        // Arrange: Create a URL generator and add a null list for the first series.
        CustomCategoryURLGenerator urlGenerator = new CustomCategoryURLGenerator();
        urlGenerator.addURLSeries(null);

        // Act: Attempt to retrieve the URL for an item in that series.
        String retrievedUrl = urlGenerator.getURL(0, 0);

        // Assert: The returned URL should be null.
        assertNull("The URL should be null for an item in a series that was added as a null list.", retrievedUrl);
    }
}