package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that getURL() retrieves the correct URL for a specific series and
     * item index after the URL series has been added.
     */
    @Test
    public void getURLShouldReturnCorrectURLForAddedSeries() {
        // Arrange: Create a generator and define a list of URLs for a series.
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        String expectedUrl = "http://example.com/item0";
        List<String> urlSeries = Arrays.asList(expectedUrl);

        // Act: Add the URL series and then retrieve the URL for the first item.
        generator.addURLSeries(urlSeries);
        String actualUrl = generator.getURL(0, 0);

        // Assert: The retrieved URL should match the one that was added.
        assertEquals("The retrieved URL should match the one added for the specified series and item.",
                     expectedUrl, actualUrl);
    }
}