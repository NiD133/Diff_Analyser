package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains tests for the clone() and equals() contract of the {@link CustomCategoryURLGenerator} class.
 */
// The test class name and inheritance are preserved from the original EvoSuite-generated code.
public class CustomCategoryURLGenerator_ESTestTest18 extends CustomCategoryURLGenerator_ESTest_scaffolding {

    /**
     * Verifies that a cloned CustomCategoryURLGenerator is equal to the original
     * but is a separate object instance, fulfilling the general contract for clone().
     * This test includes a URL series with a null value to ensure it's handled correctly.
     */
    @Test
    public void clone_shouldProduceEqualButNotSameInstance() throws CloneNotSupportedException {
        // Arrange: Create a generator and add a URL series containing a null value.
        CustomCategoryURLGenerator originalGenerator = new CustomCategoryURLGenerator();
        List<String> urlSeriesWithNull = new ArrayList<>();
        urlSeriesWithNull.add(null);
        originalGenerator.addURLSeries(urlSeriesWithNull);

        // Act: Clone the original generator.
        CustomCategoryURLGenerator clonedGenerator = (CustomCategoryURLGenerator) originalGenerator.clone();

        // Assert: The clone should be a different instance but equal in value.
        assertNotSame("The cloned object should be a new instance.", originalGenerator, clonedGenerator);
        assertEquals("The cloned object should be equal to the original.", originalGenerator, clonedGenerator);
    }
}