package org.jfree.chart.renderer.xy;

import org.jfree.data.Range;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Focused and readable tests for ClusteredXYBarRenderer.
 *
 * These tests exercise:
 * - Public API contracts (getPassCount, equals, clone)
 * - Domain range calculations with and without the "offset" behavior
 *   for interval datasets (SimpleHistogramDataset)
 *
 * Notes:
 * - Tests avoid exercising paint/graphics code (drawItem) because that requires
 *   heavy scaffolding and is not necessary to validate core behavior here.
 * - The protected method findDomainBoundsWithOffset is tested from the same
 *   package to validate its documented behavior.
 */
public class ClusteredXYBarRendererTest {

    // ---------------------------------------------------------------------
    // Utilities
    // ---------------------------------------------------------------------

    private static SimpleHistogramDataset newHistogramWithSingleBin(double lower, double upper) {
        SimpleHistogramDataset d = new SimpleHistogramDataset("key");
        d.addBin(new SimpleHistogramBin(lower, upper));
        return d;
    }

    // ---------------------------------------------------------------------
    // Basic API
    // ---------------------------------------------------------------------

    @Test
    public void getPassCount_returnsTwo() {
        ClusteredXYBarRenderer r = new ClusteredXYBarRenderer();
        assertEquals(2, r.getPassCount());
    }

    @Test
    public void equals_reflexiveSymmetricAndDetectsDifference() {
        ClusteredXYBarRenderer a = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer b = new ClusteredXYBarRenderer();

        // Reflexive
        assertTrue(a.equals(a));

        // Symmetric on equal state
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));

        // Detects difference when configuration changes
        ClusteredXYBarRenderer different = new ClusteredXYBarRenderer(0.15, true);
        assertFalse(a.equals(different));
        assertFalse(different.equals(a));

        // Different type
        assertFalse(a.equals(new Object()));
    }

    @Test
    public void clone_defaultState_producesEqualButDistinctInstance() throws Exception {
        ClusteredXYBarRenderer original = new ClusteredXYBarRenderer();
        Object copy = original.clone();

        assertNotSame("Clone should be a distinct instance", original, copy);
        assertTrue("Clone should be equal to the original", original.equals(copy));
    }

    // ---------------------------------------------------------------------
    // Domain bounds (without offset) for IntervalXYDataset
    // ---------------------------------------------------------------------

    @Test
    public void findDomainBounds_singleHistogramBin_returnsStartToEnd() {
        ClusteredXYBarRenderer r = new ClusteredXYBarRenderer();
        SimpleHistogramDataset d = newHistogramWithSingleBin(1.0, 3.0);

        Range range = r.findDomainBounds(d);

        assertNotNull(range);
        assertEquals(1.0, range.getLowerBound(), 1e-9);
        assertEquals(3.0, range.getUpperBound(), 1e-9);
        assertEquals(2.0, range.getCentralValue(), 1e-9);
    }

    @Test
    public void findDomainBounds_multipleHistogramBins_spansFullRange() {
        ClusteredXYBarRenderer r = new ClusteredXYBarRenderer();
        SimpleHistogramDataset d = new SimpleHistogramDataset("key");
        d.addBin(new SimpleHistogramBin(-10.0, -2.0));
        d.addBin(new SimpleHistogramBin(5.0, 9.0));

        Range range = r.findDomainBounds(d);

        assertNotNull(range);
        assertEquals(-10.0, range.getLowerBound(), 1e-9);
        assertEquals(9.0, range.getUpperBound(), 1e-9);
    }

    @Test
    public void findDomainBounds_nullDataset_returnsNull() {
        ClusteredXYBarRenderer r = new ClusteredXYBarRenderer();
        Range range = r.findDomainBounds((XYDataset) null);
        assertNull(range);
    }

    // ---------------------------------------------------------------------
    // Domain bounds WITH OFFSET (protected helper, tested from same package)
    // ---------------------------------------------------------------------

    @Test
    public void findDomainBoundsWithOffset_singleBin_centersAroundStartValue() {
        // For a bin [1, 3), length = 2. With offset, the range is centered at the start:
        // [start - length/2, start + length/2] => [0, 2], center = 1
        ClusteredXYBarRenderer r = new ClusteredXYBarRenderer();
        SimpleHistogramDataset d = newHistogramWithSingleBin(1.0, 3.0);

        Range range = r.findDomainBoundsWithOffset(d);

        assertNotNull(range);
        assertEquals(0.0, range.getLowerBound(), 1e-9);
        assertEquals(2.0, range.getUpperBound(), 1e-9);
        assertEquals(1.0, range.getCentralValue(), 1e-9);
    }

    @Test
    public void findDomainBoundsWithOffset_multipleBins_spansAllOffsetIntervals() {
        // Bin A: [10, 14) -> length=4, offset range: [8, 12]
        // Bin B: [-5, 0)  -> length=5, offset range: [-7.5, -2.5]
        // Combined range: [-7.5, 12], length: 19.5
        ClusteredXYBarRenderer r = new ClusteredXYBarRenderer();
        SimpleHistogramDataset d = new SimpleHistogramDataset("key");
        d.addBin(new SimpleHistogramBin(10.0, 14.0));
        d.addBin(new SimpleHistogramBin(-5.0, 0.0));

        Range range = r.findDomainBoundsWithOffset(d);

        assertNotNull(range);
        assertEquals(-7.5, range.getLowerBound(), 1e-9);
        assertEquals(12.0, range.getUpperBound(), 1e-9);
        assertEquals(19.5, range.getLength(), 1e-9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findDomainBoundsWithOffset_nullDataset_throwsIllegalArgumentException() {
        ClusteredXYBarRenderer r = new ClusteredXYBarRenderer();
        r.findDomainBoundsWithOffset(null);
    }

    @Test
    public void findDomainBoundsWithOffset_emptyDataset_returnsNull() {
        ClusteredXYBarRenderer r = new ClusteredXYBarRenderer();
        SimpleHistogramDataset empty = new SimpleHistogramDataset("key"); // no bins
        Range range = r.findDomainBoundsWithOffset(empty);
        assertNull(range);
    }
}