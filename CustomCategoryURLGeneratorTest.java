package org.jfree.chart.urls;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Tests the equality of two CustomCategoryURLGenerator instances.
     */
    @Test
    public void testEquals() {
        CustomCategoryURLGenerator generator1 = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator generator2 = new CustomCategoryURLGenerator();
        
        // Initially, two new instances should be equal
        assertEquals(generator1, generator2);

        // Add URL series to the first generator
        List<String> urls1 = List.of("URL A1", "URL A2", "URL A3");
        generator1.addURLSeries(urls1);

        // Now, the generators should not be equal
        assertNotEquals(generator1, generator2);

        // Add the same URL series to the second generator
        List<String> urls2 = List.of("URL A1", "URL A2", "URL A3");
        generator2.addURLSeries(urls2);

        // The generators should be equal again
        assertEquals(generator1, generator2);
    }

    /**
     * Tests the cloning functionality of CustomCategoryURLGenerator.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        CustomCategoryURLGenerator original = new CustomCategoryURLGenerator();
        List<String> urls = List.of("URL A1", "URL A2", "URL A3");
        original.addURLSeries(urls);

        // Clone the original generator
        CustomCategoryURLGenerator clone = CloneUtils.clone(original);

        // Verify that the clone is a separate instance but equal to the original
        assertNotSame(original, clone);
        assertEquals(original, clone);

        // Modify the original and verify that the clone remains unchanged
        original.addURLSeries(List.of("URL XXX"));
        assertNotEquals(original, clone);

        // Modify the clone to match the original again
        clone.addURLSeries(List.of("URL XXX"));
        assertEquals(original, clone);
    }

    /**
     * Verifies that CustomCategoryURLGenerator implements PublicCloneable.
     */
    @Test
    public void testPublicCloneable() {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        assertTrue(generator instanceof PublicCloneable);
    }

    /**
     * Tests the serialization and deserialization of CustomCategoryURLGenerator.
     */
    @Test
    public void testSerialization() {
        List<String> urls1 = List.of("URL A1", "URL A2", "URL A3");
        List<String> urls2 = List.of("URL B1", "URL B2", "URL B3");

        CustomCategoryURLGenerator original = new CustomCategoryURLGenerator();
        original.addURLSeries(urls1);
        original.addURLSeries(urls2);

        // Serialize and deserialize the generator
        CustomCategoryURLGenerator deserialized = TestUtils.serialised(original);

        // Verify that the deserialized instance is equal to the original
        assertEquals(original, deserialized);
    }

    /**
     * Tests the addURLSeries() method of CustomCategoryURLGenerator.
     */
    @Test
    public void testAddURLSeries() {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();

        // Adding a null list should increase the list count but not the URL count
        generator.addURLSeries(null);
        assertEquals(1, generator.getListCount());
        assertEquals(0, generator.getURLCount(0));

        // Add a non-null list and verify the counts
        List<String> urls = new ArrayList<>();
        urls.add("URL1");
        generator.addURLSeries(urls);
        assertEquals(2, generator.getListCount());
        assertEquals(0, generator.getURLCount(0));
        assertEquals(1, generator.getURLCount(1));
        assertEquals("URL1", generator.getURL(1, 0));

        // Ensure that modifying the original list does not affect the generator
        urls.clear();
        assertEquals("URL1", generator.getURL(1, 0));
    }
}