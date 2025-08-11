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
 * -------------------------------
 * ClusteredXYBarRendererTest.java
 * -------------------------------
 * (C) Copyright 2003-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;

import org.jfree.data.Range;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ClusteredXYBarRenderer} class.
 */
public class ClusteredXYBarRendererTest {

    // Test constants
    private static final double EPSILON = 0.0000000001;
    private static final double CUSTOM_MARGIN = 1.2;
    
    // Expected bounds for domain tests
    private static final double EXPECTED_LOWER_BOUND_DEFAULT = 0.9;
    private static final double EXPECTED_UPPER_BOUND_DEFAULT = 13.1;
    private static final double EXPECTED_LOWER_BOUND_CENTERED = 0.8;
    private static final double EXPECTED_UPPER_BOUND_CENTERED = 13.0;

    @Test
    public void testEquals_DefaultConstructors_ShouldBeEqual() {
        // Given: Two renderers created with default constructors
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer();
        
        // Then: They should be equal
        assertEquals(renderer1, renderer2);
        assertEquals(renderer2, renderer1);
    }

    @Test
    public void testEquals_DifferentMarginValues_ShouldNotBeEqual() {
        // Given: One default renderer and one with custom margin
        ClusteredXYBarRenderer defaultRenderer = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer customMarginRenderer = new ClusteredXYBarRenderer(CUSTOM_MARGIN, false);
        
        // Then: They should not be equal
        assertNotEquals(customMarginRenderer, defaultRenderer);
    }

    @Test
    public void testEquals_SameMarginValues_ShouldBeEqual() {
        // Given: Two renderers with same custom margin
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer(CUSTOM_MARGIN, false);
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer(CUSTOM_MARGIN, false);
        
        // Then: They should be equal
        assertEquals(renderer1, renderer2);
    }

    @Test
    public void testEquals_DifferentCenterBarFlags_ShouldNotBeEqual() {
        // Given: Two renderers with same margin but different centerBar flags
        ClusteredXYBarRenderer rendererWithCenterBarTrue = new ClusteredXYBarRenderer(CUSTOM_MARGIN, true);
        ClusteredXYBarRenderer rendererWithCenterBarFalse = new ClusteredXYBarRenderer(CUSTOM_MARGIN, false);
        
        // Then: They should not be equal
        assertNotEquals(rendererWithCenterBarTrue, rendererWithCenterBarFalse);
    }

    @Test
    public void testEquals_SameCenterBarFlags_ShouldBeEqual() {
        // Given: Two renderers with same margin and centerBar flag
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer(CUSTOM_MARGIN, true);
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer(CUSTOM_MARGIN, true);
        
        // Then: They should be equal
        assertEquals(renderer1, renderer2);
    }

    @Test
    public void testHashCode_EqualObjects_ShouldHaveSameHashCode() {
        // Given: Two equal renderers
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer();
        assertEquals(renderer1, renderer2);
        
        // When: Getting hash codes
        int hashCode1 = renderer1.hashCode();
        int hashCode2 = renderer2.hashCode();
        
        // Then: Hash codes should be equal
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testCloning_ShouldCreateDistinctButEqualObject() throws CloneNotSupportedException {
        // Given: A renderer
        ClusteredXYBarRenderer originalRenderer = new ClusteredXYBarRenderer();
        
        // When: Cloning the renderer
        ClusteredXYBarRenderer clonedRenderer = CloneUtils.clone(originalRenderer);
        
        // Then: Clone should be distinct but equal
        assertNotSame(originalRenderer, clonedRenderer);
        assertSame(originalRenderer.getClass(), clonedRenderer.getClass());
        assertEquals(originalRenderer, clonedRenderer);
    }

    @Test
    public void testPublicCloneable_ShouldImplementInterface() {
        // Given: A renderer
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        
        // Then: It should implement PublicCloneable
        assertTrue(renderer instanceof PublicCloneable);
    }

    @Test
    public void testSerialization_ShouldPreserveEquality() {
        // Given: A renderer
        ClusteredXYBarRenderer originalRenderer = new ClusteredXYBarRenderer();
        
        // When: Serializing and deserializing
        ClusteredXYBarRenderer deserializedRenderer = TestUtils.serialised(originalRenderer);
        
        // Then: Deserialized object should equal original
        assertEquals(originalRenderer, deserializedRenderer);
    }

    @Test
    public void testFindDomainBounds_DefaultRenderer_ShouldReturnExpectedBounds() {
        // Given: A default renderer and sample dataset
        AbstractXYItemRenderer renderer = new ClusteredXYBarRenderer();
        XYDataset<String> dataset = createSampleDataset();
        
        // When: Finding domain bounds
        Range bounds = renderer.findDomainBounds(dataset);
        
        // Then: Bounds should match expected values
        assertEquals(EXPECTED_LOWER_BOUND_DEFAULT, bounds.getLowerBound(), EPSILON);
        assertEquals(EXPECTED_UPPER_BOUND_DEFAULT, bounds.getUpperBound(), EPSILON);
    }

    @Test
    public void testFindDomainBounds_CenterBarAtStartValue_ShouldReturnAdjustedBounds() {
        // Given: A renderer with centerBarAtStartValue enabled
        AbstractXYItemRenderer renderer = new ClusteredXYBarRenderer(0.0, true);
        XYDataset<String> dataset = createSampleDataset();
        
        // When: Finding domain bounds
        Range bounds = renderer.findDomainBounds(dataset);
        
        // Then: Bounds should be adjusted for centering
        assertEquals(EXPECTED_LOWER_BOUND_CENTERED, bounds.getLowerBound(), EPSILON);
        assertEquals(EXPECTED_UPPER_BOUND_CENTERED, bounds.getUpperBound(), EPSILON);
    }

    @Test
    public void testFindDomainBounds_NullDataset_ShouldReturnNull() {
        // Given: A renderer and null dataset
        AbstractXYItemRenderer renderer = new ClusteredXYBarRenderer();
        
        // When: Finding domain bounds with null dataset
        Range bounds = renderer.findDomainBounds(null);
        
        // Then: Should return null
        assertNull(bounds);
    }

    /**
     * Creates a sample dataset for testing with two series containing interval data.
     * 
     * Series 1: x-values [1.0, 2.0, 3.0] with intervals [0.9-1.1, 1.9-2.1, 2.9-3.1]
     * Series 2: x-values [11.0, 12.0, 13.0] with intervals [10.9-11.1, 11.9-12.1, 12.9-13.1]
     *
     * @return A sample dataset for testing
     */
    private DefaultIntervalXYDataset<String> createSampleDataset() {
        DefaultIntervalXYDataset<String> dataset = new DefaultIntervalXYDataset<>();
        
        // First series data
        double[] series1XValues = {1.0, 2.0, 3.0};
        double[] series1XStart = {0.9, 1.9, 2.9};
        double[] series1XEnd = {1.1, 2.1, 3.1};
        double[] series1YValues = {4.0, 5.0, 6.0};
        double[] series1YStart = {1.09, 2.09, 3.09};
        double[] series1YEnd = {1.11, 2.11, 3.11};
        double[][] series1Data = {series1XValues, series1XStart, series1XEnd, 
                                  series1YValues, series1YStart, series1YEnd};
        dataset.addSeries("Series1", series1Data);

        // Second series data
        double[] series2XValues = {11.0, 12.0, 13.0};
        double[] series2XStart = {10.9, 11.9, 12.9};
        double[] series2XEnd = {11.1, 12.1, 13.1};
        double[] series2YValues = {14.0, 15.0, 16.0};
        double[] series2YStart = {11.09, 12.09, 13.09};
        double[] series2YEnd = {11.11, 12.11, 13.11};
        double[][] series2Data = {series2XValues, series2XStart, series2XEnd, 
                                  series2YValues, series2YStart, series2YEnd};
        dataset.addSeries("Series2", series2Data);
        
        return dataset;
    }
}