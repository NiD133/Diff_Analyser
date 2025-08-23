package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * Tests for the equals() method in the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that two CustomCategoryURLGenerator instances are not equal
     * if they contain different URL series.
     */
    @Test
    public void equals_shouldReturnFalse_whenURLSeriesAreDifferent() {
        // Arrange: Create two identical generators.
        CustomCategoryURLGenerator generator1 = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator generator2 = new CustomCategoryURLGenerator();

        // Assert that they are equal when newly created.
        assertEquals("Two new generators should be equal", generator1, generator2);

        // Act: Add a different list of URLs to each generator.
        List<String> urlSeries1 = List.of("http://example.com/series-a-url");
        generator1.addURLSeries(urlSeries1);

        List<String> urlSeries2 = List.of("http://example.com/series-b-url");
        generator2.addURLSeries(urlSeries2);

        // Assert: The generators should no longer be equal.
        assertNotEquals("Generators with different URL series should not be equal", generator1, generator2);
    }
}