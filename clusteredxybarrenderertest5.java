package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClusteredXYBarRendererTestTest5 {

    private static final double EPSILON = 0.0000000001;

    /**
     * Creates a sample dataset for testing.
     *
     * @return A sample dataset.
     */
    public DefaultIntervalXYDataset<String> createSampleDataset1() {
        DefaultIntervalXYDataset<String> d = new DefaultIntervalXYDataset<>();
        double[] x1 = new double[] { 1.0, 2.0, 3.0 };
        double[] x1Start = new double[] { 0.9, 1.9, 2.9 };
        double[] x1End = new double[] { 1.1, 2.1, 3.1 };
        double[] y1 = new double[] { 4.0, 5.0, 6.0 };
        double[] y1Start = new double[] { 1.09, 2.09, 3.09 };
        double[] y1End = new double[] { 1.11, 2.11, 3.11 };
        double[][] data1 = new double[][] { x1, x1Start, x1End, y1, y1Start, y1End };
        d.addSeries("S1", data1);
        double[] x2 = new double[] { 11.0, 12.0, 13.0 };
        double[] x2Start = new double[] { 10.9, 11.9, 12.9 };
        double[] x2End = new double[] { 11.1, 12.1, 13.1 };
        double[] y2 = new double[] { 14.0, 15.0, 16.0 };
        double[] y2Start = new double[] { 11.09, 12.09, 13.09 };
        double[] y2End = new double[] { 11.11, 12.11, 13.11 };
        double[][] data2 = new double[][] { x2, x2Start, x2End, y2, y2Start, y2End };
        d.addSeries("S2", data2);
        return d;
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        ClusteredXYBarRenderer r1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer r2 = TestUtils.serialised(r1);
        assertEquals(r1, r2);
    }
}
