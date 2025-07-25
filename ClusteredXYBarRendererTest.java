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
 * Unit tests for the {@link ClusteredXYBarRenderer} class.
 */
public class ClusteredXYBarRendererTest {

    private static final double EPSILON = 1e-10;

    /**
     * Tests the {@code equals()} method to ensure it correctly distinguishes all fields.
     */
    @Test
    public void testEquals() {
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer();
        
        // Test default constructor equality
        assertEquals(renderer1, renderer2);
        
        // Test inequality with different margin and centerBarAtStartValue
        renderer1 = new ClusteredXYBarRenderer(1.2, false);
        assertNotEquals(renderer1, renderer2);
        
        // Test equality with same margin and centerBarAtStartValue
        renderer2 = new ClusteredXYBarRenderer(1.2, false);
        assertEquals(renderer1, renderer2);

        // Test inequality with different centerBarAtStartValue
        renderer1 = new ClusteredXYBarRenderer(1.2, true);
        assertNotEquals(renderer1, renderer2);
        
        // Test equality with same margin and centerBarAtStartValue
        renderer2 = new ClusteredXYBarRenderer(1.2, true);
        assertEquals(renderer1, renderer2);
    }

    /**
     * Tests that equal objects have the same hash code.
     */
    @Test
    public void testHashcode() {
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer();
        
        assertEquals(renderer1, renderer2);
        assertEquals(renderer1.hashCode(), renderer2.hashCode());
    }

    /**
     * Tests the cloning functionality.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        ClusteredXYBarRenderer original = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer clone = CloneUtils.clone(original);
        
        assertNotSame(original, clone);
        assertEquals(original, clone);
    }

    /**
     * Verifies that the class implements {@link PublicCloneable}.
     */
    @Test
    public void testPublicCloneable() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        assertTrue(renderer instanceof PublicCloneable);
    }

    /**
     * Tests serialization and deserialization for equality.
     */
    @Test
    public void testSerialization() {
        ClusteredXYBarRenderer original = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer deserialized = TestUtils.serialised(original);
        
        assertEquals(original, deserialized);
    }

    /**
     * Tests the {@code findDomainBounds()} method with various configurations.
     */
    @Test
    public void testFindDomainBounds() {
        AbstractXYItemRenderer renderer = new ClusteredXYBarRenderer();
        XYDataset<String> dataset = createSampleDataset1();
        
        // Test default configuration
        Range range = renderer.findDomainBounds(dataset);
        assertEquals(0.9, range.getLowerBound(), EPSILON);
        assertEquals(13.1, range.getUpperBound(), EPSILON);

        // Test with centerBarAtStartValue set to true
        renderer = new ClusteredXYBarRenderer(0.0, true);
        range = renderer.findDomainBounds(dataset);
        assertEquals(0.8, range.getLowerBound(), EPSILON);
        assertEquals(13.0, range.getUpperBound(), EPSILON);

        // Test with null dataset
        assertNull(renderer.findDomainBounds(null));
    }

    /**
     * Creates a sample dataset for testing purposes.
     *
     * @return A sample dataset.
     */
    private DefaultIntervalXYDataset<String> createSampleDataset1() {
        DefaultIntervalXYDataset<String> dataset = new DefaultIntervalXYDataset<>();
        
        // Series 1 data
        double[] x1 = {1.0, 2.0, 3.0};
        double[] x1Start = {0.9, 1.9, 2.9};
        double[] x1End = {1.1, 2.1, 3.1};
        double[] y1 = {4.0, 5.0, 6.0};
        double[] y1Start = {1.09, 2.09, 3.09};
        double[] y1End = {1.11, 2.11, 3.11};
        dataset.addSeries("S1", new double[][] {x1, x1Start, x1End, y1, y1Start, y1End});

        // Series 2 data
        double[] x2 = {11.0, 12.0, 13.0};
        double[] x2Start = {10.9, 11.9, 12.9};
        double[] x2End = {11.1, 12.1, 13.1};
        double[] y2 = {14.0, 15.0, 16.0};
        double[] y2Start = {11.09, 12.09, 13.09};
        double[] y2End = {11.11, 12.11, 13.11};
        dataset.addSeries("S2", new double[][] {x2, x2Start, x2End, y2, y2Start, y2End});
        
        return dataset;
    }
}