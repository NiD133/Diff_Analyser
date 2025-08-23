package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This test suite verifies the behavior of the {@link CustomCategoryURLGenerator} class,
 * focusing on its cloning and equality logic.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Tests that a cloned CustomCategoryURLGenerator is considered equal to the original object,
     * but is not the same instance. This verifies the correctness of both the clone() and equals() methods.
     */
    @Test
    public void equals_onClonedObject_returnsTrue() throws CloneNotSupportedException {
        // Arrange: Create a generator and add a list of URLs. [1, 2, 3]
        CustomCategoryURLGenerator originalGenerator = new CustomCategoryURLGenerator();
        List<String> urlSeries = new ArrayList<>();
        urlSeries.add("https://www.jfree.org/jfreechart/series1-url1");
        urlSeries.add("https://www.jfree.org/jfreechart/series1-url2");
        originalGenerator.addURLSeries(urlSeries);

        // Act: Clone the original generator. [4]
        CustomCategoryURLGenerator clonedGenerator = (CustomCategoryURLGenerator) originalGenerator.clone();

        // Assert: The cloned object should be equal to the original, but not the same instance. [5]
        // Verifies that the clone has the same content as the original.
        assertEquals(originalGenerator, clonedGenerator);

        // Verifies that the clone is a separate object in memory.
        assertNotSame(originalGenerator, clonedGenerator);
    }
}