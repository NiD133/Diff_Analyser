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
     * Verifies that getListCount() correctly reports the number of URL series
     * that have been added.
     */
    @Test
    public void getListCount_shouldReturnOne_whenOneURLSeriesIsAdded() {
        // Arrange: Create a new URL generator and a list for URLs.
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        List<String> urlSeries = new ArrayList<>();

        // Act: Add the (empty) list of URLs to the generator.
        generator.addURLSeries(urlSeries);
        int listCount = generator.getListCount();

        // Assert: The generator should now report that it contains one list.
        assertEquals("The list count should be 1 after adding one URL series.", 1, listCount);
    }
}