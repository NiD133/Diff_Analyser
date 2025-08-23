package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that getURLCount() returns 0 for a URL series that was added as null.
     * This ensures the generator correctly handles null lists without throwing an exception,
     * treating them as a series with zero URLs.
     */
    @Test
    public void getURLCount_WhenSeriesIsAddedAsNull_ShouldReturnZero() {
        // Arrange: Create a URL generator and add a null list to represent a series.
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        generator.addURLSeries(null);

        // Act: Get the URL count for the newly added series (at index 0).
        int urlCount = generator.getURLCount(0);

        // Assert: The count should be 0, as a null list contains no URLs.
        assertEquals("The URL count for a null series should be 0.", 0, urlCount);
    }
}