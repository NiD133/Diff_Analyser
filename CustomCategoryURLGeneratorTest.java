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
     * Tests the equality of two {@link CustomCategoryURLGenerator} instances.
     */
    @Test
    public void testEquals() {
        CustomCategoryURLGenerator generator1 = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator generator2 = new CustomCategoryURLGenerator();
        
        // Initially, two new generators should be equal
        assertEquals(generator1, generator2);

        // Add URL series to generator1 and check inequality
        List<String> urls1 = List.of("URL A1", "URL A2", "URL A3");
        generator1.addURLSeries(urls1);
        assertNotEquals(generator1, generator2);

        // Add the same URL series to generator2 and check equality
        List<String> urls2 = List.of("URL A1", "URL A2", "URL A3");
        generator2.addURLSeries(urls2);
        assertEquals(generator1, generator2);
    }

    /**
     * Tests the cloning functionality of {@link CustomCategoryURLGenerator}.
     * 
     * @throws CloneNotSupportedException if cloning is not supported
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        CustomCategoryURLGenerator originalGenerator = new CustomCategoryURLGenerator();
        List<String> urls = List.of("URL A1", "URL A2", "URL A3");
        originalGenerator.addURLSeries(urls);

        // Clone the original generator
        CustomCategoryURLGenerator clonedGenerator = CloneUtils.clone(originalGenerator);

        // Verify that the clone is a separate instance but equal to the original
        assertNotSame(originalGenerator, clonedGenerator);
        assertSame(originalGenerator.getClass(), clonedGenerator.getClass());
        assertEquals(originalGenerator, clonedGenerator);

        // Modify the original and ensure the clone remains unchanged
        List<String> newUrls = List.of("URL XXX");
        originalGenerator.addURLSeries(newUrls);
        assertNotEquals(originalGenerator, clonedGenerator);

        // Add the same new URLs to the clone and check equality
        clonedGenerator.addURLSeries(new ArrayList<>(newUrls));
        assertEquals(originalGenerator, clonedGenerator);
    }

    /**
     * Tests that {@link CustomCategoryURLGenerator} implements {@link PublicCloneable}.
     */
    @Test
    public void testPublicCloneable() {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        assertTrue(generator instanceof PublicCloneable);
    }

    /**
     * Tests serialization and deserialization of {@link CustomCategoryURLGenerator}.
     */
    @Test
    public void testSerialization() {
        List<String> urls1 = List.of("URL A1", "URL A2", "URL A3");
        List<String> urls2 = List.of("URL B1", "URL B2", "URL B3");

        CustomCategoryURLGenerator originalGenerator = new CustomCategoryURLGenerator();
        originalGenerator.addURLSeries(urls1);
        originalGenerator.addURLSeries(urls2);

        // Serialize and deserialize the generator
        CustomCategoryURLGenerator deserializedGenerator = TestUtils.serialised(originalGenerator);

        // Verify that the deserialized generator is equal to the original
        assertEquals(originalGenerator, deserializedGenerator);
    }

    /**
     * Tests the {@link CustomCategoryURLGenerator#addURLSeries(List)} method.
     */
    @Test
    public void testAddURLSeries() {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();

        // Adding a null list should result in an empty series
        generator.addURLSeries(null);
        assertEquals(1, generator.getListCount());
        assertEquals(0, generator.getURLCount(0));

        // Add a list with one URL and verify
        List<String> singleUrlList = new ArrayList<>();
        singleUrlList.add("URL1");
        generator.addURLSeries(singleUrlList);
        assertEquals(2, generator.getListCount());
        assertEquals(0, generator.getURLCount(0));
        assertEquals(1, generator.getURLCount(1));
        assertEquals("URL1", generator.getURL(1, 0));

        // Ensure the generator is not affected by changes to the original list
        singleUrlList.clear();
        assertEquals("URL1", generator.getURL(1, 0));
    }
}