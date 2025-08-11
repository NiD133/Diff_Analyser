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
import java.util.Arrays;
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

    // Test data constants for URLs
    private static final List<String> URLS_SERIES_A = Arrays.asList("URL A1", "URL A2", "URL A3");
    private static final List<String> URLS_SERIES_B = Arrays.asList("URL B1", "URL B2", "URL B3");
    private static final List<String> URLS_SINGLE = Arrays.asList("URL1");
    private static final List<String> URLS_MODIFIED = Arrays.asList("URL XXX");

    /**
     * Tests that two empty generators are equal.
     */
    @Test
    public void testEqualsForEmptyGenerators() {
        CustomCategoryURLGenerator g1 = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator g2 = new CustomCategoryURLGenerator();
        assertEquals(g1, g2, "Empty generators should be equal");
    }

    /**
     * Tests that generators with different URL series are not equal.
     */
    @Test
    public void testEqualsWhenGeneratorHasURLSeriesAndOtherDoesNot() {
        CustomCategoryURLGenerator g1 = new CustomCategoryURLGenerator();
        g1.addURLSeries(URLS_SERIES_A);
        
        CustomCategoryURLGenerator g2 = new CustomCategoryURLGenerator();
        
        assertNotEquals(g1, g2, "Generators with different URL series should not be equal");
    }

    /**
     * Tests that generators with identical URL series are equal.
     */
    @Test
    public void testEqualsWhenGeneratorsHaveSameURLSeries() {
        CustomCategoryURLGenerator g1 = new CustomCategoryURLGenerator();
        g1.addURLSeries(URLS_SERIES_A);
        
        CustomCategoryURLGenerator g2 = new CustomCategoryURLGenerator();
        g2.addURLSeries(new ArrayList<>(URLS_SERIES_A)); // Defensive copy
        
        assertEquals(g1, g2, "Generators with same URL series should be equal");
    }

    /**
     * Tests that cloned generator is equal but not the same instance.
     */
    @Test
    public void testCloningCreatesIndependentEqualInstance() throws CloneNotSupportedException {
        CustomCategoryURLGenerator original = new CustomCategoryURLGenerator();
        original.addURLSeries(URLS_SERIES_A);
        
        CustomCategoryURLGenerator clone = CloneUtils.clone(original);
        
        assertNotSame(original, clone, "Cloned instance should be a different object");
        assertEquals(original, clone, "Cloned instance should be equal to the original");
    }

    /**
     * Tests that modifications to original generator do not affect the clone.
     */
    @Test
    public void testCloningMaintainsIndependence() throws CloneNotSupportedException {
        CustomCategoryURLGenerator original = new CustomCategoryURLGenerator();
        original.addURLSeries(URLS_SERIES_A);
        CustomCategoryURLGenerator clone = CloneUtils.clone(original);
        
        // Modify original after cloning
        original.addURLSeries(URLS_MODIFIED);
        
        assertNotEquals(original, clone, "Modifying original should break equality with clone");
        
        // Synchronize clone with modification
        clone.addURLSeries(new ArrayList<>(URLS_MODIFIED));
        assertEquals(original, clone, "Synchronized generators should be equal again");
    }

    /**
     * Tests that the generator implements the PublicCloneable interface.
     */
    @Test
    public void testPublicCloneableInterface() {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        assertTrue(generator instanceof PublicCloneable, 
                "Generator should implement PublicCloneable");
    }

    /**
     * Tests serialization and deserialization maintains equality.
     */
    @Test
    public void testSerializationMaintainsEquality() {
        CustomCategoryURLGenerator original = new CustomCategoryURLGenerator();
        original.addURLSeries(URLS_SERIES_A);
        original.addURLSeries(URLS_SERIES_B);
        
        CustomCategoryURLGenerator deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized generator should equal original");
    }

    /**
     * Tests adding a null URL series is handled correctly.
     */
    @Test
    public void testAddURLSeriesWithNull() {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        generator.addURLSeries(null);
        
        assertEquals(1, generator.getListCount(), 
                "Adding null should create one URL list");
        assertEquals(0, generator.getURLCount(0), 
                "Null URL series should have zero URLs");
    }

    /**
     * Tests adding a valid URL series updates the generator state.
     */
    @Test
    public void testAddURLSeriesWithValidList() {
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        generator.addURLSeries(URLS_SINGLE);
        
        assertEquals(1, generator.getListCount(), 
                "Generator should have one URL list");
        assertEquals(1, generator.getURLCount(0), 
                "URL list should contain one URL");
        assertEquals("URL1", generator.getURL(0, 0), 
                "Stored URL should match added URL");
    }

    /**
     * Tests that modifying the original list after adding does not affect the generator.
     */
    @Test
    public void testAddURLSeriesIsIndependentOfSourceList() {
        // Create mutable list
        List<String> urls = new ArrayList<>();
        urls.add("URL1");
        
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        generator.addURLSeries(urls);
        
        // Modify original list
        urls.clear();
        
        assertEquals("URL1", generator.getURL(0, 0), 
                "Modifying source list should not affect stored URLs");
    }
}