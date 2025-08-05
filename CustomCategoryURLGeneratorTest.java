/* ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Project Info:  https://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * -----------------------------------
 * CustomCategoryURLGeneratorTest.java
 * -----------------------------------
 * (C) Copyright 2008-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.urls;

import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    // Test data constants for better maintainability
    private static final String URL_A1 = "URL A1";
    private static final String URL_A2 = "URL A2";
    private static final String URL_A3 = "URL A3";
    private static final String URL_B1 = "URL B1";
    private static final String URL_B2 = "URL B2";
    private static final String URL_B3 = "URL B3";
    private static final String URL_XXX = "URL XXX";
    private static final String URL_1 = "URL1";

    /**
     * Tests the equals() method to ensure proper equality comparison.
     * Verifies that:
     * 1. Two empty generators are equal
     * 2. Generators with different URL series are not equal
     * 3. Generators with identical URL series are equal
     */
    @Test
    public void testEquals() {
        // Given: Two empty URL generators
        CustomCategoryURLGenerator emptyGenerator1 = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator emptyGenerator2 = new CustomCategoryURLGenerator();
        
        // Then: Empty generators should be equal
        assertEquals(emptyGenerator1, emptyGenerator2, 
            "Two empty CustomCategoryURLGenerator instances should be equal");

        // When: Adding URL series to first generator only
        List<String> urlSeriesA = createUrlSeries(URL_A1, URL_A2, URL_A3);
        emptyGenerator1.addURLSeries(urlSeriesA);
        
        // Then: Generators should no longer be equal
        assertNotEquals(emptyGenerator1, emptyGenerator2,
            "Generators with different URL series should not be equal");

        // When: Adding identical URL series to second generator
        List<String> identicalUrlSeries = createUrlSeries(URL_A1, URL_A2, URL_A3);
        emptyGenerator2.addURLSeries(identicalUrlSeries);
        
        // Then: Generators should be equal again
        assertEquals(emptyGenerator1, emptyGenerator2,
            "Generators with identical URL series should be equal");
    }

    /**
     * Tests the cloning functionality to ensure:
     * 1. Cloned objects are not the same instance
     * 2. Cloned objects have the same class
     * 3. Cloned objects are equal to the original
     * 4. Cloned objects are independent (deep copy)
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        // Given: A URL generator with URL series
        CustomCategoryURLGenerator originalGenerator = new CustomCategoryURLGenerator();
        List<String> initialUrlSeries = createUrlSeries(URL_A1, URL_A2, URL_A3);
        originalGenerator.addURLSeries(initialUrlSeries);
        
        // When: Cloning the generator
        CustomCategoryURLGenerator clonedGenerator = CloneUtils.clone(originalGenerator);
        
        // Then: Verify basic clone properties
        assertNotSame(originalGenerator, clonedGenerator,
            "Cloned generator should be a different instance");
        assertSame(originalGenerator.getClass(), clonedGenerator.getClass(),
            "Cloned generator should have the same class");
        assertEquals(originalGenerator, clonedGenerator,
            "Cloned generator should be equal to the original");

        // When: Modifying the original generator after cloning
        List<String> additionalUrlSeries = createUrlSeries(URL_XXX);
        originalGenerator.addURLSeries(additionalUrlSeries);
        
        // Then: Clone should remain unchanged (independence test)
        assertNotEquals(originalGenerator, clonedGenerator,
            "Original and clone should be independent after modification");
        
        // When: Applying same modification to clone
        clonedGenerator.addURLSeries(new ArrayList<>(additionalUrlSeries));
        
        // Then: They should be equal again
        assertEquals(originalGenerator, clonedGenerator,
            "Generators should be equal after applying same modifications");
    }

    /**
     * Verifies that CustomCategoryURLGenerator implements the PublicCloneable interface.
     * This is important for ensuring the class can be cloned in public contexts.
     */
    @Test
    public void testImplementsPublicCloneable() {
        // Given: A URL generator instance
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        
        // Then: It should implement PublicCloneable
        assertTrue(generator instanceof PublicCloneable,
            "CustomCategoryURLGenerator should implement PublicCloneable interface");
    }

    /**
     * Tests serialization and deserialization to ensure:
     * 1. The object can be serialized and deserialized successfully
     * 2. The deserialized object is equal to the original
     */
    @Test
    public void testSerialization() {
        // Given: A URL generator with multiple URL series
        CustomCategoryURLGenerator originalGenerator = createGeneratorWithMultipleSeries();
        
        // When: Serializing and deserializing the generator
        CustomCategoryURLGenerator deserializedGenerator = TestUtils.serialised(originalGenerator);
        
        // Then: Deserialized generator should equal the original
        assertEquals(originalGenerator, deserializedGenerator,
            "Deserialized generator should be equal to the original");
    }

    /**
     * Tests the addURLSeries() method to verify:
     * 1. Null lists can be added (legacy behavior)
     * 2. URL series are properly stored and accessible
     * 3. The generator is independent of the original list (defensive copying)
     */
    @Test
    public void testAddURLSeries() {
        // Given: An empty URL generator
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        
        // When: Adding a null URL series
        generator.addURLSeries(null);
        
        // Then: Verify null series handling
        assertEquals(1, generator.getListCount(),
            "Generator should have one list after adding null series");
        assertEquals(0, generator.getURLCount(0),
            "Null series should contain zero URLs");

        // When: Adding a URL series with one URL
        List<String> urlList = new ArrayList<>();
        urlList.add(URL_1);
        generator.addURLSeries(urlList);
        
        // Then: Verify the URL series was added correctly
        assertEquals(2, generator.getListCount(),
            "Generator should have two lists after adding URL series");
        assertEquals(0, generator.getURLCount(0),
            "First list (null) should still contain zero URLs");
        assertEquals(1, generator.getURLCount(1),
            "Second list should contain one URL");
        assertEquals(URL_1, generator.getURL(1, 0),
            "Retrieved URL should match the added URL");

        // When: Modifying the original list after adding it
        urlList.clear();
        
        // Then: Generator should be unaffected (defensive copying test)
        assertEquals(URL_1, generator.getURL(1, 0),
            "Generator should be independent of the original list modifications");
    }

    // Helper methods for better test readability and maintainability

    /**
     * Creates a URL series list with the specified URLs.
     */
    private List<String> createUrlSeries(String... urls) {
        List<String> urlList = new ArrayList<>();
        for (String url : urls) {
            urlList.add(url);
        }
        return urlList;
    }

    /**
     * Creates a URL generator with multiple predefined URL series for testing.
     */
    private CustomCategoryURLGenerator createGeneratorWithMultipleSeries() {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        
        List<String> seriesA = createUrlSeries(URL_A1, URL_A2, URL_A3);
        List<String> seriesB = createUrlSeries(URL_B1, URL_B2, URL_B3);
        
        generator.addURLSeries(seriesA);
        generator.addURLSeries(seriesB);
        
        return generator;
    }
}