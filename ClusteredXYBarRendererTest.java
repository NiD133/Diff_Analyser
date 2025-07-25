package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ClusteredXYBarRenderer} class.  This test suite
 * focuses on verifying the core functionality of the renderer, including
 * equality checks, cloning, serialization, and domain bounds calculation.
 */
public class ClusteredXYBarRendererTest {

    private static final double EPSILON = 0.0000000001;

    @Test
    @DisplayName("Test equals() method")
    public void testEquals() {
        // Two default renderers should be equal
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer();
        assertEquals(renderer1, renderer2, "Two default renderers should be equal.");

        // Different margin should make them unequal
        renderer1 = new ClusteredXYBarRenderer(1.2, false);
        assertNotEquals(renderer1, renderer2, "Renderers with different margin should not be equal.");
        renderer2 = new ClusteredXYBarRenderer(1.2, false);
        assertEquals(renderer1, renderer2, "Renderers with same margin should be equal.");

        // Different centerBarAtStartValue should make them unequal
        renderer1 = new ClusteredXYBarRenderer(1.2, true);
        assertNotEquals(renderer1, renderer2, "Renderers with different centerBarAtStartValue should not be equal.");
        renderer2 = new ClusteredXYBarRenderer(1.2, true);
        assertEquals(renderer1, renderer2, "Renderers with same centerBarAtStartValue should be equal.");
    }

    @Test
    @DisplayName("Test hashCode() method")
    public void testHashcode() {
        // Equal objects must have the same hashcode
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer();
        assertEquals(renderer1, renderer2, "Renderers should be equal.");
        assertEquals(renderer1.hashCode(), renderer2.hashCode(), "Equal renderers should have the same hashcode.");
    }

    @Test
    @DisplayName("Test cloning")
    public void testCloning() throws CloneNotSupportedException {
        // Verify that cloning creates a new object with the same state
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer renderer2 = CloneUtils.clone(renderer1);

        assertNotSame(renderer1, renderer2, "The cloned object should be a different instance.");
        assertEquals(renderer1.getClass(), renderer2.getClass(), "The cloned object should be of the same class.");
        assertEquals(renderer1, renderer2, "The cloned object should be equal to the original.");
    }

    @Test
    @DisplayName("Test PublicCloneable interface")
    public void testPublicCloneable() {
        // Verify that the class implements the PublicCloneable interface
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        assertTrue(renderer instanceof PublicCloneable, "ClusteredXYBarRenderer should implement PublicCloneable.");
    }

    @Test
    @DisplayName("Test serialization")
    public void testSerialization() {
        // Verify that an instance can be serialized and deserialized without data loss
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer renderer2 = TestUtils.serialised(renderer1);
        assertEquals(renderer1, renderer2, "The serialized and deserialized objects should be equal.");
    }

    @Test
    @DisplayName("Test findDomainBounds() method")
    public void testFindDomainBounds() {
        // Test the findDomainBounds() method with and without the centerBarAtStartValue flag

        XYDataset<String> dataset = createSampleDataset1();

        // Default renderer
        AbstractXYItemRenderer renderer = new ClusteredXYBarRenderer();
        Range range = renderer.findDomainBounds(dataset);
        assertNotNull(range, "Domain bounds should not be null for non-empty dataset.");
        assertEquals(0.9, range.getLowerBound(), EPSILON, "Lower bound of domain range is incorrect.");
        assertEquals(13.1, range.getUpperBound(), EPSILON, "Upper bound of domain range is incorrect.");

        // Renderer with centerBarAtStartValue = true
        renderer = new ClusteredXYBarRenderer(0.0, true);
        range = renderer.findDomainBounds(dataset);
        assertNotNull(range, "Domain bounds should not be null for non-empty dataset.");
        assertEquals(0.8, range.getLowerBound(), EPSILON, "Lower bound of domain range is incorrect with centerBarAtStartValue=true.");
        assertEquals(13.0, range.getUpperBound(), EPSILON, "Upper bound of domain range is incorrect with centerBarAtStartValue=true.");

        // Test with a null dataset
        assertNull(renderer.findDomainBounds(null), "Domain bounds should be null for a null dataset.");
    }

    /**
     * Creates a sample dataset for testing. This dataset contains two series with three data points each,
     * including start and end values for both X and Y axes.
     *
     * @return A sample dataset.
     */
    private DefaultIntervalXYDataset<String> createSampleDataset1() {
        DefaultIntervalXYDataset<String> dataset = new DefaultIntervalXYDataset<>();

        // Series 1 data
        double[] x1 = new double[] {1.0, 2.0, 3.0};
        double[] x1Start = new double[] {0.9, 1.9, 2.9};
        double[] x1End = new double[] {1.1, 2.1, 3.1};
        double[] y1 = new double[] {4.0, 5.0, 6.0};
        double[] y1Start = new double[] {1.09, 2.09, 3.09};
        double[] y1End = new double[] {1.11, 2.11, 3.11};
        double[][] data1 = new double[][] {x1, x1Start, x1End, y1, y1Start, y1End};
        dataset.addSeries("S1", data1);

        // Series 2 data
        double[] x2 = new double[] {11.0, 12.0, 13.0};
        double[] x2Start = new double[] {10.9, 11.9, 12.9};
        double[] x2End = new double[] {11.1, 12.1, 13.1};
        double[] y2 = new double[] {14.0, 15.0, 16.0};
        double[] y2Start = new double[] {11.09, 12.09, 13.09};
        double[] y2End = new double[] {11.11, 12.11, 13.11};
        double[][] data2 = new double[][] {x2, x2Start, x2End, y2, y2Start, y2End};
        dataset.addSeries("S2", data2);

        return dataset;
    }
}