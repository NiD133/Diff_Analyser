package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.data.xy.XYInterval;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class XYInterval_ESTest extends XYInterval_ESTest_scaffolding {

    // Tests for getYLow()
    @Test(timeout = 4000)
    public void testGetYLow_positive()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(2850.5914155, 2850.5914155, 2850.5914155, 2850.5914155, (-1461.548));
        double double0 = xYInterval0.getYLow();
        assertEquals(2850.5914155, double0, 0.01);
    }

    @Test(timeout = 4000)
    public void testGetYLow_negative()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval((-1.0), (-1.0), 0.0, (-3333.3187), (-1.0));
        double double0 = xYInterval0.getYLow();
        assertEquals((-3333.3187), double0, 0.01);
    }

    @Test(timeout = 4000)
    public void testGetYLow_zero()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        double double0 = xYInterval0.getYLow();
        assertEquals(0.0, double0, 0.01);
    }

    // Tests for getYHigh()
    @Test(timeout = 4000)
    public void testGetYHigh_positive()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(1.0, 1.0, 1.0, 1.0, 1.0);
        double double0 = xYInterval0.getYHigh();
        assertEquals(1.0, double0, 0.01);
    }

    @Test(timeout = 4000)
    public void testGetYHigh_negative()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, (-4479.95885));
        double double0 = xYInterval0.getYHigh();
        assertEquals((-4479.95885), double0, 0.01);
    }

    @Test(timeout = 4000)
    public void testGetYHigh_zero()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        double double0 = xYInterval0.getYHigh();
        assertEquals(0.0, double0, 0.01);
    }

    // Tests for getY()
    @Test(timeout = 4000)
    public void testGetY_positive()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(2850.5914155, 2850.5914155, 2850.5914155, 2850.5914155, (-1461.548));
        double double0 = xYInterval0.getY();
        assertEquals(2850.5914155, double0, 0.01);
    }

    @Test(timeout = 4000)
    public void testGetY_negative()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval((-1607.637878647), (-1607.637878647), (-1607.637878647), 0.0, 0.0);
        double double0 = xYInterval0.getY();
        assertEquals((-1607.637878647), double0, 0.01);
    }

    @Test(timeout = 4000)
    public void testGetY_zero()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, (-4479.95885));
        double double0 = xYInterval0.getY();
        assertEquals(0.0, double0, 0.01);
    }

    // Tests for getXLow()
    @Test(timeout = 4000)
    public void testGetXLow_positive()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(2850.5914155, 2850.5914155, 2850.5914155, 2850.5914155, (-1461.548));
        double double0 = xYInterval0.getXLow();
        assertEquals(2850.5914155, double0, 0.01);
    }

    @Test(timeout = 4000)
    public void testGetXLow_negative()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval((-2082.436), (-1.0), 1907.7299979743054, 0.0, 1.0);
        double double0 = xYInterval0.getXLow();
        assertEquals((-2082.436), double0, 0.01);
    }

    @Test(timeout = 4000)
    public void testGetXLow_zero()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        double double0 = xYInterval0.getXLow();
        assertEquals(0.0, double0, 0.01);
    }

    // Tests for getXHigh()
    @Test(timeout = 4000)
    public void testGetXHigh_positive()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(2850.5914155, 2850.5914155, 2850.5914155, 2850.5914155, (-1461.548));
        double double0 = xYInterval0.getXHigh();
        assertEquals(2850.5914155, double0, 0.01);
    }

    @Test(timeout = 4000)
    public void testGetXHigh_negative()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval((-2082.436), (-1.0), 1907.7299979743054, 0.0, 1.0);
        double double0 = xYInterval0.getXHigh();
        assertEquals((-1.0), double0, 0.01);
    }

    @Test(timeout = 4000)
    public void testGetXHigh_zero()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        double double0 = xYInterval0.getXHigh();
        assertEquals(0.0, double0, 0.01);
    }

    // Tests for equals() - Field-specific comparisons
    @Test(timeout = 4000)
    public void testEquals_whenYHighDifferent()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval xYInterval1 = new XYInterval(0.0, 0.0, 0.0, 0.0, (-482.4691016522));
        boolean boolean0 = xYInterval1.equals(xYInterval0);
        assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void testEquals_whenYLowDifferent()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval xYInterval1 = new XYInterval(0.0, 0.0, 0.0, (-4225.494), 953.2917971756976);
        boolean boolean0 = xYInterval1.equals(xYInterval0);
        assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void testEquals_whenYAndYLowAndYHighDifferent()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval xYInterval1 = new XYInterval(0.0, 0.0, (-2810.0), 2884.608252158, (-2810.0));
        boolean boolean0 = xYInterval1.equals(xYInterval0);
        assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void testEquals_whenXHighAndYHighDifferent()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, (-1.0), 0.0, 0.0, (-1.0));
        XYInterval xYInterval1 = new XYInterval(0.0, 0.0, 1.0, 0.0, 0.0);
        boolean boolean0 = xYInterval0.equals(xYInterval1);
        assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void testEquals_whenXLowDifferent()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval xYInterval1 = new XYInterval(1.0, 0.0, 0.0, 0.0, 0.0);
        boolean boolean0 = xYInterval1.equals(xYInterval0);
        assertFalse(boolean0);
    }

    // Tests for equals() - Special cases
    @Test(timeout = 4000)
    public void testEquals_whenYHighDifferent_reversed()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval xYInterval1 = new XYInterval(0.0, 0.0, 0.0, 0.0, (-482.4691016522));
        boolean boolean0 = xYInterval0.equals(xYInterval1);
        assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void testEquals_whenSameFieldValues()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval xYInterval1 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        boolean boolean0 = xYInterval1.equals(xYInterval0);
        assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void testEquals_whenYAndYLowAndYHighDifferent_reversed()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval xYInterval1 = new XYInterval(0.0, 0.0, (-2810.0), 2884.608252158, (-2810.0));
        boolean boolean0 = xYInterval0.equals(xYInterval1);
        assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void testEquals_whenXHighAndYAndYHighDifferent()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(689.6742630008566, 689.6742630008566, 689.6742630008566, 689.6742630008566, 689.6742630008566);
        XYInterval xYInterval1 = new XYInterval(689.6742630008566, (-1.0), (-1.0), 689.6742630008566, (-1.0));
        boolean boolean0 = xYInterval0.equals(xYInterval1);
        assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void testEquals_whenXLowDifferent_reversed()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval xYInterval1 = new XYInterval(1.0, 0.0, 0.0, 0.0, 0.0);
        boolean boolean0 = xYInterval0.equals(xYInterval1);
        assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void testEquals_withNonXYIntervalObject()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        Object object0 = new Object();
        boolean boolean0 = xYInterval0.equals(object0);
        assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void testEquals_reflexive()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval((-4479.95885), (-4479.95885), (-446.52402887375), (-482.4691016522), 1.0);
        boolean boolean0 = xYInterval0.equals(xYInterval0);
        assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void testEquals_whenYLowDifferent()  throws Throwable  {
        XYInterval xYInterval0 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval xYInterval1 = new XYInterval(0.0, 0.0, 0.0, 3289.17253997, 0.0);
        boolean boolean0 = xYInterval1.equals(xYInterval0);
        assertFalse(boolean0);
    }
}