package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * Contains tests for the equals() method of the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGenerator_ESTestTest3 extends CustomCategoryURLGenerator_ESTest_scaffolding {

    /**
     * Verifies that two CustomCategoryURLGenerator instances are not equal
     * if one contains a URL series and the other is empty.
     */
    @Test
    public void equals_shouldReturnFalseForGeneratorsWithDifferentContent() {
        // Arrange: Create a generator and add a list of URLs to it.
        CustomCategoryURLGenerator generatorWithURLs = new CustomCategoryURLGenerator();
        List<String> urlSeries = List.of("series1_url1.html", "series1_url2.html");
        generatorWithURLs.addURLSeries(urlSeries);

        // Arrange: Create a second, empty generator.
        CustomCategoryURLGenerator emptyGenerator = new CustomCategoryURLGenerator();

        // Act & Assert: The two generators should not be equal because their content differs.
        // We also check for symmetry, a key part of the equals() contract.
        assertNotEquals("A generator with URLs should not be equal to an empty one.", generatorWithURLs, emptyGenerator);
        assertNotEquals("Symmetry check: An empty generator should not be equal to one with URLs.", emptyGenerator, generatorWithURLs);
    }
}