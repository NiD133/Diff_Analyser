package org.jfree.chart.urls;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Readable and maintainable tests for CustomCategoryURLGenerator.
 */
@DisplayName("CustomCategoryURLGenerator")
public class CustomCategoryURLGeneratorTest {

    private static List<String> mutableList(String... urls) {
        List<String> list = new ArrayList<>();
        for (String url : urls) {
            list.add(url);
        }
        return list;
    }

    private static CustomCategoryURLGenerator generatorWithSeries(List<String>... series) {
        CustomCategoryURLGenerator gen = new CustomCategoryURLGenerator();
        for (List<String> s : series) {
            gen.addURLSeries(s);
        }
        return gen;
    }

    @Test
    @DisplayName("equals(): two generators are equal when their URL series match")
    public void testEquals() {
        CustomCategoryURLGenerator g1 = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator g2 = new CustomCategoryURLGenerator();
        assertEquals(g1, g2, "Empty generators should be equal");

        List<String> seriesA = mutableList("URL A1", "URL A2", "URL A3");
        g1.addURLSeries(seriesA);
        assertNotEquals(g1, g2, "Adding a series should break equality");

        // Add an equivalent series to g2 to restore equality
        List<String> seriesA_copy = mutableList("URL A1", "URL A2", "URL A3");
        g2.addURLSeries(seriesA_copy);
        assertEquals(g1, g2, "Generators with the same series should be equal");
    }

    @Test
    @DisplayName("clone(): creates an independent deep copy")
    public void testCloning() throws CloneNotSupportedException {
        List<String> seriesA = mutableList("URL A1", "URL A2", "URL A3");
        CustomCategoryURLGenerator original = generatorWithSeries(seriesA);

        CustomCategoryURLGenerator clone = CloneUtils.clone(original);
        assertNotSame(original, clone, "Clone should be a different instance");
        assertSame(original.getClass(), clone.getClass(), "Clone should have the same type");
        assertEquals(original, clone, "Clone should be equal to the original");

        // Verify independence: changing one should not affect the other
        List<String> newSeries = mutableList("URL XXX");
        original.addURLSeries(newSeries);
        assertNotEquals(original, clone, "After mutation, clone should no longer be equal");

        clone.addURLSeries(mutableList("URL XXX"));
        assertEquals(original, clone, "Clones should match again after equivalent mutation");
    }

    @Test
    @DisplayName("Implements PublicCloneable")
    public void testPublicCloneable() {
        assertTrue(new CustomCategoryURLGenerator() instanceof PublicCloneable);
    }

    @Test
    @DisplayName("Serialization round-trip preserves equality")
    public void testSerialization() {
        List<String> seriesA = mutableList("URL A1", "URL A2", "URL A3");
        List<String> seriesB = mutableList("URL B1", "URL B2", "URL B3");

        CustomCategoryURLGenerator original = generatorWithSeries(seriesA, seriesB);
        CustomCategoryURLGenerator restored = TestUtils.serialised(original);

        assertEquals(original, restored);
    }

    @Test
    @DisplayName("addURLSeries(): handles null and defensively copies input")
    public void testAddURLSeries() {
        CustomCategoryURLGenerator gen = new CustomCategoryURLGenerator();

        // You can add a null list; it contributes an empty series
        gen.addURLSeries(null);
        assertEquals(1, gen.getListCount(), "Null series should count as a series");
        assertEquals(0, gen.getURLCount(0), "Null series should have zero URLs");

        // Add a real series
        List<String> inputSeries = mutableList("URL1");
        gen.addURLSeries(inputSeries);

        assertEquals(2, gen.getListCount(), "Two series after adding one real series");
        assertEquals(0, gen.getURLCount(0), "First series is the null (empty) one");
        assertEquals(1, gen.getURLCount(1), "Second series has one URL");
        assertEquals("URL1", gen.getURL(1, 0), "URL content should match");

        // Defensive copy: mutating the original list should not affect the generator
        inputSeries.clear();
        assertEquals("URL1", gen.getURL(1, 0), "Generator must not be affected by external list changes");
    }
}