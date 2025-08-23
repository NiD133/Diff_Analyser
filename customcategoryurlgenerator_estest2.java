package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

/**
 * Unit tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies that the equals() method correctly identifies two generators
     * as unequal when they have been configured with different URL series.
     */
    @Test
    public void equals_shouldReturnFalse_whenUrlSeriesAreDifferent() {
        // Arrange: Create two generators with different URL series.
        // The first generator has a list containing one empty URL.
        CustomCategoryURLGenerator generatorWithUrlList = new CustomCategoryURLGenerator();
        List<String> urlList = List.of("");
        generatorWithUrlList.addURLSeries(urlList);

        // The second generator has a null list added as its series.
        CustomCategoryURLGenerator generatorWithNullList = new CustomCategoryURLGenerator();
        generatorWithNullList.addURLSeries(null);

        // Act & Assert: The generators should not be considered equal.
        // We also test for symmetry, which is a requirement of the equals() contract.
        assertNotEquals("Generators with different URL series should not be equal",
                generatorWithUrlList, generatorWithNullList);
        assertNotEquals("Symmetry check: Generators should still not be equal",
                generatorWithNullList, generatorWithUrlList);
    }
}