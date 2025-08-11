package org.jfree.chart.renderer.xy;

import org.jfree.data.Range;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeviationRendererTest {

    private static final float FLOAT_DELTA = 1e-6f;
    private static final double DOUBLE_DELTA = 1e-9;

    @Test
    public void defaultConfiguration_hasHalfAlpha_andDrawsLinesAsPath() {
        DeviationRenderer r = new DeviationRenderer();

        assertEquals("Default alpha should be 0.5", 0.5f, r.getAlpha(), FLOAT_DELTA);
        assertTrue("Renderer should always draw series line as path", r.getDrawSeriesLineAsPath());
    }

    @Test
    public void constructorFlags_stillForceDrawSeriesLineAsPathTrue() {
        DeviationRenderer r = new DeviationRenderer(false, false);

        // Internally the renderer forces this flag to true
        assertTrue("Renderer should always draw series line as path", r.getDrawSeriesLineAsPath());
    }

    @Test
    public void setDrawSeriesLineAsPath_isIgnoredAndRemainsTrue() {
        DeviationRenderer r = new DeviationRenderer(false, false);

        r.setDrawSeriesLineAsPath(false); // contract: this should be ignored
        assertTrue("Renderer should keep drawSeriesLineAsPath = true", r.getDrawSeriesLineAsPath());

        r.setDrawSeriesLineAsPath(true);  // explicitly setting true is fine
        assertTrue(r.getDrawSeriesLineAsPath());
    }

    @Test
    public void alphaSetter_acceptsBounds0and1() {
        DeviationRenderer r = new DeviationRenderer(false, false);

        r.setAlpha(0.0f);
        assertEquals(0.0f, r.getAlpha(), FLOAT_DELTA);

        r.setAlpha(1.0f);
        assertEquals(1.0f, r.getAlpha(), FLOAT_DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void alphaBelowZero_throwsIllegalArgumentException() {
        DeviationRenderer r = new DeviationRenderer();
        r.setAlpha(-0.01f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void alphaAboveOne_throwsIllegalArgumentException() {
        DeviationRenderer r = new DeviationRenderer();
        r.setAlpha(1.01f);
    }

    @Test
    public void passCount_andPassRoles_areAsDocumented() {
        DeviationRenderer r = new DeviationRenderer(false, false);

        assertEquals("Renderer uses three passes", 3, r.getPassCount());

        // Based on JFreeChart conventions for this renderer:
        // - pass 0: fill/shading (neither line nor item)
        // - pass 1: line
        // - pass 2: items/shapes
        assertFalse("Pass 0 is not line pass", r.isLinePass(0));
        assertTrue("Pass 1 is line pass", r.isLinePass(1));
        assertFalse("Pass 2 is not line pass", r.isLinePass(2));

        assertFalse("Pass 0 is not item pass", r.isItemPass(0));
        assertFalse("Pass 1 is not item pass", r.isItemPass(1));
        assertTrue("Pass 2 is item pass", r.isItemPass(2));
    }

    @Test
    public void equals_isReflexive_andNotEqualToUnrelatedType_orNull() {
        DeviationRenderer r = new DeviationRenderer();

        assertTrue("Equals must be reflexive", r.equals(r));
        assertFalse("Equals must return false for null", r.equals(null));
        assertFalse("Equals must return false for unrelated type", r.equals("not a renderer"));
    }

    @Test
    public void clone_producesEqualButIndependentCopy() throws CloneNotSupportedException {
        DeviationRenderer original = new DeviationRenderer();
        DeviationRenderer copy = (DeviationRenderer) original.clone();

        assertNotSame("Clone should be a different instance", original, copy);
        assertTrue("Clone should be equal to original", original.equals(copy));

        // Change a property on the clone and verify originals remains equal to old value
        copy.setAlpha(1.0f);
        assertEquals("Original alpha unchanged", 0.5f, original.getAlpha(), FLOAT_DELTA);
        assertEquals("Copy alpha updated", 1.0f, copy.getAlpha(), FLOAT_DELTA);
        assertFalse("Objects should no longer be equal after change", original.equals(copy));
    }

    @Test
    public void findRangeBounds_usesIntervalExtentsFromDataset() {
        // Build a simple interval dataset:
        // - One series with two items:
        //   (x=1, y=10, yLow=8,  yHigh=12)
        //   (x=2, y=20, yLow=18, yHigh=21)
        // Expected y-range: [8, 21]
        YIntervalSeries s = new YIntervalSeries("s1");
        s.add(1.0, 10.0, 8.0, 12.0);
        s.add(2.0, 20.0, 18.0, 21.0);

        YIntervalSeriesCollection dataset = new YIntervalSeriesCollection();
        dataset.addSeries(s);

        DeviationRenderer r = new DeviationRenderer();
        Range range = r.findRangeBounds(dataset);

        assertNotNull("Range should be computed for non-empty interval dataset", range);
        assertEquals("Lower bound should reflect min yLow", 8.0, range.getLowerBound(), DOUBLE_DELTA);
        assertEquals("Upper bound should reflect max yHigh", 21.0, range.getUpperBound(), DOUBLE_DELTA);
    }
}