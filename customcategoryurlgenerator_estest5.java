package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that getURL() returns null when the item index is out of bounds
     * for a given series.
     */
    @Test
    public void getURL_withOutOfBoundsItemIndex_shouldReturnNull() {
        // Arrange: Create a generator and add a series with an empty list of URLs.
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        List<String> emptyUrlList = new ArrayList<>();
        generator.addURLSeries(emptyUrlList);

        // Act: Attempt to retrieve a URL for an item index that does not exist.
        // The series index (0) is valid, but the item index (1) is out of bounds
        // for the empty list.
        String retrievedUrl = generator.getURL(0, 1);

        // Assert: The returned URL should be null.
        assertNull("Expected a null URL for an out-of-bounds item index", retrievedUrl);
    }
}