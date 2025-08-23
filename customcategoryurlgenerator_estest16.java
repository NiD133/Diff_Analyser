package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that a newly instantiated CustomCategoryURLGenerator has an initial list count of zero.
     */
    @Test
    public void getListCount_shouldReturnZero_whenNewlyCreated() {
        // Arrange: Create a new instance of the URL generator.
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();

        // Act: Get the number of URL lists from the new instance.
        int listCount = generator.getListCount();

        // Assert: The count should be 0, as no URL series have been added yet.
        assertEquals(0, listCount);
    }
}