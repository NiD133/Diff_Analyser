package org.jfree.chart.urls;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that calling getURL() with a negative item index throws an
     * IndexOutOfBoundsException, as expected.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getURL_withNegativeItemIndex_throwsIndexOutOfBoundsException() {
        // Arrange: Create a generator and add one empty list of URLs.
        // This sets up the scenario where the series index is valid, but the
        // item index will be invalid.
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        List<String> emptyUrlSeries = new ArrayList<>();
        generator.addURLSeries(emptyUrlSeries);

        // Act: Attempt to retrieve a URL with a valid series index (0) but an
        // invalid, negative item index (-1).
        generator.getURL(0, -1);

        // Assert: The test framework will automatically assert that an
        // IndexOutOfBoundsException is thrown. If no exception or a different
        // one is thrown, the test will fail.
    }
}