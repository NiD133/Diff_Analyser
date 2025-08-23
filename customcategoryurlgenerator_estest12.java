package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that getURL() returns the correct URL for the first item
     * in the first series after a URL series has been added.
     */
    @Test
    public void getURL_shouldReturnCorrectURL_forFirstSeriesFirstItem() {
        // Arrange
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        List<String> urlsForSeries0 = new ArrayList<>();
        String expectedUrl = "anchor";
        urlsForSeries0.add(expectedUrl);

        generator.addURLSeries(urlsForSeries0);

        // Act
        String actualUrl = generator.getURL(0, 0);

        // Assert
        assertEquals(expectedUrl, actualUrl);
    }
}