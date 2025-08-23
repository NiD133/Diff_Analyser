package org.jfree.chart.urls;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;

/**
 * Unit tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that generateURL() throws an IndexOutOfBoundsException when the
     * series index is negative. The generator has no URLs configured, so any
     * attempt to access an index will be out of bounds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void generateURLShouldThrowExceptionForNegativeSeriesIndex() {
        // Arrange: Create a URL generator and a dataset.
        CustomCategoryURLGenerator urlGenerator = new CustomCategoryURLGenerator();
        CategoryDataset dataset = new DefaultCategoryDataset();
        int negativeSeriesIndex = -1;
        int categoryIndex = 0; // This value is arbitrary for this test case.

        // Act: Call generateURL with an invalid negative index.
        // The assertion is that this line throws the expected exception.
        urlGenerator.generateURL(dataset, negativeSeriesIndex, categoryIndex);
    }
}