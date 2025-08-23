package org.jfree.chart.urls;

import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link CustomCategoryURLGenerator} class, focusing on cloning behavior.
 */
class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that cloning a CustomCategoryURLGenerator creates a deep,
     * independent copy. Changes to the original object after cloning should not
     * affect the clone.
     */
    @Test
    void clone_shouldCreateIndependentCopy() throws CloneNotSupportedException {
        // Arrange: Create a generator and add an initial series of URLs.
        CustomCategoryURLGenerator original = new CustomCategoryURLGenerator();
        List<String> initialUrlSeries = new ArrayList<>();
        initialUrlSeries.add("https://example.com/series1/item1");
        initialUrlSeries.add("https://example.com/series1/item2");
        original.addURLSeries(initialUrlSeries);

        // Act: Clone the original generator.
        CustomCategoryURLGenerator clone = CloneUtils.clone(original);

        // Assert: The clone should be a new instance but logically equal to the original.
        assertNotSame(original, clone, "The clone must be a different object instance.");
        assertEquals(original, clone, "The clone should be equal to the original before any modifications.");

        // Act: Modify the original generator by adding a new URL series.
        List<String> additionalUrlSeries = new ArrayList<>();
        additionalUrlSeries.add("https://example.com/series2/item1");
        original.addURLSeries(additionalUrlSeries);

        // Assert: The modification to the original should not affect the clone.
        assertNotEquals(original, clone, "Modifying the original should not impact the clone, proving independence.");
    }
}