package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClusteredXYBarRendererTestTest6 {

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
     * Some checks for the findDomainBounds() method (which requires special
     * handling when the centerBarAtStartValue flag is set to true).
     */
    @Test
    public void testFindDomainBounds() {
        AbstractXYItemRenderer renderer = new ClusteredXYBarRenderer();
        XYDataset<String> dataset = createSampleDataset1();
        Range r = renderer.findDomainBounds(dataset);
        assertEquals(0.9, r.getLowerBound(), EPSILON);
        assertEquals(13.1, r.getUpperBound(), EPSILON);
        renderer = new ClusteredXYBarRenderer(0.0, true);
        r = renderer.findDomainBounds(dataset);
        assertEquals(0.8, r.getLowerBound(), EPSILON);
        assertEquals(13.0, r.getUpperBound(), EPSILON);
        // check that a null dataset returns null bounds
        assertNull(renderer.findDomainBounds(null));
    }
}
