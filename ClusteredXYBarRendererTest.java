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
import org.jfree.chart.api.PublicCloneable;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ClusteredXYBarRenderer} class.
 */
@DisplayName("ClusteredXYBarRenderer")
class ClusteredXYBarRendererTest {

    private static final double EPSILON = 0.0000000001;

    @Nested
    @DisplayName("Standard Contract Tests")
    class ContractTests {

        private ClusteredXYBarRenderer renderer1;
        private ClusteredXYBarRenderer renderer2;

        @BeforeEach
        void setUp() {
            renderer1 = new ClusteredXYBarRenderer();
            renderer2 = new ClusteredXYBarRenderer();
        }

        @Test
        @DisplayName("equals() should be true for two default instances")
        void equals_withDefaultInstances_shouldBeEqual() {
            assertEquals(renderer1, renderer2);
        }

        @Test
        @DisplayName("equals() should be true for two instances with the same properties")
        void equals_withSameProperties_shouldBeEqual() {
            renderer1 = new ClusteredXYBarRenderer(1.2, true);
            renderer2 = new ClusteredXYBarRenderer(1.2, true);
            assertEquals(renderer1, renderer2);
        }

        @Test
        @DisplayName("equals() should be false when centerBarAtStartValue differs")
        void equals_withDifferentCenterBarAtStartValue_shouldNotBeEqual() {
            renderer1 = new ClusteredXYBarRenderer(1.2, false);
            renderer2 = new ClusteredXYBarRenderer(1.2, true);
            assertNotEquals(renderer1, renderer2);
        }

        @Test
        @DisplayName("hashCode() should be consistent with equals()")
        void hashCode_shouldBeConsistentWithEquals() {
            assertEquals(renderer1.hashCode(), renderer2.hashCode());

            renderer1 = new ClusteredXYBarRenderer(1.2, true);
            renderer2 = new ClusteredXYBarRenderer(1.2, true);
            assertEquals(renderer1.hashCode(), renderer2.hashCode());
        }

        @Test
        @DisplayName("clone() should produce an independent but equal instance")
        void clone_shouldReturnIndependentButEqualInstance() throws CloneNotSupportedException {
            ClusteredXYBarRenderer clonedRenderer = CloneUtils.clone(renderer1);
            assertNotSame(renderer1, clonedRenderer);
            assertEquals(renderer1, clonedRenderer);
        }

        @Test
        @DisplayName("should implement PublicCloneable")
        void shouldImplementPublicCloneable() {
            assertTrue(renderer1 instanceof PublicCloneable);
        }

        @Test
        @DisplayName("serialization should preserve object state")
        void serialization_shouldPreserveObjectState() {
            ClusteredXYBarRenderer deserializedRenderer = TestUtils.serialised(renderer1);
            assertEquals(renderer1, deserializedRenderer);
        }
    }

    @Nested
    @DisplayName("findDomainBounds() method")
    class FindDomainBoundsTests {

        private XYDataset sampleDataset;

        @BeforeEach
        void setUp() {
            sampleDataset = createSampleDataset();
        }

        @Test
        @DisplayName("should return null for a null dataset")
        void findDomainBounds_withNullDataset_shouldReturnNull() {
            AbstractXYItemRenderer renderer = new ClusteredXYBarRenderer();
            assertNull(renderer.findDomainBounds(null));
        }

        @Test
        @DisplayName("with default settings, should return the min/max of the data intervals")
        void findDomainBounds_withDefaultRenderer_shouldReturnDataIntervalBounds() {
            AbstractXYItemRenderer renderer = new ClusteredXYBarRenderer(0.0, false);
            
            // The default behavior is to use the start and end X-values from the dataset.
            // For our sample data, min(xStart) is 0.9 and max(xEnd) is 13.1.
            final double expectedLowerBound = 0.9;
            final double expectedUpperBound = 13.1;

            Range bounds = renderer.findDomainBounds(sampleDataset);
            assertNotNull(bounds);
            assertEquals(expectedLowerBound, bounds.getLowerBound(), EPSILON);
            assertEquals(expectedUpperBound, bounds.getUpperBound(), EPSILON);
        }

        @Test
        @DisplayName("when centering bars at start, should return offset bounds")
        void findDomainBounds_whenCenteringBarsAtStart_shouldReturnOffsetBounds() {
            // This renderer configuration shifts the bar interval.
            AbstractXYItemRenderer renderer = new ClusteredXYBarRenderer(0.0, true);

            // When centerBarAtStartValue is true, the interval for each item is calculated as
            // [x - (xEnd - xStart), x].
            // For our sample data:
            // - The interval width is consistently 0.2 (e.g., 1.1 - 0.9).
            // - The minimum x-value is 1.0. The new lower bound is 1.0 - 0.2 = 0.8.
            // - The maximum x-value is 13.0. This becomes the new upper bound.
            final double expectedLowerBound = 0.8;
            final double expectedUpperBound = 13.0;

            Range bounds = renderer.findDomainBounds(sampleDataset);
            assertNotNull(bounds);
            assertEquals(expectedLowerBound, bounds.getLowerBound(), EPSILON);
            assertEquals(expectedUpperBound, bounds.getUpperBound(), EPSILON);
        }
    }

    /**
     * Creates a sample dataset with two series for testing.
     * The data array for an IntervalXYDataset has the following structure:
     * {x-values, x-start-values, x-end-values, y-values, y-start-values, y-end-values}
     *
     * @return A sample dataset.
     */
    private DefaultIntervalXYDataset<String> createSampleDataset() {
        DefaultIntervalXYDataset<String> dataset = new DefaultIntervalXYDataset<>();

        // Series 1
        double[] x1 = {1.0, 2.0, 3.0};
        double[] x1Start = {0.9, 1.9, 2.9};
        double[] x1End = {1.1, 2.1, 3.1};
        double[] y1 = {4.0, 5.0, 6.0};
        double[] y1Start = {1.09, 2.09, 3.09};
        double[] y1End = {1.11, 2.11, 3.11};
        double[][] data1 = {x1, x1Start, x1End, y1, y1Start, y1End};
        dataset.addSeries("S1", data1);

        // Series 2
        double[] x2 = {11.0, 12.0, 13.0};
        double[] x2Start = {10.9, 11.9, 12.9};
        double[] x2End = {11.1, 12.1, 13.1};
        double[] y2 = {14.0, 15.0, 16.0};
        double[] y2Start = {11.09, 12.09, 13.09};
        double[] y2End = {11.11, 12.11, 13.11};
        double[][] data2 = {x2, x2Start, x2End, y2, y2Start, y2End};
        dataset.addSeries("S2", data2);

        return dataset;
    }
}