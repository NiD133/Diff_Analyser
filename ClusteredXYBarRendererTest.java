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
     * Tests the equality of two {@link ClusteredXYBarRenderer} instances.
     */
    @Test
    public void testEquals() {
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer();
        assertEquals(renderer1, renderer2);
        assertEquals(renderer2, renderer1);

        renderer1 = new ClusteredXYBarRenderer(1.2, false);
        assertNotEquals(renderer1, renderer2);
        renderer2 = new ClusteredXYBarRenderer(1.2, false);
        assertEquals(renderer1, renderer2);

        renderer1 = new ClusteredXYBarRenderer(1.2, true);
        assertNotEquals(renderer1, renderer2);
        renderer2 = new ClusteredXYBarRenderer(1.2, true);
        assertEquals(renderer1, renderer2);
    }

    /**
     * Tests that two equal objects have the same hash code.
     */
    @Test
    public void testHashcode() {
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer();
        assertEquals(renderer1, renderer2);
        assertEquals(renderer1.hashCode(), renderer2.hashCode());
    }

    /**
     * Tests that the renderer can be cloned correctly.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        ClusteredXYBarRenderer originalRenderer = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer clonedRenderer = CloneUtils.clone(originalRenderer);
        assertNotSame(originalRenderer, clonedRenderer);
        assertSame(originalRenderer.getClass(), clonedRenderer.getClass());
        assertEquals(originalRenderer, clonedRenderer);
    }

    /**
     * Tests that the renderer implements {@link PublicCloneable}.
     */
    @Test
    public void testPublicCloneable() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        assertTrue(renderer instanceof PublicCloneable);
    }

    /**
     * Tests that a serialized renderer can be deserialized correctly.
     */
    @Test
    public void testSerialization() {
        ClusteredXYBarRenderer originalRenderer = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer deserializedRenderer = TestUtils.serialised(originalRenderer);
        assertEquals(originalRenderer, deserializedRenderer);
    }

    /**
     * Tests the findDomainBounds() method for different configurations.
     */
    @Test
    public void testFindDomainBounds() {
        AbstractXYItemRenderer renderer = new ClusteredXYBarRenderer();
        XYDataset<String> dataset = createSampleDataset();
        Range domainBounds = renderer.findDomainBounds(dataset);
        assertEquals(0.9, domainBounds.getLowerBound(), EPSILON);
        assertEquals(13.1, domainBounds.getUpperBound(), EPSILON);

        renderer = new ClusteredXYBarRenderer(0.0, true);
        domainBounds = renderer.findDomainBounds(dataset);
        assertEquals(0.8, domainBounds.getLowerBound(), EPSILON);
        assertEquals(13.0, domainBounds.getUpperBound(), EPSILON);

        // Verify that a null dataset returns null bounds
        assertNull(renderer.findDomainBounds(null));
    }

    /**
     * Creates a sample dataset for testing.
     *
     * @return A sample dataset.
     */
    private DefaultIntervalXYDataset<String> createSampleDataset() {
        DefaultIntervalXYDataset<String> dataset = new DefaultIntervalXYDataset<>();
        addSeriesToDataset(dataset, "S1", new double[]{1.0, 2.0, 3.0}, new double[]{0.9, 1.9, 2.9}, new double[]{1.1, 2.1, 3.1}, new double[]{4.0, 5.0, 6.0}, new double[]{1.09, 2.09, 3.09}, new double[]{1.11, 2.11, 3.11});
        addSeriesToDataset(dataset, "S2", new double[]{11.0, 12.0, 13.0}, new double[]{10.9, 11.9, 12.9}, new double[]{11.1, 12.1, 13.1}, new double[]{14.0, 15.0, 16.0}, new double[]{11.09, 12.09, 13.09}, new double[]{11.11, 12.11, 13.11});
        return dataset;
    }

    /**
     * Adds a series to the dataset.
     */
    private void addSeriesToDataset(DefaultIntervalXYDataset<String> dataset, String seriesKey, double[] x, double[] xStart, double[] xEnd, double[] y, double[] yStart, double[] yEnd) {
        double[][] data = new double[][]{x, xStart, xEnd, y, yStart, yEnd};
        dataset.addSeries(seriesKey, data);
    }
}