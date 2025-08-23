package org.jfree.chart.urls;

import org.junit.Test;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link CustomCategoryURLGenerator} class, focusing on the equals() method.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Tests that the equals() method correctly identifies two generators as unequal
     * when their URL series lists contain different content.
     */
    @Test
    public void equals_shouldReturnFalse_whenUrlSeriesHaveDifferentContent() {
        // Arrange: Create two generators that are initially identical.
        CustomCategoryURLGenerator generator1 = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator generator2 = new CustomCategoryURLGenerator();

        // Sanity check: two newly created generators should be equal.
        assertEquals("Newly created generators should be equal", generator1, generator2);

        // Arrange: Add URL series with different content to each generator.
        // One list contains a null, the other a non-null string.
        List<String> urlsForGenerator1 = Collections.singletonList(null);
        List<String> urlsForGenerator2 = Collections.singletonList("http://www.jfree.org/jfreechart/");

        generator1.addURLSeries(urlsForGenerator1);
        generator2.addURLSeries(urlsForGenerator2);

        // Act & Assert: The generators should no longer be equal.
        assertNotEquals("Generators with different URL series content should not be equal", generator1, generator2);

        // Assert: The equals method should be symmetric.
        assertNotEquals("The equals method should be symmetric", generator2, generator1);
    }
}