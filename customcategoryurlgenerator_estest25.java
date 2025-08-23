package org.jfree.chart.urls;

import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that generateURL() returns null when the generator has no URLs
     * configured. This effectively tests the case where the requested series
     * index is out of bounds.
     */
    @Test
    public void generateURL_withEmptyGenerator_shouldReturnNull() {
        // Arrange: Create a generator with no URLs added.
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        DefaultCategoryDataset<String, String> dataset = new DefaultCategoryDataset<>();
        int seriesIndex = 0;     // Any index is out of bounds for an empty generator.
        int categoryIndex = 0;

        // Act: Attempt to generate a URL.
        String generatedUrl = generator.generateURL(dataset, seriesIndex, categoryIndex);

        // Assert: The result should be null as no URL exists for the given index.
        assertNull("Expected a null URL for a series index that does not exist.", generatedUrl);
    }
}