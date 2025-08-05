package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class XYInterval_ESTest extends XYInterval_ESTest_scaffolding {

    private static final double DELTA = 0.01;

    @Test(timeout = 4000)
    public void testXYIntervalEqualityWithDifferentYHigh() {
        XYInterval interval1 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval interval2 = new XYInterval(0.0, 0.0, 0.0, 0.0, -482.4691016522);

        // Verify that intervals with different yHigh are not equal
        assertFalse(interval1.equals(interval2));
        assertFalse(interval2.equals(interval1));

        // Verify individual properties
        assertEquals(0.0, interval2.getYLow(), DELTA);
        assertEquals(0.0, interval2.getXHigh(), DELTA);
        assertEquals(-482.4691016522, interval2.getYHigh(), DELTA);
    }

    @Test(timeout = 4000)
    public void testXYIntervalEqualityWithDifferentYLow() {
        XYInterval interval1 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval interval2 = new XYInterval(0.0, 0.0, 0.0, -4225.494, 953.2917971756976);

        // Verify that intervals with different yLow are not equal
        assertFalse(interval1.equals(interval2));

        // Verify individual properties
        assertEquals(-4225.494, interval2.getYLow(), DELTA);
        assertEquals(953.2917971756976, interval2.getYHigh(), DELTA);
    }

    @Test(timeout = 4000)
    public void testXYIntervalEqualityWithDifferentY() {
        XYInterval interval1 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval interval2 = new XYInterval(0.0, 0.0, -2810.0, 2884.608252158, -2810.0);

        // Verify that intervals with different y are not equal
        assertFalse(interval1.equals(interval2));

        // Verify individual properties
        assertEquals(-2810.0, interval2.getY(), DELTA);
        assertEquals(2884.608252158, interval2.getYLow(), DELTA);
    }

    @Test(timeout = 4000)
    public void testXYIntervalEqualityWithDifferentXHigh() {
        XYInterval interval1 = new XYInterval(0.0, -1.0, 0.0, 0.0, -1.0);
        XYInterval interval2 = new XYInterval(0.0, 0.0, 1.0, 0.0, 0.0);

        // Verify that intervals with different xHigh are not equal
        assertFalse(interval1.equals(interval2));

        // Verify individual properties
        assertEquals(-1.0, interval1.getXHigh(), DELTA);
        assertEquals(1.0, interval2.getY(), DELTA);
    }

    @Test(timeout = 4000)
    public void testXYIntervalEqualityWithDifferentXLow() {
        XYInterval interval1 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval interval2 = new XYInterval(1.0, 0.0, 0.0, 0.0, 0.0);

        // Verify that intervals with different xLow are not equal
        assertFalse(interval1.equals(interval2));

        // Verify individual properties
        assertEquals(1.0, interval2.getXLow(), DELTA);
    }

    @Test(timeout = 4000)
    public void testXYIntervalProperties() {
        XYInterval interval = new XYInterval(2850.5914155, 2850.5914155, 2850.5914155, 2850.5914155, -1461.548);

        // Verify properties of the interval
        assertEquals(2850.5914155, interval.getXLow(), DELTA);
        assertEquals(2850.5914155, interval.getXHigh(), DELTA);
        assertEquals(2850.5914155, interval.getY(), DELTA);
        assertEquals(2850.5914155, interval.getYLow(), DELTA);
        assertEquals(-1461.548, interval.getYHigh(), DELTA);
    }

    @Test(timeout = 4000)
    public void testXYIntervalEqualityWithItself() {
        XYInterval interval = new XYInterval(-4479.95885, -4479.95885, -446.52402887375, -482.4691016522, 1.0);

        // Verify that an interval is equal to itself
        assertTrue(interval.equals(interval));

        // Verify individual properties
        assertEquals(-4479.95885, interval.getXLow(), DELTA);
        assertEquals(-4479.95885, interval.getXHigh(), DELTA);
        assertEquals(-446.52402887375, interval.getY(), DELTA);
        assertEquals(-482.4691016522, interval.getYLow(), DELTA);
        assertEquals(1.0, interval.getYHigh(), DELTA);
    }

    @Test(timeout = 4000)
    public void testXYIntervalEqualityWithDifferentObject() {
        XYInterval interval = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        Object otherObject = new Object();

        // Verify that an interval is not equal to a different type of object
        assertFalse(interval.equals(otherObject));
    }

    @Test(timeout = 4000)
    public void testXYIntervalEqualityWithIdenticalIntervals() {
        XYInterval interval1 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval interval2 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);

        // Verify that identical intervals are equal
        assertTrue(interval1.equals(interval2));
    }
}