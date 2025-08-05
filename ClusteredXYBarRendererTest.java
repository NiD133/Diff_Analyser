package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClusteredXYBarRendererTest {
    private static final double EPSILON = 1e-10;

    // ========================================================================
    // Equality Tests
    // ========================================================================
    
    @Test
    void equals_shouldReturnTrueForDefaultConstructedInstances() {
        ClusteredXYBarRenderer r1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer r2 = new ClusteredXYBarRenderer();
        assertEquals(r1, r2, "Default constructed instances should be equal");
    }

    @Test
    void equals_shouldDistinguishDifferentConstructorParameters() {
        ClusteredXYBarRenderer defaultRenderer = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer customRenderer = new ClusteredXYBarRenderer(1.2, false);
        
        assertNotEquals(defaultRenderer, customRenderer, 
            "Instances with different parameters should not be equal");
    }

    @Test
    void equals_shouldDetectCenterBarAtStartValueDifference() {
        ClusteredXYBarRenderer r1 = new ClusteredXYBarRenderer(1.2, true);
        ClusteredXYBarRenderer r2 = new ClusteredXYBarRenderer(1.2, false);
        
        assertNotEquals(r1, r2, 
            "Difference in centerBarAtStartValue should break equality");
    }

    // ========================================================================
    // HashCode Tests
    // ========================================================================
    
    @Test
    void hashCode_shouldBeEqualForEqualObjects() {
        ClusteredXYBarRenderer r1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer r2 = new ClusteredXYBarRenderer();
        assertEquals(r1.hashCode(), r2.hashCode(), 
            "Equal objects must have equal hash codes");
    }

    // ========================================================================
    // Cloning Tests
    // ========================================================================
    
    @Test
    void cloning_shouldCreateEqualButDistinctInstance() throws CloneNotSupportedException {
        ClusteredXYBarRenderer original = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer clone = CloneUtils.clone(original);
        
        assertNotSame(original, clone, "Clone should be distinct object");
        assertEquals(original, clone, "Clone should be equal to original");
    }

    // ========================================================================
    // PublicCloneable Tests
    // ========================================================================
    
    @Test
    void shouldImplementPublicCloneableInterface() {
        assertTrue(new ClusteredXYBarRenderer() instanceof PublicCloneable,
            "Renderer should implement PublicCloneable");
    }

    // ========================================================================
    // Serialization Tests
    // ========================================================================
    
    @Test
    void serialization_shouldPreserveObjectEquality() {
        ClusteredXYBarRenderer original = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, 
            "Deserialized instance should equal original");
    }

    // ========================================================================
    // Domain Bounds Calculation Tests
    // ========================================================================
    
    @Test
    void findDomainBounds_withoutCenterBarAtStartValue_shouldCalculateCorrectBounds() {
        AbstractXYItemRenderer renderer = new ClusteredXYBarRenderer();
        XYDataset<String> dataset = createSampleDataset();
        Range bounds = renderer.findDomainBounds(dataset);
        
        assertEquals(0.9, bounds.getLowerBound(), EPSILON, 
            "Lower bound without centerBarAtStartValue");
        assertEquals(13.1, bounds.getUpperBound(), EPSILON,
            "Upper bound without centerBarAtStartValue");
    }

    @Test
    void findDomainBounds_withCenterBarAtStartValue_shouldCalculateCorrectBounds() {
        AbstractXYItemRenderer renderer = new ClusteredXYBarRenderer(0.0, true);
        XYDataset<String> dataset = createSampleDataset();
        Range bounds = renderer.findDomainBounds(dataset);
        
        assertEquals(0.8, bounds.getLowerBound(), EPSILON,
            "Lower bound with centerBarAtStartValue");
        assertEquals(13.0, bounds.getUpperBound(), EPSILON,
            "Upper bound with centerBarAtStartValue");
    }

    @Test
    void findDomainBounds_withNullDataset_shouldReturnNull() {
        AbstractXYItemRenderer renderer = new ClusteredXYBarRenderer();
        assertNull(renderer.findDomainBounds(null),
            "Null dataset should return null bounds");
    }

    // ========================================================================
    // Test Data Helpers
    // ========================================================================
    
    private static DefaultIntervalXYDataset<String> createSampleDataset() {
        DefaultIntervalXYDataset<String> dataset = new DefaultIntervalXYDataset<>();
        
        // Series 1: Three data points with intervals
        addSeriesToDataset(dataset, "S1", 
            new double[]{1.0, 2.0, 3.0},        // x
            new double[]{0.9, 1.9, 2.9},         // xStart
            new double[]{1.1, 2.1, 3.1},         // xEnd
            new double[]{4.0, 5.0, 6.0},         // y
            new double[]{1.09, 2.09, 3.09},      // yStart
            new double[]{1.11, 2.11, 3.11}      // yEnd
        );
        
        // Series 2: Three data points at higher values
        addSeriesToDataset(dataset, "S2", 
            new double[]{11.0, 12.0, 13.0},      // x
            new double[]{10.9, 11.9, 12.9},      // xStart
            new double[]{11.1, 12.1, 13.1},      // xEnd
            new double[]{14.0, 15.0, 16.0},      // y
            new double[]{11.09, 12.09, 13.09},   // yStart
            new double[]{11.11, 12.11, 13.11}    // yEnd
        );
        
        return dataset;
    }

    private static void addSeriesToDataset(DefaultIntervalXYDataset<String> dataset, 
                                          String seriesKey,
                                          double[] x, double[] xStart, double[] xEnd,
                                          double[] y, double[] yStart, double[] yEnd) {
        double[][] seriesData = {x, xStart, xEnd, y, yStart, yEnd};
        dataset.addSeries(seriesKey, seriesData);
    }
}