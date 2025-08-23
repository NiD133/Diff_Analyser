package org.jfree.chart.urls;

import org.junit.Test;

/**
 * Tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that calling getURLCount() with an invalid index on a newly
     * created (and thus empty) generator throws an IndexOutOfBoundsException.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getURLCountOnEmptyGeneratorShouldThrowException() {
        // Arrange: Create a new generator, which contains no URL series lists.
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();

        // Act & Assert: Attempting to get the URL count for any series index
        // should fail. We use index 0 as the simplest boundary case.
        generator.getURLCount(0);
    }
}