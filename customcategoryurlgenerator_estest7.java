package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.List;

/**
 * Tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that getURLCount() returns the correct number of URLs
     * for a specific series that has been added.
     */
    @Test
    public void getURLCount_shouldReturnCorrectSizeForAddedSeries() {
        // Arrange: Create a URL generator and add a list of URLs as the first series.
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        List<String> urlSeries = List.of(
            "details.html?id=1",
            "details.html?id=2",
            "details.html?id=3",
            "details.html?id=4",
            "details.html?id=5",
            "details.html?id=6"
        );
        generator.addURLSeries(urlSeries);

        // Act: Get the URL count for the first series (at index 0).
        int actualUrlCount = generator.getURLCount(0);

        // Assert: The returned count should match the size of the list that was added.
        int expectedUrlCount = urlSeries.size();
        assertEquals(expectedUrlCount, actualUrlCount);
    }
}